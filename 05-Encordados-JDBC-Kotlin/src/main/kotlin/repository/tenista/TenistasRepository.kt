package repository.tenista

import model.Tenista
import repository.CrudRepository
import java.util.*

interface TenistasRepository : CrudRepository<Tenista, UUID>