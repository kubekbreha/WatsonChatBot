package com.kubekbreha.watsonchatbot.authentication.util

import android.app.Activity
import android.widget.Toast
import com.kubekbreha.watsonchatbot.authentication.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

object FirestoreUtil{

    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val currentUserDocRef: DocumentReference
        get() = firestoreInstance.document("users/${FirebaseAuth.getInstance().uid?:
        throw NullPointerException("UID is null.")}")


    fun initCurrentUserIfFirstTime(activity: Activity, onComplete: () -> Unit){
        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            if(!documentSnapshot.exists()){
                val newUser = User(FirebaseAuth.getInstance().currentUser?.displayName ?: "",
                        "", null)
                currentUserDocRef.set(newUser).addOnSuccessListener {
//                    Toast.makeText(activity, "Data saved.",
//                            Toast.LENGTH_SHORT).show()
                    onComplete()
                }
            }else{
//                Toast.makeText(activity, "Failed.",
//                        Toast.LENGTH_SHORT).show()
                onComplete()
            }
        }
    }


    fun updateCurrentUser(activity: Activity, name: String = "", bio: String = "", profilePicturePath: String? = null) {
        val userFieldMap = mutableMapOf<String, Any>()
        if(name.isNotBlank()) userFieldMap["name"] = name
        if(bio.isNotBlank()) userFieldMap["bio"] = bio
        if(profilePicturePath != null)
            userFieldMap["profilePicturePath"] = profilePicturePath

        currentUserDocRef.update(userFieldMap).addOnSuccessListener {
            Toast.makeText(activity, "Data saved.",
                    Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(activity, "Failed.",
                    Toast.LENGTH_SHORT).show()
        }
    }


    fun getCurrentUser(onComplete: (User) -> Unit){
        currentUserDocRef.get()
                .addOnSuccessListener {
                    onComplete(it.toObject(User::class.java)!!)
                }

    }
}