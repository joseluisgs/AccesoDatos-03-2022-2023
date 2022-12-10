package db;

import enums.ManoDominante;
import enums.TipoReves;
import models.Raqueta;
import models.Tenista;

import java.time.LocalDate;
import java.util.List;

public final class DataDB {
    public static List<Raqueta> getRaquetasInit() {
        return List.of(
                new Raqueta("Babolat", 200.0),
                new Raqueta("Wilson", 225.0),
                new Raqueta("Head", 250.0)
        );
    }

    public static List<Tenista> getTenistasInit() {
        return List.of(
                new Tenista("Rafael Nadal", 2, LocalDate.parse("1985-06-04"), 2005, 185, 80, 10000000.0, ManoDominante.IZQUIERDA, TipoReves.DOS_MANOS, 6789, "España"),
                new Tenista("Roger Federer", 3, LocalDate.parse("1981-01-01"), 2000, 188, 83, 20000000.0, ManoDominante.DERECHA, TipoReves.UNA_MANO, 3789, "Suiza"),
                new Tenista("Novak Djokovic", 4, LocalDate.parse("1986-05-05"), 2004, 189, 81, 10000000.0, ManoDominante.DERECHA, TipoReves.DOS_MANOS, 1970, "Serbia"),
                new Tenista("Dominic Thiem", 5, LocalDate.parse("1985-06-04"), 2015, 188, 82, 10000.0, ManoDominante.DERECHA, TipoReves.UNA_MANO, 1234, "Austria"),
                new Tenista("Carlos Alcaraz", 1, LocalDate.parse("2003-05-05"), 2019, 185, 81, 5000000.0, ManoDominante.DERECHA, TipoReves.DOS_MANOS, 6789, "España")
        );
    }
}
