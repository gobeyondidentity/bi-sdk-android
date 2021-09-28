package com.beyondidentity.embedded.embeddedui.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.preference.PreferenceManager
import com.beyondidentity.embedded.embeddedui.R
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus.BiEvent.CredentialSetup

class BeyondIdentityAddCredentialFragment : BiBaseBottomSheetDialogFragment() {
    private lateinit var createCred: AppCompatTextView
    private lateinit var addCred: AppCompatTextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_bi_add_credential, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
    }

    private fun setupViews(view: View) {
        createCred = view.findViewById(R.id.add_cred_create_new)
        addCred = view.findViewById(R.id.add_cred_add_to_device)

        createCred.setOnClickListener {
            BiEventBus.post(CredentialSetup)
        }

        addCred.setOnClickListener {
            val pref = PreferenceManager.getDefaultSharedPreferences(context).edit()
            pref.putString(MIGRATION_SOURCE, MIGRATION_SOURCE_SETTINGS)
            pref.apply()

            BeyondIdentityScanQrCodeFragment
                .newInstance()
                .show(parentFragmentManager, BeyondIdentityScanQrCodeFragment.TAG)
        }
    }

    companion object {
        const val TAG = "bi-add-cred-fragment"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @return A new instance of fragment [BeyondIdentityAddCredentialFragment].
         */
        @JvmStatic
        fun newInstance() = BeyondIdentityAddCredentialFragment()
    }
}
