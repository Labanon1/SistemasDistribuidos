package org.example;
import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {
    private static List<String> palabras = Arrays.asList("León", "Cabra", "Tiburón", "Pelícano", "Toro");
    private static Socket cliente1, cliente2;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(666);

            System.out.println("Esperando a Cliente1...");
            cliente1 = serverSocket.accept();
            System.out.println("Cliente1 conectado.");

            System.out.println("Esperando a Cliente2...");
            cliente2 = serverSocket.accept();
            System.out.println("Cliente2 conectado.");

        // Seleccionar una palabra aleatoria del ArrayList
            String palabra = seleccionarPalabra();

            // Enviar la palabra secreta solo a cliente1
            enviarPalabra(cliente1, palabra);

// Comunicarse con los clientes
            ClienteJuegoThread clienteJuegoThread1 = new ClienteJuegoThread(cliente1, cliente2);
            ClienteJuegoThread clienteJuegoThread2 = new ClienteJuegoThread(cliente2, cliente1);

            new Thread(clienteJuegoThread1).start();
            new Thread(clienteJuegoThread2).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String seleccionarPalabra() {
// Seleccionar una palabra aleatoria del ArrayList
        Random random = new Random();
        return palabras.get(random.nextInt(palabras.size()));
    }

    private static void enviarPalabra(Socket cliente, String palabra) {
        try {
            DataOutputStream outToCliente = new DataOutputStream(cliente.getOutputStream());
            if (cliente.equals(cliente1)) {
                outToCliente.writeUTF("LA PALABRA SELECCIONADA ES: " + palabra);
            } else {
                outToCliente.writeUTF("Espera tu turno...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}