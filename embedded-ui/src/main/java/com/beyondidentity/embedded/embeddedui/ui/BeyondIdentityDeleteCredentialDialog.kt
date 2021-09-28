package com.beyondidentity.embedded.embeddedui.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.DialogFragment
import com.beyondidentity.embedded.embeddedui.R
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus.BiEvent.DeleteCredential

class BeyondIdentityDeleteCredentialDialog : DialogFragment() {
    private lateinit var delete: AppCompatTextView
    private lateinit var cancel: AppCompatTextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.dialog_bi_delete_credential, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
    }

    private fun setupViews(view: View) {
        delete = view.findViewById(R.id.delete_cred_delete)
        cancel = view.findViewById(R.id.delete_cred_cancel)

        delete.setOnClickListener {
            BiEventBus.post(DeleteCredential)
            dismiss()
        }
        cancel.setOnClickListener { dismiss() }
    }

    companion object {
        const val TAG = "bi-delete-cred-dialog"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         **/
        @JvmStatic
        fun newInstance() = BeyondIdentityDeleteCredentialDialog()
    }
}
