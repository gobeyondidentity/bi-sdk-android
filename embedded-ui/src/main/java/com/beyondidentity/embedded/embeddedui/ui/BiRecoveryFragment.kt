package com.beyondidentity.embedded.embeddedui.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import com.beyondidentity.embedded.embeddedui.R
import com.beyondidentity.embedded.sdk.EmbeddedSdk
import com.google.android.material.textfield.TextInputEditText

class BiRecoveryFragment : BiBaseBottomSheetDialogFragment() {
    private lateinit var recoveryDescription: AppCompatTextView
    private lateinit var recoverEmailInput: TextInputEditText
    private lateinit var recoverButton: BiLoadingButton
    private lateinit var recoverError: AppCompatTextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_bi_recovery, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
    }

    private fun setupViews(view: View) {
        recoveryDescription = view.findViewById(R.id.recovery_description)
        recoverEmailInput = view.findViewById(R.id.recovery_email_input)
        recoverButton = view.findViewById(R.id.recover_email_button)
        recoverError = view.findViewById(R.id.could_not_complete_recovery)

        recoveryDescription.text = getString(R.string.recovery_desc, EmbeddedSdk.config.appDisplayName)
        recoverButton.setText(getString(R.string.recover_credential))
        recoverButton.setBackground(R.drawable.primary_danger_button_background)

        recoverButton.setOnClickListener {
            recoverButton.showLoading()
            recoverError.gone()
            if (recoverEmailInput.isValidEmail()) {
                EmbeddedSdk.recoverUser(
                    externalId = recoverEmailInput.text.toString(),
                ) { result ->
                    result.onSuccess {
                        recoverButton.hideLoading()
                        BiCheckEmailDialog
                            .newInstance()
                            .show(parentFragmentManager, BiCheckEmailDialog.TAG)
                    }
                    result.onFailure {
                        recoverButton.hideLoading()
                        recoverError.visible()
                    }
                }
            } else {
                Toast.makeText(context, getString(R.string.enter_valid_email), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    companion object {
        const val TAG = "bi-recovery-fragment"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         **/
        @JvmStatic
        fun newInstance() = BiRecoveryFragment()
    }
}
