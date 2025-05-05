package com.example.contactsapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactsapp.data.ContactRepository
import com.example.contactsapp.domain.ContactModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ContactViewModel(private val repository: ContactRepository) : ViewModel() {

    private val _contacts = MutableStateFlow<List<ContactModel>>(emptyList())
    val contacts: StateFlow<List<ContactModel>> = _contacts

    private val _groupedContacts = MutableStateFlow<Map<Char, List<ContactModel>>>(emptyMap())
    val groupedContacts: StateFlow<Map<Char, List<ContactModel>>> = _groupedContacts

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadContacts() {
        viewModelScope.launch {
            runCatching {
                repository.getContacts()
            }.onSuccess { list ->
                val sorted = list.sortedBy { it.name.lowercase() }
                _contacts.value = sorted
                _groupedContacts.value = sorted.groupBy { it.name.firstOrNull()?.uppercaseChar() ?: '#' }
                _errorMessage.value = null // Очистить предыдущую ошибку, если все прошло успешно
            }.onFailure { e ->
                e.printStackTrace()
                _errorMessage.value = "Ошибка при загрузке контактов: ${e.message}"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
