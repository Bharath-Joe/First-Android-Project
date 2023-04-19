package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tipcalculator.ui.theme.TipCalculatorTheme
import kotlin.math.ceil

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipCalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TipMainScreen()
                }
            }
        }
    }
}

@Composable
fun TipMainScreen(){
    var amountInput by remember { mutableStateOf("") }
    var tipInput by remember { mutableStateOf("") }
    var roundUp by remember { mutableStateOf(false) }
    var tips by remember { mutableStateOf(listOf<String>())}
    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0
    val tip = calculateTip(amount, tipPercent, roundUp)

    Column(
        modifier = Modifier.padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "Calculate Tip", 
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        EditNumberField(amountInput, "Bill Amount", onValueChanged = { amountInput = it })
        EditNumberField(tipInput, "Tip(%)", onValueChanged = { tipInput = it })
        RoundTheTipRow(roundUp, onSwitchChanged = { roundUp = it} )
        Text(
            text = "Tip Amount: $$tip",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                tips = tips + "Amount: $$amountInput\nTip Percent: $tipPercent%\nTip: $$tip"
            }
        ){
            Text(text = "Save Tip!")
        }

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            items(tips) {currentTip ->
                Divider ( )
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(10.dp),
                    text = currentTip
                )
            }
        }

    }
}

@Composable
fun RoundTheTipRow(roundUp: Boolean, onSwitchChanged: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Round up tip?")
        Switch(
            checked = roundUp,
            onCheckedChange = onSwitchChanged,
            colors = SwitchDefaults.colors(uncheckedThumbColor = Color.DarkGray),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
        )
    }
}

@Composable
fun EditNumberField(amountInput: String, label: String, onValueChanged: (String) -> Unit) {
    TextField(
        value = amountInput,
        onValueChange = onValueChanged,
        singleLine = true,
        label = { Text(text = label)},
        modifier = Modifier.fillMaxWidth()
    )
}
private fun calculateTip(amount: Double, tipPercent: Double = 15.0, roundUp: Boolean): String {
    return if (roundUp){
        String.format("%.2f", ceil(amount * (tipPercent / 100)))
    }
    else{
        String.format("%.2f", amount * (tipPercent / 100))
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TipCalculatorTheme {
        TipMainScreen()
    }
}