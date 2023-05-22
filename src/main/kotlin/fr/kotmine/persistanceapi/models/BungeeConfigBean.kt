package fr.kotmine.persistanceapi.models

data class BungeeConfigBean(
    var slots: Int,
    var motd: String,
    var closeType: String,
    var serverLine: String,
    var maxPlayers: Int,
    var priorityTitle: String,
    var welcomeMessage: String
)

