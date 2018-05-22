package com.example.bottomnavigation.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.ListenerRegistration
import com.kubekbreha.watsonchatbot.AppConstants
import com.kubekbreha.watsonchatbot.R
import com.kubekbreha.watsonchatbot.authentication.AuthenticationActivity
import com.kubekbreha.watsonchatbot.main.ChatActivity
import com.kubekbreha.watsonchatbot.recyclerview.item.PersonItem
import com.kubekbreha.watsonchatbot.util.FirestoreUtil

import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_peoples.*


class PeoplesFragment : Fragment() {

    private lateinit var userListenerRegistration: ListenerRegistration

    private var shouldInitRecyclerView = true

    private lateinit var peopleSection: Section

    companion object {
        val TAG: String = PeoplesFragment::class.java.simpleName
        fun newInstance() = PeoplesFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_peoples, container, false)

        userListenerRegistration =
                FirestoreUtil.addUsersListener(this.activity!!, this::updateRecyclerView)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        FirestoreUtil.removeListener(userListenerRegistration)
        shouldInitRecyclerView = true
    }

    private fun updateRecyclerView(items: List<Item>) {

        fun init() {
            frag_peoples_recycler_view.apply {
                layoutManager = LinearLayoutManager(this@PeoplesFragment.context)
                adapter = GroupAdapter<ViewHolder>().apply {
                    peopleSection = Section(items)
                    add(peopleSection)
                    setOnItemClickListener(onItemClick)
                }
            }
            shouldInitRecyclerView = false
        }

        fun updateItems() = peopleSection.update(items)

        if (shouldInitRecyclerView)
            init()
        else
            updateItems()

    }

    private val onItemClick = OnItemClickListener { item, view ->
        if (item is PersonItem) {
            val accountIntent = Intent(activity, ChatActivity::class.java)
            accountIntent.putExtra(AppConstants.USER_NAME, item.person.name)
            accountIntent.putExtra(AppConstants.USER_ID, item.userId)
            startActivity(accountIntent)

        }
    }

}