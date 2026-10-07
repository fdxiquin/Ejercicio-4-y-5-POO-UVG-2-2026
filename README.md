# Ejercicios 4 y 5 - Herencia y Polimorfismo: RentaMovil

**Nombre completo:** Daniel Fernando Xiquin Tezén
**Carné:** 26896

## Descripción

El programa es una aplicación de consola en Java para RentaMovil, una empresa que alquila automóviles, motocicletas, camionetas de carga y microbuses a clientes individuales y corporativos. Permite registrar vehículos y clientes, cotizar, confirmar y cancelar alquileres, registrar devoluciones y fines de mantenimiento, y consultar reportes. Calcula el cobro de cada categoría, aplica el descuento según el tipo de cliente, valida la licencia y el límite de alquileres activos, y envía un vehículo a mantenimiento cuando llega al umbral de días de su categoría.

**Clases (patrón Modelo-Vista-Controlador):**

- **Main:** inicia el programa.
- **Controladores:** `ControladorRenta` (menú principal, cotizaciones, alquileres, devoluciones y reportes), `ControladorVehiculos` y `ControladorClientes`.
- **Vista:** `Vista` muestra los menús y mensajes y lee las entradas. No conoce modelos ni controladores.
- **Modelo de vehículos:** `Vehiculo` (abstracta) con `Automovil`, `Motocicleta`, `CamionetaCarga` y `Microbus`.
- **Modelo de clientes:** `Cliente` (abstracta) con `ClienteIndividual` y `ClienteCorporativo`.
- **Modelo de la empresa:** `RentaAutomoviles` (flota, clientes, alquileres e ingresos) y `Alquiler`.
- **Enumeraciones:** `EstadoVehiculo` y `TipoLicencia`.

**Decisiones de diseño:**

- `Vehiculo` y `Cliente` son abstractas porque no existe un vehículo ni un cliente "genérico".
- Lo que cambia según la categoría o el tipo (cobro, descuento, licencia, umbral de mantenimiento, límite de alquileres y descripción) se resuelve con métodos sobrescritos, sin `instanceof` ni condicionales por tipo.
- La flota y los clientes se guardan en listas del tipo común (`List<Vehiculo>` y `List<Cliente>`).
- Cada vehículo define como constante las licencias que acepta. El cliente guarda las suyas en un arreglo `TipoLicencia[]`.
- Cotizar no modifica nada: los ingresos, estados y conteos solo cambian al confirmar un alquiler.
- Los errores de validación se muestran con un mensaje y el programa regresa al menú sin terminar.
- Para agregar vehículos eléctricos o clientes gubernamentales solo hay que crear una nueva subclase.
- El programa inicia con 2 vehículos de cada categoría y 2 clientes de cada tipo. Un automóvil (`P-001`) está cerca de su umbral de mantenimiento y un cliente individual (DPI `9876543210987`) ya tiene 3 alquileres confirmados.

## Cómo ejecutar

```bash
javac -encoding UTF-8 -d bin src/*.java
java -cp bin Main
```