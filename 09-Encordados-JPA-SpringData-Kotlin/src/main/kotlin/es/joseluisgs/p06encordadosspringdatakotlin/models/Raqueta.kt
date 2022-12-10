package es.joseluisgs.p06encordadosspringdatakotlin.models

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.Type
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "raquetas")
data class Raqueta(
    // Generated Value UUID
    @Id @GeneratedValue()
    @Type(type = "uuid-char")
    val uuid: UUID = UUID.randomUUID(),
    var marca: String,
    var precio: Double,

    // 1-N con Tenista, una raqueta puede tener muchos tenistas que la usan por patrocinio
    // No queremos que se borre en cascada porque no queremos que se borren los tenistas
    // @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST, mappedBy = "orderRequest")
    @OneToMany(mappedBy = "raqueta", orphanRemoval = true, fetch = FetchType.EAGER)
    val tenistas: MutableList<Tenista> = mutableListOf(),

    // Mi representante
    @Embedded
    var representante: Representante? = null,

    @Column(name = "created_at")
    @CreationTimestamp
    val createdAt: LocalDateTime = LocalDateTime.now(),
)

// No voy a guardar para las raquetas los tenistas, porque eso lo puedo consultar de otras maneras ...
// Si no tendríamos un problema de recursividad... Ahora no lo ves, pero seguro que en Acceso a Datos lo ves!!