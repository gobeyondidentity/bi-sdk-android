package com.beyondidentity.embedded.embeddedui.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import com.beyondidentity.embedded.embeddedui.R
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus.BiEvent
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus.BiEvent.CredentialDeleted
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus.BiEvent.DeleteCredential
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus.BiObserver
import com.beyondidentity.embedded.sdk.EmbeddedSdk
import java.util.Locale

class BeyondIdentityCredentialInfoFragment :
    BiBaseBottomSheetDialogFragment(),
    BiObserver {
    private lateinit var credInfoTenant: AppCompatTextView
    private lateinit var credInfoCredFor: AppCompatTextView
    private lateinit var credInfoModel: AppCompatTextView
    private lateinit var credInfoVersion: AppCompatTextView
    private lateinit var credInfoDeleteCred: AppCompatTextView

    private var credHandle: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_bi_credential_info, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
        BiEventBus.registerObserver(this)
    }

    override fun onDestroyView() {
        BiEventBus.unRegisterObserver(this)
        super.onDestroyView()
    }

    private fun setupViews(view: View) {
        credInfoTenant = view.findViewById(R.id.cred_info_cred_tenant)
        credInfoCredFor = view.findViewById(R.id.cred_info_cred_for_device)
        credInfoModel = view.findViewById(R.id.cred_info_model_text)
        credInfoVersion = view.findViewById(R.id.cred_info_version_text)
        credInfoDeleteCred = view.findViewById(R.id.cred_info_delete_cred)

        EmbeddedSdk.getCredentials { result ->
            result.onSuccess { credList ->
                if (credList.isNotEmpty()) {
                    credHandle = credList[0].handle
                    val device =
                        "${android.os.Build.MANUFACTURER.capitalize(Locale.getDefault())} ${android.os.Build.MODEL}"
                    credInfoTenant.text = credList[0].name
                    credInfoCredFor.text =
                        getString(R.string.cred_info_credential_for_device, device)
                    credInfoModel.text = device
                    credInfoVersion.text =
                        getString(R.string.cred_info_version_text, android.os.Build.VERSION.RELEASE)

                    credInfoDeleteCred.setOnClickListener {
                        val fragment = BeyondIdentityDeleteCredentialDialog
                            .newInstance()
                        fragment.show(parentFragmentManager, BeyondIdentityDeleteCredentialDialog.TAG)
                    }
                }
            }
            result.onFailure {
            }
        }
    }

    private fun onDeleteCred() {
        credHandle?.let { handle ->
            EmbeddedSdk.deleteCredential(handle) { result ->
                result.onSuccess {
                    Toast.makeText(
                        context,
                        getString(R.string.delete_cred_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    BiEventBus.post(CredentialDeleted)
                    dismiss()
                }

                result.onFailure {
                    Toast.makeText(
                        context,
                        getString(R.string.something_went_wrong),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onEvent(event: BiEvent) {
        when (event) {
            is DeleteCredential -> onDeleteCred()
            else -> Unit
        }
    }

    companion object {
        const val TAG = "bi-credential-info-fragment"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @return A new instance of fragment [BeyondIdentityCredentialInfoFragment].
         */
        @JvmStatic
        fun newInstance() = BeyondIdentityCredentialInfoFragment()
    }
}
