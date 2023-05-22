package fr.kotmine.persistanceapi

import fr.kotmine.persistanceapi.classes.Permissions
import fr.kotmine.persistanceapi.classes.Statistics
import fr.kotmine.persistanceapi.managers.ConfigurationManager
import fr.kotmine.persistanceapi.managers.DatabaseManager
import fr.kotmine.persistanceapi.managers.event.EventManager
import fr.kotmine.persistanceapi.managers.message.MessageManager
import fr.kotmine.persistanceapi.managers.permissions.*
import fr.kotmine.persistanceapi.managers.players.*
import fr.kotmine.persistanceapi.managers.players.DenunciationManager.denouncePlayer
import fr.kotmine.persistanceapi.managers.statistics.*
import fr.kotmine.persistanceapi.models.BungeeConfigBean
import fr.kotmine.persistanceapi.models.events.EventBean
import fr.kotmine.persistanceapi.models.events.EventWinnerBean
import fr.kotmine.persistanceapi.models.message.ScheduledMessageBean
import fr.kotmine.persistanceapi.models.permissions.*
import fr.kotmine.persistanceapi.models.players.*
import fr.kotmine.persistanceapi.models.shop.PromotionsBean
import fr.kotmine.persistanceapi.models.statistics.*
import java.sql.Timestamp
import java.util.*


class PersistenceAPI(url: String, name: String, password: String, minPoolSize: Int, maxPoolSize: Int) {
    private val databaseManager: DatabaseManager = DatabaseManager(url, name, password, minPoolSize, maxPoolSize)

    /*============================================
      Part of denunciation manager
    ============================================*/
    // Make a denunciation

    @Synchronized
    @Throws(Exception::class)
    fun denouncePlayer(player: PlayerBean, denunciation: DenunciationBean?) {
        denouncePlayer(player, denunciation!!, databaseManager.getDataSource()!!)
    }


    /*============================================
      Part of config manager
    ============================================*/

    /*============================================
      Part of config manager
    ============================================*/
    // Get the bungee config
    @Synchronized
    @Throws(Exception::class)
    fun getBungeeConfig(): BungeeConfigBean {
        return ConfigurationManager.getConfig(databaseManager.getDataSource()!!)!!
    }

    // Update the bungee config
    @Synchronized
    @Throws(Exception::class)
    fun updateBungeeConfig(config: BungeeConfigBean) {
        ConfigurationManager.updateConfig(config, databaseManager.getDataSource()!!)
    }


    /*============================================
      Part of player manager
    ============================================*/

    /*============================================
      Part of player manager
    ============================================*/
    // Get the player by UUID
    @Synchronized
    @Throws(Exception::class)
    fun getPlayer(uuid: UUID, player: PlayerBean): PlayerBean {
        // Get the PlayerBean
        return PlayerManager.getPlayer(uuid, player, databaseManager.getDataSource()!!)
    }

    // Update the player
    @Synchronized
    @Throws(Exception::class)
    fun updatePlayer(player: PlayerBean) {
        // Update datas of player
        PlayerManager.updatePlayer(player, databaseManager.getDataSource()!!)
    }

    // Create the player
    @Synchronized
    @Throws(Exception::class)
    fun createPlayer(player: PlayerBean) {
        // Create the player
        PlayerManager.createPlayer(player, databaseManager.getDataSource()!!)
    }


    /*============================================
      Part of sanction manager
    ============================================*/

    /*============================================
      Part of sanction manager
    ============================================*/
    // Apply a sanction to a player
    @Synchronized
    @Throws(Exception::class)
    fun applySanction(sanctionType: Int, sanction: SanctionBean) {
        // Do the sanction
        SanctionManager.applySanction(sanctionType, sanction, databaseManager.getDataSource()!!)
    }

    @Synchronized
    @Throws(Exception::class)
    fun removeSanction(sanctionType: Int, player: PlayerBean) {
        // Remove the sanction
        SanctionManager.removeSanction(sanctionType, player, databaseManager.getDataSource()!!)
    }

    // Check if a player is banned
    @Synchronized
    @Throws(Exception::class)
    fun getPlayerBanned(player: PlayerBean): SanctionBean? {
        // Check the ban status
        return SanctionManager.getPlayerBanned(player, databaseManager.getDataSource()!!)
    }

    // Check if a player is muted
    @Synchronized
    @Throws(Exception::class)
    fun getPlayerMuted(player: PlayerBean): SanctionBean? {
        // Check the mute status
        return SanctionManager.getPlayerMuted(player, databaseManager.getDataSource()!!)
    }

    // Get all sanctions for a player and type
    @Synchronized
    @Throws(Exception::class)
    fun getAllSanctions(uuid: UUID, sanctionType: Int): List<SanctionBean?> {
        // Get sanctions
        return SanctionManager.getAllSanctions(uuid, sanctionType, databaseManager.getDataSource()!!)
    }

    // Get all actives sanctions for a player and type
    @Synchronized
    @Throws(Exception::class)
    fun getAllActiveSanctions(uuid: UUID, sanctionType: Int): List<SanctionBean?> {
        // Get sanctions
        return SanctionManager.getAllActiveSanctions(uuid, sanctionType, databaseManager.getDataSource()!!)
    }

    // Get all passives sanctions for a player and type
    @Synchronized
    @Throws(Exception::class)
    fun getAllPassiveSanctions(uuid: UUID, sanctionType: Int): List<SanctionBean?> {
        // Get sanctions
        return SanctionManager.getAllPassiveSanctions(uuid, sanctionType, databaseManager.getDataSource()!!)
    }

    // Get all sanctions by a moderator
    @Synchronized
    @Throws(Exception::class)
    fun getAllModeratorSanctions(uuid: UUID): List<SanctionBean?>? {
        // Get sanctions
        return SanctionManager.getAllModeratorSanctions(uuid, databaseManager.getDataSource()!!)
    }

    /*============================================
      Part of statistics manager
    ============================================*/

    // Get JukeBox player statistics
    @Synchronized
    @Throws(Exception::class)
    fun getJukeBoxStatistics(player: PlayerBean): JukeBoxStatisticsBean? {
        // Get the statistics
        return JukeBoxStatisticsManager.getJukeBoxStatistics(player, databaseManager.getDataSource()!!)
    }

    // Get Network player statistics
    @Synchronized
    @Throws(Exception::class)
    fun getNetworkStatistics(player: PlayerBean): NetworkStatisticsBean? {
        // Get the statistics
        return NetworkStatisticsManager.getNetworkStatistics(player, databaseManager.getDataSource()!!)
    }

    // Get fallenKingDom player statistics
    @Synchronized
    @Throws(Exception::class)
    fun getFallenKingDomStatistics(player: PlayerBean): FallenKingdomStatisticsBean {
        // Get the statistics
        return FallenKingDomStatisticsManager.getFallenKingDomStatistics(player, databaseManager.getDataSource()!!)
    }

    // Get versus player statistics
    @Synchronized
    @Throws(Exception::class)
    fun getVersusStatistics(player: PlayerBean): VersusStatisticsBean {
        // Get the statistics
        return VersusStatisticsManager.getVersusStatistics(player, databaseManager.getDataSource()!!)
    }

    // Get all player statistics
    @Synchronized
    @Throws(Exception::class)
    fun getAllStatistics(player: PlayerBean): PlayerStatisticsBean {
        // Get all the statistics
        return Statistics.getAllPlayerStatistics(player, databaseManager.getDataSource()!!)
    }

    // Update all player statistics
    @Synchronized
    @Throws(Exception::class)
    fun updateAllStatistics(player: PlayerBean, stats: PlayerStatisticsBean) {
        // Update statistics
       Statistics.updateAllPlayerStatistics(player, stats, databaseManager.getDataSource()!!)
    }

    // Update JukeBox statistics
    @Synchronized
    @Throws(Exception::class)
    fun updateJukeBoxStatistics(player: PlayerBean, jukeBoxStats: JukeBoxStatisticsBean) {
        // Update statistics
        JukeBoxStatisticsManager.updateJukeBoxStatistics(player, jukeBoxStats, databaseManager.getDataSource()!!)
    }

    // Update network statistics
    @Synchronized
    @Throws(Exception::class)
    fun updateNetworkStatistics(player: PlayerBean, networkStats: NetworkStatisticsBean) {
        // Update statistics
        NetworkStatisticsManager.updateNetworkStatistics(player, networkStats, databaseManager.getDataSource()!!)
    }


    // Get the jukebox leaderboard
    @Synchronized
    @Throws(Exception::class)
    fun getJukeBoxLeaderBoard(category: String): List<LeaderboardBean> {
        // Get the leaderboard
        return JukeBoxStatisticsManager.getLeaderBoard(category, databaseManager.getDataSource()!!)
    }

    /*============================================
      Part of permissions manager
    ============================================*/
    // Get API permissions
    @Synchronized
    @Throws(Exception::class)
    fun getAPIPermissions(player: PlayerBean): APIPermissionsBean {
        // Get the permissions
        return APIPermissionsManager.getAPIPermissions(player, databaseManager.getDataSource()!!)
    }

    // Get Bukkit permissions
    @Synchronized
    @Throws(Exception::class)
    fun getBukkitPermissions(player: PlayerBean): BukkitPermissionsBean {
        // Get the permissions
        return BukkitPermissionsManager.getBukkitPermissions(player, databaseManager.getDataSource()!!)
    }

    // Get Bungee & Redis permissions
    @Synchronized
    @Throws(Exception::class)
    fun getBungeeRedisPermissions(player: PlayerBean): BungeeRedisPermissionsBean {
        // Get the permissions
        return BungeeRedisPermissionsManager.getBungeeRedisPermissions(player, databaseManager.getDataSource()!!)
    }

    // Get Hub permissions
    @Synchronized
    @Throws(Exception::class)
    fun getHubPermissions(player: PlayerBean): HubPermissionsBean {
        // Get the permissions
        return HubPermissionsManager.getHubPermissions(player, databaseManager.getDataSource()!!)
    }

    // Get Moderation permissions
    @Synchronized
    @Throws(Exception::class)
    fun getModerationPermissions(player: PlayerBean): ModerationPermissionsBean {
        // Get the permissions
        return ModerationPermissionsManager.getModerationPermissions(player, databaseManager.getDataSource()!!)
    }

    // Get Proxies permissions
    @Synchronized
    @Throws(Exception::class)
    fun getProxiesPermissions(player: PlayerBean): ProxiesPermissionsBean {
        // Get the permissions
        return ProxiesPermissionsManager.getProxiesPermissions(player, databaseManager.getDataSource()!!)
    }

    // Get Staff permissions
    @Synchronized
    @Throws(Exception::class)
    fun getStaffPermissions(player: PlayerBean): StaffPermissionsBean {
        // Get the permissions
        return StaffPermissionsManager.getStaffPermissions(player, databaseManager.getDataSource()!!)
    }

    // Get all player permissions
    @Synchronized
    @Throws(Exception::class)
    fun getAllPlayerPermissions(player: PlayerBean): PlayerPermissionsBean {
        // Get all the statistics
        return Permissions.getAllPlayerPermissions(player, databaseManager.getDataSource()!!)
    }

    /*============================================
      Part of groups manager
    ============================================*/
    @Synchronized
    @Throws(Exception::class)
    fun getPlayerGroup(player: PlayerBean): GroupsBean {
        // Get the groups of a player
        return GroupsManager.getPlayerGroup(player, databaseManager.getDataSource()!!)
    }

    /*============================================
      Part of groups manager
    ============================================*/
    // Post a friendship demand
    @Synchronized
    @Throws(Exception::class)
    fun postFriendshipDemand(friendship: FriendshipBean) {
        // Post the friendship demand
        FriendshipManager.postFriendshipDemand(friendship, databaseManager.getDataSource()!!)
    }

    // Accept a friendship demand
    @Synchronized
    @Throws(Exception::class)
    fun acceptFriendshipDemand(friendship: FriendshipBean) {
        // Accept the demand
       FriendshipManager.acceptFriendshipDemand(friendship, databaseManager.getDataSource()!!)
    }

    // Refuse a friendship demand
    @Synchronized
    @Throws(Exception::class)
    fun refuseFriendshipDemand(friendship: FriendshipBean) {
        // Refuse the demand
       FriendshipManager.refuseFriendshipDemand(friendship, databaseManager.getDataSource()!!)
    }

    // Get the list of friendship demand
    @Synchronized
    @Throws(Exception::class)
    fun getFriendshipDemandList(player: PlayerBean): List<FriendshipBean> {
        // Get the list
        return FriendshipManager.getFriendshipDemandList(player, databaseManager.getDataSource()!!)
    }

    // Get the list of friendship
    @Synchronized
    @Throws(Exception::class)
    fun getFriendshipList(player: PlayerBean): List<FriendshipBean> {
        // Get the list
        return FriendshipManager.getFriendshipList(player, databaseManager.getDataSource()!!)
    }

    // Get the list of friendship with both requester/recipient
    @Synchronized
    @Throws(Exception::class)
    fun getFriendshipNamedList(requester: PlayerBean, recipient: PlayerBean): FriendshipBean {
        // Get the list
        return FriendshipManager.getFriendshipNamedList(requester, recipient, databaseManager.getDataSource()!!)
    }

    @get:Throws(Exception::class)
    @get:Synchronized
    val allActivePromotions: List<Any>
        get() = PromotionsManager.getAllActivePromotions(databaseManager.getDataSource()!!)

    // Get specific promotions
    @Synchronized
    @Throws(Exception::class)
    fun getPromotion(typePromotion: Int, typeGame: Int): List<PromotionsBean> {
        // Get promotions
        return PromotionsManager.getPromotion(databaseManager.getDataSource()!!, typePromotion, typeGame)
    }

    // Create promotion
    @Synchronized
    @Throws(Exception::class)
    fun createPromotion(promotionsBean: PromotionsBean) {
        // Create promotion
        PromotionsManager.createPromotion(promotionsBean, databaseManager.getDataSource()!!)
    }

    // Delete promotion
    @Synchronized
    @Throws(Exception::class)
    fun deletePromotion(promotionsBean: PromotionsBean) {
        // Delete promotion
        PromotionsManager.deletePromotion(promotionsBean, databaseManager.getDataSource()!!)
    }

    /*============================================
      Part of player settings manager
    ============================================*/
    // Get the player settings
    @Synchronized
    @Throws(Exception::class)
    fun getPlayerSettings(player: PlayerBean): PlayerSettingsBean {
        // Get settings
        return PlayerSettingsManager.getPlayerSettings(player, databaseManager.getDataSource()!!)
    }

    // Set the player settings
    @Synchronized
    @Throws(Exception::class)
    fun setPlayerSettings(player: PlayerBean, settingsBeans: PlayerSettingsBean) {
        // Set settings
        PlayerSettingsManager.setPlayerSettings(player, settingsBeans, databaseManager.getDataSource()!!)
    }

    // Create default settings
    @Synchronized
    @Throws(Exception::class)
    fun createDefaultPlayerSettings(player: PlayerBean) {
        // Create settings
        PlayerSettingsManager.createDefaultPlayerSettings(player, databaseManager.getDataSource()!!)
    }

    /*============================================
      Part of transaction manager
    ============================================*/
    // Get all the player transactions
    /*@Synchronized
    @Throws(Exception::class)
    fun getPlayerTransactions(player: PlayerBean): List<TransactionBean> {
        // Get transactions
        return Trans.getPlayerTransactions(player, databaseManager.getDataSource()!!)
    }

    // Get all the player transactions with selected items
    @Synchronized
    @Throws(Exception::class)
    fun getPlayerSelectedTransactions(player: PlayerBean): List<TransactionBean> {
        // Get transactions
        return this.transactionManager.getPlayerSelectedTransactions(player, databaseManager.getDataSource()!!)
    }

    // Get all the player transactions with selected items for a game
    @Synchronized
    @Throws(Exception::class)
    fun getPlayerGameSelectedTransactions(player: PlayerBean, selectedGame: Int): List<TransactionBean> {
        // Get transactions
        return this.transactionManager.getPlayerGameSelectedTransactions(
            player,
            databaseManager.getDataSource()!!,
            selectedGame
        )
    }

    // Get all the player transactions for a selected game
    @Synchronized
    @Throws(Exception::class)
    fun getPlayerGameTransactions(player: PlayerBean, selectedGame: Int): List<TransactionBean> {
        // Get transactions
        return this.transactionManager.getPlayerGameTransactions(player, databaseManager.getDataSource()!!, selectedGame)
    }

    // Create a transaction shop for a player
    @Synchronized
    @Throws(Exception::class)
    fun createTransaction(player: PlayerBean, transaction: TransactionBean?) {
        // Write transaction
        this.transactionManager.createTransaction(player, databaseManager.getDataSource()!!, transaction)
    }

    // Update a specified transaction
    @Synchronized
    @Throws(Exception::class)
    fun updateTransaction(transaction: TransactionBean?) {
        // Do the update
        this.transactionManager.updateTransaction(transaction, databaseManager.getDataSource()!!)
    }*/

    @get:Throws(Exception::class)
    @get:Synchronized
    val randomNickname: NicknameBean?
        get() = NickNameManager.getRandomNickname(databaseManager.getDataSource()!!)

    // Check if a nickname is blacklisted
    @Synchronized
    @Throws(Exception::class)
    fun isNicknameBlacklisted(nickname: String): Boolean {
        // Check the blacklist
        return NickNameManager.isNicknameBlacklisted(nickname, databaseManager.getDataSource()!!)
    }

    // Free a nickname
    @Synchronized
    @Throws(Exception::class)
    fun freeNickname(nickname: String) {
        // Free
        NickNameManager.freeNickname(nickname, databaseManager.getDataSource()!!)
    }

    /*============================================
      Part of host statistic manager
    ============================================*/
    // Creat a statistic host record
    @Synchronized
    @Throws(Exception::class)
    fun createHostRecord(hostStatisticsBean: HostStatisticsBean) {
        // Create record
        HostStatisticsManager.createHostRecord(hostStatisticsBean, databaseManager.getDataSource()!!)
    }

    /*============================================
      Part of event manager
    ============================================*/
    // Get event
    @Synchronized
    @Throws(Exception::class)
    fun getEvent(eventId: Long): EventBean {
        return EventManager.getEvent(eventId, databaseManager.getDataSource()!!)
    }

    // Get event
    @Synchronized
    @Throws(Exception::class)
    fun getEvent(eventDate: Timestamp): EventBean {
        return EventManager.getEvent(eventDate, databaseManager.getDataSource()!!)
    }

    @get:Throws(Exception::class)
    @get:Synchronized
    val events: List<Any>
        // Get the events
        get() = EventManager.getEvents(databaseManager.getDataSource()!!)

    // Get the winners of event
    @Synchronized
    @Throws(Exception::class)
    fun getEventWinners(eventId: Long): List<EventWinnerBean> {
        return EventManager.getEventWinners(eventId, databaseManager.getDataSource()!!)
    }

    // Create an event
    @Synchronized
    @Throws(Exception::class)
    fun createEvent(event: EventBean) {
        EventManager.createEvent(event, databaseManager.getDataSource()!!)
    }

    // Create a winner entry for an event
    @Synchronized
    @Throws(Exception::class)
    fun createEventWinner(eventWinner: EventWinnerBean) {
        EventManager.createWinnerEvent(eventWinner, databaseManager.getDataSource()!!)
    }

    // Update an event
    @Synchronized
    @Throws(Exception::class)
    fun updateEvent(event: EventBean) {
        EventManager.updateEvent(event, databaseManager.getDataSource()!!)
    }

    // Update the winner entry of an event
    @Synchronized
    @Throws(Exception::class)
    fun updateEventWinner(eventWinner: EventWinnerBean) {
        EventManager.updateEventWinner(eventWinner, databaseManager.getDataSource()!!)
    }

    /*============================================
      Part of message manager
    ============================================*/
    // Get a scheduled message
    @Synchronized
    @Throws(Exception::class)
    fun getScheduledMessage(messageId: Int): ScheduledMessageBean {
        return MessageManager.getScheduledMessage(messageId, databaseManager.getDataSource()!!)
    }

    @get:Throws(Exception::class)
    @get:Synchronized
    val scheduledMessages: List<Any>
        // Get the scheduled messages
        get() = MessageManager.getScheduledMessages(databaseManager.getDataSource()!!)

    // Create a scheduled message
    @Synchronized
    @Throws(Exception::class)
    fun createScheduledMessage(message: ScheduledMessageBean) {
        MessageManager.updateScheduledMessage(message, databaseManager.getDataSource()!!)
    }

    // Update a scheduled message
    @Synchronized
    @Throws(Exception::class)
    fun updateScheduledMessage(message: ScheduledMessageBean) {
        MessageManager.updateScheduledMessage(message, databaseManager.getDataSource()!!)
    }
}