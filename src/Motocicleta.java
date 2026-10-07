/**
 * Vehículo de categoría Motocicleta: tiene cilindraje.
 */
public class Motocicleta extends Vehiculo {

    private static final TipoLicencia LICENCIA_REQUERIDA = TipoLicencia.M;
    private static final int UMBRAL_MANTENIMIENTO = 20;

    private double cilindraje;

    /**
     * Propósito: crear una motocicleta. Llama a super con la categoría "Motocicleta", valida
     * que el cilindraje sea mayor que cero y lo asigna.
     */
    public Motocicleta(String placa, String marca, String modelo, double tarifa, double cilindraje) {
        super(placa, marca, modelo, tarifa, "Motocicleta");
        if (!(cilindraje > 0)) {
            throw new IllegalArgumentException("El cilindraje debe ser mayor que cero.");
        }
        this.cilindraje = cilindraje;
    }

    /**
     * Propósito: subtotal = tarifa x días; si el cilindraje supera 250 cc suma Q75 una sola vez
     * por todo el alquiler (no por día).
     */
    @Override
    public double calcularSubtotal(int dias) {
        if (dias <= 0) {
            throw new IllegalArgumentException("Los días de alquiler deben ser un entero positivo.");
        }
        double subtotal = tarifa * dias;
        if (cilindraje > 250) {
            subtotal += 75;
        }
        return subtotal;
    }

    /** Propósito: retornar un arreglo de un elemento con la licencia M (única aceptada). */
    @Override
    public TipoLicencia[] getLicenciasRequeridas() {
        return new TipoLicencia[]{LICENCIA_REQUERIDA};
    }

    /** Propósito: retornar el umbral de mantenimiento de la motocicleta (20 días). */
    @Override
    public int getUmbralMantenimiento() {
        return UMBRAL_MANTENIMIENTO;
    }

    /** Propósito: describir el cilindraje en cc. */
    @Override
    public String describirCaracteristicas() {
        return "Cilindraje: " + cilindraje + " cc";
    }
}
