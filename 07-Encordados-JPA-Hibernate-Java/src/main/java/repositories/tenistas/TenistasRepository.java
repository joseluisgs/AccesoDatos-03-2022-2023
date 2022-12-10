package repositories.tenistas;

import models.Tenista;
import repositories.CrudRepository;

import java.util.UUID;

public interface TenistasRepository extends CrudRepository<Tenista, UUID> {
}