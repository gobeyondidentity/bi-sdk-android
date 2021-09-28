package com.beyondidentity.embedded.embeddedui.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.DialogFragment
import com.beyondidentity.embedded.embeddedui.R

class BeyondIdentityWhatIsCredDialog : DialogFragment() {
    private lateinit var gotItButton: AppCompatTextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_bi_what_is_cred, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gotItButton = view.findViewById(R.id.what_is_cred_got_it)

        gotItButton.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        const val TAG = "bi-whats-credential-fragment"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         **/
        @JvmStatic
        fun newInstance() = BeyondIdentityWhatIsCredDialog()
    }
}
