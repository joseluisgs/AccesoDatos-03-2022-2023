package validators

import com.github.michaelbull.result.get
import com.github.michaelbull.result.getError
import errors.GuitarraError
import models.Guitarra
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.util.*

class GuitarraValidatorKtTest {

    @Test
    fun validateOk() {
        val guitarra = Guitarra(1, UUID.randomUUID(), "Test Marca", "Test Modelo", 10.0, 10)
        val result = guitarra.validate()

        // En este caso es correcto
        assertAll(
            { assert(result.getError() == null) },
            { assert(result.get() == guitarra) }
        )
    }

    @Test
    fun validateFailPrecio() {
        val guitarra = Guitarra(1, UUID.randomUUID(), "Test Marca", "Test Modelo", -10.0, 10)
        val result = guitarra.validate()

        // En este caso es correcto
        assertAll(
            { assert(result.getError() != null) },
            { assert(result.get() == null) },
            { assert(result.getError() is GuitarraError.PrecioNoValido) },
            { assert(result.getError()!!.message == "El precio no puede ser negativo") }
        )
    }

    @Test
    fun validateFailStock() {
        val guitarra = Guitarra(1, UUID.randomUUID(), "Test Marca", "Test Modelo", 10.0, -10)
        val result = guitarra.validate()

        // En este caso es correcto
        assertAll(
            { assert(result.getError() != null) },
            { assert(result.get() == null) },
            { assert(result.getError() is GuitarraError.StockNoValido) },
            { assert(result.getError()!!.message == "El stock no puede ser negativo") }
        )
    }

    @Test
    fun validateFailMarca() {
        val guitarra = Guitarra(1, UUID.randomUUID(), "", "Test Modelo", 10.0, 10)
        val result = guitarra.validate()

        // En este caso es correcto
        assertAll(
            { assert(result.getError() != null) },
            { assert(result.get() == null) },
            { assert(result.getError() is GuitarraError.MarcaNoValida) },
            { assert(result.getError()!!.message == "La marca no puede estar vacía") }
        )
    }

    @Test
    fun validateFailModelo() {
        val guitarra = Guitarra(1, UUID.randomUUID(), "Test Marca", "", 10.0, 10)
        val result = guitarra.validate()

        // En este caso es correcto
        assertAll(
            { assert(result.getError() != null) },
            { assert(result.get() == null) },
            { assert(result.getError() is GuitarraError.ModeloNoValido) },
            { assert(result.getError()!!.message == "El modelo no puede estar vacío") }
        )
    }
}