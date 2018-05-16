package com.example.bottomnavigation.helper

import android.support.v4.app.Fragment
import com.example.bottomnavigation.ui.WatsonFragment
import com.example.bottomnavigation.ui.PeoplesFragment
import com.example.bottomnavigation.ui.ProfileFragment
import com.kubekbreha.watsonchatbot.R

enum class BottomNavigationPosition(val position: Int, val id: Int) {
    PEOPLES(0, R.id.peoples),
    WATSON(1, R.id.watson),
    PROFILE(2, R.id.profile),
}

fun findNavigationPositionById(id: Int): BottomNavigationPosition = when (id) {
    BottomNavigationPosition.PEOPLES.id -> BottomNavigationPosition.PEOPLES
    BottomNavigationPosition.WATSON.id -> BottomNavigationPosition.WATSON
    BottomNavigationPosition.PROFILE.id -> BottomNavigationPosition.PROFILE
    else -> BottomNavigationPosition.PEOPLES
}

fun BottomNavigationPosition.createFragment(): Fragment = when (this) {
    BottomNavigationPosition.PEOPLES -> PeoplesFragment.newInstance()
    BottomNavigationPosition.WATSON -> WatsonFragment.newInstance()
    BottomNavigationPosition.PROFILE -> ProfileFragment.newInstance()
}

fun BottomNavigationPosition.getTag(): String = when (this) {
    BottomNavigationPosition.PEOPLES -> PeoplesFragment.TAG
    BottomNavigationPosition.WATSON -> WatsonFragment.TAG
    BottomNavigationPosition.PROFILE -> ProfileFragment.TAG
}

