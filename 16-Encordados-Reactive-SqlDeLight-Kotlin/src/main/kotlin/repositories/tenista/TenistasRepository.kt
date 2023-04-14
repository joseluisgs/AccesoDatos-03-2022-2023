package repositories.tenista

import models.Tenista
import repositories.CrudRepository

interface TenistasRepository : CrudRepository<Tenista, Long>