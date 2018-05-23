package com.kubekbreha.watsonchatbot.authentication.fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kubekbreha.watsonchatbot.R
import com.kubekbreha.watsonchatbot.util.FirestoreUtil
import com.kubekbreha.watsonchatbot.main.MainActivity


class AuthenticationFragment : Fragment(), View.OnClickListener {

    private var TAG : String = "AuthenticationFragment"

    //UI elements
    private var btnLogIn: Button? = null
    private var btnSignUp: Button? = null
    private var btnGoogle: Button? = null

    private var mAuth: FirebaseAuth? = null

    //Request codes
    val GOOGLE_LOG_IN_RC = 1

    // Google API Client object.
    var googleApiClient: GoogleApiClient? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_authentification, container, false)
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


        btnLogIn = view.findViewById<View>(R.id.frag_authentication_login) as Button
        btnSignUp = view.findViewById<View>(R.id.frag_authentication_sign_up_with_email) as Button
        btnGoogle = view.findViewById<View>(R.id.frag_authentication_btn_google_sign_in) as Button

        btnLogIn!!.setOnClickListener(this)
        btnSignUp!!.setOnClickListener(this)
        btnGoogle!!.setOnClickListener ( this )

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.frag_authentication_login -> {
                val transaction = fragmentManager!!.beginTransaction()
                transaction.addToBackStack(null)

                val newFragment = LoginFragment()

                transaction.replace(R.id.act_authentication_authentication_frame, newFragment)
                transaction.commit()
            }
            R.id.frag_authentication_sign_up_with_email -> {

                val transaction = fragmentManager!!.beginTransaction()
                transaction.addToBackStack(null)

                val newFragment = RegisterFragment()

                transaction.replace(R.id.act_authentication_authentication_frame, newFragment)
                transaction.commit()
            }
            R.id.frag_authentication_btn_google_sign_in -> {
                Log.i(TAG, "Starting Google LogIn Flow.")
                val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
                startActivityForResult(signInIntent, GOOGLE_LOG_IN_RC)
            }

            else -> {
            }
        }
    }


    override fun onPause() {
        super.onPause()
        googleApiClient!!.stopAutoManage(activity!!)
        googleApiClient!!.disconnect()
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
        }
    }


    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.i(TAG, "Authenticating user with firebase.")
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth?.signInWithCredential(credential)?.addOnCompleteListener(this!!.activity!!) { task ->
            Log.i(TAG, "Firebase Authentication, is result a success? ${task.isSuccessful}.")
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                FirestoreUtil.initCurrentUserIfFirstTime(activity!!, "") {
                    startActivity(Intent(activity, MainActivity::class.java))
                    activity!!.finish()
                }

            } else {
                // If sign in fails, display a message to the user.
                Log.e(TAG, "Authenticating with Google credentials in firebase FAILED !!")
            }
        }
    }

}
