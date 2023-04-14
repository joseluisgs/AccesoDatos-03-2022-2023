# Kotlin Inyección de Dependencias con Koin
Ejemplos de cómo aplicar Inyección de Dependencias en Kotlin 

[![Kotlin](https://img.shields.io/badge/Code-Kotlin-blueviolet)](https://kotlinlang.org/)
[![LICENSE](https://img.shields.io/badge/License-CC-%23e64545)](https://joseluisgs.github.io/docs/license/)



![imagen](https://miro.medium.com/max/960/1*qS2HAgsw9grFfPS3L-Rx1g.png)

- [Kotlin Inyección de Dependencias con Koin](#kotlin-inyección-de-dependencias-con-koin)
  - [Acerca de](#acerca-de)
  - [Inyección de Dependencias (DI)](#inyección-de-dependencias-di)
    - [Código Acoplado](#código-acoplado)
    - [Inyección por Setter](#inyección-por-setter)
    - [Inyección con Constructor](#inyección-con-constructor)
    - [Inyección de dependencias IoC o Service Locator](#inyección-de-dependencias-ioc-o-service-locator)
  - [Inyección de dependencias manual](#inyección-de-dependencias-manual)
  - [Inyección de dependencias con Koin](#inyección-de-dependencias-con-koin)
  - [Conclusiones](#conclusiones)
  - [Referencias](#referencias)
  - [Recursos](#recursos)
  - [Autor](#autor)
    - [Contacto](#contacto)
    - [¿Un café?](#un-café)
  - [Licencia de uso](#licencia-de-uso)

## Acerca de
El siguiente proyecto tiene como objetivo acercar cómo usar la Inyección de Dependencias en Kotlin

## Inyección de Dependencias (DI)

![imagen2](https://koenig-media.raywenderlich.com/uploads/2016/11/Dagger-feature.png)

La inyección de dependencias es una técnica de desarrollo que permite a los desarrolladores de software, a través de la inyección de dependencias, obtener una dependencia de una clase en una clase que no tiene acceso a ella.

El Principio de inyección de dependencia no es más que poder pasar (inyectar) las dependencias cuando sea necesario en lugar de inicializar las dependencias dentro de la clase receptora y con ello poder desacoplar la construcción de sus clases de la construcción de las dependencias de sus clases.

Es decir, aplicamos una composición entre clases, con el objetivo que cada clase tenga sus responsabilidades bien definidas y acotadas. Es decir, si una clase A, necesita alguna funcionalidad de B, nosotros al crear A, debemos "inyectarle" B. De esta manera A, puede usar la funcionalidad de B. 

De esta manera, podemos cambiar B, por C, siempre y cuando mantengan el contrato que permite ser usado por A. Ya no es la clase A la responsable de definir sus dependencias sino que lo es el programa o clase superior que le inyecta la dependencia que en ese momento necesite según los requerimientos.

### Código Acoplado
Esto es lo que **no deberíamos hacer**
```kotlin
class ClassA {

  var classB = ClassB()

  fun tenPercent() {
    return classB.calculate() * 0.1d
  }
}
```
```kotlin
fun main() {
    val classA = ClassA()
}
```
### Inyección por Setter
No recomendado. Con este enfoque, eliminamos la palabra clave new ClassB de nuestra ClassA. Por lo tanto, alejamos la responsabilidad de la creación de ClassB deClassA.

```kotlin
class ClassA {

  var lateinit classB: ClassB

  /* Setter Injection */
  fun setClassB(injected: ClassB) {
    classB = injected
  }

  fun tenPercent() {
    return classB.calculate() * 0.1d
  }
}
```
```kotlin
class Main {
  fun main() {
    val classA = ClassA()
    val classB = ClassB()

    classA.setClassB(classB)

    println("Ten Percent: ${classA.tenPercent()}")
  }
}
```

Pero hay un problema significativo con el enfoque de Inyección con Setters:

Estamos ocultando la dependencia ClassB enClassA porque al leer la firma del constructor, no podemos identificar sus dependencias de inmediato. El siguiente código provoca una NullPointerException en tiempo de ejecución:
```kotlin
class Main {
  fun void main() {
    val classA = ClassA()

     println("Ten Percent: ${classA.tenPercent()}") // NullPointerException here
  }
}
```

### Inyección con Constructor
ClassA todavía tiene una fuerte dependencia de ClassB pero ahora se puede inyectar desde afuera usando el constructor:

```kotlin
class ClassA(val classB: ClassB) {

  int tenPercent() {
    return classB.calculate() * 0.1d
  }
}
```
```kotlin
class Main {
  fun main() {
    /* Notice that we are creating ClassB fisrt */
    val classB = ClassB()

    /* Constructor Injection */
    val classA = ClassA(classB)

    println("Ten Percent: ${classA.tenPercent()}")
  }
}
```

La funcionalidad permanece intacta en comparación con el enfoque de Inyección Setter. Eliminamos la inicialización nueva de la ClaseA.

Todavía podemos inyectar una subclase especializada de ClassB a ClassA.

Ahora el compilador nos pedirá las dependencias que necesitamos en tiempo de compilación.

### Inyección de dependencias IoC o Service Locator
![image](https://www.apriorit.com/images/articles/ServiceLocator-DI.png)
![image](https://i.stack.imgur.com/BrkBd.png)
![image](https://www.rookian.com/img/solid.png)

A la hora de resolver las dependencias veremos que tendremos dos enfoques, uno puro, basado en un contenedor de DI, grafo de dependencias o módulo de Inversión de Control y otros enfoques que es a través de un proveedor de servicios.

La inyección de dependencia es una técnica en la que un objeto recibe otros objetos de los que depende. Estos otros objetos se denominan dependencias..

El patrón de localización de servicios es un patrón de diseño utilizado en el desarrollo de software para encapsular los procesos involucrados en la obtención de un servicio con una fuerte capa de abstracción. Este patrón utiliza un registro central conocido como “localizador de servicios”, que a pedido devuelve la información necesaria para realizar una determinada tarea.

Service Locator se utiliza cuando no conoce el proveedor real del servicio antes del tiempo de ejecución. DI se usa cuando sabe que es el contenedor estático el que proporciona ese servicio.

En resumen, el Localizador de servicios y la Inyección de dependencias son solo implementaciones del ***Principio de inversión de dependencias***.

Ambos suenan similares y nos brindan beneficios similares, pero en algún lugar te preguntas por qué tenemos dos nombres para el mismo patrón que hace un trabajo casi similar.

La diferencia puede parecer leve aquí, pero incluso con Service Locator, la clase sigue siendo responsable de crear sus dependencias. Simplemente usa el localizador de servicios para hacerlo. Le pide a ServiceLocator que obtenga sus dependencias. Con la inyección de dependencia, la clase recibe sus dependencias. No sabe ni le importa de dónde vienen.

## Inyección de dependencias manual
En estos ejemplos, se muestra distintos tipos de inyecciones, ya sea usando clases o aplicando el patrón de inyección en base a interfaces.

Se implementan desde constructores o builders que las obtienen en base a una función de inyección, a construcción de las dependencias de manera "perezosa" o lazy, con el objetivo de que la dependencia solo se cargue la primera vez que se ejecute.

## Inyección de dependencias con Koin

![imageKoin](https://www.kotzilla.io/wp-content/uploads/2022/01/kotzilla-moodboard_Koin_format-site-web-line.png)

Koin es un framework de inyección de dependencias pragmático y liviano para desarrolladores Kotlin.
Técnicamente Koin es un Service Locator. La idea básica detrás de un Service Locator es tener una clase que sepa cómo obtener todos los servicios que utiliza nuestra aplicación. Así que, el Service Locator tendría una propiedad por cada uno de esos servicios, que devolvería un objeto del tipo adecuado cuando se lo soliciten. Service Locator garantiza que el desarrollador obtenga lo solicitado automáticamente, introduzca un poco más de código, pero luego facilite la trazabilidad.

![ServiceLocator](https://miro.medium.com/max/411/0*HX5NbuNoewvMi5O2.png)

```kotlin
class Something {
    //...
}

class OtherThing() {
    //...
}
class Dependency(
    something: Something,
    otherThing: OtherThing) {
    // ... Do something
}
val mainKoinModule =
    module {
        single { Something() }
        single { OtherThing() }
        single { Dependency(get(), get()) }
    }
class Target {
   private val dependency: Dependency by inject()
}
```

El principal secreto de Korin es usar los Reified Functions, es decir, reificar la información de tipo genérico en tiempo de ejecución. Además basado en DSL (Domain Specific Language) otras de las características de usar Kotlin.

Para trabajar con Koin debemos manejar estos conceptos: 

- **Funciones:**
  - **startKoin { }** Crea una instancia de Koin y registra su contexto.
  - **logger()** Carga el logger a usar por Koin, si necesitamos de ello.
  - **modules()** Carga la lista de módulos que va a usar Koin.
  - **by inject()** Obtiene la dependencia de manera perezosa o lazy.
  - **get()** Obtiene la dependencia de manera directa, es decir, la instancia.
  - **getProperty()/setProperty()** Getter/Setter de una propiedad.
  - **KoinComponent { }** Te permite usar las facilidades de Koin.

- **Scope:**
  - **module { }** Crea el módulo que Koin usa para proveer todas las dependencias.
  - **factory { }** Nos ofrece una *instancia nueva* del objeto cada vez que se produzca la inyección.
  - **single { }** Nos ofrece la dependencia como *singleton*, es decir, siempre la misma instancia del objeto cada vez que sea inyectada.
   - **get()** Es usado en el constructor o en otros contextos para proveer las dependencias indicadas.
  - **scope { }** Grupo logico para el scope
  - **scoped { }** Ofrece la definición de una dependencia activa un contexto, o scope

- **Modulos:**
  - **named("a_qualifier")** Ponemos un texto a la definición para "cualificarlo".
  - **named<MyType>()** Devuelve un tipo a partir de una "definición" dada.
  - **bind<MyInterface>()** Indica el tipo de dependencia se va a hacer el bind con el objeto.
  - **binds(arrayOf(...))** Indica un array de tipos se va a hacer el bind con el objeto.
  - **createdAtStart()** Crea una instancia de Koin del tipo Singleton al comienzo.


Por otro lado, Koin también te deja trabajar con anotaciones, lo que le da un efoque muy rápido cómo definimos las dependencias.

Más información en: https://insert-koin.io/

## Conclusiones
Es importante no obsesionarse en si la inyección se resuelve por anotaciones, por DSL o si realmente las librerías que usas son un sistema DI puro o basado en un Service Locator (no te vuelvas loco/a por eso ni seas tan purista, lo importante es que las dependencias te las da). Mi consejo es que uses el que más seguro te haga sentir y sobre todo el que se adapte mejor a tu problema o aplicación de desarrollar.

Koin es un service locator, pero que te resuelve el problema y de manera muy óptima. Tiene elementos muy interesante y funciona a la perfección con Kotlin. Puedes nombrar dependencias y puedes aplicar Lazy de la misma manera que lo hace Kotlin y no cargando una librería especial. Para medianos o pequeños es una opción muy recomendada. Koin usa DSL de Kotlin y resuelve de forma perezosa sus dependencias en tiempo de ejecución. No hace nada en tiempo de compilación. Es una biblioteca mucho más pequeña y liviana. Con Koin Annotation Processor puedes generar código de inyección de dependencias en tiempo de compilación.


Más información: https://proandroiddev.com/exploring-dependency-injection-in-android-dagger-koin-and-kodein-e219a764be52

## Referencias
- [Koin](https://insert-koin.io/)
- [Koin Baeldung](https://www.baeldung.com/kotlin/koin-di)

## Recursos
- Twitter: https://twitter.com/joseluisgonsan
- GitHub: https://github.com/joseluisgs
- Web: https://joseluisgs.github.io
- Discord del módulo: https://discord.gg/RRGsXfFDya
- Aula DAMnificad@s: https://discord.gg/XT8G5rRySU


## Autor

Codificado con :sparkling_heart: por [José Luis González Sánchez](https://twitter.com/joseluisgonsan)

[![Twitter](https://img.shields.io/twitter/follow/joseluisgonsan?style=social)](https://twitter.com/joseluisgonsan)
[![GitHub](https://img.shields.io/github/followers/joseluisgs?style=social)](https://github.com/joseluisgs)

### Contacto
<p>
  Cualquier cosa que necesites házmelo saber por si puedo ayudarte 💬.
</p>
<p>
 <a href="https://joseluisgs.github.io/" target="_blank">
        <img src="https://joseluisgs.github.io/img/favicon.png" 
    height="30">
    </a>  &nbsp;&nbsp;
    <a href="https://github.com/joseluisgs" target="_blank">
        <img src="https://distreau.com/github.svg" 
    height="30">
    </a> &nbsp;&nbsp;
        <a href="https://twitter.com/joseluisgonsan" target="_blank">
        <img src="https://i.imgur.com/U4Uiaef.png" 
    height="30">
    </a> &nbsp;&nbsp;
    <a href="https://www.linkedin.com/in/joseluisgonsan" target="_blank">
        <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/c/ca/LinkedIn_logo_initials.png/768px-LinkedIn_logo_initials.png" 
    height="30">
    </a>  &nbsp;&nbsp;
    <a href="https://discordapp.com/users/joseluisgs#3560" target="_blank">
        <img src="https://logodownload.org/wp-content/uploads/2017/11/discord-logo-4-1.png" 
    height="30">
    </a> &nbsp;&nbsp;
    <a href="https://g.dev/joseluisgs" target="_blank">
        <img loading="lazy" src="https://googlediscovery.com/wp-content/uploads/google-developers.png" 
    height="30">
    </a>    
</p>

### ¿Un café?
<p><a href="https://www.buymeacoffee.com/joseluisgs"> <img align="left" src="https://cdn.buymeacoffee.com/buttons/v2/default-blue.png" height="50" alt="joseluisgs" /></a></p><br><br><br>

## Licencia de uso

Este repositorio y todo su contenido está licenciado bajo licencia **Creative Commons**, si desea saber más, vea la [LICENSE](https://joseluisgs.github.io/docs/license/). Por favor si compartes, usas o modificas este proyecto cita a su autor, y usa las mismas condiciones para su uso docente, formativo o educativo y no comercial.

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Licencia de Creative Commons" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br /><span xmlns:dct="http://purl.org/dc/terms/" property="dct:title">JoseLuisGS</span> by <a xmlns:cc="http://creativecommons.org/ns#" href="https://joseluisgs.github.io/" property="cc:attributionName" rel="cc:attributionURL">José Luis González Sánchez</a> is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Reconocimiento-NoComercial-CompartirIgual 4.0 Internacional License</a>.<br />Creado a partir de la obra en <a xmlns:dct="http://purl.org/dc/terms/" href="https://github.com/joseluisgs" rel="dct:source">https://github.com/joseluisgs</a>.
