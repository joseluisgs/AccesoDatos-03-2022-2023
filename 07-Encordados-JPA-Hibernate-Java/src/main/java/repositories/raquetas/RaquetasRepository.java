package repositories.raquetas;

import models.Raqueta;
import repositories.CrudRepository;

import java.util.UUID;

public interface RaquetasRepository extends CrudRepository<Raqueta, UUID> {
}