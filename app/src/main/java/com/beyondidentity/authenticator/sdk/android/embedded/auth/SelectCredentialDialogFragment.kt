package com.beyondidentity.authenticator.sdk.android.embedded.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.DialogFragment
import com.beyondidentity.authenticator.sdk.android.R
import com.beyondidentity.embedded.sdk.models.Credential
import com.beyondidentity.embedded.sdk.models.OnSelectedCredential

class SelectCredentialDialogFragment(
    val credentials: List<Credential>,
    val onCredentialSelectedCallback: OnSelectedCredential,
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
        val credentialListView = view.findViewById<LinearLayout>(R.id.select_account_view)
        credentials.forEach { credential ->
            createAccountItem(credential.identity.displayName, credentialListView) {
                onCredentialSelectedCallback(credential.id)
                dismiss()
            }
        }
    }

    private fun createAccountItem(
        accountText: String,
        credentialListView: ViewGroup,
        onClickListener: View.OnClickListener,
    ) {
        val view = LayoutInflater.from(context).inflate(R.layout.select_credential_item, credentialListView, false)
        view.findViewById<AppCompatTextView>(R.id.itemTitle).text = accountText
        view.setOnClickListener(onClickListener)
        credentialListView.addView(view)
    }

    companion object {
        const val TAG = "bi-select-account-fragment"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @return A new instance of fragment [SelectCredentialDialogFragment].
         */
        @JvmStatic
        fun newInstance(
            credentials: List<Credential>,
            onCredentialSelectedCallback: OnSelectedCredential,
        ) = SelectCredentialDialogFragment(credentials, onCredentialSelectedCallback)
    }
}
