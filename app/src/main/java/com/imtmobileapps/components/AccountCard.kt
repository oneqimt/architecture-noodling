@file:OptIn(ExperimentalMaterialApi::class)

package com.imtmobileapps.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imtmobileapps.R
import com.imtmobileapps.model.Person
import com.imtmobileapps.model.State
import com.imtmobileapps.ui.theme.cardBackgroundColor
import com.imtmobileapps.ui.theme.cardBorderColor
import com.imtmobileapps.util.getDummyPerson
import logcat.logcat

@ExperimentalMaterialApi
@Composable
fun AccountCard(
    person: Person,

    ) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(10.dp),
        elevation = 4.dp,
        border = BorderStroke(0.3.dp, MaterialTheme.colors.cardBorderColor),
        backgroundColor = MaterialTheme.colors.cardBackgroundColor,
        shape = RoundedCornerShape(corner = CornerSize(6.dp))
    ) {
        val scaffoldState = rememberScaffoldState()

        val entry1 = State(id = 3, "Arizona", abbreviation = "AZ")
        val entry2 = State(id = 35, "Ohio", abbreviation = "OH")
        val entry3 = State(id = 1, "Alabama", abbreviation = "AL")
        val states = mutableListOf(entry1, entry2, entry3)

        Scaffold(scaffoldState = scaffoldState) {
            it.calculateTopPadding()
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(10.dp, 20.dp)

            ) {
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // FIRST NAME
                    person.firstName?.apply {
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = this,
                            label = { Text(text = stringResource(id = R.string.first_name)) },
                            onValueChange = {}
                        )
                    }
                }// end first name

                Spacer(modifier = Modifier.height(20.dp))

                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // LAST NAME
                    person.lastName?.apply {
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = this,
                            label = { Text(text = stringResource(id = R.string.last_name)) },
                            onValueChange = {}
                        )
                    }
                }// end last name

                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // EMAIL
                    person.email?.apply {
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = this,
                            label = { Text(text = stringResource(id = R.string.email)) },
                            onValueChange = {}
                        )
                    }
                }// end email
                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // ADDRESS
                    person.address?.apply {
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            value = this,
                            label = { Text(text = stringResource(id = R.string.address)) },
                            maxLines = 2,
                            onValueChange = {}
                        )
                    }
                }// end address

                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // PHONE
                    person.phone?.apply {
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = this,
                            label = { Text(text = stringResource(id = R.string.phone)) },
                            onValueChange = {}
                        )
                    }
                }// end phone

                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // CITY
                    person.city?.apply {
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = this,
                            label = { Text(text = stringResource(id = R.string.city)) },
                            onValueChange = {}
                        )
                    }
                }// end city

                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // STATE
                    person.state?.apply {
                        SpinnerCompose(
                            states = states,
                            preselectedState = person.state
                        ) {
                            logcat("AccountCard") { "state changed and it is ${it.name}" }
                        }
                    }
                }// end state

                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // ZIP
                    person.zip?.apply {
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = this,
                            label = { Text(text = stringResource(id = R.string.zip)) },
                            onValueChange = {}
                        )
                    }
                }// end zip
            }
        }
    }

}

@Preview
@Composable
fun AccountCardPreview(

) {
    AccountCard(person = getDummyPerson())
}