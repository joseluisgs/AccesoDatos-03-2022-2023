package dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val name: String,
    val alias: String?,
    val role: String
)