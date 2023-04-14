# Result

## Railway Oriented Programming
[Railway Oriented Programming](https://fsharpforfunandprofit.com/rop/) es un patrón de diseño que nos permite escribir código más limpio y mantenible. Este patrón se basa en el concepto de [programación funcional](https://es.wikipedia.org/wiki/Programaci%C3%B3n_funcional) y en el uso de [monadas](https://es.wikipedia.org/wiki/Monada_(programaci%C3%B3n_funcional)). 

Es una técnica de programación funcional que nos permite manejar errores de forma más sencilla y segura. En lugar de usar excepciones, se usan valores de retorno para indicar si una operación ha tenido éxito o no. En el caso de que la operación haya fallado, se devuelve un valor que indica el error.

Se van encadenando operaciones que pueden fallar, y en caso de que alguna de ellas falle, se devuelve el error. De esta forma, se evita el uso de excepciones, que pueden ser difíciles de manejar.

![imagen](https://user-images.githubusercontent.com/24237865/229043283-3584b713-42a4-4491-a26c-a06b68b57f0d.jpg)

![imagen](https://www.netmentor.es/Imagen/65a1e2de-b4ed-4ca6-9ac7-6ae6fca01723.jpg)

- https://www.netmentor.es/entrada/railway-oriented-programming
- https://fsharpforfunandprofit.com/rop/
- https://adambennett.dev/2020/05/the-result-monad/
- https://github.com/michaelbull/kotlin-result
- https://github.com/GetStream/stream-result
- https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-result/
