import java.util.Scanner;

/**
 * Única vista del programa. Muestra todos los menús, lee las entradas y muestra mensajes.
 * No conoce modelos ni controladores: solo recibe y devuelve datos en crudo.
 */
public class Vista {

    private Scanner entrada;

    /** Propósito: crear la vista con un Scanner sobre la entrada estándar. */
    public Vista() {
        entrada = new Scanner(System.in);
    }

    /** Propósito: mostrar el menú principal y retornar la opción elegida. */
    public int mostrarMenuPrincipal() {
        System.out.println();
        System.out.println("\n===== RENTAMOVIL =====");
        System.out.println("1. Registrar vehículo");
        System.out.println("2. Registrar cliente");
        System.out.println("3. Consultar la flota");
        System.out.println("4. Consultar los clientes");
        System.out.println("5. Cotizar / confirmar alquiler");
        System.out.println("6. Cancelar un alquiler");
        System.out.println("7. Registrar una devolución");
        System.out.println("8. Registrar fin de mantenimiento");
        System.out.println("9. Reportes");
        System.out.println("10. Salir");
        return leerEntero("Seleccione una opción: ");
    }

    /** Propósito: mostrar el submenú de categorías de vehículo y retornar la opción elegida. */
    public int mostrarMenuCategoriasVehiculo() {
        System.out.println();
        System.out.println("\n --- Categoría de vehículo ---");
        System.out.println("1. Automóvil");
        System.out.println("2. Motocicleta");
        System.out.println("3. Camioneta de carga");
        System.out.println("4. Microbús");
        return leerEntero("Seleccione la categoría: ");
    }

    /** Propósito: mostrar el submenú de tipos de cliente y retornar la opción elegida. */
    public int mostrarMenuTiposCliente() {
        System.out.println();
        System.out.println("\n--- Tipo de cliente ---");
        System.out.println("1. Individual");
        System.out.println("2. Corporativo");
        return leerEntero("Seleccione el tipo de cliente: ");
    }

    /** Propósito: mostrar el submenú de reportes y retornar la opción elegida. */
    public int mostrarMenuReportes() {
        System.out.println();
        System.out.println("\n--- Reportes ---");
        System.out.println("1. Flota por categoría y estado");
        System.out.println("2. Ingresos (total y por categoría)");
        System.out.println("3. Descuentos otorgados");
        System.out.println("4. Alquileres activos");
        System.out.println("5. Historial de un cliente");
        System.out.println("6. Volver al menú principal");
        return leerEntero("Seleccione un reporte: ");
    }

    /** Propósito: mostrar un mensaje y leer una línea de texto no vacía (repite si queda vacía). */
    public String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = entrada.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            mostrarError("Este campo no puede estar vacío.");
        }
    }

    /** Propósito: mostrar un mensaje y leer un entero; repite hasta recibir un entero válido. */
    public int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = entrada.nextLine().trim();
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                mostrarError("Debe ingresar un número entero válido.");
            }
        }
    }

    /** Propósito: mostrar un mensaje y leer un decimal; repite hasta recibir un número válido. */
    public double leerDecimal(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = entrada.nextLine().trim();
            try {
                double valor = Double.parseDouble(texto);
                if (Double.isNaN(valor) || Double.isInfinite(valor)) {
                    mostrarError("Debe ingresar un número decimal válido.");
                } else {
                    return valor;
                }
            } catch (NumberFormatException e) {
                mostrarError("Debe ingresar un número decimal válido.");
            }
        }
    }

    /** Propósito: hacer una pregunta de sí/no; repite hasta recibir S o N. Retorna true si es sí. */
    public boolean leerConfirmacion(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (S/N): ");
            String texto = entrada.nextLine().trim().toLowerCase();
            if (texto.equals("s") || texto.equals("si") || texto.equals("sí")) {
                return true;
            }
            if (texto.equals("n") || texto.equals("no")) {
                return false;
            }
            mostrarError("Responda S o N.");
        }
    }

    /** Propósito: mostrar cualquier mensaje genérico que envíe un controlador. */
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    /** Propósito: mostrar un mensaje de error genérico sin terminar el programa. */
    public void mostrarError(String mensaje) {
        System.out.println("[ERROR] " + mensaje);
    }
}
