package com.example.whatsapp.presentation.mainscreen

import com.example.whatsapp.presentation.chat.ChatModel
import com.example.whatsapp.presentation.chat.MessageModel
import com.example.whatsapp.presentation.userregisterationscreen.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase,
) {

    private val _userList = MutableStateFlow<List<UserModel>>(emptyList())
    val userList: StateFlow<List<UserModel>> = _userList

    private val _currentUser = MutableStateFlow<UserModel?>(null)
    val currentUser: StateFlow<UserModel?> = _currentUser

    private val _chatMessages = MutableStateFlow<List<MessageModel>>(emptyList())
    val chatMessages: StateFlow<List<MessageModel>> = _chatMessages

    private val _chatList = MutableStateFlow<List<ChatModel>>(emptyList())
    val chatList: StateFlow<List<ChatModel>> = _chatList




    fun fetchChatList() {
        val userId = _currentUser.value?.userId ?: return

        val chatListRef = database.getReference().child("CHAT_LIST").child(userId)

        chatListRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempList = mutableListOf<ChatModel>()
                for (snap in snapshot.children) {
                    val chat = snap.getValue(ChatModel::class.java)
                    chat?.let { tempList.add(it) }
                }
                _chatList.value = tempList
            }

            override fun onCancelled(error: DatabaseError) {
                // Optionally log the error
            }
        })
    }

    suspend fun getCurrentUser() {
        try {
            val dataSnapshot = database.getReference("USERS")
                .child(auth.currentUser?.uid ?: "")
                .get()
                .await()

            val userModel = dataSnapshot.getValue(UserModel::class.java)
            _currentUser.value = userModel
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun fetchAllUsers() {
        val userRef = database.getReference().child("USERS")

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Empty the list before adding new data to avoid duplicates
                _userList.value = emptyList()

                val users = snapshot.children.mapNotNull {
                    it.getValue(UserModel::class.java)
                }
                _userList.value = users
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error if needed
            }
        })
    }

    fun sendMessage(
        senderRoom: String,
        receiverRoom: String,
        messageModel: MessageModel,
        senderName: String,
        receiverName: String,
        chatModelForSender: ChatModel,
        receiverId: String,
    ) {
        val senderId = _currentUser.value?.userId
        val senderImageProfile = currentUser.value?.profileimage
        database.getReference().child("CHATS").child(senderRoom)
            .push().setValue(messageModel)
            .addOnSuccessListener {
                if (messageModel.senderId.equals(messageModel.receiverId)) {
                    return@addOnSuccessListener
                }
                val receiveModel = messageModel.copy(
                    senderrecievername = "recieveby ${receiverName} from ${senderName}",
                    senderrecieverId = "recieveby ${messageModel.receiverId} from ${messageModel.senderId}"
                )
                database.getReference().child("CHATS").child(receiverRoom)
                    .push().setValue(receiveModel)
            }


        database.getReference().child("CHAT_LIST").child(senderId!!).child(receiverId)
            .setValue(chatModelForSender).addOnSuccessListener {


                val chatModelForReceiver = chatModelForSender.copy(
                    senderId = receiverId,
                    receiverId = senderId,
                    senderName = receiverName,
                    receiverName = senderName,
                    receiverProfileImage = senderImageProfile!!, // 👈 Tumhare current user ki image
                )

                database.getReference().child("CHAT_LIST").child(receiverId).child(senderId)
                    .setValue(chatModelForReceiver)

            }
    }

    fun listenToChatMessages(senderRoom: String) {
        val chatRef = database.getReference().child("CHATS").child(senderRoom)

        chatRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                _chatMessages.value = emptyList()
                val tempList = mutableListOf<MessageModel>()


                for (snap in snapshot.children) {
                    val msg = snap.getValue(MessageModel::class.java)
                    msg?.let { tempList.add(msg) }
                }
                _chatMessages.value = tempList
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

    }
}
