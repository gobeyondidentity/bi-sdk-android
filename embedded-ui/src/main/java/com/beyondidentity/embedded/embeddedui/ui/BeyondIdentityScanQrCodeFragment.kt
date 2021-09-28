package com.beyondidentity.embedded.embeddedui.ui

import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.beyondidentity.embedded.embeddedui.R
import com.beyondidentity.embedded.embeddedui.ui.ActionType.Migration
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus.BiEvent.CredentialRecovery
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.zxing.BarcodeFormat

class BeyondIdentityScanQrCodeFragment : BiBaseBottomSheetDialogFragment() {
    private lateinit var codeScanner: CodeScanner
    private lateinit var codeScannerView: CodeScannerView
    private lateinit var useDigitCode: AppCompatTextView
    private lateinit var recoverAccount: AppCompatTextView
    private lateinit var noCameraPermissions: ConstraintLayout
    private lateinit var errorScanningCode: AppCompatTextView
    private lateinit var enableCameraAccess: AppCompatTextView
    private lateinit var codeDesc: AppCompatTextView

    private var requestedOnce = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_bi_scan_qr_code, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestCameraPermission()
        } else {
            codeScanner.startPreview()
        }
    }

    override fun onPause() {
        super.onPause()
        if (codeScanner.isPreviewActive) {
            requestedOnce = false
            codeScanner.stopPreview()
        }
    }

    private fun setupViews(view: View) {
        codeScannerView = view.findViewById(R.id.scan_qr_camera)
        useDigitCode = view.findViewById(R.id.scan_qr_enter_digits_instead)
        recoverAccount = view.findViewById(R.id.scan_qr_recover_account)
        noCameraPermissions = view.findViewById(R.id.scan_qr_no_permissions)
        errorScanningCode = view.findViewById(R.id.scan_qr_error_scanning)
        enableCameraAccess = view.findViewById(R.id.scan_qr_pls_enable_camera)
        codeDesc = view.findViewById(R.id.scan_qr_locate_desc)

        codeScanner = CodeScanner(requireContext(), codeScannerView).apply {
            camera = CodeScanner.CAMERA_BACK
            formats = listOf(BarcodeFormat.QR_CODE)
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            autoFocusMode = AutoFocusMode.SAFE
            isFlashEnabled = false
            decodeCallback = DecodeCallback { result ->
                result.text?.let { code ->
                    BeyondIdentityActionHandlerFragment
                        .newInstance(actionType = Migration(code))
                        .show(parentFragmentManager, BeyondIdentityActionHandlerFragment.TAG)
                }
            }
            errorCallback = ErrorCallback {
                showCodeScanError()
            }
        }

        noCameraPermissions.setOnClickListener { launchAppPermissionSettings() }
        enableCameraAccess.setOnClickListener { launchAppPermissionSettings() }

        useDigitCode.setOnClickListener {
            BeyondIdentityEnterCodeFragment
                .newInstance()
                .show(parentFragmentManager, BeyondIdentityEnterCodeFragment.TAG)
        }

        recoverAccount.setOnClickListener {
            BiEventBus.post(CredentialRecovery)
        }
    }

    private fun requestCameraPermission() {
        if (!requestedOnce) {
            requestPermissions(arrayOf(permission.CAMERA), CAMERA_PERMISSION_REQUEST)
            requestedOnce = true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST -> {
                permissions.forEachIndexed { index, p ->
                    if (p == permission.CAMERA) {
                        if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                            showCodeScanner()
                            codeScanner.startPreview()
                        } else {
                            showNoCameraPermission()
                        }
                    }
                }
            }
        }
    }

    private fun showNoCameraPermission() {
        noCameraPermissions.visible()
        enableCameraAccess.visible()
        codeScannerView.invisible()
        codeDesc.invisible()
    }

    private fun showCodeScanner() {
        noCameraPermissions.gone()
        enableCameraAccess.gone()
        codeScannerView.visible()
        codeDesc.visible()
    }

    private fun showCodeScanError() {
        errorScanningCode.visible()
    }

    override fun onDestroyView() {
        codeScanner.releaseResources()
        super.onDestroyView()
    }

    private fun launchAppPermissionSettings() {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", context?.packageName, null)
            intent.data = uri
            startActivity(intent)
        } catch (e: Exception) {
            // noop
        }
    }

    companion object {
        const val TAG = "bi-scan-qr-code-fragment"

        private const val CAMERA_PERMISSION_REQUEST: Int = 4125

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @return A new instance of fragment [BeyondIdentityScanQrCodeFragment].
         */
        @JvmStatic
        fun newInstance() = BeyondIdentityScanQrCodeFragment()
    }
}
