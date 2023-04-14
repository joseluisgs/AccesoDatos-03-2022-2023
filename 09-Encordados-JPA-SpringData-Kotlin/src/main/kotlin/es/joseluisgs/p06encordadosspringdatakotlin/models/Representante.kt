package es.joseluisgs.p06encordadosspringdatakotlin.models

import javax.persistence.Embeddable


@Embeddable
data class Representante(
    var nombre: String,
    var email: String,
)
