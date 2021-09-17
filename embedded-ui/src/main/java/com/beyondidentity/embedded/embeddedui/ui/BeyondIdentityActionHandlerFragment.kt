package com.beyondidentity.embedded.embeddedui.ui

import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.Group
import androidx.core.os.bundleOf
import com.beyondidentity.embedded.embeddedui.R
import com.beyondidentity.embedded.embeddedui.ui.ActionType.Authentication
import com.beyondidentity.embedded.embeddedui.ui.ActionType.Migration
import com.beyondidentity.embedded.embeddedui.ui.ActionType.Registration
import com.beyondidentity.embedded.sdk.EmbeddedSdk
import com.beyondidentity.embedded.sdk.EmbeddedSdk.AuthenticationData.ConfidentialClientData
import com.beyondidentity.embedded.sdk.EmbeddedSdk.AuthenticationData.PublicClientData
import com.beyondidentity.embedded.sdk.models.Credential
import com.beyondidentity.embedded.sdk.models.TokenResponse
import com.beyondidentity.embedded.sdk.utils.supportEmailIntent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.parcelize.Parcelize

class BeyondIdentityActionHandlerFragment : BiBaseBottomSheetDialogFragment() {
    private lateinit var loadingText: AppCompatTextView
    private lateinit var loadingVisitSupport: AppCompatTextView
    private lateinit var errorDescription: AppCompatTextView

    private lateinit var supportGroup: Group
    private lateinit var loadingGroup: Group
    private lateinit var errorGroup: Group

    private var actionType: ActionType? = null

    var onRegisterListener: OnRegisterListener? = null
    var onAuthenticationListener: OnAuthenticationListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            actionType = it.getParcelable(ACTION_TYPE_ARG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_bi_action_handler, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)

        runAction(actionType) { actionType ->
            when (actionType) {
                is Registration -> registerCredential(actionType.registerUri)
                Authentication -> authenticate(false)
                is Migration -> migration(actionType.code)
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        EmbeddedSdk.cancel {
            super.onDismiss(dialog)
        }
    }

    private fun setupViews(view: View) {
        loadingText = view.findViewById(R.id.loading_text)
        loadingVisitSupport = view.findViewById(R.id.loading_visit_support)
        errorDescription = view.findViewById(R.id.error_description)

        supportGroup = view.findViewById(R.id.support_group)
        loadingGroup = view.findViewById(R.id.loading_group)
        errorGroup = view.findViewById(R.id.error_group)

        loadingVisitSupport.setOnClickListener {
            val intent = supportEmailIntent(EmbeddedSdk.config.supportEmail)
            startActivity(intent)
        }
    }

    private fun registerCredential(
        registerUri: String,
    ) {
        showRegistration()
        EmbeddedSdk.register(registerUri) { result ->
            result.onSuccess { credential ->
                authenticate(true)
                onRegisterListener?.onCredentialRegistered(credential)
            }
            result.onFailure { throwable ->
                showRegistrationError()
                onRegisterListener?.onCredentialRegistrationError(throwable)
            }
        }
    }

    private fun authenticate(
        isAfterRegistration: Boolean,
    ) {
        if (isAfterRegistration)
            showAuthenticationAfterRegistration()
        else
            showAuthentication()

        when (val authenticateData = EmbeddedSdk.config.authenticationData) {
            is PublicClientData -> {
                authenticatePublicClient(
                    authenticateData.clientId,
                    authenticateData.redirectUri
                )
            }
            is ConfidentialClientData -> {
                authenticateConfidentialClient(
                    authenticateData.clientId,
                    authenticateData.redirectUri,
                    authenticateData.scope,
                )
            }
        }
    }

    private fun authenticateConfidentialClient(
        clientId: String,
        redirectUri: String,
        scope: String,
        pkce: String? = null,
    ) {
        EmbeddedSdk.authenticateConfidential(
            clientId = clientId,
            redirectUri = redirectUri,
            scope = scope,
            pkceS256CodeChallenge = pkce,
        ) { result ->
            result.onSuccess { authorizationCode ->
                onAuthenticationListener?.onConfidentialClientAuthentication(authorizationCode)
                clearFragments()
            }
            result.onFailure { throwable ->
                onAuthenticationListener?.onAuthenticationError(throwable)
                showAuthenticationError()
            }
        }
    }

    private fun authenticatePublicClient(
        clientId: String,
        redirectUri: String,
    ) {
        EmbeddedSdk.authenticatePublic(
            clientId = clientId,
            redirectUri = redirectUri,
        ) { result ->
            result.onSuccess { tokenResponse ->
                onAuthenticationListener?.onPublicClientAuthentication(tokenResponse)
                clearFragments()
            }
            result.onFailure { throwable ->
                onAuthenticationListener?.onAuthenticationError(throwable)
                showAuthenticationError()
            }
        }
    }

    private fun migration(
        code: String,
    ) {
        showRegistration()
        EmbeddedSdk.import(code) { result ->
            result.onSuccess {
                authenticate(true)
            }
            result.onFailure {
                showMigrationError()
            }
        }
    }

    private fun showRegistration() {
        loadingGroup.visible()

        supportGroup.gone()
        errorGroup.gone()

        loadingText.text = getString(
            R.string.setting_up_your_credential_on_this_device,
            EmbeddedSdk.config.appDisplayName
        )
    }

    private fun showAuthentication() {
        loadingGroup.visible()
        supportGroup.gone()
        errorGroup.gone()

        loadingText.text =
            getString(R.string.verifying_your_identity, EmbeddedSdk.config.appDisplayName)
    }

    private fun showAuthenticationAfterRegistration() {
        loadingText.text =
            context?.getString(
                R.string.credential_setup_on_this_device,
                EmbeddedSdk.config.appDisplayName
            )
    }

    private fun showRegistrationError() {
        loadingGroup.gone()

        supportGroup.visible()
        errorGroup.visible()

        errorDescription.text = Html.fromHtml(
            getString(R.string.could_not_setup_an_account),
            Html.FROM_HTML_MODE_LEGACY
        )
        errorDescription.setOnClickListener {
            runAction(actionType) { at ->
                when (at) {
                    is Registration -> {
                        showRegistration()
                        registerCredential(at.registerUri)
                    }
                    else -> showNoDataError()
                }
            }
        }
    }

    private fun showMigrationError() {
        loadingGroup.gone()

        supportGroup.visible()
        errorGroup.visible()

        errorDescription.text = Html.fromHtml(
            getString(R.string.could_not_setup_an_account),
            Html.FROM_HTML_MODE_LEGACY
        )
        errorDescription.setOnClickListener {
            dismiss()
        }
    }

    private fun showAuthenticationError() {
        loadingGroup.gone()

        supportGroup.visible()
        errorGroup.visible()

        errorDescription.text = Html.fromHtml(
            getString(R.string.could_not_complete_authentication),
            Html.FROM_HTML_MODE_LEGACY
        )
        errorDescription.setOnClickListener {
            runAction(actionType) { at ->
                when (at) {
                    Authentication -> {
                        showAuthentication()
                        authenticate(false)
                    }
                    else -> showNoDataError()
                }
            }
        }
    }

    private fun showNoDataError() {
        loadingGroup.gone()

        supportGroup.visible()
        errorGroup.visible()

        errorDescription.text = getString(R.string.could_not_perform_operation_no_data)
    }

    private fun clearFragments() {
        when (actionType) {
            is Migration -> {
                parentFragmentManager.fragments.forEach { fragment ->
                    if (fragment.tag == BiEnterCodeFragment.TAG ||
                        fragment.tag == BiScanQrCodeFragment.TAG ||
                        fragment.tag == BeyondIdentityRegistrationFragment.TAG
                    ) {
                        (fragment as BottomSheetDialogFragment).dismiss()
                    }
                }
                dismiss()
            }
            is Registration -> {
                dismiss()
            }
            Authentication -> {
                dismiss()
            }
        }
    }

    private fun <T> runAction(data: T?, block: (T) -> Unit) {
        data?.let { d ->
            block(d)
        } ?: run {
            showNoDataError()
        }
    }

    companion object {
        const val TAG = "bi-action-handler-fragment"

        const val ACTION_TYPE_ARG = "bi-action-type-arg"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param actionType Action to be handled
         *
         * @return A new instance of fragment BeyondIdentityActionHandlerFragment.
         */
        @JvmStatic
        fun newInstance(actionType: ActionType) =
            BeyondIdentityActionHandlerFragment().apply {
                arguments = bundleOf(ACTION_TYPE_ARG to actionType)
            }
    }
}

sealed class ActionType {
    @Parcelize
    data class Registration(val registerUri: String) : ActionType(), Parcelable

    @Parcelize
    object Authentication : ActionType(), Parcelable

    @Parcelize
    data class Migration(val code: String) : ActionType(), Parcelable
}

interface OnRegisterListener {
    fun onCredentialRegistered(credential: Credential)
    fun onCredentialRegistrationError(throwable: Throwable)
}

interface OnAuthenticationListener {
    fun onPublicClientAuthentication(token: TokenResponse)
    fun onConfidentialClientAuthentication(authorizationCode: String)
    fun onAuthenticationError(throwable: Throwable)
}
