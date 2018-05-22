package com.kubekbreha.watsonchatbot.recyclerview.item

import android.content.Context
import com.kubekbreha.watsonchatbot.R
import com.kubekbreha.watsonchatbot.model.TextMessage
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder


class TextMessageItem(val message: TextMessage,
                      val context: Context)
    : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        //TODO: Placeholder bind
    }

    override fun getLayout() = R.layout.item_chat

}