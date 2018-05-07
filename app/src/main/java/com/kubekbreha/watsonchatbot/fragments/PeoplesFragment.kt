package com.kubekbreha.watsonchatbot.fragments


import android.os.Bundle
import android.support.v4.app.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.kubekbreha.watsonchatbot.R


/**
 * A simple [Fragment] subclass.
 *
 */
class PeoplesFragment : Fragment() {

    companion object {
        fun newInstance() : Fragment{
            var fb : Fragment = PeoplesFragment()
            return fb
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_peoples, container, false)
    }

}
