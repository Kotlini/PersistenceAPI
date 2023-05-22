package fr.kotmine.persistanceapi.models.players

import java.sql.Timestamp
import java.util.UUID

data class FriendshipBean(
    val friendshipId: Long,
    val requesterUUID: UUID,
    val recipientUUID: UUID,
    val demandDate: Timestamp,
    val acceptationDate: Timestamp?,
    val activeStatus: Boolean
) {
    constructor(requesterUUID: UUID, recipientUUID: UUID, demandDate: Timestamp, acceptationDate: Timestamp?, activeStatus: Boolean) :
            this(0, requesterUUID, recipientUUID, demandDate, acceptationDate, activeStatus)
}
