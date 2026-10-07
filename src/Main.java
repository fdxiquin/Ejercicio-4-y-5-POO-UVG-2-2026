// Clase que inicia el programa. Contiene el controlador principal y el main
public class Main {

    private static ControladorRenta controlador = new ControladorRenta();

    // Propósito: punto de entrada del programa; solo inicia el controlador principal.
    public static void main(String[] args) {
        controlador.iniciar();
    }
}
