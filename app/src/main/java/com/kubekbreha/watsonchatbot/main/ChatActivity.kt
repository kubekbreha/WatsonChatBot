package com.kubekbreha.watsonchatbot.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.ListenerRegistration
import com.kubekbreha.watsonchatbot.AppConstants
import com.kubekbreha.watsonchatbot.R
import com.kubekbreha.watsonchatbot.util.FirestoreUtil
import com.xwray.groupie.kotlinandroidextensions.Item


class ChatActivity : AppCompatActivity() {

    private lateinit var messagesListenerRegistration: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra(AppConstants.USER_NAME)


        val otherUserId = intent.getStringExtra(AppConstants.USER_ID)
        FirestoreUtil.getOrCreateChatChannel(otherUserId) { channelId ->
            messagesListenerRegistration =
                    FirestoreUtil.addChatMessagesListener(channelId, this, this::onMessagesChanged)
        }

    }

    private fun onMessagesChanged(messages: List<Item>) {
        Toast.makeText(this, "onMessagesChangedRunning", Toast.LENGTH_SHORT).show()
    }
}
