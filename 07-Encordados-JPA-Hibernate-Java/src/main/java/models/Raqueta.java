package models;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "raquetas")
@NamedQuery(name = "Raqueta.findAll", query = "SELECT r FROM Raqueta r")
public class Raqueta {
    private UUID uuid = UUID.randomUUID();
    private String marca;
    private Double precio;
    // Mi lista de tenistas
    private Set<Tenista> tenistas;

    public Raqueta(String marca, Double precio) {
        this.marca = marca;
        this.precio = precio;
    }

    public Raqueta() {
    }

    @Id
    @GeneratedValue
    @UuidGenerator // Genera un UUID
    @Column(name = "uuid")
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    // 1-N con Tenista, una raqueta puede tener muchos tenistas que la usan por patrocinio
    // No queremos que se borre en cascada porque no queremos que se borren los tenistas
    // @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST, mappedBy = "orderRequest")
    @OneToMany(mappedBy = "raqueta", orphanRemoval = true,  cascade = CascadeType.PERSIST)
    public Set<Tenista> getTenistas() {
        return tenistas;
    }

    public void setTenistas(Set<Tenista> tenistas) {
        this.tenistas = tenistas;
    }

    // Metodo helpers
    public void addTenista(Tenista tenista) {
        //tenistas.add(tenista);
        tenista.setRaqueta(this);
    }

    public void removeTenista(Tenista tenista) {
        tenista.removeRaqueta();
    }

    @Override
    public String toString() {
        return "Raqueta{" +
                "uuid=" + uuid +
                ", marca='" + marca + '\'' +
                ", precio=" + precio +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Raqueta)) return false;
        Raqueta raqueta = (Raqueta) o;
        return uuid.equals(raqueta.uuid) && marca.equals(raqueta.marca) && precio.equals(raqueta.precio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, marca, precio);
    }
}