package com.maduradias.otpfield


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow


@Composable
fun OtpFields(
    modifier: Modifier = Modifier,
    onKeyboardValueChange: (String) -> Unit = {},
    fieldBackgroundColor: Color? = Color.White,
    fieldBorderRadius: Dp? = 10.dp,
    fieldBorderColor: Color? = UI_Grey_Stroke_1,
    cursorColor: Color? = TextGrey_400,
    fieldTextStyle: TextStyle? = textStyle1,
    fieldCount: Int? = 4,
    isError: MutableState<Boolean> = mutableStateOf(false),
    errorBoarderColor: Color? = Color.Red,
    otpStateFlow: MutableStateFlow<OtpState>,
) {
    var otpFieldCount = 4
    if (fieldCount != null && fieldCount > 2)
        otpFieldCount = fieldCount


    val otpViewModel: OtpViewModel = viewModel(
        key = "otpViewModel-${otpStateFlow.hashCode()}",
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(OtpViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return OtpViewModel(otpStateFlow, otpFieldCount) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")

            }
        }
    )

    val state = otpViewModel.state.collectAsStateWithLifecycle()
    val focusRequesters = remember {
        List(otpFieldCount) { FocusRequester() }
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
        modifier = modifier,
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
                            otpViewModel.onAction(OtpAction.OnChangeFieldFocused(index))
                    },
                    onNumberChanged = { newNumber ->
                        if (newNumber != null) {
                            focusRequesters[index].freeFocus()
                        }

                        otpViewModel.onAction(OtpAction.OnEnterNumber(newNumber, index))
                        onKeyboardValueChange(otpViewModel.state.value.code.joinToString(""))
                    },
                    onKeyboardBack = {
                        otpViewModel.onAction(OtpAction.OnKeyboardBack)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    fieldBackgroundColor = fieldBackgroundColor,
                    fieldBorderRadius = fieldBorderRadius,
                    fieldBorderColor = fieldBorderColor,
                    cursorColor = cursorColor,
                    fieldTextStyle = fieldTextStyle,
                    isError = isError,
                    errorBoarderColor = errorBoarderColor,
                )
            }
        }


    }
}