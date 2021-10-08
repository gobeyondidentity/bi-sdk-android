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
import androidx.preference.PreferenceManager
import com.beyondidentity.embedded.embeddedui.R
import com.beyondidentity.embedded.embeddedui.ui.ActionType.Authentication
import com.beyondidentity.embedded.embeddedui.ui.ActionType.Migration
import com.beyondidentity.embedded.embeddedui.ui.ActionType.Registration
import com.beyondidentity.embedded.embeddedui.ui.EmbeddedUiConfig.AuthenticationData.ConfidentialClientData
import com.beyondidentity.embedded.embeddedui.ui.EmbeddedUiConfig.AuthenticationData.PublicClientData
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus.BiEvent
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus.BiEvent.Authorization
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus.BiEvent.BiEventError
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus.BiEvent.CredentialRegistered
import com.beyondidentity.embedded.sdk.EmbeddedSdk
import com.beyondidentity.embedded.sdk.utils.supportIntent
import com.google.android.material.card.MaterialCardView
import kotlinx.parcelize.Parcelize

const val MIGRATION_SOURCE = "bi-migration-source"
const val MIGRATION_SOURCE_SETTINGS = "bi-migration-source-settings"

class BeyondIdentityActionHandlerFragment : BiBaseBottomSheetDialogFragment() {
    private lateinit var loadingCardContainer: MaterialCardView
    private lateinit var migrationDoneContainer: MaterialCardView

    private lateinit var loadingText: AppCompatTextView
    private lateinit var loadingVisitSupport: AppCompatTextView
    private lateinit var errorDescription: AppCompatTextView
    private lateinit var errorPersists: AppCompatTextView

    private lateinit var migrationDoneText: AppCompatTextView
    private lateinit var migrationDoneButton: AppCompatTextView

    private lateinit var supportGroup: Group
    private lateinit var loadingGroup: Group
    private lateinit var errorGroup: Group

    private var actionType: ActionType? = null

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
        EmbeddedSdk.cancelExport {
            super.onDismiss(dialog)
        }
    }

    private fun setupViews(view: View) {
        loadingCardContainer = view.findViewById(R.id.loading_card_container)
        migrationDoneContainer = view.findViewById(R.id.migration_done_container)

        loadingText = view.findViewById(R.id.loading_text)
        loadingVisitSupport = view.findViewById(R.id.loading_visit_support)
        errorDescription = view.findViewById(R.id.error_description)
        errorPersists = view.findViewById(R.id.error_persists)

        migrationDoneText = view.findViewById(R.id.migration_done_text)
        migrationDoneText.text = getString(
            R.string.action_handler_migration_done_desc,
            EmbeddedUiConfig.config.appDisplayName
        )
        migrationDoneButton = view.findViewById(R.id.migration_done_button)

        supportGroup = view.findViewById(R.id.support_group)
        loadingGroup = view.findViewById(R.id.loading_group)
        errorGroup = view.findViewById(R.id.error_group)

        loadingVisitSupport.setOnClickListener {
            val intent = supportIntent(EmbeddedUiConfig.config.supportUrlOrEmail)
            startActivity(intent)
        }
    }

    private fun registerCredential(
        registerUri: String,
    ) {
        showRegistration()
        EmbeddedSdk.registerCredential(registerUri) { result ->
            result.onSuccess { credential ->
                authenticate(true)
                BiEventBus.post(CredentialRegistered(credential))
            }
            result.onFailure { throwable ->
                showRegistrationError()
                BiEventBus.post(BiEventError(throwable))
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

        when (val authenticateData = EmbeddedUiConfig.config.authenticationData) {
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
        EmbeddedSdk.authorize(
            clientId = clientId,
            redirectUri = redirectUri,
            scope = scope,
            pkceS256CodeChallenge = pkce,
        ) { result ->
            result.onSuccess { authorizationCode ->
                BiEventBus.post(Authorization(authorizationCode))
                clearFragments()
            }
            result.onFailure { throwable ->
                BiEventBus.post(BiEventError((throwable)))
                showAuthenticationError()
            }
        }
    }

    private fun authenticatePublicClient(
        clientId: String,
        redirectUri: String,
    ) {
        EmbeddedSdk.authenticate(
            clientId = clientId,
            redirectUri = redirectUri,
        ) { result ->
            result.onSuccess { tokenResponse ->
                BiEventBus.post(BiEvent.Authentication(tokenResponse))
                clearFragments()
            }
            result.onFailure { throwable ->
                BiEventBus.post(BiEventError((throwable)))
                showAuthenticationError()
            }
        }
    }

    private fun migration(
        code: String,
    ) {
        showRegistration()
        EmbeddedSdk.importCredentials(code) { result ->
            result.onSuccess { credList ->
                val prefs = PreferenceManager
                    .getDefaultSharedPreferences(context)
                val migrationSource = prefs
                    .getString(MIGRATION_SOURCE, "")

                if (migrationSource == MIGRATION_SOURCE_SETTINGS) {
                    loadingCardContainer.gone()
                    migrationDoneContainer.visible()
                    migrationDoneButton.setOnClickListener {
                        BiEventBus.post(CredentialRegistered(credList.first()))
                        clearFragments()
                    }
                } else {
                    authenticate(true)
                }

                prefs.edit()
                    .remove(MIGRATION_SOURCE)
                    .apply()
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
            R.string.action_handler_setting_up_your_credential,
            EmbeddedUiConfig.config.appDisplayName
        )
    }

    private fun showAuthentication() {
        loadingGroup.visible()
        supportGroup.gone()
        errorGroup.gone()

        loadingText.text =
            getString(R.string.action_handler_verifying_your_identity, EmbeddedUiConfig.config.appDisplayName)
    }

    private fun showAuthenticationAfterRegistration() {
        loadingText.text =
            context?.getString(
                R.string.action_handler_credential_setup_on_this_device,
                EmbeddedUiConfig.config.appDisplayName
            )
    }

    private fun showRegistrationError() {
        loadingGroup.gone()

        supportGroup.visible()
        errorGroup.visible()

        errorDescription.text = Html.fromHtml(
            getString(R.string.action_handler_could_not_setup_an_account),
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
            getString(R.string.action_handler_could_not_setup_an_account),
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
            getString(R.string.action_handler_could_not_complete_authentication),
            Html.FROM_HTML_MODE_LEGACY
        )
        errorPersists.text = Html.fromHtml(
            getString(R.string.action_handler_if_the_issue_persists_authentication),
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

        errorPersists.setOnClickListener {
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

        errorDescription.text = getString(R.string.action_handler_could_not_perform_operation_no_data)
    }

    private fun clearFragments() {
        when (actionType) {
            is Migration -> {
                parentFragmentManager.clearFragments(
                    setOf(
                        BeyondIdentityEnterCodeFragment.TAG,
                        BeyondIdentityScanQrCodeFragment.TAG,
                        BeyondIdentityRegistrationFragment.TAG,
                        BeyondIdentityAddCredentialFragment.TAG,
                        BeyondIdentityBeforeAuthFragment.TAG,
                    )
                )
                dismiss()
            }
            is Registration -> {
                dismiss()
            }
            Authentication -> {
                parentFragmentManager.clearFragments(setOf(BeyondIdentityBeforeAuthFragment.TAG))
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
