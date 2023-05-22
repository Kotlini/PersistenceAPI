package fr.kotmine.persistanceapi.models.players

import java.sql.Timestamp
import java.util.UUID

data class PlayerBean(
    var uuid: UUID? = null,
    var name: String = "",
    var nickName: String? = null,
    var coins: Int? = null,
    var stars: Int? = null,
    var powders: Int? = null,
    var lastLogin: Timestamp? = null,
    var firstLogin: Timestamp? = null,
    var lastIP: String? = null,
    var topTpKey: String? = null,
    var groupId: Long? = null
)
