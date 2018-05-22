package com.kubekbreha.watsonchatbot.model

data class ChatChannel(val userIds: MutableList<String>) {
    constructor() : this(mutableListOf())
}