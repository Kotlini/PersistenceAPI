package fr.kotmine.persistanceapi.models.players

import java.sql.Timestamp
import java.util.UUID

class SanctionBean(
    var sanctionId: Long = 0,
    var playerUuid: UUID? = null,
    var typeId: Int = 0,
    var reason: String? = null,
    var punisherUuid: UUID? = null,
    var expirationTime: Timestamp? = null,
    var isDeleted: Boolean = false,
    var creationDate: Timestamp? = null,
    var updateDate: Timestamp? = null
) {
    companion object {
        const val AVERTISSEMENT = 1
        const val BAN = 2
        const val KICK = 3
        const val MUTE = 4
        const val TEXT = 5
    }

    constructor(
        playerUuid: UUID?,
        typeId: Int,
        reason: String?,
        punisherUuid: UUID?,
        expirationTime: Timestamp?,
        isDeleted: Boolean
    ) : this(0, playerUuid, typeId, reason, punisherUuid, expirationTime, isDeleted, null, null)

    constructor(
        playerUuid: UUID?,
        typeId: Int,
        reason: String?,
        punisherUuid: UUID?,
        isDeleted: Boolean
    ) : this(playerUuid, typeId, reason, punisherUuid, null, isDeleted)
}
