package enums;

public class TipoRevesUtils {
    public static TipoReves from(String tipoReves) {
        switch (tipoReves) {
            case "UNA MANO":
                return TipoReves.UNA_MANO;
            case "DOS MANOS":
                return TipoReves.DOS_MANOS;
            default:
                throw new IllegalArgumentException("Tipo de revés no válido");
        }
    }

    public static String toString(TipoReves tipoReves) {
        switch (tipoReves) {
            case UNA_MANO:
                return "UNA MANO";
            case DOS_MANOS:
                return "DOS MANOS";
            default:
                throw new IllegalArgumentException("Tipo de revés no válido");
        }
    }
}
