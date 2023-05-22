package fr.kotmine.persistanceapi.models.permissions

import fr.kotmine.persistanceapi.utils.Perm
import fr.kotmine.persistanceapi.utils.Transcoder

data class StaffPermissionsBean(
    private val groupsId: Long,
    @Perm("netjoin.closed")
    var netjoinClosed: Boolean,
    @Perm("netjoin.vip")
    var netjoinVip: Boolean,
    @Perm("netjoin.full")
    var netjoinFull: Boolean,
    @Perm("tracker.famous")
    var trackerFamous: Boolean,
    @Perm("network.vip")
    var networkVip: Boolean,
    @Perm("network.vipplus")
    var networkVipplus: Boolean,
    @Perm("network.staff")
    var networkStaff: Boolean,
    @Perm("network.admin")
    var networkAdmin: Boolean
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

