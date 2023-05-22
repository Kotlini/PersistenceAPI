package fr.kotmine.persistanceapi.models.permissions

import fr.kotmine.persistanceapi.utils.Perm
import fr.kotmine.persistanceapi.utils.Transcoder

data class ModerationPermissionsBean(
    val groupsId: Long,
    @Perm("mod.ban")
    var modBan: Boolean,
    @Perm("mod.tp")
    var modTp: Boolean,
    @Perm("mod.kick")
    var modKick: Boolean,
    @Perm("mod.pardon")
    var modPardon: Boolean,
    @Perm("mod.mute.longtime")
    var modMuteLongtime: Boolean,
    @Perm("mod.mute")
    var modMute: Boolean,
    @Perm("mod.channel")
    var modChannel: Boolean,
    @Perm("mod.channel.report")
    var modChannelReport: Boolean,
    @Perm("mod.quiet")
    var modQuiet: Boolean
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

