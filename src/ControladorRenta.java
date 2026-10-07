import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controlador principal: inicia el programa, coordina a los otros controladores y resuelve
 * cotizaciones, alquileres, cancelaciones, devoluciones y reportes.
 */
public class ControladorRenta {

    private RentaAutomoviles renta;
    private Vista vista;
    private ControladorVehiculos controladorVehiculos;
    private ControladorClientes controladorClientes;

    /**
     * Propósito: crear el modelo, la vista y los controladores secundarios (que comparten las
     * mismas instancias) y cargar los datos iniciales de demostración.
     */
    public ControladorRenta() {
        renta = new RentaAutomoviles();
        vista = new Vista();
        controladorVehiculos = new ControladorVehiculos(renta, vista);
        controladorClientes = new ControladorClientes(renta, vista);
        controladorVehiculos.cargarVehiculosIniciales();
        controladorClientes.cargarClientesIniciales();
    }

    /**
     * Propósito: iniciar el programa. Muestra el menú principal dentro de un ciclo que no termina
     * hasta elegir Salir y ejecuta la opción elegida. Los errores de validación se muestran como
     * mensaje y el programa regresa al menú sin terminar.
     */
    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            try {
                int opcion = vista.mostrarMenuPrincipal();
                switch (opcion) {
                    case 1:
                        controladorVehiculos.registrarVehiculo();
                        break;
                    case 2:
                        controladorClientes.registrarCliente();
                        break;
                    case 3:
                        controladorVehiculos.consultarFlota();
                        break;
                    case 4:
                        controladorClientes.consultarClientes();
                        break;
                    case 5:
                        cotizarYConfirmar();
                        break;
                    case 6:
                        cancelarAlquiler();
                        break;
                    case 7:
                        registrarDevolucion();
                        break;
                    case 8:
                        controladorVehiculos.registrarFinMantenimiento();
                        break;
                    case 9:
                        mostrarReportes();
                        break;
                    case 10:
                        vista.mostrarMensaje("Hasta pronto.");
                        salir = true;
                        break;
                    default:
                        vista.mostrarError("Opción inválida.");
                        break;
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                vista.mostrarError(e.getMessage());
            }
        }
    }

    /**
     * Propósito: cotizar un alquiler y, si el cliente puede alquilar y acepta, confirmarlo. Cotizar
     * no modifica estados, ingresos ni conteos. Subtotal y descuento se calculan con métodos
     * sobrescritos, así que cada objeto aplica la regla de su tipo real.
     */
    private void cotizarYConfirmar() {
        String placa = vista.leerTexto("Placa del vehículo: ");
        String idCliente = vista.leerTexto("Identificador del cliente (DPI o NIT): ");
        int dias = vista.leerEntero("Cantidad de días: ");

        Vehiculo vehiculo = renta.buscarVehiculo(placa);
        if (vehiculo == null) {
            throw new IllegalArgumentException("No existe un vehículo con la placa " + placa + ".");
        }
        Cliente cliente = renta.buscarCliente(idCliente);
        if (cliente == null) {
            throw new IllegalArgumentException("No existe un cliente con el identificador " + idCliente + ".");
        }
        if (dias <= 0) {
            throw new IllegalArgumentException("Los días de alquiler deben ser un entero positivo.");
        }

        double subtotal = vehiculo.calcularSubtotal(dias);
        double descuento = cliente.calcularDescuento(subtotal);
        double total = subtotal - descuento;
        List<String> razones = calcularRazonesRechazo(vehiculo, cliente);

        vista.mostrarMensaje("--- Cotización ---");
        vista.mostrarMensaje(vehiculo.toString());
        vista.mostrarMensaje("Cliente: " + cliente.getNombre() + " | Días: " + dias);
        vista.mostrarMensaje(String.format("Subtotal: Q%.2f | Descuento: Q%.2f | Total: Q%.2f", subtotal, descuento, total));

        if (!razones.isEmpty()) {
            vista.mostrarMensaje("El cliente NO puede alquilarlo en este momento por:");
            for (String razon : razones) {
                vista.mostrarMensaje(" - " + razon);
            }
            return;
        }
        vista.mostrarMensaje("El cliente puede alquilar este vehículo.");
        if (vista.leerConfirmacion("¿Desea confirmar el alquiler?")) {
            Alquiler alquiler = confirmarAlquiler(vehiculo, cliente, dias, subtotal, descuento);
            vista.mostrarMensaje(String.format("Alquiler #%d confirmado. Total cobrado: Q%.2f",
                    alquiler.getNumero(), alquiler.getTotal()));
        } else {
            vista.mostrarMensaje("Alquiler cancelado. No se realizó ningún cambio.");
        }
    }

    /**
     * Propósito: revisar todas las condiciones que impiden el alquiler y retornar una razón por
     * cada una incumplida (vehículo no disponible, licencia inadecuada, límite alcanzado). Lista
     * vacía significa que sí puede alquilar. Solo consulta.
     */
    private List<String> calcularRazonesRechazo(Vehiculo vehiculo, Cliente cliente) {
        List<String> razones = new ArrayList<>();
        if (vehiculo.getEstado() != EstadoVehiculo.DISPONIBLE) {
            razones.add("Vehículo no disponible (estado actual: " + vehiculo.getEstado() + ")");
        }
        if (!cliente.tieneLicenciaAdecuada(vehiculo.getLicenciasRequeridas())) {
            razones.add("Licencia inadecuada para este vehículo");
        }
        if (!cliente.puedeAlquilarMas()) {
            razones.add("Límite de alquileres activos alcanzado (" + cliente.getAlquileresActivos()
                    + " de " + cliente.getLimiteAlquileresActivos() + ")");
        }
        return razones;
    }

    /**
     * Propósito: confirmar el alquiler ya aceptado. Pasa el vehículo a ALQUILADO, suma el
     * alquiler activo al cliente, crea y guarda el Alquiler con su número correlativo y registra
     * el ingreso y el descuento. Retorna el alquiler creado.
     */
    private Alquiler confirmarAlquiler(Vehiculo vehiculo, Cliente cliente, int dias,
                                       double subtotal, double descuento) {
        vehiculo.alquilar();
        cliente.registrarAlquilerActivo();
        Alquiler alquiler = new Alquiler(renta.getSiguienteNumeroAlquiler(), cliente, vehiculo,
                dias, subtotal, descuento);
        renta.agregarAlquiler(alquiler);
        renta.registrarIngreso(vehiculo.getCategoria(), alquiler.getTotal(), descuento);
        return alquiler;
    }

    /**
     * Propósito: cancelar un alquiler activo por placa. Rechaza placas inexistentes y vehículos
     * sin alquiler activo. Finaliza el alquiler, libera el vehículo y el alquiler activo del
     * cliente. No modifica ingresos, descuentos ni el conteo de alquileres confirmados.
     */
    private void cancelarAlquiler() {
        String placa = vista.leerTexto("Placa del vehículo: ");
        Vehiculo vehiculo = renta.buscarVehiculo(placa);
        if (vehiculo == null) {
            throw new IllegalArgumentException("No existe un vehículo con la placa " + placa + ".");
        }
        Alquiler alquiler = renta.buscarAlquilerActivo(placa);
        if (alquiler == null) {
            throw new IllegalStateException("El vehículo " + vehiculo.getPlaca() + " no tiene un alquiler activo.");
        }
        vehiculo.liberar();
        alquiler.finalizar();
        alquiler.getCliente().liberarAlquilerActivo();
        vista.mostrarMensaje("Alquiler #" + alquiler.getNumero() + " cancelado. El vehículo "
                + vehiculo.getPlaca() + " vuelve a estar DISPONIBLE. Los ingresos no se modifican.");
    }

    /**
     * Propósito: registrar la devolución de un vehículo por placa. Rechaza placas inexistentes y
     * vehículos sin alquiler activo. Finaliza el alquiler, deja que el vehículo decida si pasa a
     * MANTENIMIENTO o DISPONIBLE según el umbral de su categoría y libera el alquiler activo del
     * cliente. No genera otro cobro ni modifica los ingresos.
     */
    private void registrarDevolucion() {
        String placa = vista.leerTexto("Placa del vehículo: ");
        Vehiculo vehiculo = renta.buscarVehiculo(placa);
        if (vehiculo == null) {
            throw new IllegalArgumentException("No existe un vehículo con la placa " + placa + ".");
        }
        Alquiler alquiler = renta.buscarAlquilerActivo(placa);
        if (alquiler == null) {
            throw new IllegalStateException("El vehículo " + vehiculo.getPlaca() + " no está alquilado.");
        }
        EstadoVehiculo nuevoEstado = vehiculo.registrarDevolucion(alquiler.getDias());
        alquiler.finalizar();
        alquiler.getCliente().liberarAlquilerActivo();
        vista.mostrarMensaje("Devolución registrada. Alquiler #" + alquiler.getNumero()
                + " finalizado. El vehículo " + vehiculo.getPlaca() + " quedó en estado " + nuevoEstado + ".");
    }

    /**
     * Propósito: mostrar el submenú de reportes en un ciclo y ejecutar el reporte elegido hasta
     * que se elija volver. Solo consulta, no modifica nada.
     */
    private void mostrarReportes() {
        boolean volver = false;
        while (!volver) {
            int opcion = vista.mostrarMenuReportes();
            switch (opcion) {
                case 1:
                    reporteFlota();
                    break;
                case 2:
                    reporteIngresos();
                    break;
                case 3:
                    reporteDescuentos();
                    break;
                case 4:
                    reporteAlquileresActivos();
                    break;
                case 5:
                    reporteHistorialCliente();
                    break;
                case 6:
                    volver = true;
                    break;
                default:
                    vista.mostrarError("Opción inválida.");
                    break;
            }
        }
    }

    /**
     * Propósito: reportar, por categoría, cuántos vehículos hay registrados y cuántos están
     * disponibles, alquilados o en mantenimiento.
     */
    private void reporteFlota() {
        vista.mostrarMensaje("--- Flota por categoría y estado ---");
        for (String categoria : renta.obtenerCategorias()) {
            vista.mostrarMensaje(categoria + ": registrados " + renta.contarPorCategoria(categoria)
                    + " | disponibles " + renta.contarPorCategoriaYEstado(categoria, EstadoVehiculo.DISPONIBLE)
                    + " | alquilados " + renta.contarPorCategoriaYEstado(categoria, EstadoVehiculo.ALQUILADO)
                    + " | en mantenimiento " + renta.contarPorCategoriaYEstado(categoria, EstadoVehiculo.MANTENIMIENTO));
        }
    }

    /** Propósito: reportar el dinero acumulado por alquileres confirmados, en total y por categoría. */
    private void reporteIngresos() {
        vista.mostrarMensaje("--- Ingresos ---");
        vista.mostrarMensaje(String.format("Total acumulado: Q%.2f", renta.getIngresoTotal()));
        for (Map.Entry<String, Double> entrada : renta.getIngresoPorCategoria().entrySet()) {
            vista.mostrarMensaje(String.format("  %s: Q%.2f", entrada.getKey(), entrada.getValue()));
        }
    }

    /** Propósito: reportar el monto total de descuentos otorgados. */
    private void reporteDescuentos() {
        vista.mostrarMensaje(String.format("Total de descuentos otorgados: Q%.2f", renta.getDescuentoTotal()));
    }

    /** Propósito: reportar los alquileres que siguen activos. */
    private void reporteAlquileresActivos() {
        List<Alquiler> activos = renta.obtenerAlquileresActivos();
        if (activos.isEmpty()) {
            vista.mostrarMensaje("No hay alquileres activos.");
            return;
        }
        vista.mostrarMensaje("--- Alquileres activos ---");
        for (Alquiler alquiler : activos) {
            vista.mostrarMensaje(describirAlquiler(alquiler));
        }
    }

    /**
     * Propósito: reportar el historial de alquileres de un cliente y el total que ha pagado.
     * Rechaza identificadores inexistentes.
     */
    private void reporteHistorialCliente() {
        String id = vista.leerTexto("Identificador del cliente (DPI o NIT): ");
        Cliente cliente = renta.buscarCliente(id);
        if (cliente == null) {
            throw new IllegalArgumentException("No existe un cliente con el identificador " + id + ".");
        }
        List<Alquiler> historial = renta.obtenerHistorialCliente(id);
        vista.mostrarMensaje("--- Historial de " + cliente.getNombre() + " ---");
        if (historial.isEmpty()) {
            vista.mostrarMensaje("Sin alquileres confirmados.");
        }
        for (Alquiler alquiler : historial) {
            vista.mostrarMensaje(describirAlquiler(alquiler));
        }
        vista.mostrarMensaje(String.format("Total pagado: Q%.2f", renta.calcularTotalPagado(id)));
    }

    /**
     * Propósito: convertir un alquiler en texto para la vista (la vista no conoce modelos):
     * número, cliente, vehículo, días, montos con dos decimales y estado.
     */
    private String describirAlquiler(Alquiler alquiler) {
        return String.format("Alquiler #%d | Cliente: %s | Vehículo: %s | Días: %d | Subtotal: Q%.2f | Descuento: Q%.2f | Total: Q%.2f | %s",
                alquiler.getNumero(), alquiler.getCliente().getNombre(), alquiler.getVehiculo().getPlaca(),
                alquiler.getDias(), alquiler.getSubtotal(), alquiler.getDescuento(), alquiler.getTotal(),
                alquiler.isActivo() ? "ACTIVO" : "FINALIZADO");
    }
}
