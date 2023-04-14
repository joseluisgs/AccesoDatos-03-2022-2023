package repository

import model.Tenista
import java.util.*

interface TenistasRepository : CrudRepository<Tenista, UUID>