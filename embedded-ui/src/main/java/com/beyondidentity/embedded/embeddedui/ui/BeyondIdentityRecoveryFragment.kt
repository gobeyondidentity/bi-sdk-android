package com.beyondidentity.embedded.embeddedui.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import com.beyondidentity.embedded.embeddedui.R
import com.beyondidentity.embedded.embeddedui.ui.views.BeyondIdentityLoadingButton
import com.google.android.material.textfield.TextInputEditText

private class BeyondIdentityRecoveryFragment : BiBaseBottomSheetDialogFragment() {
    private lateinit var recoveryDescription: AppCompatTextView
    private lateinit var recoverEmailInput: TextInputEditText
    private lateinit var recoverButton: BeyondIdentityLoadingButton
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

        recoveryDescription.text = getString(R.string.recovery_desc, EmbeddedUiConfig.config.appDisplayName)
        recoverButton.setText(getString(R.string.recovery_credential))
        recoverButton.setBackground(R.drawable.primary_danger_button_background)

        recoverButton.setOnClickListener {
            recoverButton.showLoading()
            recoverError.gone()
        }
    }

    companion object {
        const val TAG = "bi-recovery-fragment"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         **/
        @JvmStatic
        private fun newInstance() = BeyondIdentityRecoveryFragment()
    }
}
