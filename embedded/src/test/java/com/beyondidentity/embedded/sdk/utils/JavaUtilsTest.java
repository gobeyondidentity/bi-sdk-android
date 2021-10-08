package com.beyondidentity.embedded.sdk.utils;

import static com.beyondidentity.embedded.sdk.utils.JavaUtilsTestHelper.getResultFailure;
import static com.beyondidentity.embedded.sdk.utils.JavaUtilsTestHelper.getResultSuccess;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.beyondidentity.embedded.sdk.models.PkceResponse;

import org.junit.Test;

import kotlin.Unit;


public class JavaUtilsTest {

    @Test
    public void toJavaResult_Success() {
        getResultSuccess("Hello", result -> {
            JavaResult<String> jr = JavaUtils.toJavaResult(result);

            assertEquals(jr.getData(), "Hello");
            assertEquals(jr.getType(), JavaResult.SUCCESS);
            assertNull(jr.getError());

            return Unit.INSTANCE;
        });

        getResultSuccess(123, result -> {
            JavaResult<Integer> jr = JavaUtils.toJavaResult(result);

            assertEquals(jr.getData(), new Integer(123));
            assertEquals(jr.getType(), JavaResult.SUCCESS);
            assertNull(jr.getError());

            return Unit.INSTANCE;
        });

        PkceResponse pkce = new PkceResponse("1", "2", "S256");
        getResultSuccess(pkce, result -> {
            JavaResult<PkceResponse> jr = JavaUtils.toJavaResult(result);

            assertEquals(jr.getData(), pkce);
            assertEquals(jr.getType(), JavaResult.SUCCESS);
            assertNull(jr.getError());

            return Unit.INSTANCE;
        });
    }

    @Test
    public void toJavaResult_Failure() {
        getResultFailure(new Throwable("Wrong String"), result -> {
            JavaResult<String> jr = JavaUtils.toJavaResult(result);

            assertNull(jr.getData());
            assertEquals(jr.getType(), JavaResult.FAILURE);
            assertEquals(jr.getError().getMessage(), "Wrong String");

            return Unit.INSTANCE;
        });
    }

}
