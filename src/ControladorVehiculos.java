/**
 * Controlador encargado de registrar y consultar vehículos y de finalizar mantenimientos.
 */
public class ControladorVehiculos {

    private RentaAutomoviles renta;
    private Vista vista;

    /** Propósito: guardar el modelo y la vista compartidos que entrega ControladorRenta. */
    public ControladorVehiculos(RentaAutomoviles renta, Vista vista) {
        if (renta == null || vista == null) {
            throw new IllegalArgumentException("El modelo y la vista no pueden ser nulos.");
        }
        this.renta = renta;
        this.vista = vista;
    }

    /**
     * Propósito: registrar un vehículo de la categoría elegida. Pide los datos comunes y propios,
     * construye el objeto y lo entrega al modelo (que rechaza placas repetidas). Es la única
     * decisión por categoría permitida: se decide por la opción del menú, no por un objeto.
     */
    public void registrarVehiculo() {
        int opcion = vista.mostrarMenuCategoriasVehiculo();
        if (opcion < 1 || opcion > 4) {
            vista.mostrarError("Categoría inválida.");
            return;
        }
        String placa = vista.leerTexto("Placa: ");
        String marca = vista.leerTexto("Marca: ");
        String modelo = vista.leerTexto("Modelo: ");
        double tarifa = vista.leerDecimal("Tarifa diaria (Q): ");
        Vehiculo vehiculo;
        switch (opcion) {
            case 1:
                int pasajerosAuto = vista.leerEntero("Cantidad de pasajeros: ");
                boolean automatica = vista.leerConfirmacion("¿Transmisión automática?");
                vehiculo = new Automovil(placa, marca, modelo, tarifa, pasajerosAuto, automatica);
                break;
            case 2:
                double cilindraje = vista.leerDecimal("Cilindraje (cc): ");
                vehiculo = new Motocicleta(placa, marca, modelo, tarifa, cilindraje);
                break;
            case 3:
                double toneladas = vista.leerDecimal("Capacidad máxima (toneladas): ");
                vehiculo = new CamionetaCarga(placa, marca, modelo, tarifa, toneladas);
                break;
            default:
                int pasajerosBus = vista.leerEntero("Cantidad de pasajeros: ");
                boolean conPiloto = vista.leerConfirmacion("¿Se entrega con piloto de la empresa?");
                vehiculo = new Microbus(placa, marca, modelo, tarifa, pasajerosBus, conPiloto);
                break;
        }
        renta.registrarVehiculo(vehiculo);
        vista.mostrarMensaje("Vehículo registrado: " + vehiculo);
    }

    /**
     * Propósito: mostrar toda la flota. Recorre la colección declarada con el tipo común Vehiculo
     * y cada categoría resuelve su propia descripción mediante toString.
     */
    public void consultarFlota() {
        if (renta.getVehiculos().isEmpty()) {
            vista.mostrarMensaje("No hay vehículos registrados.");
            return;
        }
        vista.mostrarMensaje("--- Flota ---");
        for (Vehiculo vehiculo : renta.getVehiculos()) {
            vista.mostrarMensaje(vehiculo.toString());
        }
    }

    /**
     * Propósito: registrar el fin del mantenimiento de un vehículo. Rechaza placas inexistentes y
     * vehículos que no están en mantenimiento, sin modificar nada.
     */
    public void registrarFinMantenimiento() {
        String placa = vista.leerTexto("Placa del vehículo: ");
        Vehiculo vehiculo = renta.buscarVehiculo(placa);
        if (vehiculo == null) {
            throw new IllegalArgumentException("No existe un vehículo con la placa " + placa + ".");
        }
        vehiculo.finalizarMantenimiento();
        vista.mostrarMensaje("Mantenimiento finalizado. El vehículo " + vehiculo.getPlaca()
                + " vuelve a estar DISPONIBLE con acumulado en 0.");
    }

    /**
     * Propósito: cargar los vehículos de demostración: 2 por categoría, todos DISPONIBLES, con
     * datos que permiten ver todas las reglas de cobro, licencia y mantenimiento. Un automóvil
     * queda cercano a su umbral de mantenimiento. No registra ingresos.
     */
    public void cargarVehiculosIniciales() {
        renta.registrarVehiculo(new Automovil("P-001", "Toyota", "Yaris", 200, 5, true));
        renta.registrarVehiculo(new Automovil("P-002", "Honda", "Fit", 180, 4, false));
        renta.registrarVehiculo(new Motocicleta("M-001", "Yamaha", "R3", 120, 320));
        renta.registrarVehiculo(new Motocicleta("M-002", "Honda", "CB250", 90, 250));
        renta.registrarVehiculo(new CamionetaCarga("C-001", "Toyota", "Hilux", 200, 1.5));
        renta.registrarVehiculo(new CamionetaCarga("C-002", "Isuzu", "NPR", 300, 3));
        renta.registrarVehiculo(new Microbus("B-001", "Toyota", "Hiace", 450, 15, true));
        renta.registrarVehiculo(new Microbus("B-002", "Hyundai", "H1", 400, 12, false));
        // Vehículo cercano a su umbral (automóvil: 30 días): con 28 acumulados, un alquiler de 2+ días lo envía a mantenimiento.
        renta.buscarVehiculo("P-001").acumularDias(28);
    }
}
