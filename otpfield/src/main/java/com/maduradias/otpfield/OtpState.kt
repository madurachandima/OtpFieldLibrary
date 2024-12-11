package com.maduradias.otpfield

internal data class OtpState(
    internal val code: List<Int?> = (1..4).map { null },
    internal val focusedIndex: Int? = null,
    internal val isValid: Boolean? = null
)
