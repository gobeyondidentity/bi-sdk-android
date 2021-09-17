package com.beyondidentity.embedded.embeddedui.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import com.beyondidentity.embedded.embeddedui.R
import com.beyondidentity.embedded.sdk.EmbeddedSdk
import com.beyondidentity.embedded.sdk.utils.supportEmailIntent
import com.google.android.material.textfield.TextInputEditText

class BeyondIdentityRegistrationFragment : BiBaseBottomSheetDialogFragment() {
    private lateinit var emailInput: TextInputEditText
    private lateinit var signUpButton: BiLoadingButton
    private lateinit var whatIsCred: AppCompatTextView
    private lateinit var enterEmailToGenerate: AppCompatTextView
    private lateinit var addToThisDevice: AppCompatTextView
    private lateinit var recoverAccount: AppCompatTextView
    private lateinit var visitSupport: AppCompatTextView
    private lateinit var signUpError: AppCompatTextView

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
        emailInput = view.findViewById(R.id.registration_email_input)
        signUpButton = view.findViewById(R.id.registration_sign_up_button)
        whatIsCred = view.findViewById(R.id.what_is_cred)
        enterEmailToGenerate = view.findViewById(R.id.enter_email_to_generate_cred_details)
        addToThisDevice = view.findViewById(R.id.registration_add_to_device)
        recoverAccount = view.findViewById(R.id.registration_recover_account)
        visitSupport = view.findViewById(R.id.registration_visit_support)
        signUpError = view.findViewById(R.id.could_not_complete_signup)

        enterEmailToGenerate.text =
            getString(
                R.string.registration_enter_email_to_generate_cred,
                EmbeddedSdk.config.appDisplayName
            )

        signUpButton.setText(getString(R.string.embedded_ui_register_signup))
        signUpButton.setBackground(R.drawable.primary_button_background)

        signUpButton.setOnClickListener {
            signUpError.gone()
            if (emailInput.isValidEmail()) {
                signUpButton.showLoading()
                EmbeddedSdk.createUser(
                    externalId = emailInput.text.toString(),
                    email = emailInput.text.toString(),
                    displayName = emailInput.text.toString(),
                    userName = emailInput.text.toString(),
                ) { result ->
                    result.onSuccess {
                        Log.d(TAG, it.toString())
                        signUpButton.hideLoading()

                        BiCheckEmailDialog
                            .newInstance()
                            .show(parentFragmentManager, BiCheckEmailDialog.TAG)
                    }
                    result.onFailure {
                        Log.e(TAG, it.toString())
                        signUpError.visible()
                        signUpButton.hideLoading()
                    }
                }
            } else {
                Toast.makeText(context, getString(R.string.enter_valid_email), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        whatIsCred.setOnClickListener {
            BiWhatIsCredDialog
                .newInstance()
                .show(parentFragmentManager, BiWhatIsCredDialog.TAG)
        }

        addToThisDevice.setOnClickListener {
            BiScanQrCodeFragment
                .newInstance()
                .show(parentFragmentManager, BiScanQrCodeFragment.TAG)
        }

        recoverAccount.setOnClickListener {
            BiRecoveryFragment
                .newInstance()
                .show(parentFragmentManager, BiRecoveryFragment.TAG)
        }

        visitSupport.setOnClickListener {
            val intent = supportEmailIntent(EmbeddedSdk.config.supportEmail)
            startActivity(intent)
        }
    }

    companion object {
        const val TAG = "bi-registration-fragment"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @return A new instance of fragment [BeyondIdentityRegistrationFragment].
         */
        @JvmStatic
        fun newInstance() = BeyondIdentityRegistrationFragment()
    }
}
