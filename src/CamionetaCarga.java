/**
 * Vehículo de categoría Camioneta de carga: tiene capacidad máxima en toneladas.
 */
public class CamionetaCarga extends Vehiculo {

    private static final TipoLicencia[] LICENCIAS_REQUERIDAS = {TipoLicencia.A, TipoLicencia.B};
    private static final int UMBRAL_MANTENIMIENTO = 15;

    private double capacidadMaximaToneladas;

    /**
     * Propósito: crear una camioneta de carga. Llama a super con la categoría "Camioneta de
     * carga", valida que la capacidad sea mayor que cero y la asigna.
     */
    public CamionetaCarga(String placa, String marca, String modelo, double tarifa,
                          double capacidadMaximaToneladas) {
        super(placa, marca, modelo, tarifa, "Camioneta de carga");
        if (!(capacidadMaximaToneladas > 0)) {
            throw new IllegalArgumentException("La capacidad de carga debe ser mayor que cero.");
        }
        this.capacidadMaximaToneladas = capacidadMaximaToneladas;
    }

    /**
     * Propósito: subtotal = tarifa x días + Q100 x toneladas x días, siempre, aunque el cliente
     * transporte menos carga.
     */
    @Override
    public double calcularSubtotal(int dias) {
        if (dias <= 0) {
            throw new IllegalArgumentException("Los días de alquiler deben ser un entero positivo.");
        }
        return tarifa * dias + 100 * capacidadMaximaToneladas * dias;
    }

    /** Propósito: retornar una copia de las licencias aceptadas {A, B} (B o superior). */
    @Override
    public TipoLicencia[] getLicenciasRequeridas() {
        return LICENCIAS_REQUERIDAS.clone();
    }

    /** Propósito: retornar el umbral de mantenimiento de la camioneta (15 días). */
    @Override
    public int getUmbralMantenimiento() {
        return UMBRAL_MANTENIMIENTO;
    }

    /** Propósito: describir la capacidad máxima en toneladas. */
    @Override
    public String describirCaracteristicas() {
        return "Capacidad máxima: " + capacidadMaximaToneladas + " toneladas";
    }
}
