package com.example.bottomnavigation.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kubekbreha.watsonchatbot.R


class WatsonFragment : Fragment() {

    companion object {
        val TAG: String = WatsonFragment::class.java.simpleName
        fun newInstance() = WatsonFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_watson, container, false)
        return view
    }

}