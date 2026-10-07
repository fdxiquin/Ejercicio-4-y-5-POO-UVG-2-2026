/**
 * Clase abstracta base de todos los vehículos de la flota.
 * Contiene los datos comunes y declara los comportamientos que varían por categoría.
 */
public abstract class Vehiculo {

    protected String placa;
    protected String marca;
    protected String modelo;
    protected double tarifa;
    protected String categoria;
    protected EstadoVehiculo estado;
    protected int diasAcumulados;

    /**
     * Propósito: inicializar los datos comunes del vehículo. Valida que los textos no estén
     * vacíos y que la tarifa sea mayor que cero. El estado inicia siempre en DISPONIBLE y los
     * días acumulados en 0. Solo lo invocan las subclases con super(...).
     */
    protected Vehiculo(String placa, String marca, String modelo, double tarifa, String categoria) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa no puede estar vacía.");
        }
        if (marca == null || marca.trim().isEmpty()) {
            throw new IllegalArgumentException("La marca no puede estar vacía.");
        }
        if (modelo == null || modelo.trim().isEmpty()) {
            throw new IllegalArgumentException("El modelo no puede estar vacío.");
        }
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría no puede estar vacía.");
        }
        if (!(tarifa > 0)) {
            throw new IllegalArgumentException("La tarifa debe ser mayor que cero.");
        }
        this.placa = placa.trim();
        this.marca = marca.trim();
        this.modelo = modelo.trim();
        this.tarifa = tarifa;
        this.categoria = categoria.trim();
        this.estado = EstadoVehiculo.DISPONIBLE;
        this.diasAcumulados = 0;
    }

    /** Propósito: retornar la placa, que identifica al vehículo y se usa para buscarlo. */
    public String getPlaca() {
        return placa;
    }

    /** Propósito: retornar el nombre de la categoría, usado para agrupar en los reportes. */
    public String getCategoria() {
        return categoria;
    }

    /** Propósito: retornar el estado actual del vehículo. */
    public EstadoVehiculo getEstado() {
        return estado;
    }

    /**
     * Propósito: sumar días al acumulado desde el último mantenimiento. Valida que sean mayores
     * que cero. Lo usa registrarDevolucion y también la carga de datos iniciales.
     */
    public void acumularDias(int dias) {
        if (dias <= 0) {
            throw new IllegalArgumentException("Los días a acumular deben ser mayores que cero.");
        }
        diasAcumulados += dias;
    }

    /**
     * Propósito: pasar el vehículo a ALQUILADO. Solo es válido si está DISPONIBLE; si no,
     * lanza IllegalStateException sin cambiar nada. No acumula días (eso ocurre al devolver).
     */
    public void alquilar() {
        if (estado != EstadoVehiculo.DISPONIBLE) {
            throw new IllegalStateException("El vehículo " + placa + " no está disponible (estado: " + estado + ").");
        }
        estado = EstadoVehiculo.ALQUILADO;
    }

    /**
     * Propósito: regresar el vehículo a DISPONIBLE cuando se cancela un alquiler. Solo es válido
     * si está ALQUILADO. No acumula días ni lo envía a mantenimiento.
     */
    public void liberar() {
        if (estado != EstadoVehiculo.ALQUILADO) {
            throw new IllegalStateException("El vehículo " + placa + " no está alquilado.");
        }
        estado = EstadoVehiculo.DISPONIBLE;
    }

    /**
     * Propósito: registrar la devolución. Solo es válido si está ALQUILADO. Acumula los días del
     * alquiler y compara el acumulado con el umbral de su categoría (método sobrescrito): si lo
     * alcanza o supera pasa a MANTENIMIENTO; si no, a DISPONIBLE. Retorna el nuevo estado.
     */
    public EstadoVehiculo registrarDevolucion(int diasAlquiler) {
        if (estado != EstadoVehiculo.ALQUILADO) {
            throw new IllegalStateException("El vehículo " + placa + " no está alquilado.");
        }
        acumularDias(diasAlquiler);
        if (diasAcumulados >= getUmbralMantenimiento()) {
            estado = EstadoVehiculo.MANTENIMIENTO;
        } else {
            estado = EstadoVehiculo.DISPONIBLE;
        }
        return estado;
    }

    /**
     * Propósito: registrar el fin del mantenimiento. Solo es válido si está en MANTENIMIENTO.
     * Vuelve a DISPONIBLE y reinicia el acumulado de días en 0.
     */
    public void finalizarMantenimiento() {
        if (estado != EstadoVehiculo.MANTENIMIENTO) {
            throw new IllegalStateException("El vehículo " + placa + " no está en mantenimiento.");
        }
        estado = EstadoVehiculo.DISPONIBLE;
        diasAcumulados = 0;
    }

    /**
     * Propósito (abstracto): calcular el subtotal = tarifa x días más el recargo propio de la
     * categoría. Cada subclase lo sobrescribe.
     */
    public abstract double calcularSubtotal(int dias);

    /**
     * Propósito (abstracto): retornar las licencias que se aceptan para alquilar el vehículo.
     * El cliente cumple si tiene al menos una. Un arreglo vacío significa que no se exige licencia.
     */
    public abstract TipoLicencia[] getLicenciasRequeridas();

    /**
     * Propósito (abstracto): retornar los días acumulados a partir de los cuales el vehículo
     * debe pasar a mantenimiento al devolverlo.
     */
    public abstract int getUmbralMantenimiento();

    /**
     * Propósito (abstracto): retornar el texto con las características propias de la categoría.
     */
    public abstract String describirCaracteristicas();

    /**
     * Propósito: armar el texto completo del vehículo (datos comunes + características propias)
     * para que los controladores lo envíen a la vista.
     */
    @Override
    public String toString() {
        return String.format("[%s] Placa: %s | %s %s | Tarifa: Q%.2f/día | Estado: %s | %s",
                categoria, placa, marca, modelo, tarifa, estado, describirCaracteristicas());
    }
}
