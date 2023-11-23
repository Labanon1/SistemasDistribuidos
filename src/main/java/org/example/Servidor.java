package org.example;
import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {

    private static List<String> palabras = Arrays.asList("Palabra1", "Palabra2", "Palabra3");
    private static Socket cliente1;
    private static Socket cliente2;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);

            System.out.println("Esperando a Cliente1...");
            cliente1 = serverSocket.accept();
            System.out.println("Cliente1 conectado.");

            System.out.println("Esperando a Cliente2...");
            cliente2 = serverSocket.accept();
            System.out.println("Cliente2 conectado.");

            // Seleccionar una palabra aleatoria del ArrayList
            String palabraSecreta = seleccionarPalabra();
            System.out.println("Palabra secreta seleccionada: " + palabraSecreta);

            // Enviar la palabra secreta a ambos clientes
            enviarPalabraSecreta(cliente1, palabraSecreta);
            enviarPalabraSecreta(cliente2, palabraSecreta);

            // Comunicarse con los clientes
            ClienteJuegoThread clienteJuegoThread = new ClienteJuegoThread(cliente1, cliente2);
            new Thread(clienteJuegoThread).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String seleccionarPalabra() {
        // Seleccionar una palabra aleatoria del ArrayList
        Random random = new Random();
        return palabras.get(random.nextInt(palabras.size()));
    }

    private static void enviarPalabraSecreta(Socket cliente, String palabraSecreta) {
        try {
            ObjectOutputStream outToCliente = new ObjectOutputStream(cliente.getOutputStream());
            outToCliente.writeObject(palabraSecreta);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
