package com.example.howmuchwasit.ui.item

import android.app.DatePickerDialog
import android.icu.util.Calendar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.howmuchwasit.R
import com.example.howmuchwasit.ui.HowMuchWasItTopAppBar
import com.example.howmuchwasit.ui.navigation.NavigationDestination
import com.example.howmuchwasit.ui.theme.*

@Composable
fun ItemAddScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,
) {
    Scaffold(
        topBar = {
            HowMuchWasItTopAppBar(
                title = stringResource(id = NavigationDestination.AddItem.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        ItemAddBody(
            modifier = modifier.padding(innerPadding)
        )
    }
}

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

@Composable
fun ItemInputForm(
    modifier: Modifier = Modifier,
) {
    // calendar, dataState는 뷰모델로 이동 예정
    val calendar = Calendar.getInstance()
    var dateState by remember { mutableStateOf("") }

    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, day ->
            dateState = "${year}년 ${month + 1}월 ${day}일"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = dateState,
            onValueChange = { },
            enabled = false,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_event_available_24),
                    contentDescription = null,
                    tint = Black
                )
            },
            placeholder = {
                Text(
                    text = "날짜를 고르세요",
                    style = Typography.body2,
                    color = Color.Gray
                )
            },
            shape = Shapes.medium,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Portage,
                focusedBorderColor = MediumSlateBlue,
                textColor = Black,
                disabledTextColor = Black
            ),
            textStyle = Typography.body2,
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    datePickerDialog.show()
                }
        )
    }
}