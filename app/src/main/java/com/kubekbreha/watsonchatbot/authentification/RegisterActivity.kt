package com.kubekbreha.watsonchatbot.authentification

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kubekbreha.watsonchatbot.MainActivity
import com.kubekbreha.watsonchatbot.R

class RegisterActivity : AppCompatActivity() {

    private val TAG = "RegisterActivity"

    //global variables
    private var userName: String? = null
    private var email: String? = null
    private var password: String? = null

    //UI elements
    private var etUsername: EditText? = null
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    private var btnCreateAccount: Button? = null
    private var mProgressBar: ProgressDialog? = null
    private var btnBack: ImageButton? = null
    private var btnGoogle: Button? = null
    private var btnFacebook: Button? = null

    //Firebase references
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null


    //Request codes
    val GOOGLE_LOG_IN_RC = 1
    val FACEBOOK_LOG_IN_RC = 2
    val TWITTER_LOG_IN_RC = 3

    // Google API Client object.
    var googleApiClient: GoogleApiClient? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initialise()
    }

    private fun initialise() {
        mAuth = FirebaseAuth.getInstance()


        //automaticaly log user
        if(mAuth!!.currentUser != null){
            updateUserInfoAndUI()
        }

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.request_client_id))
                .requestEmail()
                .build()

        // Creating and Configuring Google Api Client.
        googleApiClient = GoogleApiClient.Builder(this@RegisterActivity)
                .enableAutoManage(this@RegisterActivity  /* OnConnectionFailedListener */) { }
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build()

        etUsername = findViewById<View>(R.id.et_username) as EditText
        etEmail = findViewById<View>(R.id.et_email) as EditText
        etPassword = findViewById<View>(R.id.et_password) as EditText
        btnCreateAccount = findViewById<View>(R.id.btn_register) as Button
        btnBack = findViewById<View>(R.id.btn_back_from_register) as ImageButton
        btnGoogle = findViewById<View>(R.id.register_button_google) as Button
        btnFacebook = findViewById<View>(R.id.register_button_facebook) as Button

        mProgressBar = ProgressDialog(this)
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("Users")

        btnCreateAccount!!.setOnClickListener { createNewAccount() }
        btnBack!!.setOnClickListener { finish() }

        //google login
        btnGoogle!!.setOnClickListener { googleButtonOnClick() }

        btnFacebook!!.setOnClickListener{   Toast.makeText(this@RegisterActivity, "Not implemented yet.",
                Toast.LENGTH_SHORT).show()    }


    }


    private fun createNewAccount() {

        userName = etUsername?.text.toString()
        email = etEmail?.text.toString()
        password = etPassword?.text.toString()


        if (!TextUtils.isEmpty(userName)
                && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            mProgressBar!!.setMessage("Registering User...")
            mProgressBar!!.show()

            mAuth!!
                    .createUserWithEmailAndPassword(email!!, password!!)
                    .addOnCompleteListener(this) { task ->
                        mProgressBar!!.hide()
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            val userId = mAuth!!.currentUser!!.uid
                            //Verify Email
                            verifyEmail();
                            //update user profile information
                            val currentUserDb = mDatabaseReference!!.child(userId)
                            currentUserDb.child("userName").setValue(userName)
                            updateUserInfoAndUI()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(this@RegisterActivity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }

        } else {
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
        }

    }


    fun googleButtonOnClick() {

        Log.i(TAG, "Trying to login via google.")
        googleLogin()

    }


    private fun googleLogin() {
        Log.i(TAG, "Starting Google LogIn Flow.")
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        startActivityForResult(signInIntent, GOOGLE_LOG_IN_RC)
    }

    private fun updateUserInfoAndUI() {
        //start next activity
        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }


    private fun verifyEmail() {
        val mUser = mAuth!!.currentUser
        mUser!!.sendEmailVerification()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@RegisterActivity,
                                "Verification email sent to " + mUser.getEmail(),
                                Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e(TAG, "sendEmailVerification", task.exception)
                        Toast.makeText(this@RegisterActivity,
                                "Failed to send verification email.",
                                Toast.LENGTH_SHORT).show()
                    }
                }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
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
                Toast.makeText(this@RegisterActivity, "Some error occurred.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.i(TAG, "Authenticating user with firebase.")
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth?.signInWithCredential(credential)?.addOnCompleteListener(this) { task ->
            Log.i(TAG, "Firebase Authentication, is result a success? ${task.isSuccessful}.")
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
            } else {
                // If sign in fails, display a message to the user.
                Log.e(TAG, "Authenticating with Google credentials in firebase FAILED !!")
            }
        }
    }
}
