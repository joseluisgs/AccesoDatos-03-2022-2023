package entities

import models.Role
import org.ufoss.kotysa.h2.H2Table

/**
 * Tabla de roles
 */
object Roles : H2Table<Role>() {
    val id = uuid(Role::id).primaryKey()
    val label = varchar(Role::label).unique()
}
