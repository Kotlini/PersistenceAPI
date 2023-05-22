package fr.kotmine.persistanceapi.models.permissions

import fr.kotmine.persistanceapi.utils.Perm
import fr.kotmine.persistanceapi.utils.Transcoder

data class APIPermissionsBean(
    val groupsId: Long,
    @field:Perm("api.servers.debug")
    var apiServersDebug: Boolean,
    @field:Perm("api.permissions.refresh")
    var apiPermissionsRefresh: Boolean,
    @field:Perm("api.coins.other")
    var apiCoinsGetOther: Boolean,
    @field:Perm("api.coins.credit")
    var apiCoinsCredit: Boolean,
    @field:Perm("api.coins.withdraw")
    var apiCoinsWithdraw: Boolean,
    @field:Perm("api.inventory.show")
    var apiInventoryShow: Boolean,
    @field:Perm("api.playerdata.show")
    var apiPlayerDataShow: Boolean,
    @field:Perm("api.playerdata.set")
    var apiPlayerDataSet: Boolean,
    @field:Perm("api.playerdata.del")
    var apiPlayerDataDel: Boolean,
    @field:Perm("api.modo.speakup")
    var apiModoSpeakUp: Boolean,
    @field:Perm("api.stars.getother")
    var apiStarsGetOther: Boolean,
    @field:Perm("api.stars.credit")
    var apiStarsCredit: Boolean,
    @field:Perm("api.stars.withdraw")
    var apiStarsWithdraw: Boolean,
    @field:Perm("api.game.start")
    var apiGameStart: Boolean,
    @field:Perm("api.chat.bypass")
    var apiChatBypass: Boolean
) {
    // Reverse the bean to HashMap
    fun getHashMap(): Map<String, Boolean> {
        return Transcoder.getHashMapPerm(this)
    }

    // Set a value into the HashMap
    fun set(key: String, value: Boolean) {
        Transcoder.setAnnotationValue(this, key, value)
    }
}

