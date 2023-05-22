package fr.kotmine.persistanceapi.models.permissions

import fr.kotmine.persistanceapi.utils.Perm
import fr.kotmine.persistanceapi.utils.Transcoder

data class BukkitPermissionsBean(
    val groupsId: Long,
    @field:Perm("minecraft.command.op")
    var minecraftCommandOp: Boolean,
    @field:Perm("bukkit.command.op.give")
    var bukkitCommandOpGive: Boolean,
    @field:Perm("minecraft.command.effect")
    var bukkitCommandEffect: Boolean,
    @field:Perm("minecraft.command.gamemode")
    var bukkitCommandGamemode: Boolean,
    @field:Perm("minecraft.command.tp")
    var bukkitCommandTeleport: Boolean
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
