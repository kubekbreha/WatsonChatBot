package com.example.bottomnavigation.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kubekbreha.watsonchatbot.R


class PeoplesFragment : Fragment() {

    companion object {
        val TAG: String = PeoplesFragment::class.java.simpleName
        fun newInstance() = PeoplesFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_peoples, container, false)
        return view
    }


}