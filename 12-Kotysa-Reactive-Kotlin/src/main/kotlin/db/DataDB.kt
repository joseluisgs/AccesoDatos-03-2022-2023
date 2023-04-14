package db

import models.Role
import models.User
import java.util.*

private val role_user_uuid = UUID.fromString("79e9eb45-2835-49c8-ad3b-c951b591bc7f")
private val role_admin_uuid = UUID.fromString("67d4306e-d99d-4e54-8b1d-5b1e92691a4e")
private val role_puto_amo_uuid = UUID.fromString("c0e5b9a0-5b9a-4b0c-8b9c-8c5c0b9c8b9c")

fun getRolesInit() = listOf(
    Role("user", role_user_uuid),
    Role("admin", role_admin_uuid),
    Role("puto-amo", role_puto_amo_uuid)
)

fun getUsersInit() = listOf(
    User("Pepe", "Perez", false, role_user_uuid, id = 123, alias = "pepe"),
    User("Big", "Boss", true, role_admin_uuid, "SolidSnake"),
    User("Puto", "Amo", true, role_puto_amo_uuid, "PutoAmo"),
    User("Ana", "Perez", false, role_user_uuid, "AnaPerez"),
)