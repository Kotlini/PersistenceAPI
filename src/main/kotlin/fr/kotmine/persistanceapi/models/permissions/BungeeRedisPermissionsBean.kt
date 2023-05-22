package fr.kotmine.persistanceapi.models.permissions

import fr.kotmine.persistanceapi.utils.Perm
import fr.kotmine.persistanceapi.utils.Transcoder

data class BungeeRedisPermissionsBean(
    var groupsId: Long,
    @Perm("bungeecord.command.list")
    var bungeecordCommandList: Boolean,
    @Perm("bungeecord.command.find")
    var bungeecordCommandFind: Boolean,
    @Perm("redisbungee.command.lastseen")
    var redisbungeeCommandLastSeen: Boolean,
    @Perm("redisbungee.command.sendtoall")
    var redisbungeeCommandSendtoAll: Boolean,
    @Perm("bungeecord.command.ip")
    var bungeecordCommandIp: Boolean,
    @Perm("redisbungee.command.serverid")
    var redisbungeeCommandServerId: Boolean,
    @Perm("redisbungee.command.serverids")
    var redisbungeCommandServerIds: Boolean,
    @Perm("redisbungee.command.pproxy")
    var redisbungeeCommandPproxy: Boolean,
    @Perm("redisbungee.command.plist")
    var redisbungeeCommandPlist: Boolean,
    @Perm("bungeecord.command.server")
    var bungeecordCommandServer: Boolean,
    @Perm("bungeecord.command.send")
    var bungeecordCommandSend: Boolean,
    @Perm("bungeecord.command.end")
    var bungeecordCommandEnd: Boolean,
    @Perm("bungeecord.command.alert")
    var bungeecordCommandAlert: Boolean
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

