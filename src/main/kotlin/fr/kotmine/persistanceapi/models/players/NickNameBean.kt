package fr.kotmine.persistanceapi.models.players

data class NicknameBean(
    var nickId: Long = 0,
    var nickname: String = "",
    var blackListed: Boolean = false,
    var used: Boolean = false
)

