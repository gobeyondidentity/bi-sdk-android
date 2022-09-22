import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import com.beyondidentity.authenticator.sdk.android.SdkSelectorActivity
import org.junit.Rule
import org.junit.Test

class BasicTest {
    //@Rule
    //public ActivityScenarioRule<SdkSelectorActivity> mActivity = new ActivityScenarioRule<>(SdkSelectorActivity.class);
    @get:Rule
    var composeTestRule: ComposeContentTestRule = createAndroidComposeRule(
        SdkSelectorActivity::class.java
    )

    //onNodeWithText = onNode(hasText(text, substring, ignoreCase), useUnmergedTree)
    @Test
    @Throws(InterruptedException::class)
    fun changeText_sameActivity() {
        composeTestRule.onNode(
            matcher = hasText(text = "View Embedded SDK", substring = false, ignoreCase = false),
            useUnmergedTree = false
        ).performClick()
        composeTestRule.onNode(hasText("URL Validation")).performScrollTo()
        composeTestRule.onNode(hasText("Bind Credential URL")).performTextInput("Boof Pack")
        composeTestRule.onNode(hasText("Boof Pack")).assertExists("Boof pack DNE")
    }
}
