package models

import java.time.LocalDate

data class User(
    val id: Long,
    val name: String,
    val email: String,
    val password: String,
    val createdAt: LocalDate = LocalDate.now(),
    val updatedAt: LocalDate = LocalDate.now(),
    val role: Role = Role.USER
) {
    enum class Role {
        ADMIN, USER
    }
}