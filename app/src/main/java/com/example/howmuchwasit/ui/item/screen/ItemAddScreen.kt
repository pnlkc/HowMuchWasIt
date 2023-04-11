package com.example.howmuchwasit.ui.item.screen

import android.view.ContextThemeWrapper
import android.widget.CalendarView
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.howmuchwasit.MainActivity.Companion.screenHeightDp
import com.example.howmuchwasit.R
import com.example.howmuchwasit.ui.HowMuchWasItTopAppBar
import com.example.howmuchwasit.ui.home.ResultEmpty
import com.example.howmuchwasit.ui.item.*
import com.example.howmuchwasit.ui.item.viewmodel.ItemAddViewModel
import com.example.howmuchwasit.ui.navigation.NavigationDestination
import com.example.howmuchwasit.ui.theme.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

// 아이템 추가 화면 컴포저블
@Composable
fun ItemAddScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: ItemAddViewModel = hiltViewModel(),
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
            itemNameListUiState = viewModel.itemNameListUiState,
            searchTerm = viewModel.searchTerm,
            debounceSearchTerm = viewModel.debounceSearchTerm,
            onItemValueChanged = viewModel::updateItemUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveItem()
                    navigateBack()
                }
            },
            datePick = viewModel.datePick,
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
    datePick: MutableState<LocalDate>,
    itemNameListUiState: StateFlow<ItemNameListUiState>,
    searchTerm: MutableStateFlow<String>,
    debounceSearchTerm: StateFlow<ItemNameListUiState>,
) {
    // 포커스 관리하는 포커스 매니저
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
            itemNameListUiState = itemNameListUiState,
            searchTerm = searchTerm,
            debounceSearchTerm = debounceSearchTerm,
            onItemValueChanged = onItemValueChanged,
            datePick = datePick,
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
    datePick: MutableState<LocalDate>,
    itemNameListUiState: StateFlow<ItemNameListUiState>,
    searchTerm: MutableStateFlow<String>,
    debounceSearchTerm: StateFlow<ItemNameListUiState>,
) {
    // 컴포저블에 포커스가 있는지 확인하는 변수
    var isFocused by remember { mutableStateOf(false) }
    var needRecentNameDialog by rememberSaveable { mutableStateOf(false) }
    val nameList = itemNameListUiState.collectAsState().value.itemNameList

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
            onItemValueChanged = onItemValueChanged,
            datePick = datePick
        )

        ItemAddTextField(
            placeholderText = stringResource(id = R.string.input_product_name),
            leadingIconPainter = painterResource(id = R.drawable.product_name),
            trailingIconPainter = if (nameList.isEmpty()) {
                null
            } else {
                painterResource(id = R.drawable.outline_restore)
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            value = itemUiState.name,
            itemUiState = itemUiState,
            onItemValueChanged = onItemValueChanged,
            onRecentItemClick = {
                needRecentNameDialog = true
            },
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
            onItemValueChanged = onItemValueChanged,
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
            onItemValueChanged = onItemValueChanged,
        )
    }

    if (needRecentNameDialog) {
        RecentNameDialog(
            onDismissRequest = {
                needRecentNameDialog = false
                searchTerm.value = ""
            },
            onSearchItemClicked = { onItemValueChanged(itemUiState.copy(name = it)) },
            searchTerm = searchTerm,
            debounceSearchTerm = debounceSearchTerm,
        )
    }
}

// 날짜 선택 TextField 컴포저블
@Composable
fun DatePickTextField(
    modifier: Modifier = Modifier,
    itemUiState: ItemUiState,
    onItemValueChanged: (ItemUiState) -> Unit,
    datePick: MutableState<LocalDate>,
) {
    // DatePickerDialog를 보여줘야 하는지 결정하는데 필요한 변수
    var needDatePickerDialog by rememberSaveable { mutableStateOf(false) }

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
            .clickable { needDatePickerDialog = true }
    )

    if (needDatePickerDialog) {
        DatePicker(
            itemUiState = itemUiState,
            onDateSelected = {
                val date = it.format(DateTimeFormatter.ofPattern("yyyy / M / d"))
                onItemValueChanged(itemUiState.copy(date = date))
            },
            onDismissRequest = { needDatePickerDialog = false },
            datePick = datePick
        )
    }
}

// 아이템 정보 입력 컴포저블
@Composable
fun ItemAddTextField(
    modifier: Modifier = Modifier,
    placeholderText: String,
    leadingIconPainter: Painter? = null,
    trailingIconPainter: Painter? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    value: String,
    itemUiState: ItemUiState,
    onItemValueChanged: (ItemUiState) -> Unit,
    onRecentItemClick: () -> Unit = { },
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
            trailingIcon = if (trailingIconPainter == null) {
                null
            } else {
                {
                    Icon(
                        painter = trailingIconPainter,
                        contentDescription = null,
                        tint = Black,
                        modifier = modifier
                            .padding(vertical = 16.dp)
                            .clickable {
                                onRecentItemClick()
                            }
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

// 날짜 선택 다이얼로그
@Composable
fun DatePicker(
    itemUiState: ItemUiState,
    onDateSelected: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit,
    datePick: MutableState<LocalDate>,
) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties()
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    color = White,
                    shape = Shapes.medium
                )
        ) {
            Column(
                Modifier
                    .defaultMinSize(minHeight = 72.dp)
                    .fillMaxWidth()
                    .background(
                        color = Portage,
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
                    .padding(24.dp)
            ) {
                Text(
                    text = datePick.value.format(DateTimeFormatter.ofPattern("yyyy년")),
                    style = MaterialTheme.typography.h6,
                    color = LightGray
                )

                Text(
                    text = datePick.value.format(DateTimeFormatter.ofPattern("M월 d일 (E)")),
                    style = MaterialTheme.typography.h4,
                    color = White
                )
            }

            CustomCalendarView(
                itemUiState = itemUiState,
                onItemValueChanged = { datePick.value = it }
            )

            Spacer(modifier = Modifier.size(8.dp))

            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 16.dp, end = 16.dp)
            ) {
                TextButton(
                    onClick = onDismissRequest
                ) {
                    Text(
                        text = "취소",
                        style = MaterialTheme.typography.button,
                        color = Portage
                    )
                }

                TextButton(
                    onClick = {
                        onDateSelected(datePick.value)
                        onDismissRequest()
                    }
                ) {
                    Text(
                        text = "확인",
                        style = MaterialTheme.typography.button,
                        color = Portage
                    )
                }

            }
        }
    }
}

// 날짜 선택 다이얼로그 안에 들어가는 CalendarView
@Composable
fun CustomCalendarView(
    itemUiState: ItemUiState,
    modifier: Modifier = Modifier,
    onItemValueChanged: (LocalDate) -> Unit,
) {
    AndroidView(
        modifier = modifier.wrapContentSize(),
        factory = { context ->
            CalendarView(ContextThemeWrapper(context, R.style.CustomCalendarTheme))
        },
        update = { view ->
            // String을 LocalDate로 변환
            val date =
                LocalDate.parse(itemUiState.date, DateTimeFormatter.ofPattern("yyyy / M / d"))
            // date의 자정 날짜의 instant 객체 생성
            val instant = date.atStartOfDay(ZoneId.systemDefault()).toInstant()
            // instant 객체를 밀리초 단위의 Long 값으로 변환
            val timeInMillis = instant.toEpochMilli()

            // CalendarView 시작 값을 itemUiState.date이 변환값으로 설정
            view.date = timeInMillis

            view.setOnDateChangeListener { _, year, month, day ->
                onItemValueChanged(
                    LocalDate
                        .now()
                        .withYear(year)
                        .withMonth(month + 1)
                        .withDayOfMonth(day)
                )
            }

        }
    )
}

@Composable
fun RecentNameDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onSearchItemClicked: (String) -> Unit,
    searchTerm: MutableStateFlow<String>,
    debounceSearchTerm: StateFlow<ItemNameListUiState>,
) {
    val name = remember { mutableStateOf("") }
    val itemList = debounceSearchTerm.collectAsState().value.itemNameList
    val text = searchTerm.collectAsState().value

    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(),
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .heightIn(max = (screenHeightDp / 4 * 3).dp)
                .background(
                    color = White,
                    shape = Shapes.medium
                )
                .animateContentSize()
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(
                        color = Portage,
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
                    .padding(24.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.buy_list),
                    style = MaterialTheme.typography.h3,
                    color = White
                )
            }

            RecentNameDialogInputText(
                text = text,
                onValueChange = {
                    searchTerm.value = it
                    name.value = ""
                }
            )

            Box(
                modifier = modifier
                    .wrapContentSize()
            ) {
                LazyColumn(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, bottom = 64.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    if (itemList.isEmpty()) {
                        item {
                            ResultEmpty(
                                painterResource = painterResource(id = R.drawable.search_no_result_icon),
                                stringResource = stringResource(id = R.string.no_search_result),
                                bottomSpaceOff = true,
                                modifier = modifier.padding(vertical = 24.dp)
                            )
                        }
                    } else {
                        items(itemList) { string ->
                            Text(
                                text = string,
                                style = Typography.h3,
                                color = if (name.value == string) Portage else Black,
                                fontWeight = if (name.value == string) FontWeight.Bold else FontWeight.Medium,
                                modifier = modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        name.value = string
                                    }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 16.dp, end = 16.dp)
                        .heightIn(max = 32.dp)
                ) {
                    TextButton(
                        onClick = onDismissRequest
                    ) {
                        Text(
                            text = "취소",
                            style = MaterialTheme.typography.button,
                            color = Portage
                        )
                    }

                    TextButton(
                        onClick = {
                            onSearchItemClicked(name.value)
                            onDismissRequest()
                        }
                    ) {
                        Text(
                            text = "확인",
                            style = MaterialTheme.typography.button,
                            color = Portage
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecentNameDialogInputText(
    modifier: Modifier = Modifier,
    text: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    // 텍스트 선택 손잡이(textSelectHandle), 텍스트 선택 배경(textColorHighlight) 변경
    val customTextSelectionColors = TextSelectionColors(
        handleColor = MediumSlateBlue,
        backgroundColor = MediumSlateBlue.copy(alpha = 0.2f)
    )

    // 포커스 관리하는 포커스 매니저
    val focusManager = LocalFocusManager.current

    // customTextSelectionColors 값 적용
    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        OutlinedTextField(
            value = text,
            onValueChange = {
                onValueChange(it)
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                    tint = Black
                )
            },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.hint),
                    color = Color.Gray,
                    style = Typography.h3,
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
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp, horizontal = 12.dp),
        )
    }
}

@Preview(showBackground = false)
@Composable
fun CustomCalendarViewPreView() {
    HowMuchWasItTheme {
        DatePicker(
            itemUiState = ItemUiState(),
            onDateSelected = { },
            onDismissRequest = { },
            datePick = remember { mutableStateOf(LocalDate.now()) }
        )
    }
}

