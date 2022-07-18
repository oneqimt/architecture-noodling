package com.imtmobileapps.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imtmobileapps.R
import com.imtmobileapps.model.State

@ExperimentalMaterialApi
@Composable
fun SpinnerCompose(
    states: List<State>,
    preselectedState: State?,
    onSelectionChanged: (selection: State) -> Unit,
    ) {
    var selected by remember { mutableStateOf(preselectedState) }
    var expanded by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            selected?.abbreviation?.let { abbr ->
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = abbr,
                    label = { (Text(text = stringResource(id = R.string.state))) },
                    onValueChange = {}
                )

                DropdownMenu(
                    modifier = Modifier.fillMaxWidth(),
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    states.forEach { state ->

                        DropdownMenuItem(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                selected = state
                                expanded = false
                                onSelectionChanged(state)
                            },

                            ) {
                            state.abbreviation?.let { abbr ->
                                Text(
                                    modifier = Modifier.wrapContentWidth(Alignment.Start),
                                    text = abbr,
                                    style = MaterialTheme.typography.subtitle2
                                )
                            }
                        }
                    }
                }
            }
        }
        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Transparent)
                .padding(10.dp)
                .clickable(
                    onClick = { expanded = !expanded }
                )
        )

    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun SpinnerComposable_Preview() {
    MaterialTheme {

        val entry1 = State(id = 3, "Arizona", abbreviation = "AZ")
        val entry2 = State(id = 35, "Ohio", abbreviation = "OH")
        val entry3 = State(id = 1, "Alabama", abbreviation = "AL")

        SpinnerCompose(

            listOf(entry1, entry2, entry3),
            preselectedState = entry2
        ) { selected -> /* do something with selected */ }
    }
}

