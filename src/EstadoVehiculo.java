/**
 * Enumeración con los tres estados posibles de un vehículo.
 */
public enum EstadoVehiculo {
    /** El vehículo puede alquilarse. Es el estado inicial de todo vehículo nuevo. */
    DISPONIBLE,
    /** Un cliente tiene el vehículo en este momento. */
    ALQUILADO,
    /** El vehículo está en el taller y no puede alquilarse. */
    MANTENIMIENTO
}
