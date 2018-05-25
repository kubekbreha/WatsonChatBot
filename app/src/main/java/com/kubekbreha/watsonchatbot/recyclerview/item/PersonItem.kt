package com.kubekbreha.watsonchatbot.recyclerview.item

import com.kubekbreha.watsonchatbot.model.User
import android.content.Context
import android.os.Message
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
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

    private val TAG = "PERSON ITEM"
    private val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

    override fun bind(viewHolder: ViewHolder, position: Int) {

        //last message and time of last message in peoples fragment
        val dateFormat = SimpleDateFormat
                .getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)

        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users").document(userId)
                .collection("engagedChatChannels")
                .document(currentUserId)
                .collection("lastMessage")
                .document("lastMessage")

        docRef.get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists()) {

                    val mess = document.toObject(TextMessage::class.java)!!

                    viewHolder.peoples_list_one_person_last_message_time.text = dateFormat.format(mess.time)
                    viewHolder.peoples_list_one_person_last_message.text = mess.text

                } else {
                    Log.d(TAG, "No such document")
                }
            } else {
                Log.d(TAG, "get failed with ", task.exception)
            }
        })


        viewHolder.peoples_list_one_person_name.text = person.name

        if (person.profilePicturePath != null) {
            viewHolder.peoples_list_one_person_name.text = person.name
            viewHolder.peoples_list_one_person_last_message.text = person.bio
            if (person.profilePicturePath != null)
                GlideApp.with(contex)
                        .load(StorageUtil.pathToReference(person.profilePicturePath))
                        .placeholder(R.drawable.setup_profile)
                        .into(viewHolder.peoples_list_one_person_image)
        }
    }

    override fun getLayout() = R.layout.item_one_person

}

