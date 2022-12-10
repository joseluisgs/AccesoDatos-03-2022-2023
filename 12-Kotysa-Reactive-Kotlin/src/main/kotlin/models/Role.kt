package models

import java.util.*

data class Role(
    val label: String,
    val id: UUID = UUID.randomUUID()
)