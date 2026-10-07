import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Modelo central de la empresa: guarda la flota, los clientes, los alquileres y los acumulados
 * de ingresos y descuentos. No interactúa con el usuario.
 */
public class RentaAutomoviles {

    private List<Vehiculo> vehiculos;
    private List<Cliente> clientes;
    private List<Alquiler> alquileres;
    private int contadorAlquiler;
    private double ingresoTotal;
    private Map<String, Double> ingresoPorCategoria;
    private double descuentoTotal;

    /**
     * Propósito: crear la empresa vacía: colecciones sin elementos, contador de alquileres en 1
     * y acumulados en 0.
     */
    public RentaAutomoviles() {
        vehiculos = new ArrayList<>();
        clientes = new ArrayList<>();
        alquileres = new ArrayList<>();
        contadorAlquiler = 1;
        ingresoTotal = 0;
        ingresoPorCategoria = new LinkedHashMap<>();
        descuentoTotal = 0;
    }

    /**
     * Propósito: registrar un vehículo de cualquier categoría. Rechaza nulos y placas repetidas
     * sin modificar la flota.
     */
    public void registrarVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehículo no puede ser nulo.");
        }
        if (buscarVehiculo(vehiculo.getPlaca()) != null) {
            throw new IllegalArgumentException("Ya existe un vehículo con la placa " + vehiculo.getPlaca() + ".");
        }
        vehiculos.add(vehiculo);
    }

    /**
     * Propósito: registrar un cliente de cualquier tipo. Rechaza nulos e identificadores
     * repetidos (aunque sean de distinto tipo) sin modificar nada.
     */
    public void registrarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo.");
        }
        if (buscarCliente(cliente.getIdentificador()) != null) {
            throw new IllegalArgumentException("Ya existe un cliente con el identificador " + cliente.getIdentificador() + ".");
        }
        clientes.add(cliente);
    }

    /** Propósito: buscar un vehículo por su placa. Retorna null si no existe. */
    public Vehiculo buscarVehiculo(String placa) {
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getPlaca().equalsIgnoreCase(placa.trim())) {
                return vehiculo;
            }
        }
        return null;
    }

    /** Propósito: buscar un cliente por su identificador. Retorna null si no existe. */
    public Cliente buscarCliente(String identificador) {
        for (Cliente cliente : clientes) {
            if (cliente.getIdentificador().equalsIgnoreCase(identificador.trim())) {
                return cliente;
            }
        }
        return null;
    }

    /** Propósito: retornar la flota en solo lectura para que los controladores la recorran. */
    public List<Vehiculo> getVehiculos() {
        return Collections.unmodifiableList(vehiculos);
    }

    /** Propósito: retornar los clientes en solo lectura para que los controladores los recorran. */
    public List<Cliente> getClientes() {
        return Collections.unmodifiableList(clientes);
    }

    /**
     * Propósito: entregar el siguiente número correlativo de alquiler y avanzar el contador.
     * Solo debe llamarse al confirmar un alquiler.
     */
    public int getSiguienteNumeroAlquiler() {
        return contadorAlquiler++;
    }

    /** Propósito: guardar un alquiler confirmado en el registro de alquileres. */
    public void agregarAlquiler(Alquiler alquiler) {
        if (alquiler == null) {
            throw new IllegalArgumentException("El alquiler no puede ser nulo.");
        }
        alquileres.add(alquiler);
    }

    /** Propósito: buscar el alquiler activo de un vehículo por su placa. Retorna null si no hay. */
    public Alquiler buscarAlquilerActivo(String placa) {
        for (Alquiler alquiler : alquileres) {
            if (alquiler.isActivo() && alquiler.getVehiculo().getPlaca().equalsIgnoreCase(placa.trim())) {
                return alquiler;
            }
        }
        return null;
    }

    /**
     * Propósito: acumular el ingreso y el descuento de un alquiler confirmado, en total y por
     * categoría. Solo se invoca al confirmar; cotizar, cancelar, rechazar o devolver no lo usan.
     */
    public void registrarIngreso(String categoria, double total, double descuento) {
        if (total < 0 || descuento < 0) {
            throw new IllegalArgumentException("El ingreso y el descuento no pueden ser negativos.");
        }
        ingresoTotal += total;
        ingresoPorCategoria.merge(categoria, total, Double::sum);
        descuentoTotal += descuento;
    }

    /** Propósito: retornar el dinero acumulado por alquileres confirmados. */
    public double getIngresoTotal() {
        return ingresoTotal;
    }

    /** Propósito: retornar una copia del dinero acumulado por categoría. */
    public Map<String, Double> getIngresoPorCategoria() {
        return new LinkedHashMap<>(ingresoPorCategoria);
    }

    /** Propósito: retornar el monto total de descuentos otorgados. */
    public double getDescuentoTotal() {
        return descuentoTotal;
    }

    /** Propósito: retornar una lista nueva con los alquileres que siguen activos. */
    public List<Alquiler> obtenerAlquileresActivos() {
        List<Alquiler> activos = new ArrayList<>();
        for (Alquiler alquiler : alquileres) {
            if (alquiler.isActivo()) {
                activos.add(alquiler);
            }
        }
        return activos;
    }

    /** Propósito: retornar una lista nueva con todos los alquileres de un cliente (activos y finalizados). */
    public List<Alquiler> obtenerHistorialCliente(String identificador) {
        List<Alquiler> historial = new ArrayList<>();
        for (Alquiler alquiler : alquileres) {
            if (alquiler.getCliente().getIdentificador().equalsIgnoreCase(identificador.trim())) {
                historial.add(alquiler);
            }
        }
        return historial;
    }

    /**
     * Propósito: sumar el total de todos los alquileres confirmados de un cliente. Incluye los
     * cancelados porque la empresa cobra el monto completo al confirmar.
     */
    public double calcularTotalPagado(String identificador) {
        double suma = 0;
        for (Alquiler alquiler : obtenerHistorialCliente(identificador)) {
            suma += alquiler.getTotal();
        }
        return suma;
    }

    /** Propósito: retornar los nombres de categoría presentes en la flota, sin repetir. */
    public List<String> obtenerCategorias() {
        List<String> categorias = new ArrayList<>();
        for (Vehiculo vehiculo : vehiculos) {
            if (!categorias.contains(vehiculo.getCategoria())) {
                categorias.add(vehiculo.getCategoria());
            }
        }
        return categorias;
    }

    /** Propósito: contar cuántos vehículos hay registrados en una categoría. */
    public int contarPorCategoria(String categoria) {
        int cantidad = 0;
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getCategoria().equals(categoria)) {
                cantidad++;
            }
        }
        return cantidad;
    }

    /** Propósito: contar cuántos vehículos de una categoría están en un estado dado. */
    public int contarPorCategoriaYEstado(String categoria, EstadoVehiculo estado) {
        int cantidad = 0;
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getCategoria().equals(categoria) && vehiculo.getEstado() == estado) {
                cantidad++;
            }
        }
        return cantidad;
    }
}
