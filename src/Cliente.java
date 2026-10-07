import java.util.Arrays;

/**
 * Clase abstracta base de los clientes. Contiene los datos comunes y declara los
 * comportamientos que varían por tipo de cliente (descuento y límite de alquileres activos).
 */
public abstract class Cliente {

    protected String identificador;
    protected String nombre;
    protected TipoLicencia[] licencias;
    protected int alquileresActivos;

    /**
     * Propósito: inicializar los datos comunes. Valida que identificador y nombre no estén
     * vacíos y que haya al menos una licencia válida. Guarda una copia del arreglo de licencias
     * y deja los alquileres activos en 0. Solo lo invocan las subclases con super(...).
     */
    protected Cliente(String identificador, String nombre, TipoLicencia[] licencias) {
        if (identificador == null || identificador.trim().isEmpty()) {
            throw new IllegalArgumentException("El identificador no puede estar vacío.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (licencias == null || licencias.length == 0) {
            throw new IllegalArgumentException("El cliente debe presentar al menos un tipo de licencia válido.");
        }
        for (TipoLicencia licencia : licencias) {
            if (licencia == null) {
                throw new IllegalArgumentException("Hay una licencia inválida en la lista del cliente.");
            }
        }
        this.identificador = identificador.trim();
        this.nombre = nombre.trim();
        this.licencias = licencias.clone();
        this.alquileresActivos = 0;
    }

    /** Propósito: retornar el identificador (DPI o NIT) para buscar al cliente. */
    public String getIdentificador() {
        return identificador;
    }

    /** Propósito: retornar el nombre del cliente para armar mensajes y reportes. */
    public String getNombre() {
        return nombre;
    }

    /** Propósito: retornar la cantidad de alquileres activos del cliente. */
    public int getAlquileresActivos() {
        return alquileresActivos;
    }

    /**
     * Propósito: indicar si el cliente tiene una licencia adecuada. Retorna true si el arreglo
     * recibido está vacío (el vehículo no exige licencia) o si alguna licencia del cliente
     * aparece en él. Solo consulta, no modifica nada.
     */
    public boolean tieneLicenciaAdecuada(TipoLicencia[] licenciasAceptadas) {
        if (licenciasAceptadas.length == 0) {
            return true;
        }
        for (TipoLicencia propia : licencias) {
            for (TipoLicencia aceptada : licenciasAceptadas) {
                if (propia == aceptada) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Propósito: indicar si el cliente aún puede tener más alquileres activos, comparando los
     * activos con el límite de su tipo de cliente (método sobrescrito).
     */
    public boolean puedeAlquilarMas() {
        return alquileresActivos < getLimiteAlquileresActivos();
    }

    /**
     * Propósito: sumar un alquiler activo al confirmar un alquiler. Valida el límite; si ya se
     * alcanzó lanza IllegalStateException sin modificar nada.
     */
    public void registrarAlquilerActivo() {
        if (!puedeAlquilarMas()) {
            throw new IllegalStateException("El cliente " + nombre + " alcanzó su límite de alquileres activos ("
                    + getLimiteAlquileresActivos() + ").");
        }
        alquileresActivos++;
    }

    /**
     * Propósito: restar un alquiler activo al devolver o cancelar. Valida que haya al menos uno.
     * No altera el conteo de alquileres confirmados.
     */
    public void liberarAlquilerActivo() {
        if (alquileresActivos <= 0) {
            throw new IllegalStateException("El cliente " + nombre + " no tiene alquileres activos.");
        }
        alquileresActivos--;
    }

    /**
     * Propósito (abstracto): calcular el monto de descuento (no el porcentaje) sobre el subtotal.
     * Cada tipo de cliente lo sobrescribe.
     */
    public abstract double calcularDescuento(double subtotal);

    /**
     * Propósito (abstracto): retornar la cantidad máxima de alquileres activos simultáneos
     * permitidos. Cada tipo de cliente lo sobrescribe.
     */
    public abstract int getLimiteAlquileresActivos();

    /**
     * Propósito: armar el texto con los datos comunes del cliente. Las subclases lo
     * sobrescriben para agregar sus datos propios.
     */
    @Override
    public String toString() {
        return "ID: " + identificador + " | Nombre: " + nombre
                + " | Licencias: " + Arrays.toString(licencias)
                + " | Alquileres activos: " + alquileresActivos;
    }
}
