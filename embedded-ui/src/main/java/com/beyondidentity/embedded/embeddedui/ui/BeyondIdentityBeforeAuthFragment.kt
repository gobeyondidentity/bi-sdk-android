package com.beyondidentity.embedded.embeddedui.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import com.beyondidentity.embedded.embeddedui.R
import com.beyondidentity.embedded.embeddedui.ui.ActionType.Authentication
import com.beyondidentity.embedded.embeddedui.ui.views.BeyondIdentityLoadingButton

class BeyondIdentityBeforeAuthFragment : BiBaseBottomSheetDialogFragment() {
    private lateinit var beforeAuthContinue: BeyondIdentityLoadingButton
    private lateinit var beforeAuthReplaceCred: AppCompatTextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_bi_before_auth, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
    }

    private fun setupViews(view: View) {
        beforeAuthContinue = view.findViewById(R.id.before_auth_auth_button)
        beforeAuthReplaceCred = view.findViewById(R.id.before_auth_replace_cred)

        beforeAuthContinue.setText(getString(R.string.before_auth_continue_to_login))
        beforeAuthContinue.setOnClickListener {
            BeyondIdentityActionHandlerFragment
                .newInstance(actionType = Authentication)
                .show(parentFragmentManager, BeyondIdentityActionHandlerFragment.TAG)
        }

        beforeAuthReplaceCred.setOnClickListener {
            BeyondIdentityRegistrationFragment
                .newInstance(true)
                .show(parentFragmentManager, BeyondIdentityRegistrationFragment.TAG)
        }
    }

    companion object {
        const val TAG = "bi-before-auth-fragment"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @return A new instance of fragment [BeyondIdentityBeforeAuthFragment].
         */
        @JvmStatic
        fun newInstance() = BeyondIdentityBeforeAuthFragment()
    }
}
