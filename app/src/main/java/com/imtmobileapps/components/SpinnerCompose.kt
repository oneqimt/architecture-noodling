package com.imtmobileapps.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imtmobileapps.R
import com.imtmobileapps.model.State
import com.imtmobileapps.ui.theme.cardBorderColor
import com.imtmobileapps.ui.theme.fabIconBackgroundColor

@ExperimentalMaterialApi
@Composable
fun SpinnerCompose(
    states: List<State>?,
    preselectedState: State?,
    onSelectionChanged: (selection: State) -> Unit,
) {
    var selected by remember { mutableStateOf(preselectedState) }
    var expanded by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Column(
            modifier = Modifier.fillMaxWidth()) {
            selected?.abbreviation?.let { abbr ->
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = abbr,
                    label = { (Text(text = stringResource(id = R.string.state))) },
                    trailingIcon = {
                        IconButton(onClick = {
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "ArrowDropDown",
                                tint = MaterialTheme.colors.fabIconBackgroundColor,
                                modifier = Modifier
                                    .clickable {
                                        expanded = !expanded
                                    }
                                    .scale(1f, if (expanded) -1f else 1f)
                            )
                        }
                    },
                    onValueChange = {
                        // may not need
                    },
                    shape = RoundedCornerShape(CornerSize(6.dp),
                        CornerSize(6.dp),
                        CornerSize(1.dp),
                        CornerSize(1.dp))
                )

                DropdownMenu(
                    modifier = Modifier.fillMaxWidth(.7f),
                    expanded = expanded,
                    onDismissRequest = { expanded = false },

                ) {
                    states?.forEach { state ->

                        DropdownMenuItem(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                selected = state
                                expanded = false
                                onSelectionChanged(state)
                            },

                            ) {

                            val stateAbbr = "${state.abbreviation}"
                            val stateName = "${state.name}"
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp, 20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween)
                            {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = stateAbbr,
                                    style = MaterialTheme.typography.subtitle2
                                )

                                Text(
                                    text = stateName,
                                    style = MaterialTheme.typography.subtitle2
                                )
                            }
                        }

                        Divider(
                            color = MaterialTheme.colors.cardBorderColor,
                            modifier = Modifier
                                .fillMaxWidth()
                                .width(1.dp))
                    }
                }
            }
        }// end column
        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Transparent)
                .padding(10.dp)
                .clickable(
                    onClick = { expanded = !expanded }
                )
        )

    }// end box
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun SpinnerComposable_Preview() {
    MaterialTheme {

        val state1 = State(id = 3, "Arizona", abbreviation = "AZ")
        val state2 = State(id = 35, "Ohio", abbreviation = "OH")
        val state3 = State(id = 1, "Alabama", abbreviation = "AL")

        SpinnerCompose(

            listOf(state1, state2, state3),
            preselectedState = state2,
            onSelectionChanged = {}
        )
    }
}

