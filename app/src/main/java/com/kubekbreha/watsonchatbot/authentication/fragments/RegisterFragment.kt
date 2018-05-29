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
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.firebase.ui.auth.util.ExtraConstants.EMAIL
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import com.kubekbreha.watsonchatbot.R
import com.kubekbreha.watsonchatbot.util.FirestoreUtil
import com.kubekbreha.watsonchatbot.main.MainActivity
import java.util.*

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
    private var btnGoogle: Button? = null
    private var btnFacebook: Button? = null
    //private var btnTwitter: Button? = null
    lateinit var facebookSignInButton: LoginButton
    var callbackManager: CallbackManager? = null


    private var mFacebookCallbackManager: CallbackManager? = null // for facebook log in

    //Firebase references
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null


    //Request codes
    val GOOGLE_LOG_IN_RC = 1
    val FACEBOOK_LOG_IN_RC = 2
    val TWITTER_LOG_IN_RC = 3
    private val FACEBOOK_LOG_IN_REQUEST_CODE = 64206


    // Google API Client object.
    var googleApiClient: GoogleApiClient? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        FacebookSdk.sdkInitialize(activity!!.getApplicationContext())
        mFacebookCallbackManager = CallbackManager.Factory.create()


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
        btnGoogle = view.findViewById<View>(R.id.frag_register_register_button_google) as Button
        btnFacebook = view.findViewById<View>(R.id.frag_register_register_button_facebook) as Button
        //btnTwitter = view.findViewById<View>(R.id.frag_register_register_button_twitter) as Button
        facebookSignInButton = view.findViewById<View>(R.id.login_button) as LoginButton

        mProgressBar = ProgressDialog(activity)
        mDatabase = FirebaseDatabase.getInstance()
        //mDatabaseReference = mDatabase!!.reference!!.child("Users")

        btnCreateAccount!!.setOnClickListener(this)
        btnBack!!.setOnClickListener(this)
        //google login
        btnGoogle!!.setOnClickListener(this)
        btnFacebook!!.setOnClickListener(this)


        //facevbook

        callbackManager = CallbackManager.Factory.create()
        facebookSignInButton.setReadPermissions("email")
// Callback registration
        facebookSignInButton.setReadPermissions()

        facebookSignInButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                // App code
                handleFacebookAccessToken(loginResult.accessToken);
            }

            override fun onCancel() {
                // App code
            }

            override fun onError(exception: FacebookException) {
                // App code
            }
        })

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.frag_register_btn_register -> {
                createNewAccount()
            }

            R.id.frag_register_btn_back_from_register -> {
                activity!!.onBackPressed()
            }

            R.id.frag_register_register_button_google -> {
                Log.i(TAG, "Trying to login via google.")
                googleLogin()
            }

            R.id.frag_register_register_button_facebook -> {
            }

//            R.id.twitter_sign_in_button -> {
//                Log.i(TAG, "Trying Twitter LogIn.")
//                twitterLogin()
//            }

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


    private fun googleLogin() {
        Log.i(TAG, "Starting Google LogIn Flow.")
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        startActivityForResult(signInIntent, GOOGLE_LOG_IN_RC)
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i(TAG, "Got Result code ${requestCode}.")
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE_LOG_IN_RC) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            Log.i(TAG, "With Google LogIn, is result a success? ${result.isSuccess}.")
            if (result.isSuccess) {
                // Google Sign In was successful, authenticate with Firebase
                firebaseAuthWithGoogle(result.signInAccount!!)
            } else {
                Toast.makeText(activity, "Some error occurred.", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == FACEBOOK_LOG_IN_REQUEST_CODE)
            callbackManager!!.onActivityResult(requestCode, resultCode, data)
    }


    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:" + token)
        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(activity!!) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        val user = mAuth!!.currentUser
                        startActivity(Intent(activity, MainActivity::class.java))
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException())
                        Toast.makeText(activity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                    }
                }
    }


    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        mProgressBar!!.setMessage("Registering User...")
        mProgressBar!!.show()
        Log.i(TAG, "Authenticating user with firebase.")
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth?.signInWithCredential(credential)?.addOnCompleteListener(this!!.activity!!) { task ->
            Log.i(TAG, "Firebase Authentication, is result a success? ${task.isSuccessful}.")
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                FirestoreUtil.initCurrentUserIfFirstTime(activity!!, "") {
                    startActivity(Intent(activity, MainActivity::class.java))
                    activity!!.finish()
                    mProgressBar!!.hide()
                }
            } else {
                // If sign in fails, display a message to the user.
                Log.e(TAG, "Authenticating with Google credentials in firebase FAILED !!")
            }
        }
    }


}














