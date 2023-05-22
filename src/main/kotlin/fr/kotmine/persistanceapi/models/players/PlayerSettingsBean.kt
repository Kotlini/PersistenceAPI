package fr.kotmine.persistanceapi.models.players

import java.util.UUID

data class PlayerSettingsBean(
    var uuid: UUID? = null,
    var jukeboxListen: Boolean = false,
    var groupDemandReceive: Boolean = false,
    var friendshipDemandReceive: Boolean = false,
    var notificationReceive: Boolean = false,
    var privateMessageReceive: Boolean = false,
    var chatVisible: Boolean = false,
    var playerVisible: Boolean = false,
    var waitingLineNotification: Boolean = false,
    var otherPlayerInteraction: Boolean = false,
    var clickOnMeActivation: Boolean = false,
    var allowStatisticOnClick: Boolean = false,
    var allowCoinsOnClick: Boolean = false,
    var allowPowdersOnClick: Boolean = false,
    var allowClickOnOther: Boolean = false,
    )
