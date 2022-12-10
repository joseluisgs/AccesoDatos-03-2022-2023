package repositories

import models.User

interface UsersRepository : CrudRepository<User, Int>