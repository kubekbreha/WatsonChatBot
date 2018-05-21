package com.kubekbreha.watsonchatbot.authentication.model

data class user(val name: String,
                val bio: String,
                val profilePicturePath: String?){
    constructor(): this("", "", null)
}