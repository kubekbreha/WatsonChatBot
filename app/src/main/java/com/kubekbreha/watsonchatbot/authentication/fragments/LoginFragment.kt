package com.kubekbreha.watsonchatbot.authentification.fragments


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
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

import com.kubekbreha.watsonchatbot.R
import com.kubekbreha.watsonchatbot.main.MainActivity


class LoginFragment : Fragment() {

    private var TAG : String = "LoginFragment"

    //global variables
    private var email: String? = null
    private var password: String? = null

    //UI elements
    private var textForgotPassword: TextView? = null
    private var editEmail: EditText? = null
    private var editPassword: EditText? = null
    private var butonLogin: Button? = null
    private var mProgressBar: ProgressDialog? = null

    private var mAuth: FirebaseAuth? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialise()
    }


    private fun initialise() {
        textForgotPassword = view!!.findViewById<View>(R.id.frag_login_forgot) as TextView
        editEmail = view!!.findViewById<View>(R.id.frag_login_edit_email) as EditText
        editPassword = view!!.findViewById<View>(R.id.frag_login_edit_password) as EditText
        butonLogin = view!!.findViewById<View>(R.id.frag_login_btn_login) as Button
        mProgressBar = ProgressDialog(activity)
        mAuth = FirebaseAuth.getInstance()
//        textForgotPassword!!
//                .setOnClickListener {
//                    startActivity(Intent(activity,
//                            ForgotPasswordActivity::class.java))
//                }
        butonLogin!!.setOnClickListener { loginUser() }
    }


    private fun loginUser() {
        email = editEmail?.text.toString()
        password = editPassword?.text.toString()
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mProgressBar!!.setMessage("Registering User...")
            mProgressBar!!.show()
            Log.d(TAG, "Logging in user.")
            mAuth!!.signInWithEmailAndPassword(email!!, password!!)
                    .addOnCompleteListener(activity!!) { task ->
                        mProgressBar!!.hide()
                        if (task.isSuccessful) {
                            // Sign in success, update UI with signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            updateUI()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
        } else {
            Toast.makeText(activity, "Enter all details", Toast.LENGTH_SHORT).show()
        }
    }


    private fun updateUI() {
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

}
