package com.beyondidentity.authenticator.sdk.android.embedded.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.DialogFragment
import com.beyondidentity.authenticator.sdk.android.R
import com.beyondidentity.embedded.sdk.models.OnSelectedPasskey
import com.beyondidentity.embedded.sdk.models.Passkey

class SelectPasskeyDialogFragment(
    val passkeys: List<Passkey>,
    val onPasskeySelectedCallback: OnSelectedPasskey,
) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.select_account_dialog_fragment, container, true)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
    }

    private fun setupViews(view: View) {
        val passkeyListView = view.findViewById<LinearLayout>(R.id.select_account_view)
        passkeys.forEach { passkey ->
            createAccountItem(passkey.identity.displayName, passkeyListView) {
                onPasskeySelectedCallback(passkey.id)
                dismiss()
            }
        }
    }

    private fun createAccountItem(
        accountText: String,
        passkeyListView: ViewGroup,
        onClickListener: View.OnClickListener,
    ) {
        val view = LayoutInflater.from(context).inflate(R.layout.select_passkey_item, passkeyListView, false)
        view.findViewById<AppCompatTextView>(R.id.itemTitle).text = accountText
        view.setOnClickListener(onClickListener)
        passkeyListView.addView(view)
    }

    companion object {
        const val TAG = "bi-select-account-fragment"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @return A new instance of fragment [SelectPasskeyDialogFragment].
         */
        @JvmStatic
        fun newInstance(
            passkeys: List<Passkey>,
            onPasskeySelectedCallback: OnSelectedPasskey,
        ) = SelectPasskeyDialogFragment(passkeys, onPasskeySelectedCallback)
    }
}
