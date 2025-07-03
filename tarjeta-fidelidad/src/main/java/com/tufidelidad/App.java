package com.tufidelidad;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

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
        // ID automático
        String id = String.format("CL%03d", contadorClientes);

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Correo: ");
        String correo = scanner.nextLine();

        try {
            Cliente nuevo = new Cliente(id, nombre, correo);
            clienteRepo.agregar(nuevo);
            contadorClientes++; // Incrementar si se agrega correctamente
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
        String id = scanner.nextLine();

        try {
            clienteRepo.eliminar(id);
            System.out.println("Cliente eliminado.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
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

}
