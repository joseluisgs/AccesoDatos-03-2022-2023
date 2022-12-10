package models

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "raquetas")
// Defino estas queries que estarán disponibles globalmente
@NamedQuery(name = "Raqueta.findAll", query = "SELECT r FROM Raqueta r")
data class Raqueta(
    @Id @GeneratedValue
    // @UuidGenerator // Hibernate 6
    // Hibernate 5
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator",
    )
    @Column(name = "uuid")
    @Type(type = "uuid-char")
    val uuid: UUID = UUID.randomUUID(),
    var marca: String,
    var precio: Double,

    // Opcional, solo si queremos bidireccionalidad
    // 1-N con Tenista, una raqueta puede tener muchos tenistas que la usan por patrocinio
    // No queremos que se borre en cascada porque no queremos que se borren los tenistas
    // No pengais persistir en cascada porque no queremos que se persistan los tenistas
    // @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST, mappedBy = "orderRequest")
    @OneToMany(mappedBy = "raqueta", orphanRemoval = true, fetch = FetchType.EAGER)
    val tenistas: MutableList<Tenista> = mutableListOf(),

    // Mi representante
    @Embedded
    var representante: Representante? = null,

    @Column(name = "created_at")
    @Type(type = "org.hibernate.type.LocalDateTimeType")
    @CreationTimestamp
    val createdAt: LocalDateTime = LocalDateTime.now(),

    )

// No voy a guardar para las raquetas los tenistas, porque eso lo puedo consultar de otras maneras ...
// Si no tendríamos un problema de recursividad... Ahora no lo ves, pero seguro que en Acceso a Datos lo ves!!