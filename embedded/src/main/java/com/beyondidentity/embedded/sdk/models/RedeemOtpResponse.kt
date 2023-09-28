package com.beyondidentity.embedded.sdk.models
/**
 * A response returned after successfully authenticating.
 *
 * @property Success A response returned after successfully authenticating containing [AuthenticateResponse].
 * @property FailedOtp A response returned on failure to authenticate with the provided OTP code. Use [OtpChallengeResponse] to initiate a retry on either redeemOtp or authenticateOtp.
 */
sealed class RedeemOtpResponse {
    data class Success(val authenticateResponse: AuthenticateResponse) : RedeemOtpResponse()
    data class FailedOtp(val otpChallengeResponse: OtpChallengeResponse) : RedeemOtpResponse()
}
