package koin

import koin.di.DiAnnotationModule
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.ksp.generated.module

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class DiModuleTest : KoinComponent {
    @BeforeAll
    fun setUpDiModule() {
        startKoin {
            // Indicamos los módulos que queremos usar
            modules(DiAnnotationModule().module)
        }
    }

    @AfterAll
    fun tearDownDiModule() {
        stopKoin()
    }
}