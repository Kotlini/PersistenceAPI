package fr.kotmine.persistanceapi.models.permissions

import fr.kotmine.persistanceapi.utils.Perm
import fr.kotmine.persistanceapi.utils.Transcoder

data class HubPermissionsBean(
    val groupsId: Long,
    @Perm("hub.jukebox.lock")
    var hubJukeboxLock: Boolean,
    @Perm("hub.jukebox.next")
    var hubJukeboxNext: Boolean,
    @Perm("hub.jukebox.clear")
    var hubJukeBoxClear: Boolean,
    @Perm("hub.mod.slow")
    var hubModSlow: Boolean,
    @Perm("hub.mod.shutup")
    var hubModShutup: Boolean,
    @Perm("hub.admin.npc")
    var hubAdminNpc: Boolean,
    @Perm("hub.admin.sign")
    var hubAdminSign: Boolean,
    @Perm("hub.anguille")
    var hubAnguille: Boolean,
    @Perm("hub.jukebox.nbs")
    var hubJukeboxNbs: Boolean,
    @Perm("hub.admin.evacuate")
    var hubAdminEvacuate: Boolean,
    @Perm("hub.announce")
    var hubAnnounce: Boolean,
    @Perm("hub.gadgets.cooldownbypass")
    var hubGadgetsCooldownbypass: Boolean,
    @Perm("hub.gadgets.nuke")
    var hubGadgetsNuke: Boolean,
    @Perm("hub.jukebox.limitbypass")
    var hubJukeboxLimitbypass: Boolean,
    @Perm("hub.jukebox.limitstaff")
    var hubJukeboxLimitstaff: Boolean,
    @Perm("hub.bypassmute")
    var hubBypassmute: Boolean,
    @Perm("hub.fly")
    var hubFly: Boolean,
    @Perm("hub.debug.sign")
    var hubDebugSign: Boolean,
    @Perm("hub.sign.selection")
    var hubSignSelection: Boolean,
    @Perm("hub.beta.vip")
    var hubBetaVIP: Boolean,
    @Perm("hub.admin.pearl")
    var hubAdminPearl: Boolean,
    @Perm("hub.animating.event")
    var hubAnimatingEvent: Boolean
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
