package fr.kotmine.persistanceapi.models.permissions

data class PlayerPermissionsBean(
    var apiPermissions: APIPermissionsBean,
    var bukkitPermissions: BukkitPermissionsBean,
    var bungeeRedisPermissions: BungeeRedisPermissionsBean,
    var hubPermissions: HubPermissionsBean,
    var moderationPermissions: ModerationPermissionsBean,
    var proxiesPermissions: ProxiesPermissionsBean,
    var staffPermissions: StaffPermissionsBean
) {
    // Reverse the bean to HashMap
    fun getHashMap(): HashMap<String, Boolean> {
        val permissionHashMap = HashMap<String, Boolean>()
        permissionHashMap.putAll(apiPermissions.getHashMap())
        permissionHashMap.putAll(bukkitPermissions.getHashMap())
        permissionHashMap.putAll(bungeeRedisPermissions.getHashMap())
        permissionHashMap.putAll(hubPermissions.getHashMap())
        permissionHashMap.putAll(moderationPermissions.getHashMap())
        permissionHashMap.putAll(proxiesPermissions.getHashMap())
        permissionHashMap.putAll(staffPermissions.getHashMap())
        return permissionHashMap
    }
}
