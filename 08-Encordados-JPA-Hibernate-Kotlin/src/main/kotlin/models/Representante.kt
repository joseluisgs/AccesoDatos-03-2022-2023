package models

import javax.persistence.Embeddable


@Embeddable
data class Representante(
    var nombre: String,
    var email: String,
)
