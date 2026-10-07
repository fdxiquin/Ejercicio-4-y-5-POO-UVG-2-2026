/**
 * Cliente individual: se identifica con su DPI, tiene un único alquiler activo y recibe
 * descuento del 5 % a partir de su cuarto alquiler confirmado.
 */
public class ClienteIndividual extends Cliente {

    private static final int LIMITE_ALQUILERES_ACTIVOS = 1;

    private int alquileresConfirmados;

    /**
     * Propósito: crear un cliente individual. Llama a super con el DPI como identificador y
     * valida que el DPI tenga exactamente 13 dígitos. Inicia sin alquileres confirmados.
     */
    public ClienteIndividual(String dpi, String nombre, TipoLicencia[] licencias) {
        super(dpi, nombre, licencias);
        if (!identificador.matches("\\d{13}")) {
            throw new IllegalArgumentException("El DPI debe tener exactamente 13 dígitos numéricos.");
        }
        this.alquileresConfirmados = 0;
    }

    /**
     * Propósito: crear un cliente individual con alquileres confirmados previos. Se usa en los
     * datos iniciales para demostrar el descuento del cuarto alquiler sin registrar ingresos.
     */
    public ClienteIndividual(String dpi, String nombre, TipoLicencia[] licencias,
                             int alquileresConfirmadosPrevios) {
        this(dpi, nombre, licencias);
        if (alquileresConfirmadosPrevios < 0) {
            throw new IllegalArgumentException("Los alquileres confirmados previos no pueden ser negativos.");
        }
        this.alquileresConfirmados = alquileresConfirmadosPrevios;
    }

    /**
     * Propósito: retornar 0 si tiene menos de 3 alquileres confirmados, o el 5 % del subtotal
     * si ya tiene 3 o más (es decir, desde el cuarto alquiler confirmado).
     */
    @Override
    public double calcularDescuento(double subtotal) {
        if (alquileresConfirmados >= 3) {
            return subtotal * 0.05;
        }
        return 0;
    }

    /** Propósito: retornar el límite de alquileres activos del individual (1). */
    @Override
    public int getLimiteAlquileresActivos() {
        return LIMITE_ALQUILERES_ACTIVOS;
    }

    /**
     * Propósito: además de sumar el alquiler activo (valida el límite en la clase base), suma
     * uno al conteo de alquileres confirmados que determina el descuento.
     */
    @Override
    public void registrarAlquilerActivo() {
        super.registrarAlquilerActivo();
        alquileresConfirmados++;
    }

    /** Propósito: agregar al texto base el tipo, el DPI y los alquileres confirmados. */
    @Override
    public String toString() {
        return "[Individual] " + super.toString() + " | DPI: " + identificador
                + " | Alquileres confirmados: " + alquileresConfirmados;
    }
}
