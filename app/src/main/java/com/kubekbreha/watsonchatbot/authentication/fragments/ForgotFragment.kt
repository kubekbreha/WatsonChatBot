package com.kubekbreha.watsonchatbot.authentication.fragments


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
import com.google.firebase.auth.FirebaseAuth

import com.kubekbreha.watsonchatbot.R
import com.kubekbreha.watsonchatbot.authentication.AuthenticationActivity


class ForgotFragment : Fragment() {

    private val TAG = "ForgotPasswordFragment"

    //UI elements
    private var etEmail: EditText? = null
    private var btnSubmit: Button? = null
    private var btnBack: ImageButton? = null

    //Firebase references
    private var mAuth: FirebaseAuth? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_forgot, container, false)
        initialise(view)
        return view
    }


    private fun initialise(view: View) {
        etEmail = view.findViewById<View>(R.id.frag_forgot_edit_email) as EditText
        btnSubmit = view.findViewById<View>(R.id.frag_forgot_btn_submit) as Button
        btnBack = view.findViewById<View>(R.id.frag_forgot_btn_back_from_forgot) as ImageButton
        mAuth = FirebaseAuth.getInstance()
        btnSubmit!!.setOnClickListener { sendPasswordResetEmail() }
        btnBack!!.setOnClickListener{ activity!!.onBackPressed() }
    }


    private fun sendPasswordResetEmail() {
        val email = etEmail?.text.toString()
        if (!TextUtils.isEmpty(email)) {
            mAuth!!
                    .sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val message = "Email sent."
                            Log.d(TAG, message)
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                            updateUI()
                        } else {
                            Log.w(TAG, task.exception!!.message)
                            Toast.makeText(activity, "No user found with this email.", Toast.LENGTH_SHORT).show()
                        }
                    }
        } else {
            Toast.makeText(activity, "Enter Email", Toast.LENGTH_SHORT).show()
        }
    }


    private fun updateUI() {
        val intent = Intent(activity, AuthenticationActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

}
