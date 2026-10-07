/**
 * Cliente corporativo: empresa identificada por su NIT, hasta tres alquileres activos al mismo
 * tiempo y 10 % de descuento siempre.
 */
public class ClienteCorporativo extends Cliente {

    private static final int LIMITE_ALQUILERES_ACTIVOS = 3;

    private String nombreContacto;

    /**
     * Propósito: crear un cliente corporativo. El NIT queda como identificador y el nombre de la
     * empresa como nombre (ambos heredados). Valida que el contacto no esté vacío. Las licencias
     * representan a los pilotos autorizados de la empresa.
     */
    public ClienteCorporativo(String nit, String nombreEmpresa, String nombreContacto,
                              TipoLicencia[] licencias) {
        super(nit, nombreEmpresa, licencias);
        if (nombreContacto == null || nombreContacto.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del contacto no puede estar vacío.");
        }
        this.nombreContacto = nombreContacto.trim();
    }

    /** Propósito: retornar siempre el 10 % del subtotal. */
    @Override
    public double calcularDescuento(double subtotal) {
        return subtotal * 0.10;
    }

    /** Propósito: retornar el límite de alquileres activos del corporativo (3). */
    @Override
    public int getLimiteAlquileresActivos() {
        return LIMITE_ALQUILERES_ACTIVOS;
    }

    /** Propósito: agregar al texto base el tipo, el NIT y el nombre del contacto. */
    @Override
    public String toString() {
        return "[Corporativo] " + super.toString() + " | NIT: " + identificador
                + " | Contacto: " + nombreContacto;
    }
}
