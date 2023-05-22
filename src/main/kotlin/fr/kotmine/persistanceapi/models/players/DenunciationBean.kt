package fr.kotmine.persistanceapi.models.players

import java.sql.Timestamp
import java.util.UUID

data class DenunciationBean(
    var denouncer: UUID,
    var date: Timestamp,
    var reason: String,
    var suspectName: String
)

