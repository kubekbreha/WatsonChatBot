package com.kubekbreha.watsonchatbot.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.firestore.ListenerRegistration
import com.kubekbreha.watsonchatbot.AppConstants
import com.kubekbreha.watsonchatbot.R
import com.kubekbreha.watsonchatbot.util.FirestoreUtil
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_chat.*


class ChatActivity : AppCompatActivity() {

    private lateinit var messagesListenerRegistration: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        //show transparent activity tab
        val w = window // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        act_chat_chatting_with?.text = intent.getStringExtra(AppConstants.USER_NAME)


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
