package com.example.howmuchwasit.ui.item

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.provider.Settings.Secure.getString
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.howmuchwasit.R
import com.example.howmuchwasit.ui.HowMuchWasItTopAppBar
import com.example.howmuchwasit.ui.navigation.NavigationDestination
import com.example.howmuchwasit.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// 아이템 추가 화면 컴포저블
@Composable
fun ItemAddScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,
) {
    Scaffold(topBar = {
        HowMuchWasItTopAppBar(
            title = stringResource(id = NavigationDestination.AddItem.titleRes),
            canNavigateBack = canNavigateBack,
            navigateUp = onNavigateUp
        )
    }) { innerPadding ->
        ItemAddBody(
            modifier = modifier.padding(innerPadding)
        )
    }
}

// 아이템 추가 화면 Body 컴포저블
@Composable
fun ItemAddBody(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        ItemInputForm()
    }
}

// 아이템 추가 화면 항목들 컴포저블
@Composable
fun ItemInputForm(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DatePickTextField()

        ItemAddTextField(
            placeholderText = stringResource(id = R.string.input_product_name),
            leadingIconPainter = painterResource(id = R.drawable.product_name)
        )

        ItemAddTextField(
            placeholderText = stringResource(id = R.string.input_price),
            leadingIconPainter = painterResource(id = R.drawable.price)
        )

        ItemAddTextField(
            placeholderText = stringResource(id = R.string.input_quantity),
            leadingIconPainter = painterResource(id = R.drawable.quantity)
        )

        Box(modifier = modifier.weight(1f))

        ItemAddButton()
    }
}

// 날짜 선택 TextField 컴포저블
@Composable
fun DatePickTextField(
    modifier: Modifier = Modifier,
) {
    // calendar, dataState는 뷰모델로 이동 예정
    val calendar = Calendar.getInstance()
    val currentDate = DateTimeFormatter.ofPattern("yyyy / M / d").format(LocalDate.now())
    var dateState by remember { mutableStateOf(currentDate) }


    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, day ->
            dateState = "$year / $month / $day"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    OutlinedTextField(
        value = dateState,
        onValueChange = { },
        enabled = false,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_event_available_24),
                contentDescription = null,
                tint = Black,
                modifier = modifier.padding(vertical = 24.dp)
            )
        },
        placeholder = {
            Text(
                text = stringResource(id = R.string.pick_date),
                style = Typography.body2,
                color = Color.Gray
            )
        },
        shape = Shapes.medium,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            disabledBorderColor = GraySuit,
            textColor = Black,
            disabledTextColor = Black
        ),
        textStyle = Typography.body2,
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
) {
    var text by remember { mutableStateOf("") }

    // 텍스트 선택 손잡이(textSelectHandle), 텍스트 선택 배경(textColorHighlight) 변경
    val customTextSelectionColors = TextSelectionColors(
        handleColor = MediumSlateBlue,
        backgroundColor = MediumSlateBlue.copy(alpha = 0.2f)
    )

    // customTextSelectionColors 값 적용
    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        OutlinedTextField(
            value = text,
            onValueChange = { input -> text = input },
            leadingIcon = if (leadingIconPainter == null) {
                null
            } else {
                {
                    Icon(
                        painter = leadingIconPainter,
                        contentDescription = null,
                        tint = Black,
                        modifier = modifier.padding(vertical = 24.dp)
                    )
                }
            },
            placeholder = {
                Text(
                    text = placeholderText,
                    style = Typography.body2,
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
            textStyle = Typography.body2,
            singleLine = true,
            modifier = modifier
                .fillMaxWidth(),
        )
    }
}

// 저장 버튼 컴포저블
@Composable
fun ItemAddButton(
    modifier: Modifier = Modifier,
    onSaveClick: () -> Unit = { },
) {
    Button(
        onClick = onSaveClick,
//        enabled = itemUiState.actionEnabled,
        modifier = modifier
            .fillMaxWidth(),
        shape = Shapes.medium,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Portage,
            contentColor = White
        ),
        elevation = null
    ) {
        Text(
            text = stringResource(R.string.save),
            style = Typography.body2,
            modifier = modifier
                .padding(vertical = 10.dp)
        )
    }
}