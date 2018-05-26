package com.kubekbreha.watsonchatbot.util

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.kubekbreha.watsonchatbot.model.*
import com.kubekbreha.watsonchatbot.recyclerview.item.PersonItem
import com.kubekbreha.watsonchatbot.recyclerview.item.TextMessageItem
import com.xwray.groupie.kotlinandroidextensions.Item
import java.text.SimpleDateFormat
import java.util.*
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull




object FirestoreUtil {

    private var TAG: String = "FirestoreUtil"

    private val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val currentUserDocRef: DocumentReference
        get() = firestoreInstance.document("users/${FirebaseAuth.getInstance().currentUser?.uid
                ?: throw NullPointerException("UID is null.")}")


    private val chatChannelsCollectionRef = firestoreInstance.collection("chatChannels")


    fun initCurrentUserIfFirstTime(activity: Activity, name: String, onComplete: () -> Unit) {
        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                val newUser = User(FirebaseAuth.getInstance().currentUser?.displayName ?: name,
                        "", null)
                currentUserDocRef.set(newUser).addOnSuccessListener {
                    //                    Toast.makeText(activity, "Data saved.",
//                            Toast.LENGTH_SHORT).show()
                    onComplete()
                }
            } else {
//                Toast.makeText(activity, "Failed.",
//                        Toast.LENGTH_SHORT).show()
                onComplete()
            }
        }
    }


    fun updateCurrentUser(activity: Activity, name: String = "", bio: String = "", profilePicturePath: String? = null) {
        val userFieldMap = mutableMapOf<String, Any>()
        if (name.isNotBlank()) userFieldMap["name"] = name
        if (bio.isNotBlank()) userFieldMap["bio"] = bio
        if (profilePicturePath != null)
            userFieldMap["profilePicturePath"] = profilePicturePath

        currentUserDocRef.update(userFieldMap).addOnSuccessListener {
            Toast.makeText(activity, "Data saved.",
                    Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(activity, "Failed.",
                    Toast.LENGTH_SHORT).show()
        }
    }


    fun getCurrentUser(onComplete: (User) -> Unit) {
        currentUserDocRef.get()
                .addOnSuccessListener {
                    onComplete(it.toObject(User::class.java)!!)
                }
    }


    fun addUsersListener(context: Context, onListen: (List<Item>) -> Unit): ListenerRegistration {
        return firestoreInstance.collection("users")
                //.orderBy("engagedChatChannels/lastMessage/lastMessage/time")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        Log.e("FIRESTORE", "Users listener error.", firebaseFirestoreException)
                        return@addSnapshotListener
                    }

                    val items = mutableListOf<Item>()
                    querySnapshot!!.documents.forEach {
                        if (it.id != FirebaseAuth.getInstance().currentUser?.uid)
                            items.add(PersonItem(it.toObject(User::class.java)!!, it.id, context))

                    }

                    onListen(items)
                }
    }


    fun removeListener(registration: ListenerRegistration) = registration.remove()

    fun getOrCreateChatChannel(otherUserId: String,
                               onComplete: (channelId: String) -> Unit) {
        currentUserDocRef.collection("engagedChatChannels")
                .document(otherUserId).collection("chanelId")
                .document("chanelId").get().addOnSuccessListener {
                    if (it.exists()) {
                        onComplete(it["channelId"] as String)
                        return@addOnSuccessListener
                    }

                    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

                    val newChannel = chatChannelsCollectionRef.document()
                    newChannel.set(ChatChannel(mutableListOf(currentUserId, otherUserId)))

                    currentUserDocRef
                            .collection("engagedChatChannels")
                            .document(otherUserId)
                            .collection("chanelId")
                            .document("chanelId")
                            .set(mapOf("channelId" to newChannel.id))

                    firestoreInstance.collection("users").document(otherUserId)
                            .collection("engagedChatChannels")
                            .document(currentUserId)
                            .collection("chanelId")
                            .document("chanelId")
                            .set(mapOf("channelId" to newChannel.id))

                    onComplete(newChannel.id)
                }
    }

    fun addChatMessagesListener(channelId: String, context: Context,
                                onListen: (List<Item>) -> Unit): ListenerRegistration {

        return chatChannelsCollectionRef.document(channelId).collection("messages")
                .document("allMessages")
                .collection("messages")
                .orderBy("time")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        Log.e("FIRESTORE", "ChatMessagesListener error.", firebaseFirestoreException)
                        return@addSnapshotListener
                    }


                    val items = mutableListOf<Item>()
                    querySnapshot!!.documents.forEach {
                        if (it["type"] == MessageType.TEXT)
                            items.add(TextMessageItem(it.toObject(TextMessage::class.java)!!, context))
                        else
                            TODO("Add image message.")
                    }
                    onListen(items)
                }
    }


    fun sendMessage(message: Message, channelId: String, otherUserId: String) {
        chatChannelsCollectionRef.document(channelId)
                .collection("messages")
                .document("allMessages")
                .collection("messages")
                .add(message)

        firestoreInstance.collection("users").document(currentUserId)
                .collection("engagedChatChannels")
                .document(otherUserId)
                .collection("lastMessage")
                .document("lastMessage")
                .set(message)

        firestoreInstance.collection("users").document(otherUserId)
                .collection("engagedChatChannels")
                .document(currentUserId)
                .collection("lastMessage")
                .document("lastMessage")
                .set(message)
    }

}