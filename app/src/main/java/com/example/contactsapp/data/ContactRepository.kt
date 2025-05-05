package com.example.contactsapp.data

import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import com.example.contactsapp.domain.ContactModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactRepository(private val context: Context) {

    suspend fun getContacts(): List<ContactModel> = withContext(Dispatchers.IO) {
        val contacts = mutableListOf<ContactModel>()
        try {
            val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
            val projection = arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            )

            val cursor = context.contentResolver.query(
                uri,
                projection,
                null,
                null,
                "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"
            )

            cursor?.use {
                val nameIndex = it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val numberIndex = it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
                while (it.moveToNext()) {
                    val name = it.getString(nameIndex) ?: ""
                    val number = it.getString(numberIndex) ?: ""
                    contacts.add(ContactModel(name, number))
                }
            }
        } catch (e: SecurityException) {
            // Например, разрешение не выдано
            Log.e("ContactRepository", "Error fetching contacts", e)
        } catch (e: Exception) {
            // Любая другая ошибка (например, база контактов недоступна)
            e.printStackTrace()
        }
        contacts
    }
}
