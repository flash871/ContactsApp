package com.example.contactsapp.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.contactsapp.presentation.ContactViewModel
import com.example.contactsapp.presentation.ui.components.ContactItem

@Composable
fun ContactsScreen(
    viewModel: ContactViewModel,
    onCallClick: (String) -> Unit
) {
    val groupedContacts by viewModel.groupedContacts.collectAsState()
    val groupedList = groupedContacts.entries.toList()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        if (groupedList.isEmpty()) {
            item {
                Text(
                    text = "Контакты не найдены",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            groupedList.forEach { (letter, contacts) ->
                item(key = "header_$letter") {
                    Column {
                        Divider(
                            color = Color.LightGray,
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        Text(
                            text = letter.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )

                        Divider(
                            color = Color.LightGray,
                            thickness = 1.dp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }

                items(contacts, key = { it.phoneNumber }) { contact ->
                    ContactItem(contact = contact, onCallClick = onCallClick)
                }
            }
        }
    }
}







