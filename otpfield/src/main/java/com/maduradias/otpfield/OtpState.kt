package com.maduradias.otpfield

 data class OtpState(
     internal var code: List<Int?> = listOf(),
     internal val focusedIndex: Int? = null,
     internal val isValid: Boolean? = null
)
