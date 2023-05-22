package fr.kotmine.persistanceapi.models.players

data class GroupsBean(
    val groupId: Long,
    val groupName: String,
    val rank: Int,
    val tag: String,
    val prefix: String,
    val suffix: String,
    val multiplier: Int
)
