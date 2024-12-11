package com.maduradias.otpfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun OtpScreen(
    modifier: Modifier = Modifier,
    onKeyboardValueChange: (String) -> Unit = {}

) {

    val otpViewModel = viewModel<OtpViewModel>()
    val state = otpViewModel.state.collectAsStateWithLifecycle()
    val focusRequesters = remember {
        List(4) { FocusRequester() }
    }
    val focusManager = LocalFocusManager.current
    val keyboardManager = LocalSoftwareKeyboardController.current

    LaunchedEffect(state.value.focusedIndex) {
        state.value.focusedIndex?.let { index ->
            focusRequesters.getOrNull(index)?.requestFocus()
        }
    }

    LaunchedEffect(state.value.code, keyboardManager) {
        val allNumbersEntered = state.value.code.none { it == null }
        if (allNumbersEntered) {
            focusRequesters.forEach {
                it.freeFocus()
            }
            focusManager.clearFocus()
            keyboardManager?.hide()
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                8.dp,
                alignment = Alignment.CenterHorizontally
            )
        ) {

            state.value.code.forEachIndexed { index, number ->
                OtpInputField(
                    number = number,
                    focusRequester = focusRequesters[index],
                    onFocusChanged = { isFocused ->
                        if (isFocused)
//                            onAction(OtpAction.OnChangeFieldFocused(index))
                            otpViewModel.onAction(OtpAction.OnChangeFieldFocused(index))
                    },
                    onNumberChanged = { newNumber ->
//                        onAction(OtpAction.OnEnterNumber(newNumber, index))
                        if (newNumber != null) {
                            focusRequesters[index].freeFocus()
                        }

                        otpViewModel.onAction(OtpAction.OnEnterNumber(newNumber, index))
                        onKeyboardValueChange(otpViewModel.state.value.code.joinToString(""))
                    },
                    onKeyboardBack = {
//                        onAction(OtpAction.OnKeyboardBack)
                        otpViewModel.onAction(OtpAction.OnKeyboardBack)
                    }, modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                )
            }
        }
//        state.value.isValid?.let {
//            Text(
//                text = if (it) "OTP is Valid" else "OTP is Invalid!",
//                color = if (it) Color.Green else Color.Red,
//                fontSize = 16.sp
//            )
//        }

    }
}