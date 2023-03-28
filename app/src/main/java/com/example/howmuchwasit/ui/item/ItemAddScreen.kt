package com.example.howmuchwasit.ui.item

import android.app.DatePickerDialog
import android.icu.util.Calendar
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.howmuchwasit.R
import com.example.howmuchwasit.ui.AppViewModelProvider
import com.example.howmuchwasit.ui.HowMuchWasItTopAppBar
import com.example.howmuchwasit.ui.navigation.NavigationDestination
import com.example.howmuchwasit.ui.theme.*
import kotlinx.coroutines.launch

// 아이템 추가 화면 컴포저블
@Composable
fun ItemAddScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: ItemAddViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(topBar = {
        HowMuchWasItTopAppBar(
            title = stringResource(id = NavigationDestination.AddItem.titleRes),
            canNavigateBack = canNavigateBack,
            navigateUp = onNavigateUp
        )
    }) { innerPadding ->
        ItemAddBody(
            modifier = modifier.padding(innerPadding),
            navigateBack = navigateBack,
            itemUiState = viewModel.itemUiState,
            onItemValueChanged = viewModel::updateItemUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveItem()
                    // 전체 목록 화면으로 이동하도록 변경 예정
                    navigateBack()
                }
            }
        )
    }
}

// 아이템 추가 화면 Body 컴포저블
@Composable
fun ItemAddBody(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    itemUiState: ItemUiState,
    onItemValueChanged: (ItemUiState) -> Unit,
    onSaveClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = { focusManager.clearFocus() }
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        ItemInputForm(
            navigateBack = navigateBack,
            itemUiState = itemUiState,
            onItemValueChanged = onItemValueChanged
        )

        Spacer(modifier = modifier.weight(1f))

        ItemAddButton(
            itemUiState = itemUiState,
            onSaveClick = onSaveClick
        )
    }
}

// 아이템 추가 화면 항목들 컴포저블
@Composable
fun ItemInputForm(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    itemUiState: ItemUiState,
    onItemValueChanged: (ItemUiState) -> Unit,
) {
    var isFocused by remember { mutableStateOf(false) }

    // 포커스 관리하는 포커스 매니저
    val focusManager = LocalFocusManager.current

    // 백버튼 동작 설정하는 코드
    BackHandler {
        if (isFocused) focusManager.clearFocus() else navigateBack()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { isFocused = it.hasFocus },
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DatePickTextField(
            itemUiState = itemUiState,
            onItemValueChanged = onItemValueChanged
        )

        ItemAddTextField(
            placeholderText = stringResource(id = R.string.input_product_name),
            leadingIconPainter = painterResource(id = R.drawable.product_name),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            value = itemUiState.name,
            itemUiState = itemUiState,
            onItemValueChanged = onItemValueChanged
        )

        ItemAddTextField(
            placeholderText = stringResource(id = R.string.input_price),
            leadingIconPainter = painterResource(id = R.drawable.price),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = itemUiState.price,
            itemUiState = itemUiState,
            onItemValueChanged = onItemValueChanged
        )

        ItemAddTextField(
            placeholderText = stringResource(id = R.string.input_quantity),
            leadingIconPainter = painterResource(id = R.drawable.quantity),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            value = itemUiState.quantity,
            itemUiState = itemUiState,
            onItemValueChanged = onItemValueChanged
        )
    }
}

// 날짜 선택 TextField 컴포저블
@Composable
fun DatePickTextField(
    modifier: Modifier = Modifier,
    itemUiState: ItemUiState,
    onItemValueChanged: (ItemUiState) -> Unit,
) {
    // Calender 인스턴스 생성
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, day ->
            onItemValueChanged(itemUiState.copy(date = "$year / $month / $day"))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    OutlinedTextField(
        value = itemUiState.date,
        onValueChange = { },
        enabled = false,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_event_available_24),
                contentDescription = null,
                tint = Black,
                modifier = modifier.padding(vertical = 16.dp)
            )
        },
        placeholder = {
            Text(
                text = stringResource(id = R.string.pick_date),
                style = Typography.h3,
                color = Color.Gray
            )
        },
        shape = Shapes.medium,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            disabledBorderColor = GraySuit,
            textColor = Black,
            disabledTextColor = Black
        ),
        textStyle = Typography.h3,
        // singleLine = true 설정 안 하면 center vertical 하지 않음
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            // 클릭 했을 때 리플도 모양에 맞춰 나오게 하려면 clip(Shapes.medium) 필요
            .clip(Shapes.medium)
            .clickable {
                datePickerDialog.show()
            }
    )
}

// 아이템 정보 입력 컴포저블
@Composable
fun ItemAddTextField(
    modifier: Modifier = Modifier,
    placeholderText: String,
    leadingIconPainter: Painter? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    value: String,
    itemUiState: ItemUiState,
    onItemValueChanged: (ItemUiState) -> Unit,
) {
    // 텍스트 선택 손잡이(textSelectHandle), 텍스트 선택 배경(textColorHighlight) 변경
    val customTextSelectionColors = TextSelectionColors(
        handleColor = MediumSlateBlue,
        backgroundColor = MediumSlateBlue.copy(alpha = 0.2f)
    )

    // 포커스 관리하는 포커스 매니저
    val focusManager = LocalFocusManager.current

    // getString() 사용하기 위한 context
    val context = LocalContext.current

    // price와 quantity 값이 Int 범위를 넘지 않도록 길이 제한
    val maxLength = 9

    // customTextSelectionColors 값 적용
    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                when (placeholderText) {
                    context.getString(R.string.input_product_name) -> {
                        onItemValueChanged(itemUiState.copy(name = it))
                    }
                    context.getString(R.string.input_price) -> {
                        if (it.length <= maxLength) onItemValueChanged(itemUiState.copy(price = it))
                    }
                    else -> if (it.length <= maxLength) onItemValueChanged(itemUiState.copy(quantity = it))
                }
            },
            leadingIcon = if (leadingIconPainter == null) {
                null
            } else {
                {
                    Icon(
                        painter = leadingIconPainter,
                        contentDescription = null,
                        tint = Black,
                        modifier = modifier.padding(vertical = 16.dp)
                    )
                }
            },
            placeholder = {
                Text(
                    text = placeholderText,
                    style = Typography.h3,
                    color = Color.Gray
                )
            },
            shape = Shapes.medium,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = GraySuit,
                focusedBorderColor = MediumSlateBlue,
                cursorColor = MediumSlateBlue,
                textColor = Black,
                disabledTextColor = Black
            ),
            textStyle = Typography.h3,
            singleLine = true,
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            isError = when (placeholderText) {
                context.getString(R.string.input_price) -> !itemUiState.isPriceDigitsOnly()
                context.getString(R.string.input_quantity) -> !itemUiState.isQuantityDigitsOnly()
                else -> false
            },
            modifier = modifier
                .fillMaxWidth(),
        )
    }
}

// 저장 버튼 컴포저블
@Composable
fun ItemAddButton(
    modifier: Modifier = Modifier,
    itemUiState: ItemUiState,
    onSaveClick: () -> Unit,
) {
    Button(
        onClick = onSaveClick,
        enabled = itemUiState.isValid(),
        modifier = modifier
            .fillMaxWidth(),
        shape = Shapes.medium,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Portage,
            contentColor = White,
            disabledBackgroundColor = Gray,
            disabledContentColor = White
        ),
        elevation = null
    ) {
        Text(
            text = stringResource(R.string.save),
            style = Typography.h3,
            modifier = modifier
                .padding(vertical = 10.dp)
        )
    }
}