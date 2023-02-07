package com.beyondidentity.authenticator.sdk.android.embedded;

import com.beyondidentity.embedded.sdk.EmbeddedSdk;
import com.beyondidentity.embedded.sdk.models.Passkey;
import com.beyondidentity.embedded.sdk.utils.JavaResult;
import com.beyondidentity.embedded.sdk.utils.JavaUtils;

import java.util.List;

import kotlin.Unit;
import timber.log.Timber;

public class JavaExampleUsage {
    public JavaExampleUsage() {
    }

    void getPasskeys() {
        EmbeddedSdk.getPasskeys(passkeyResult -> {
            // Map kotlin.Result to JavaResult
            JavaResult<List<Passkey>> passkeysJava = JavaUtils.toJavaResult(passkeyResult);
            // Use JavaResult.type to switch on JavaResult.SUCCESS or case JavaResult.ERROR
            switch (passkeysJava.getType()) {
                case JavaResult.SUCCESS: {
                    Timber.d(passkeysJava.getData().toString());

                    Timber.d(passkeysJava.getData().get(0).getId());
                }
                case JavaResult.FAILURE: {
                    Timber.e(passkeysJava.getError());
                }
            }
            return Unit.INSTANCE;
        });
    }
}
