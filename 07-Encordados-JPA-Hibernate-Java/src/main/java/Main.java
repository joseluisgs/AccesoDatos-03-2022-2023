import controllers.MutuaController;
import db.DataDB;
import db.HibernateManager;
import models.Raqueta;
import models.Tenista;
import repositories.raquetas.RaquetasRepositoryImpl;
import repositories.tenistas.TenistasRepositoryImpl;
import utils.ApplicationProperties;
import utils.MyLocale;

import java.util.Comparator;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Main {
    static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        System.out.println("\uD83C\uDFBE Mutua Madrid Open - Gestión de Tenistas \uD83C\uDFBE");

        // iniciamos las tablas de la base de datos
        initDataBase();

        // Creamos nuestro controlador y le añadimos y le inyectamos las dependencias
        var controller = new MutuaController(
                new RaquetasRepositoryImpl(),
                new TenistasRepositoryImpl()
        );

        // Vamos a hacer el CRUD de Raquetas
        // Insertamos
        DataDB.getRaquetasInit().forEach(controller::createRaqueta);

        // Obtenemos todas las raquetas ordenadas por marca
        var raquetas = controller.getRaquetas().stream()
                .sorted(Comparator.comparing(Raqueta::getMarca))
                .collect(Collectors.toList());
        raquetas.forEach(System.out::println);

        // A cada tenista debemos asignarle una raqueta, porque es 1 a M
        var tenistas = DataDB.getTenistasInit();
        tenistas.get(0).setRaqueta(raquetas.get(0)); // Nadal, Babolat
        tenistas.get(1).setRaqueta(raquetas.get(2)); // Federer, Wilson
        tenistas.get(2).setRaqueta(raquetas.get(1)); // Djokovic, Head
        tenistas.get(3).setRaqueta(raquetas.get(0)); // Thiem, Babolat
        tenistas.get(4).setRaqueta(raquetas.get(0)); // Alcaraz, Babolat

        // Insertamos los tenistas
        tenistas.forEach(controller::createTenista);

        // Obtenemos todos los tenistas
        tenistas = controller.getTenistas();
        tenistas.forEach(System.out::println);

        // Tenista por ID
        var tenista = controller.getTenistaById(tenistas.get(4).getUuid());
        // Si no es null lo imprimimos, sabemos que no lo es, pero y si no lo encuentra porque el uuid es incorrecto?
        tenista.ifPresent(System.out::println);
        // Vamos a cambiarle sus ganancias, que Carlos ha ganado el torneo
        tenista.ifPresent(t -> t.setGanancias(1000000.0 + t.getGanancias()));
        // Actualizamos el tenista
        tenista.ifPresent(controller::updateTenista);

        // vamos a buscarlo otra vez, para ver los cambios
        tenista = controller.getTenistaById(tenistas.get(4).getUuid());
        tenista.ifPresent(System.out::println);

        // Vamos a borrar a Roger Federer, porque se retira
        var roger = controller.getTenistaById(tenistas.get(1).getUuid());
        roger.ifPresent(x -> {
            if (controller.deleteTenista(x))
                System.out.println("Roger Federer se ha retirado");
        });


        // Sacamos todos los tenistas ordenados por ranking
        tenistas = controller.getTenistas().stream()
                .sorted(Comparator.comparing(Tenista::getRanking))
                .collect(Collectors.toList());
        tenistas.forEach(System.out::println);

        // Ademas podemos jugar con los tenistas
        // Tenista que más ha ganado
        System.out.println("");
        var ganador = controller.getTenistas().stream()
                .max(Comparator.comparing(Tenista::getGanancias));
        System.out.println("Tenistas con mas ganancias: ");
        ganador.ifPresent(System.out::println);
        // Tenista más novel en el circuito
        var joven = controller.getTenistas().stream()
               .max(Comparator.comparing(Tenista::getAñoProfesional));
        System.out.println("Tenistas más novel: ");
        joven.ifPresent(System.out::println);
        // Tenista más veterano en el circuito
        var veterano = controller.getTenistas().stream()
                .min(Comparator.comparing(Tenista::getAñoProfesional));
        System.out.println("Tenistas más veterano: ");
        veterano.ifPresent(System.out::println);
        // Tenista más alto
        var alto = controller.getTenistas().stream()
                .max(Comparator.comparing(Tenista::getAltura));
        System.out.println("Tenistas más alto: ");
        alto.ifPresent(System.out::println);
        // Agrupamos por nacionalidad
        System.out.println("Tenistas agrupados por nacionalidad: ");
        var nacionalidades = controller.getTenistas().stream()
                .collect(Collectors.groupingBy(Tenista::getPais));
        nacionalidades.forEach((pais, tenistasPais) -> {
            System.out.println(pais + ": " + tenistasPais);
        });
        // Agrupamos por mano hábil
        System.out.println("Tenistas agrupados por mano hábil: ");
        var manos = controller.getTenistas().stream()
                .collect(Collectors.groupingBy(Tenista::getManoDominante));
        manos.forEach((mano, tenistasMano) -> {
            System.out.println(mano + ": " + tenistasMano);
        });
        // ¿Cuantos tenistas a un o dos manos hay?
        System.out.println("Nº de tenistas por mano hábil: ");
        var manoDominante = controller.getTenistas().stream()
                .collect(Collectors.groupingBy(Tenista::getManoDominante));
        manoDominante.forEach((mano, tenistasMano) -> {
            System.out.println(mano + ": " + tenistasMano.size());
        });

        // ¿Cuantos tenistas hay por cada raqueta?
        System.out.println("Nº de tenistas por raqueta: ");
        var raquetasTenistas = controller.getTenistas().stream()
                .collect(Collectors.groupingBy(Tenista::getRaqueta));
        raquetasTenistas.forEach((raqueta, tenistasRaqueta) -> {
            System.out.println(raqueta.getMarca() + ": " + tenistasRaqueta.size());
        });
        // La raqueta más cara
        System.out.println("Raqueta más cara: ");
        var raquetaMasCara = controller.getRaquetas().stream()
                .max(Comparator.comparing(Raqueta::getPrecio));
        raquetaMasCara.ifPresent(System.out::println);
        // ¿Qué tenista usa la raqueta más cara?
        System.out.println("Tenista que usa la raqueta más cara: ");
        var tenistaRaquetaMasCara = controller.getTenistas().stream()
                .filter(t -> t.getRaqueta().equals(
                        controller.getRaquetas().stream()
                                .max(Comparator.comparing(Raqueta::getPrecio))
                                .get()
                ))
                .findFirst();
        tenistaRaquetaMasCara.ifPresent(System.out::println);
        // Ganancias totales de todos los tenistas
        System.out.println("Ganancias totales de todos los tenistas: ");
        var gananciasTotales = controller.getTenistas().stream()
                .mapToDouble(Tenista::getGanancias)
                .sum();
        System.out.println(MyLocale.toLocalMoney(gananciasTotales));
        // Precio medio de las raquetas
        System.out.println("Precio medio de las raquetas: ");
        var precioMedioRaquetas = controller.getRaquetas().stream()
                .mapToDouble(Raqueta::getPrecio)
                .average();
        precioMedioRaquetas.ifPresent(p -> System.out.println(MyLocale.toLocalMoney(p)));

    }

    private static void initDataBase() {
        // Leemos el fichero de configuración

        ApplicationProperties properties = new ApplicationProperties();
        logger.info("Leyendo fichero de configuración..." + properties.readProperty("app.title"));
        HibernateManager hb = HibernateManager.getInstance();
        hb.open();
        hb.close();
    }
}
