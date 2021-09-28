package com.beyondidentity.embedded.embeddedui.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.Group
import androidx.core.os.bundleOf
import com.beyondidentity.embedded.embeddedui.R
import com.beyondidentity.embedded.sdk.EmbeddedSdk
import com.beyondidentity.embedded.sdk.export.ExportCredentialListener
import com.beyondidentity.embedded.sdk.models.ExportResponse
import com.google.android.material.progressindicator.LinearProgressIndicator

class BeyondIdentityShowQrCodeFragment : BiBaseBottomSheetDialogFragment() {
    private lateinit var progressView: LinearProgressIndicator
    private lateinit var qrImage: AppCompatImageView
    private lateinit var qrCode: AppCompatTextView
    private lateinit var showQrDesc: AppCompatTextView

    private lateinit var contentGroup: Group
    private lateinit var errorGroup: Group

    private var credHandle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            credHandle = it.getString(SHOW_QR_CRED_HANDLE_ARG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_bi_show_qr, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
        export()
    }

    override fun onDestroyView() {
        EmbeddedSdk.cancelExport {}
        super.onDestroyView()
    }

    private fun setupViews(view: View) {
        progressView = view.findViewById(R.id.show_qr_progress)
        qrImage = view.findViewById(R.id.show_qr_image_code)
        qrCode = view.findViewById(R.id.show_qr_text_code)
        showQrDesc = view.findViewById(R.id.show_qr_desc)

        contentGroup = view.findViewById(R.id.show_qr_content_group)
        errorGroup = view.findViewById(R.id.show_qr_error_group)

        showQrDesc.text = getString(R.string.show_qr_desc, EmbeddedUiConfig.config.appDisplayName)
    }

    private fun export() {
        credHandle?.let { handle ->
            EmbeddedSdk.export(
                listOf(handle),
                object : ExportCredentialListener {
                    override fun onUpdate(token: ExportResponse?) {
                        contentGroup.visible()
                        progressView.gone()

                        token?.let { t ->
                            qrImage.setImageBitmap(t.rendezvousTokenBitmap)
                            qrCode.text = formatQrCode(t.rendezvousToken)
                        }
                    }

                    override fun onFinish() {
                        Toast.makeText(
                            context,
                            getString(R.string.show_qr_export_success),
                            Toast.LENGTH_SHORT
                        ).show()
                        dismiss()
                    }

                    override fun onError(throwable: Throwable) {
                        throwable.message?.let { m ->
                            if (m.contains("most likely user canceled") || m.contains("aborted")) {
                                dismiss()
                            } else {
                                errorGroup.visible()
                                contentGroup.gone()
                                progressView.gone()
                            }
                        } ?: run {
                            errorGroup.visible()
                            contentGroup.gone()
                            progressView.gone()
                        }
                    }
                }
            )
        }
    }

    private fun formatQrCode(code: String) =
        try {
            code.substring(0..2) + "-" +
                code.substring(3..5) + "-" +
                code.substring(6..8)
        } catch (t: IndexOutOfBoundsException) {
            code
        }

    override fun onCancel(dialog: DialogInterface) {
        EmbeddedSdk.cancelExport {
            super.onCancel(dialog)
        }
    }

    companion object {
        const val TAG = "bi-show-qr-fragment"

        const val SHOW_QR_CRED_HANDLE_ARG = "bi-show-qr-cred-handle"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         **/
        @JvmStatic
        fun newInstance(credHandle: String) = BeyondIdentityShowQrCodeFragment().apply {
            arguments = bundleOf(SHOW_QR_CRED_HANDLE_ARG to credHandle)
        }
    }
}
