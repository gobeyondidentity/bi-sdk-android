package com.beyondidentity.embedded.embeddedui.ui

import androidx.fragment.app.FragmentManager
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus.BiEvent.BiEventError
import com.beyondidentity.embedded.sdk.EmbeddedSdk

/**
 * Call this function from your own button to present custom Beyond Identity UI and begin the passwordless experience.
 *
 * @param fm Instance of the [FragmentManager] to control the BottomSheetDialog
 */
fun continueWithBeyondIdentity(fm: FragmentManager) {
    EmbeddedSdk.getCredentials { result ->
        result.onSuccess { credentials ->
            if (credentials.isNotEmpty()) {
                BeyondIdentityBeforeAuthFragment
                    .newInstance()
                    .show(fm, BeyondIdentityBeforeAuthFragment.TAG)
            } else {
                val registrationFragment = BeyondIdentityRegistrationFragment.newInstance(false)

                registrationFragment.show(fm, BeyondIdentityRegistrationFragment.TAG)
            }
        }
        result.onFailure {
            BiEventBus.post(BiEventError(it))
        }
    }
}
