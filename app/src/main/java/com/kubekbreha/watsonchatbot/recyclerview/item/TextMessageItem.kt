package com.kubekbreha.watsonchatbot.recyclerview.item

import android.content.Context
import android.view.Gravity
import com.google.firebase.auth.FirebaseAuth
import com.kubekbreha.watsonchatbot.R
import com.kubekbreha.watsonchatbot.model.TextMessage
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_chat.*
import java.text.SimpleDateFormat


class TextMessageItem(val message: TextMessage,
                      val context: Context)
    : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.item_chat_text.text = message.text
        setTimeText(viewHolder)
    }

    private fun setTimeText(viewHolder: ViewHolder) {
        val dateFormat = SimpleDateFormat
                .getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)
        viewHolder.item_chat_time.text = dateFormat.format(message.time)
    }



    override fun getLayout(): Int {
       if( message.senderId == FirebaseAuth.getInstance().currentUser?.uid){
           return R.layout.item_chat
       }else{
           return R.layout.item_chat_other
       }
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is TextMessageItem)
            return false
        if (this.message != other.message)
            return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? TextMessageItem)
    }
}