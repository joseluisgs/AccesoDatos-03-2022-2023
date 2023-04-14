package entities

import models.User
import org.ufoss.kotysa.h2.H2Table
import org.ufoss.kotysa.h2.dateTime

/**
 * Tabla de usuarios
 */
object Users : H2Table<User>("users") {
    val id = autoIncrementInteger(User::id).primaryKey()
    val firstname = varchar(User::firstname)
    val lastname = varchar(User::lastname)
    val isAdmin = boolean(User::isAdmin)
    val roleId = uuid(User::roleId).foreignKey(Roles.id)
    val alias = varchar(User::alias)
    val creationTime = dateTime(User::creationTime)
}
