package koin.di

import koin.controllers.PersonasController
import koin.repositories.PersonasRepository
import koin.repositories.PersonasRepositoryImpl
import koin.services.PersonasParser
import koin.services.PersonasParserImpl
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json
import nl.adaptivity.xmlutil.serialization.XML
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

//https://insert-koin.io/docs/reference/koin-annotations/modules
@Module
@ComponentScan("koin")
class DiAnnotationModule

// si quiero hacerlo manuales
// https://insert-koin.io/docs/reference/koin-core/dsl
val DiDslModule = module {
    // StringFormat
    single<StringFormat>(named("StringFormatJson")) { Json { prettyPrint = true } }
    single<StringFormat>(named("StringFormatXml")) { XML { indent = 4 } }

    // PersonasParser
    single<PersonasParser>(named("PersonasParserJson")) { PersonasParserImpl(get(named("StringFormatJson"))) }
    single<PersonasParser>(named("PersonasParserXml")) { PersonasParserImpl(get(named("StringFormatXml"))) }

    // Repository
    single<PersonasRepository> { PersonasRepositoryImpl() }

    // Controlador con Json
    single(named("PersonasControllerJson")) { PersonasController(get(), get(named("PersonasParserJson"))) }
    // Controlador con Xml
    single(named("PersonasControllerXml")) { PersonasController(get(), get(named("PersonasParserXml"))) }
}

// si quiero hacerlo manuales DSL Constructor, solo si no debo especificar los get con named, en repositorio si
// puedo hacerlo, es la forma sencilla como en anotaciones!!
// https://insert-koin.io/docs/reference/koin-core/dsl-update
val DiLambdaModule = module {
    // StringFormat
    single<StringFormat>(named("StringFormatJsonLambda")) { Json { prettyPrint = true } }
    single<StringFormat>(named("StringFormatXmlLambda")) { XML { indent = 4 } }

    // PersonasParser
    single<PersonasParser>(named("PersonasParserJsonLambda")) { PersonasParserImpl(get(named("StringFormatJsonLambda"))) }
    single<PersonasParser>(named("PersonasParserXmlLambda")) { PersonasParserImpl(get(named("StringFormatXmlLambda"))) }


    // Repository, con DSL Constructor
    singleOf(::PersonasRepositoryImpl) {
        bind<PersonasRepository>()
    }

    // Controlador con Json
    single(named("PersonasControllerJsonLambda")) { PersonasController(get(), get(named("PersonasParserJsonLambda"))) }
    // Controlador con Xml
    single(named("PersonasControllerXmlLambda")) { PersonasController(get(), get(named("PersonasParserXmlLambda"))) }


}