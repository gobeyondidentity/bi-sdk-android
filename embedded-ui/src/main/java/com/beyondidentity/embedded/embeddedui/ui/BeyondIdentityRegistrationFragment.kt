package com.beyondidentity.embedded.embeddedui.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.os.bundleOf
import com.beyondidentity.embedded.embeddedui.R
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus.BiEvent.CredentialRecovery
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus.BiEvent.CredentialSetup
import com.beyondidentity.embedded.embeddedui.ui.views.BeyondIdentityLoadingButton
import com.beyondidentity.embedded.sdk.utils.supportIntent

class BeyondIdentityRegistrationFragment : BiBaseBottomSheetDialogFragment() {
    private lateinit var signUpButton: BeyondIdentityLoadingButton
    private lateinit var whatIsCred: AppCompatTextView
    private lateinit var addToThisDevice: AppCompatTextView
    private lateinit var recoverAccount: AppCompatTextView
    private lateinit var visitSupport: AppCompatTextView
    private lateinit var signUpError: AppCompatTextView
    private lateinit var deactivateOldCredDesc: AppCompatTextView

    private var isReplaceCred = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isReplaceCred = it.getBoolean(IS_REPLACE_CRED_ARG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_bi_registration, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
    }

    private fun setupViews(view: View) {
        signUpButton = view.findViewById(R.id.registration_sign_up_button)
        whatIsCred = view.findViewById(R.id.what_is_cred)
        addToThisDevice = view.findViewById(R.id.registration_add_to_device)
        recoverAccount = view.findViewById(R.id.registration_recover_account)
        visitSupport = view.findViewById(R.id.registration_visit_support)
        signUpError = view.findViewById(R.id.could_not_complete_signup)
        deactivateOldCredDesc = view.findViewById(R.id.registration_deactivate_old_desc)

        if (isReplaceCred) {
            deactivateOldCredDesc.visible()
            signUpButton.setText(getString(R.string.registration_setup_new_cred))
        } else {
            deactivateOldCredDesc.gone()
            signUpButton.setText(getString(R.string.registration_setup))
        }

        signUpButton.setBackground(R.drawable.primary_button_background)

        signUpButton.setOnClickListener {
            BiEventBus.post(CredentialSetup)
        }

        whatIsCred.setOnClickListener {
            BeyondIdentityWhatIsCredDialog
                .newInstance()
                .show(parentFragmentManager, BeyondIdentityWhatIsCredDialog.TAG)
        }

        addToThisDevice.setOnClickListener {
            BeyondIdentityScanQrCodeFragment
                .newInstance()
                .show(parentFragmentManager, BeyondIdentityScanQrCodeFragment.TAG)
        }

        recoverAccount.setOnClickListener {
            BiEventBus.post(CredentialRecovery)
        }

        visitSupport.setOnClickListener {
            val intent = supportIntent(EmbeddedUiConfig.config.supportUrlOrEmail)
            startActivity(intent)
        }
    }

    companion object {
        const val TAG = "bi-registration-fragment"
        const val IS_REPLACE_CRED_ARG = "bi-reg-is-replace-cred-arg"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @return A new instance of fragment [BeyondIdentityRegistrationFragment].
         */
        @JvmStatic
        fun newInstance(isReplaceCred: Boolean = true) =
            BeyondIdentityRegistrationFragment().apply {
                arguments = bundleOf(IS_REPLACE_CRED_ARG to isReplaceCred)
            }
    }
}
