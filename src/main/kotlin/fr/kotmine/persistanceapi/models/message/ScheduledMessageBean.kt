package fr.kotmine.persistanceapi.models.message

data class ScheduledMessageBean(
    val messageId: Int,
    var messageText: String,
    var scheduleTime: Int
)