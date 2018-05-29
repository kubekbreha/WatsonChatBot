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
import com.google.firebase.auth.FirebaseAuth
import com.kubekbreha.watsonchatbot.R
import com.kubekbreha.watsonchatbot.authentication.AuthenticationActivity


class ForgotFragment : Fragment(), View.OnClickListener {

    private val TAG = "ForgotPasswordFragment"

    //UI elements
    private var etEmail: EditText? = null
    private var btnSubmit: Button? = null
    private var btnBack: ImageButton? = null
    private var mProgressBar: ProgressDialog? = null

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
        mProgressBar = ProgressDialog(activity)

        btnSubmit!!.setOnClickListener(this)
        btnBack!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.frag_forgot_btn_submit -> {
                sendPasswordResetEmail()
            }
            R.id.frag_forgot_btn_back_from_forgot -> {
                activity!!.onBackPressed()
            }

            else -> {
            }
        }
    }

    private fun sendPasswordResetEmail() {
        mProgressBar!!.setMessage("Sending email...")
        mProgressBar!!.show()
        val email = etEmail?.text.toString()
        if (!TextUtils.isEmpty(email)) {
            mAuth!!
                    .sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val message = "Email sent."
                            Log.d(TAG, message)
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                            mProgressBar!!.hide()
                            val newFragment = AuthenticationFragment()
                            val transaction = fragmentManager!!.beginTransaction()

                            val fm = activity!!.supportFragmentManager
                            for (i in 0 until fm.backStackEntryCount) {
                                fm.popBackStack()
                            }

                            transaction.replace(R.id.act_authentication_authentication_frame, newFragment)
                            transaction.commit()
                        } else {
                            Log.w(TAG, task.exception!!.message)
                            mProgressBar!!.hide()
                            Toast.makeText(activity, "No user found with this email.", Toast.LENGTH_SHORT).show()
                        }
                    }
        } else {
            Toast.makeText(activity, "Enter Email", Toast.LENGTH_SHORT).show()
        }
    }
}
