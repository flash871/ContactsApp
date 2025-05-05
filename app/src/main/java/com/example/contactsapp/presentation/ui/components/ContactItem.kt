package com.example.contactsapp.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.contactsapp.domain.ContactModel

@Composable
fun ContactItem(
    contact: ContactModel,
    onCallClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCallClick(contact.phoneNumber) }
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = contact.name, style = MaterialTheme.typography.titleMedium)
            Text(text = contact.phoneNumber, style = MaterialTheme.typography.bodyMedium)
        }

        Icon(
            imageVector = Icons.Default.Phone,
            contentDescription = "Позвонить",
            modifier = Modifier
                .padding(8.dp)

        )
    }
}
