package com.example.contactsapp.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.contactsapp.data.ContactRepository
import com.example.contactsapp.presentation.ui.ContactsScreen
import com.example.contactsapp.ui.theme.ContactsAppTheme

class MainActivity : ComponentActivity() {
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var viewModel: ContactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewModel = ContactViewModel(ContactRepository(applicationContext))

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val granted = permissions[Manifest.permission.READ_CONTACTS] == true &&
                    permissions[Manifest.permission.CALL_PHONE] == true
            if (granted) {
                viewModel.loadContacts()
            } else {
                Toast.makeText(
                    this,
                    "Для работы приложения нужно разрешение на чтение контактов и звонки",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        if (!hasPermissions()) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.CALL_PHONE
                )
            )
        } else {
            viewModel.loadContacts()
        }

        setContent {
            ContactsAppTheme {
                ContactsScreen(viewModel = viewModel) { phoneNumber ->
                    makeCall(phoneNumber)
                }
            }
        }
    }

    private fun hasPermissions(): Boolean {
        val read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
        val call = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
        return read == PackageManager.PERMISSION_GRANTED && call == PackageManager.PERMISSION_GRANTED
    }

    private fun makeCall(phoneNumber: String) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }
            startActivity(intent)
        } else {
            Toast.makeText(
                this,
                "Нет разрешения на звонки. Разрешите доступ в настройках.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
