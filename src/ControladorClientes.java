import java.util.Arrays;

/**
 * Controlador encargado de registrar y consultar clientes.
 */
public class ControladorClientes {

    private RentaAutomoviles renta;
    private Vista vista;

    /** Propósito: guardar el modelo y la vista compartidos que entrega ControladorRenta. */
    public ControladorClientes(RentaAutomoviles renta, Vista vista) {
        if (renta == null || vista == null) {
            throw new IllegalArgumentException("El modelo y la vista no pueden ser nulos.");
        }
        this.renta = renta;
        this.vista = vista;
    }

    /**
     * Propósito: registrar un cliente del tipo elegido. Pide los datos, construye el objeto y lo
     * entrega al modelo (que rechaza identificadores repetidos). Es la única decisión por tipo
     * permitida: se decide por la opción del menú, no por un objeto existente.
     */
    public void registrarCliente() {
        int opcion = vista.mostrarMenuTiposCliente();
        Cliente cliente;
        switch (opcion) {
            case 1:
                String dpi = vista.leerTexto("DPI (13 dígitos): ");
                String nombre = vista.leerTexto("Nombre: ");
                TipoLicencia[] licenciasIndividual = leerLicencias();
                cliente = new ClienteIndividual(dpi, nombre, licenciasIndividual);
                break;
            case 2:
                String nit = vista.leerTexto("NIT: ");
                String empresa = vista.leerTexto("Nombre de la empresa: ");
                String contacto = vista.leerTexto("Nombre del contacto: ");
                vista.mostrarMensaje("Ingrese las licencias de los pilotos autorizados.");
                TipoLicencia[] licenciasCorporativo = leerLicencias();
                cliente = new ClienteCorporativo(nit, empresa, contacto, licenciasCorporativo);
                break;
            default:
                vista.mostrarError("Tipo de cliente inválido.");
                return;
        }
        renta.registrarCliente(cliente);
        vista.mostrarMensaje("Cliente registrado: " + cliente);
    }

    /**
     * Propósito: pedir las licencias del cliente una por una. Valida la letra (A, B, C o M),
     * evita repetidas y pregunta si desea ingresar otra. Retorna un arreglo de tamaño exacto con
     * al menos una licencia.
     */
    private TipoLicencia[] leerLicencias() {
        TipoLicencia[] temporal = new TipoLicencia[TipoLicencia.values().length];
        int cantidad = 0;
        boolean otra = true;
        while (otra) {
            String letra = vista.leerTexto("Tipo de licencia (A, B, C o M): ").toUpperCase();
            try {
                TipoLicencia tipo = TipoLicencia.valueOf(letra);
                boolean repetida = false;
                for (int i = 0; i < cantidad; i++) {
                    if (temporal[i] == tipo) {
                        repetida = true;
                    }
                }
                if (repetida) {
                    vista.mostrarError("Esa licencia ya fue ingresada.");
                } else {
                    temporal[cantidad] = tipo;
                    cantidad++;
                }
            } catch (IllegalArgumentException e) {
                vista.mostrarError("Licencia inválida. Use A, B, C o M.");
            }
            if (cantidad == temporal.length) {
                otra = false;
            } else if (cantidad > 0) {
                otra = vista.leerConfirmacion("¿Desea ingresar otra licencia?");
            }
        }
        return Arrays.copyOf(temporal, cantidad);
    }

    /**
     * Propósito: mostrar todos los clientes. Recorre la colección declarada con el tipo común
     * Cliente y cada tipo resuelve su propia descripción mediante toString.
     */
    public void consultarClientes() {
        if (renta.getClientes().isEmpty()) {
            vista.mostrarMensaje("No hay clientes registrados.");
            return;
        }
        vista.mostrarMensaje("--- Clientes ---");
        for (Cliente cliente : renta.getClientes()) {
            vista.mostrarMensaje(cliente.toString());
        }
    }

    /**
     * Propósito: cargar los clientes de demostración: 2 individuales (uno con 3 alquileres
     * confirmados previos, para ver el descuento del cuarto alquiler) y 2 corporativos, con
     * licencias distintas. No registra ingresos.
     */
    public void cargarClientesIniciales() {
        renta.registrarCliente(new ClienteIndividual("1234567890123", "Ana López",
                new TipoLicencia[]{TipoLicencia.C}));
        renta.registrarCliente(new ClienteIndividual("9876543210987", "Luis Pérez",
                new TipoLicencia[]{TipoLicencia.A, TipoLicencia.M}, 3));
        renta.registrarCliente(new ClienteCorporativo("1234567-8", "Transportes Maya S.A.", "Carlos Méndez",
                new TipoLicencia[]{TipoLicencia.B}));
        renta.registrarCliente(new ClienteCorporativo("7654321-0", "Turismo Quetzal S.A.", "María Ruiz",
                new TipoLicencia[]{TipoLicencia.M, TipoLicencia.C}));
    }
}
