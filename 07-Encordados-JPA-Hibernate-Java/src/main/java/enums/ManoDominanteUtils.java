package enums;

public class ManoDominanteUtils {
    public static ManoDominante from(String manoDominante) {
        switch (manoDominante) {
            case "DERECHA":
                return ManoDominante.DERECHA;
            case "IZQUIERDA":
                return ManoDominante.IZQUIERDA;
            default:
                throw new IllegalArgumentException("Mano dominante no válida");
        }
    }

    public static String toString(ManoDominante manoDominante) {
        switch (manoDominante) {
            case DERECHA:
                return "DERECHA";
            case IZQUIERDA:
                return "IZQUIERDA";
            default:
                throw new IllegalArgumentException("Mano dominante no válida");
        }
    }
}
