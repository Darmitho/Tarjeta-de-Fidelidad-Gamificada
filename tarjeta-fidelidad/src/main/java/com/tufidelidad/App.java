package com.tufidelidad;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.List;

public class App {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ClienteRepository clienteRepo = new ClienteRepository();
    private static final CompraRepository compraRepo = new CompraRepository();
    private static int contadorClientes = 1;
    private static int contadorCompras = 1;

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- GESTIÓN DE CLIENTES ---");
            System.out.println("1. Agregar cliente");
            System.out.println("2. Listar clientes");
            System.out.println("3. Actualizar cliente");
            System.out.println("4. Eliminar cliente");
            System.out.println("5. Registrar compra");
            System.out.println("6. Listar compras");
            System.out.println("7. Listar compras por cliente");
            System.out.println("8. Actualizar compra");
            System.out.println("9. Eliminar compra");
            System.out.println("0. Salir");

            System.out.print("Seleccione una opción: ");

            try {
                int opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1 -> agregarCliente();
                    case 2 -> listarClientes();
                    case 3 -> actualizarCliente();
                    case 4 -> eliminarCliente();
                    case 5 -> registrarCompra();
                    case 6 -> mostrarHistorialCompras();
                    case 7 -> mostrarHistorialComprasPorCliente();
                    case 8 -> actualizarCompra();
                    case 9 -> eliminarCompra();
                    case 0 -> {
                        System.out.println("¡Hasta luego!");
                        return;
                    }
                    default -> System.out.println("Opción inválida. Intenta de nuevo.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Debes ingresar un número.");
            }
        }
    }

    private static void agregarCliente() {
        System.out.println("\n--- AGREGAR CLIENTE ---");
        String id = String.format("CL%03d", contadorClientes);

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Correo: ");
        String correo = scanner.nextLine();

        try {
            Cliente nuevo = new Cliente(id, nombre, correo);
            clienteRepo.agregar(nuevo);
            contadorClientes++;
            System.out.println("Cliente agregado correctamente. ID generado: " + id);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void listarClientes() {
        if (clienteRepo.listar().isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }
        System.out.println("Lista de clientes:");
        for (Cliente c : clienteRepo.listar()) {
            System.out.println(c.getResumen() + "\n");
        }
    }

    private static void actualizarCliente() {
        System.out.print("Ingrese ID del cliente a actualizar: ");
        String id = scanner.nextLine();

        try {
            clienteRepo.buscarPorId(id);

            System.out.print("Nuevo nombre: ");
            String nuevoNombre = scanner.nextLine();

            System.out.print("Nuevo correo: ");
            String nuevoCorreo = scanner.nextLine();

            Cliente actualizado = new Cliente(id, nuevoNombre, nuevoCorreo);
            clienteRepo.actualizar(actualizado);
            System.out.println("Cliente actualizado correctamente.");

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void eliminarCliente() {
        System.out.print("ID del cliente a eliminar: ");
        String id = scanner.nextLine().trim();

        
        try {
            clienteRepo.buscarPorId(id);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        List<Compra> comprasCliente = compraRepo.listarPorCliente(id);
        for (Compra compra : comprasCliente) {
            compraRepo.eliminar(compra.getIdCompra());
        }

        clienteRepo.eliminar(id);
        System.out.println("Cliente y todas sus compras eliminadas correctamente.");
    }

    private static void registrarCompra() {
        System.out.println("\n--- REGISTRO DE COMPRA ---");
        System.out.print("Ingrese el ID del cliente: ");
        String idCliente = scanner.nextLine().trim();

        Cliente cliente;
        try {
            cliente = clienteRepo.buscarPorId(idCliente);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        } 

        System.out.print("Ingrese el monto de la compra: ");
        double monto;
        try {
            monto = Double.parseDouble(scanner.nextLine().trim());
            if (monto <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("Monto inválido. Debe ser un número positivo.");
            return;
        }

        System.out.print("Ingrese la fecha de la compra (DD-MM-AAAA): ");
        String fechaStr = scanner.nextLine().trim();

        LocalDate fecha;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            fecha = LocalDate.parse(fechaStr, formatter);
        } catch (Exception e) {
            System.out.println("Formato de fecha inválido. Debe ser DD-MM-AAAA.");
            return;
        }

        String idCompra = String.format("CO%03d", contadorCompras++);
        LocalDateTime fechaHora = fecha.atStartOfDay();

        Compra compra = new Compra(idCompra, idCliente, monto, fechaHora);
        compraRepo.registrar(compra);
        cliente.agregarCompra(compra);

        System.out.println("Compra registrada correctamente con ID: " + idCompra);
    }

    private static void mostrarHistorialCompras() {
        List<Compra> compras = compraRepo.listarTodas();

        if (compras.isEmpty()) {
            System.out.println("No hay compras registradas.");
            return;
        }

        System.out.println("\n--- Historial de Compras ---");
        System.out.printf("%-8s %-8s %-10s %-20s%n", "ID", "Cliente", "Monto", "Fecha");
        System.out.println("--------------------------------------------------------");

        for (Compra compra : compras) {
            System.out.printf("%-8s %-8s %-10.2f %-20s%n",
                compra.getIdCompra(),
                compra.getIdCliente(),
                compra.getMonto(),
                compra.getFecha()
            );
        }
    }

    private static void mostrarHistorialComprasPorCliente() {
        System.out.print("Ingrese el ID del cliente: ");
        String idCliente = scanner.nextLine().trim();
        Cliente cliente;
        try {
            cliente = clienteRepo.buscarPorId(idCliente);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        List<Compra> historial = cliente.getHistorialCompras();

        if (historial.isEmpty()) {
            System.out.println("El cliente " + idCliente + " no tiene compras registradas.");
            return;
        }

        System.out.println("\n--- Historial de Compras de " + cliente.getNombre() + " (" + idCliente + ") ---");
        System.out.printf("%-8s %-10s %-20s%n", "ID", "Monto", "Fecha");
        System.out.println("-----------------------------------------------");

        for (Compra compra : historial) {
            System.out.printf("%-8s %-10.2f %-20s%n",
                compra.getIdCompra(),
                compra.getMonto(),
                compra.getFecha()
            );
        }
    }

    private static void actualizarCompra() {
        System.out.print("Ingrese el ID de la compra a actualizar: ");
        String idCompra = scanner.nextLine().trim();

        Compra compraExistente = compraRepo.buscarPorId(idCompra);
        if (compraExistente == null) {
            System.out.println("No se encontró una compra con ID: " + idCompra);
            return;
        }

        Cliente clienteActual = clienteRepo.buscarPorId(compraExistente.getIdCliente());
        if (clienteActual == null) {
            System.out.println("El cliente asociado a esta compra ya no existe.");
            return;
        }

        System.out.print("¿Desea cambiar el ID del cliente? (s/n): ");
        String cambiarCliente = scanner.nextLine().trim().toLowerCase();

        String nuevoIdCliente = compraExistente.getIdCliente();
        if (cambiarCliente.equals("s")) {
            System.out.print("Ingrese el nuevo ID del cliente: ");
            nuevoIdCliente = scanner.nextLine().trim();
            if (clienteRepo.buscarPorId(nuevoIdCliente) == null) {
                System.out.println("No existe un cliente con ese ID.");
                return;
            }
        }

        System.out.print("Ingrese el nuevo monto de la compra: ");
        double nuevoMonto;
        try {
            nuevoMonto = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Monto inválido.");
            return;
        }

        System.out.print("Ingrese la nueva fecha (DD-MM-AAAA): ");
        String nuevaFechaStr = scanner.nextLine();

        LocalDate nuevaFecha;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            nuevaFecha = LocalDate.parse(nuevaFechaStr, formatter);
        } catch (Exception e) {
            System.out.println("Formato de fecha inválido. Debe ser DD-MM-AAAA.");
            return;
        }

        clienteActual.eliminarCompra(idCompra);
        Compra compraActualizada = new Compra(idCompra, nuevoIdCliente, nuevoMonto, nuevaFecha.atStartOfDay());
        Cliente clienteDestino = clienteRepo.buscarPorId(nuevoIdCliente);
        clienteDestino.agregarCompra(compraActualizada);
        compraRepo.actualizar(compraActualizada);

        System.out.println("Compra actualizada correctamente.");
    }

    private static void eliminarCompra() {
        System.out.print("Ingrese el ID de la compra a eliminar: ");
        String idCompra = scanner.nextLine().trim();

        Compra compraExistente = compraRepo.buscarPorId(idCompra);
        if (compraExistente == null) {
            System.out.println("No se encontró una compra con ID: " + idCompra);
            return;
        }

        String idCliente = compraExistente.getIdCliente();
        Cliente cliente = clienteRepo.buscarPorId(idCliente);

        if (cliente == null) {
            System.out.println("El cliente asociado a esta compra ya no existe.");
            return;
        }

        try {
            cliente.eliminarCompra(idCompra);
        } catch (IllegalArgumentException e) {
            System.out.println("Error al eliminar del historial del cliente: " + e.getMessage());
            return;
        }

        try {
            compraRepo.eliminar(idCompra);
        } catch (IllegalArgumentException e) {
            System.out.println("Error al eliminar del repositorio: " + e.getMessage());
            return;
        }

        System.out.println("Compra eliminada correctamente.");
    }



}
