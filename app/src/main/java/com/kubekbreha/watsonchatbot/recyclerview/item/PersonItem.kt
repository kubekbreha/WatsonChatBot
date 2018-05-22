package com.kubekbreha.watsonchatbot.recyclerview.item

import com.kubekbreha.watsonchatbot.model.User
import android.content.Context
import com.kubekbreha.watsonchatbot.R
import com.kubekbreha.watsonchatbot.glide.GlideApp
import com.kubekbreha.watsonchatbot.util.StorageUtil
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.peoples_list_one_person.*

class PersonItem(
        val person: User,
        val userId: String,
        private val contex: Context) : Item() {


    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.peoples_list_one_person_name.text = person.name
        viewHolder.peoples_list_one_person_last_message.text = person.bio
        if(person.profilePicturePath != null) {
            viewHolder.peoples_list_one_person_name.text = person.name
            viewHolder.peoples_list_one_person_last_message.text = person.bio
            if(person.profilePicturePath != null)
                GlideApp.with(contex)
                        .load(StorageUtil.pathToReference(person.profilePicturePath))
                        .placeholder(R.drawable.setup_profile)
                        .into(viewHolder.peoples_list_one_person_image)
        }
    }

    override fun getLayout() = R.layout.peoples_list_one_person


}

