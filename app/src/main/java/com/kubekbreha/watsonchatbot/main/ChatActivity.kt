package com.kubekbreha.watsonchatbot.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.kubekbreha.watsonchatbot.AppConstants
import com.kubekbreha.watsonchatbot.R
import com.kubekbreha.watsonchatbot.model.MessageType
import com.kubekbreha.watsonchatbot.model.TextMessage
import com.kubekbreha.watsonchatbot.util.FirestoreUtil
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*


class ChatActivity : AppCompatActivity(), View.OnClickListener {


    private var shouldInitRecyclerView = true
    private lateinit var messagesSection: Section
    private lateinit var messagesListenerRegistration: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        act_chat_chatting_with?.text = intent.getStringExtra(AppConstants.USER_NAME)


        val otherUserId = intent.getStringExtra(AppConstants.USER_ID)
        FirestoreUtil.getOrCreateChatChannel(otherUserId) { channelId ->
            messagesListenerRegistration =
                    FirestoreUtil.addChatMessagesListener(channelId, this, this::updateRecyclerView)

            act_chat_send_button.setOnClickListener {
                val messageToSend =
                        TextMessage(act_chat_text_input.text.toString(), Calendar.getInstance().time,
                                FirebaseAuth.getInstance().currentUser!!.uid, MessageType.TEXT)
                act_chat_text_input.setText("")
                FirestoreUtil.sendMessage(messageToSend, channelId)
            }

//            act_chat_add_image.setOnClickListener {
//                //TODO: Send image messages
//            }
        }

        act_chat_btn_back_from_login.setOnClickListener(this)

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.act_chat_btn_back_from_login -> {
                onBackPressed()
            }

            else -> {
            }
        }
    }


    private fun updateRecyclerView(messages: List<Item>) {
        fun init() {
            act_chat_recycler_view.apply {
                layoutManager = LinearLayoutManager(this@ChatActivity)
                adapter = GroupAdapter<ViewHolder>().apply {
                    messagesSection = Section(messages)
                    this.add(messagesSection)
                }
            }
            shouldInitRecyclerView = false
        }

        fun updateItems() = messagesSection.update(messages)

        if (shouldInitRecyclerView)
            init()
        else
            updateItems()

        act_chat_recycler_view.scrollToPosition(act_chat_recycler_view.adapter.itemCount - 1)
    }
}
