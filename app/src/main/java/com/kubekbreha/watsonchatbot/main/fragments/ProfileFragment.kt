package com.kubekbreha.watsonchatbot.main.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.PopupMenu
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.kubekbreha.watsonchatbot.R
import com.kubekbreha.watsonchatbot.authentication.AuthenticationActivity
import com.kubekbreha.watsonchatbot.authentication.SettingsActivity
import com.kubekbreha.watsonchatbot.glide.GlideApp
import com.kubekbreha.watsonchatbot.util.FirestoreUtil
import com.kubekbreha.watsonchatbot.util.StorageUtil
import com.mikhaellopez.circularimageview.CircularImageView


class ProfileFragment : Fragment() {

    private var mAuth: FirebaseAuth? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var btnSettings: ImageButton? = null
    private var profileImage: CircularImageView? = null
    private var profileBio: TextView? = null

    companion object {
        val TAG: String = ProfileFragment::class.java.simpleName
        fun newInstance() = ProfileFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        initialize(view)

        return view
    }

    private fun initialize(view: View) {
        btnSettings = view.findViewById<View>(R.id.frag_profile_settings_button) as ImageButton
        profileImage = view.findViewById<View>(R.id.frag_profile_person_image) as CircularImageView
        profileBio = view.findViewById<View>(R.id.frag_profile_user_bio) as TextView

        mAuth = FirebaseAuth.getInstance()

        val googleSignInOpt = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(context!!)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOpt)
                .build()

        mGoogleApiClient!!.connect()

        btnSettings!!.setOnClickListener {
        val popupMenu = PopupMenu(activity!!, it, Gravity.END)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.settings -> {
                        val intent = Intent(activity, SettingsActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        true
                    }
                    R.id.help -> {
                        Toast.makeText(activity, "Help clicked.", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.logout -> {
                        logOutUser()
                        true
                    }
                    else -> false
                }
            }

            popupMenu.inflate(R.menu.menu_settengs_drop)

            try {
                val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
                fieldMPopup.isAccessible = true
                val mPopup = fieldMPopup.get(popupMenu)
                mPopup.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                        .invoke(mPopup, true)
            } catch (e: Exception) {
                Log.e("Main", "Error showing icon menu, ", e)
            } finally {
                popupMenu.show()
            }
            popupMenu.show()
        }
    }



    fun logOutUser() {
        mAuth!!.signOut()
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback { updateUI() }
        this.onDestroy()
    }


    private fun updateUI() {
        val accountIntent = Intent(activity, AuthenticationActivity::class.java)
        startActivity(accountIntent)
        activity!!.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        activity!!.finish()
    }


    override fun onStart() {
        super.onStart()
        FirestoreUtil.getCurrentUser { user ->
            profileBio!!.text = user.bio
            if ( user.profilePicturePath != null)
                GlideApp.with(this)
                        .load(StorageUtil.pathToReference(user.profilePicturePath))
                        .placeholder(R.drawable.setup_profile)
                        .into(profileImage!!)
        }

    }

}