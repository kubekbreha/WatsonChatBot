package com.kubekbreha.watsonchatbot.authentication.fragments


import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kubekbreha.watsonchatbot.R
import com.kubekbreha.watsonchatbot.main.MainActivity
import com.kubekbreha.watsonchatbot.util.FirestoreUtil

class RegisterFragment : Fragment(), View.OnClickListener {

    private val TAG = "RegisterActivity"

    //global variables
    private var userName: String? = null
    private var email: String? = null
    private var password: String? = null

    //UI elements
    private var editUsername: EditText? = null
    private var editEmail: EditText? = null
    private var editPassword: EditText? = null
    private var btnCreateAccount: Button? = null
    private var mProgressBar: ProgressDialog? = null
    private var btnBack: ImageButton? = null

    //Firebase references
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null



    // Google API Client object.
    var googleApiClient: GoogleApiClient? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_register, container, false)
        initialise(view)
        return view
    }


    private fun initialise(view: View) {
        mAuth = FirebaseAuth.getInstance()

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.request_client_id))
                .requestEmail()
                .build()

        // Creating and Configuring Google Api Client.
        googleApiClient = GoogleApiClient.Builder(activity!!)
                .enableAutoManage(activity!!) { }
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build()

        editUsername = view.findViewById<View>(R.id.frag_register_edit_username) as EditText
        editEmail = view.findViewById<View>(R.id.frag_register_edit_email) as EditText
        editPassword = view.findViewById<View>(R.id.frag_register_edit_password) as EditText
        btnCreateAccount = view.findViewById<View>(R.id.frag_register_btn_register) as Button
        btnBack = view.findViewById<View>(R.id.frag_register_btn_back_from_register) as ImageButton


        mProgressBar = ProgressDialog(activity)
        mDatabase = FirebaseDatabase.getInstance()
        //mDatabaseReference = mDatabase!!.reference!!.child("Users")

        btnCreateAccount!!.setOnClickListener(this)
        btnBack!!.setOnClickListener(this)

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.frag_register_btn_register -> {
                createNewAccount()
            }

            R.id.frag_register_btn_back_from_register -> {
                activity!!.onBackPressed()
            }

            else -> {
            }
        }
    }


    private fun createNewAccount() {

        userName = editUsername?.text.toString()
        email = editEmail?.text.toString()
        password = editPassword?.text.toString()


        if (!TextUtils.isEmpty(userName)
                && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            mProgressBar!!.setMessage("Registering User...")
            mProgressBar!!.show()

            mAuth!!
                    .createUserWithEmailAndPassword(email!!, password!!)
                    .addOnCompleteListener(activity!!) { task ->
                        mProgressBar!!.hide()
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            val userId = mAuth!!.currentUser!!.uid
                            //Verify Email
                            verifyEmail();
                            //update user profile information
                            //val currentUserDb = mDatabaseReference!!.child(userId)
                            //currentUserDb.child("userName").setValue(userName)
                            FirestoreUtil.initCurrentUserIfFirstTime(activity!!, userName!!) {
                                updateUserInfoAndUI()
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }

        } else {
            Toast.makeText(activity, "Enter all details", Toast.LENGTH_SHORT).show()
        }
    }



    private fun updateUserInfoAndUI() {
        //start next activity
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        activity!!.finish()
    }


    override fun onPause() {
        super.onPause()
        googleApiClient!!.stopAutoManage(activity!!)
        googleApiClient!!.disconnect()
    }


    private fun verifyEmail() {
        val mUser = mAuth!!.currentUser
        mUser!!.sendEmailVerification()
                .addOnCompleteListener(activity!!) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(activity,
                                "Verification email sent to " + mUser.getEmail(),
                                Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e(TAG, "sendEmailVerification", task.exception)
                        Toast.makeText(activity,
                                "Failed to send verification email.",
                                Toast.LENGTH_SHORT).show()
                    }
                }
    }


}














