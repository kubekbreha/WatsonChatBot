package com.kubekbreha.watsonchatbot.recyclerview.item

import com.kubekbreha.watsonchatbot.model.User
import android.content.Context
import android.os.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.kubekbreha.watsonchatbot.R
import com.kubekbreha.watsonchatbot.glide.GlideApp
import com.kubekbreha.watsonchatbot.model.TextMessage
import com.kubekbreha.watsonchatbot.util.FirestoreUtil
import com.kubekbreha.watsonchatbot.util.StorageUtil
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_one_person.*
import java.text.SimpleDateFormat

class PersonItem(
        val person: User,
        val userId: String,
        private val contex: Context) : Item() {


    override fun bind(viewHolder: ViewHolder, position: Int) {
        //set time
        val dateFormat = SimpleDateFormat
                .getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)

        viewHolder.peoples_list_one_person_last_message_time.text = ""


        viewHolder.peoples_list_one_person_name.text = person.name
        viewHolder.peoples_list_one_person_last_message.text = ""


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

    override fun getLayout() = R.layout.item_one_person


}

