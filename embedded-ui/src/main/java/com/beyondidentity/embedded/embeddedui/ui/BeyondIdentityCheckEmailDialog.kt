package com.beyondidentity.embedded.embeddedui.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.DialogFragment
import com.beyondidentity.embedded.embeddedui.R

private class BeyondIdentityCheckEmailDialog : DialogFragment() {
    private lateinit var goToInbox: AppCompatTextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_bi_check_email, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        goToInbox = view.findViewById(R.id.check_email_goto_inbox)

        goToInbox.setOnClickListener {
            val intent = Intent
                .makeMainSelectorActivity(
                    Intent.ACTION_MAIN,
                    Intent.CATEGORY_APP_EMAIL
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

            try {
                startActivity(intent)
                dismiss()
            } catch (e: ActivityNotFoundException) {
                dismiss()
            }
        }
    }

    companion object {
        const val TAG = "bi-check-email-fragment"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         **/
        @JvmStatic
        fun newInstance() = BeyondIdentityCheckEmailDialog()
    }
}
