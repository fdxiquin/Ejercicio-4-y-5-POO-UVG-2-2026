/**
 * Vehículo de categoría Microbús: tiene pasajeros y puede entregarse con piloto de la empresa.
 */
public class Microbus extends Vehiculo {

    private static final TipoLicencia[] LICENCIAS_REQUERIDAS = {TipoLicencia.A, TipoLicencia.B};
    private static final int UMBRAL_MANTENIMIENTO = 25;

    private int cantidadPasajeros;
    private boolean conPiloto;

    /**
     * Propósito: crear un microbús. Llama a super con la categoría "Microbús", valida que los
     * pasajeros sean mayores que cero y asigna los datos propios. El piloto es una característica
     * fija del microbús, no una elección del cliente.
     */
    public Microbus(String placa, String marca, String modelo, double tarifa,
                    int cantidadPasajeros, boolean conPiloto) {
        super(placa, marca, modelo, tarifa, "Microbús");
        if (cantidadPasajeros <= 0) {
            throw new IllegalArgumentException("La cantidad de pasajeros debe ser mayor que cero.");
        }
        this.cantidadPasajeros = cantidadPasajeros;
        this.conPiloto = conPiloto;
    }

    /** Propósito: subtotal = tarifa x días; si incluye piloto suma Q250 por día. */
    @Override
    public double calcularSubtotal(int dias) {
        if (dias <= 0) {
            throw new IllegalArgumentException("Los días de alquiler deben ser un entero positivo.");
        }
        double subtotal = tarifa * dias;
        if (conPiloto) {
            subtotal += 250 * dias;
        }
        return subtotal;
    }

    /**
     * Propósito: con piloto retorna un arreglo vacío (no se exige licencia porque maneja el
     * piloto de la empresa); sin piloto retorna una copia de {A, B} (B o superior).
     */
    @Override
    public TipoLicencia[] getLicenciasRequeridas() {
        if (conPiloto) {
            return new TipoLicencia[0];
        }
        return LICENCIAS_REQUERIDAS.clone();
    }

    /** Propósito: retornar el umbral de mantenimiento del microbús (25 días). */
    @Override
    public int getUmbralMantenimiento() {
        return UMBRAL_MANTENIMIENTO;
    }

    /** Propósito: describir pasajeros y si se entrega con o sin piloto. */
    @Override
    public String describirCaracteristicas() {
        return "Pasajeros: " + cantidadPasajeros + " | "
                + (conPiloto ? "Con piloto de la empresa" : "Sin piloto");
    }
}
