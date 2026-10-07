/**
 * Vehículo de categoría Automóvil: tiene pasajeros y tipo de transmisión.
 */
public class Automovil extends Vehiculo {

    private static final TipoLicencia[] LICENCIAS_REQUERIDAS =
            {TipoLicencia.A, TipoLicencia.B, TipoLicencia.C};
    private static final int UMBRAL_MANTENIMIENTO = 30;

    private int cantidadPasajeros;
    private boolean transmisionAutomatica;

    /**
     * Propósito: crear un automóvil. Llama a super con la categoría "Automóvil", valida que los
     * pasajeros sean mayores que cero y asigna los datos propios.
     */
    public Automovil(String placa, String marca, String modelo, double tarifa,
                     int cantidadPasajeros, boolean transmisionAutomatica) {
        super(placa, marca, modelo, tarifa, "Automóvil");
        if (cantidadPasajeros <= 0) {
            throw new IllegalArgumentException("La cantidad de pasajeros debe ser mayor que cero.");
        }
        this.cantidadPasajeros = cantidadPasajeros;
        this.transmisionAutomatica = transmisionAutomatica;
    }

    /**
     * Propósito: subtotal = tarifa x días; si la transmisión es automática suma Q50 por día.
     */
    @Override
    public double calcularSubtotal(int dias) {
        if (dias <= 0) {
            throw new IllegalArgumentException("Los días de alquiler deben ser un entero positivo.");
        }
        double subtotal = tarifa * dias;
        if (transmisionAutomatica) {
            subtotal += 50 * dias;
        }
        return subtotal;
    }

    /** Propósito: retornar una copia de las licencias aceptadas {A, B, C} (C o superior). */
    @Override
    public TipoLicencia[] getLicenciasRequeridas() {
        return LICENCIAS_REQUERIDAS.clone();
    }

    /** Propósito: retornar el umbral de mantenimiento del automóvil (30 días). */
    @Override
    public int getUmbralMantenimiento() {
        return UMBRAL_MANTENIMIENTO;
    }

    /** Propósito: describir pasajeros y tipo de transmisión. */
    @Override
    public String describirCaracteristicas() {
        return "Pasajeros: " + cantidadPasajeros + " | Transmisión: "
                + (transmisionAutomatica ? "automática" : "manual");
    }
}
