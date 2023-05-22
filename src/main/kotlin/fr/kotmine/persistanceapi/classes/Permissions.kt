package fr.kotmine.persistanceapi.classes

import fr.kotmine.persistanceapi.managers.permissions.*
import fr.kotmine.persistanceapi.models.permissions.*
import fr.kotmine.persistanceapi.models.players.PlayerBean

import javax.sql.DataSource

object Permissions {

    @Throws(Exception::class)
    fun getAllPlayerPermissions(player: PlayerBean, dataSource: DataSource): PlayerPermissionsBean {
        // Create the aggregation of different permissions bean
        return PlayerPermissionsBean(
            APIPermissionsManager.getAPIPermissions(player, dataSource)!!,
            BukkitPermissionsManager.getBukkitPermissions(player, dataSource)!!,
            BungeeRedisPermissionsManager.getBungeeRedisPermissions(player, dataSource)!!,
            HubPermissionsManager.getHubPermissions(player, dataSource)!!,
            ModerationPermissionsManager.getModerationPermissions(player, dataSource)!!,
            ProxiesPermissionsManager.getProxiesPermissions(player, dataSource)!!,
            StaffPermissionsManager.getStaffPermissions(player, dataSource)!!
        )
    }
}
