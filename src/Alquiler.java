/**
 * Registro de un alquiler confirmado: guarda quién alquiló, qué vehículo, por cuántos días y
 * los montos cobrados.
 */
public class Alquiler {

    private int numero;
    private Cliente cliente;
    private Vehiculo vehiculo;
    private int dias;
    private double subtotal;
    private double descuento;
    private double total;
    private boolean activo;

    /**
     * Propósito: crear el alquiler con sus datos. Valida referencias, días positivos y montos no
     * negativos. Calcula el total (subtotal - descuento) y lo deja activo.
     */
    public Alquiler(int numero, Cliente cliente, Vehiculo vehiculo, int dias,
                    double subtotal, double descuento) {
        if (cliente == null || vehiculo == null) {
            throw new IllegalArgumentException("El alquiler requiere un cliente y un vehículo.");
        }
        if (dias <= 0) {
            throw new IllegalArgumentException("Los días de alquiler deben ser un entero positivo.");
        }
        if (subtotal < 0 || descuento < 0) {
            throw new IllegalArgumentException("El subtotal y el descuento no pueden ser negativos.");
        }
        this.numero = numero;
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.dias = dias;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.total = subtotal - descuento;
        this.activo = true;
    }

    /** Propósito: retornar el número correlativo del alquiler. */
    public int getNumero() {
        return numero;
    }

    /** Propósito: retornar el cliente que alquiló. */
    public Cliente getCliente() {
        return cliente;
    }

    /** Propósito: retornar el vehículo alquilado. */
    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    /** Propósito: retornar los días del alquiler. */
    public int getDias() {
        return dias;
    }

    /** Propósito: retornar el subtotal cobrado (tarifa por días más recargo). */
    public double getSubtotal() {
        return subtotal;
    }

    /** Propósito: retornar el descuento otorgado. */
    public double getDescuento() {
        return descuento;
    }

    /** Propósito: retornar el total cobrado (subtotal menos descuento). */
    public double getTotal() {
        return total;
    }

    /** Propósito: indicar si el alquiler sigue activo. */
    public boolean isActivo() {
        return activo;
    }

    /**
     * Propósito: marcar el alquiler como finalizado (por devolución o cancelación). Valida que
     * esté activo. No modifica montos ni ingresos.
     */
    public void finalizar() {
        if (!activo) {
            throw new IllegalStateException("El alquiler " + numero + " ya está finalizado.");
        }
        activo = false;
    }
}
