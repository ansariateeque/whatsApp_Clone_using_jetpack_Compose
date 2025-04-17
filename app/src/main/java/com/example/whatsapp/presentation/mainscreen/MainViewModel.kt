package com.example.whatsapp.presentation.mainscreen

import android.content.Context
import android.provider.ContactsContract
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whatsapp.presentation.chat.ChatModel
import com.example.whatsapp.presentation.chat.MessageModel
import com.example.whatsapp.presentation.userregisterationscreen.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
) : ViewModel() {

    init {
        viewModelScope.launch {
            // Fetch the current user first
            repository.getCurrentUser()

            // Then fetch chat list and all users
            repository.fetchChatList()
            repository.fetchAllUsers()
        }


    }

    val userList: StateFlow<List<UserModel>> = repository.userList

    val currentUser: StateFlow<UserModel?> = repository.currentUser

    val chatList = repository.chatList


    val chatMessages: StateFlow<List<MessageModel>> = repository.chatMessages

    private val _firebaseUsersInContacts = MutableStateFlow<List<UserModel>>(emptyList())
    val firebaseUsersInContacts: StateFlow<List<UserModel>> = _firebaseUsersInContacts


    private val _deviceContacts = MutableStateFlow<List<ContactModel>>(emptyList())
    val deviceContacts: StateFlow<List<ContactModel>> = _deviceContacts

    private val _nonWhatsAppContacts = MutableStateFlow<List<ContactModel>>(emptyList())
    val nonWhatsAppContacts: StateFlow<List<ContactModel>> = _nonWhatsAppContacts

    fun startListeningForMessages(senderRoom: String) {
        repository.listenToChatMessages(senderRoom)
    }







    fun sendMessageToFirebase(
        message: String,
        senderRoom: String,
        receiverRoom: String,
        receiverId: String,
        senderName: String,
        receiverName: String,
        recieverProfileImage: String,
    ) {
        val senderId=currentUser.value?.userId
        if (message.isBlank()) return

        val model = MessageModel(
            senderrecieverId = "sendby ${senderId} to $receiverId",
            senderId = senderId!!,
            senderrecievername = "sendby $senderName to $receiverName",
            receiverId = receiverId,
            message = message,
            timestamp = System.currentTimeMillis()
        )


        val chatModelForSender = ChatModel(
            senderId = senderId,
            receiverId = receiverId,
            senderName = senderName,
            receiverName = receiverName,
            receiverProfileImage = recieverProfileImage,
            lastMessage = message,
            timestamp = System.currentTimeMillis()
        )



        repository.sendMessage(
            senderRoom = senderRoom,
            receiverRoom = receiverRoom,
            receiverId = receiverId,
            messageModel = model,
            senderName = senderName,
            receiverName = receiverName,
            chatModelForSender = chatModelForSender
        )
    }

    fun fetchDeviceContacts(context: Context) {
        viewModelScope.launch {
            val contentResolver = context.contentResolver
            val contactList = mutableListOf<ContactModel>()

            val cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null
            )

            cursor?.use {
                while (it.moveToNext()) {
                    val name =
                        it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    var number =
                        it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))

                    // Clean the number: remove spaces, dashes, brackets
                    number = number.replace("[\\s()-]".toRegex(), "")

                    // Ensure number starts with +91
                    if (!number.startsWith("+")) {
                        if (number.length == 10) {
                            number = "+91$number"
                        }
                    }

                    contactList.add(ContactModel(name, number))
                }
            }

            _deviceContacts.value = contactList.distinctBy { it.phone }

            val firebaseNumbers = userList.value.map { it.phoneNUmber }

            _nonWhatsAppContacts.value =
                contactList.filter { contact -> !firebaseNumbers.contains(contact.phone) }

            val contactNumbers = contactList.map { it.phone }.toSet()
            val firebaseUsersInContactsList = userList.value.filter {
                contactNumbers.contains(it.phoneNUmber)
            }
            _firebaseUsersInContacts.value = firebaseUsersInContactsList

        }
    }


}
