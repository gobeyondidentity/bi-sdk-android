package com.beyondidentity.authenticator.sdk.android.embedded;

import com.beyondidentity.embedded.sdk.EmbeddedSdk;
import com.beyondidentity.embedded.sdk.models.Credential;
import com.beyondidentity.embedded.sdk.utils.JavaResult;
import com.beyondidentity.embedded.sdk.utils.JavaUtils;

import java.util.List;

import kotlin.Unit;
import timber.log.Timber;

public class JavaExampleUsage {
    public JavaExampleUsage() {
    }

    void getCredentials() {
        EmbeddedSdk.getCredentials(credentialResult -> {
            // Map kotlin.Result to JavaResult
            JavaResult<List<Credential>> credentialsJava = JavaUtils.toJavaResult(credentialResult);
            // Use JavaResult.type to switch on JavaResult.SUCCESS or case JavaResult.ERROR
            switch (credentialsJava.getType()) {
                case JavaResult.SUCCESS: {
                    Timber.d(credentialsJava.getData().toString());

                    Timber.d(credentialsJava.getData().get(0).getId());
                }
                case JavaResult.FAILURE: {
                    Timber.e(credentialsJava.getError());
                }
            }
            return Unit.INSTANCE;
        });
    }
}
