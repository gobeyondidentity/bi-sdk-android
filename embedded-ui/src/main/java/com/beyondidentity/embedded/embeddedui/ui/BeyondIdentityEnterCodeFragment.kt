package com.beyondidentity.embedded.embeddedui.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.doOnTextChanged
import com.beyondidentity.embedded.embeddedui.R
import com.beyondidentity.embedded.embeddedui.ui.ActionType.Migration
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus.BiEvent.CredentialRecovery
import com.beyondidentity.embedded.embeddedui.ui.views.PinEntryEditText

class BeyondIdentityEnterCodeFragment : BiBaseBottomSheetDialogFragment() {
    private lateinit var codeInput: PinEntryEditText
    private lateinit var scanQrInstead: AppCompatTextView
    private lateinit var errorText: AppCompatTextView
    private lateinit var recover: AppCompatTextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_bi_enter_code, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
    }

    private fun setupViews(view: View) {
        codeInput = view.findViewById(R.id.enter_code_input)
        scanQrInstead = view.findViewById(R.id.enter_code_scan_qr_instead)
        errorText = view.findViewById(R.id.enter_code_error_description)
        recover = view.findViewById(R.id.enter_code_recover_credential)

        codeInput.doOnTextChanged { code, _, _, _ ->
            if (code?.length == MIGRATION_CODE_LENGTH) {
                BeyondIdentityActionHandlerFragment
                    .newInstance(actionType = Migration(code = codeInput.text.toString()))
                    .show(parentFragmentManager, BeyondIdentityActionHandlerFragment.TAG)

                codeInput.text?.clear()
            }
        }

        scanQrInstead.setOnClickListener {
            dismiss()
        }

        recover.setOnClickListener {
            BiEventBus.post(CredentialRecovery)
        }
    }

    companion object {
        const val TAG = "bi-enter-code-fragment"

        private const val MIGRATION_CODE_LENGTH = 9

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @return A new instance of fragment [BeyondIdentityEnterCodeFragment].
         */
        @JvmStatic
        fun newInstance() = BeyondIdentityEnterCodeFragment()
    }
}