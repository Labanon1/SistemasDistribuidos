package org.example;

import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {

    private static List<String> palabras = Arrays.asList("Palabra1", "Palabra2", "Palabra3");
    private static List<Socket> clientes = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);

            System.out.println("Esperando a Cliente1...");
            Socket cliente1Socket = serverSocket.accept();
            clientes.add(cliente1Socket);

            // Seleccionar una palabra aleatoria del ArrayList
            String palabraSecreta = seleccionarPalabra();
            System.out.println("Palabra secreta seleccionada: " + palabraSecreta);

            ObjectOutputStream outToCliente1 = new ObjectOutputStream(cliente1Socket.getOutputStream());
            outToCliente1.writeObject(palabraSecreta);

            System.out.println("Esperando a Cliente2...");

            Socket cliente2Socket = serverSocket.accept();
            clientes.add(cliente2Socket);

            // Comunicarse con Cliente1 y Cliente2
            new Thread(() -> comunicarseConClientes()).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String seleccionarPalabra() {
        // Seleccionar una palabra aleatoria del ArrayList
        Random random = new Random();
        return palabras.get(random.nextInt(palabras.size()));
    }

    private static void comunicarseConClientes() {
        try {
            ObjectOutputStream[] outToClientes = {
                    new ObjectOutputStream(clientes.get(0).getOutputStream()),
                    new ObjectOutputStream(clientes.get(1).getOutputStream())
            };

            ObjectInputStream[] inFromClientes = {
                    new ObjectInputStream(clientes.get(0).getInputStream()),
                    new ObjectInputStream(clientes.get(1).getInputStream())
            };

            // Comunicación entre Cliente1 y Cliente2
            while (true) {
                // Cliente1 hace pregunta a Cliente2
                String preguntaCliente1 = (String) inFromClientes[0].readObject();
                System.out.println("Pregunta de Cliente1 a Cliente2: " + preguntaCliente1);

                // Enviar pregunta de Cliente1 a Cliente2
                outToClientes[1].writeObject(preguntaCliente1);

                // Recibir respuesta de Cliente2
                String respuestaCliente2 = (String) inFromClientes[1].readObject();
                System.out.println("Respuesta de Cliente2 a Cliente1: " + respuestaCliente2);

                // Cliente2 hace pregunta a Cliente1
                String preguntaCliente2 = (String) inFromClientes[1].readObject();
                System.out.println("Pregunta de Cliente2 a Cliente1: " + preguntaCliente2);

                // Enviar pregunta de Cliente2 a Cliente1
                outToClientes[0].writeObject(preguntaCliente2);

                // Recibir respuesta de Cliente1
                String respuestaCliente1 = (String) inFromClientes[0].readObject();
                System.out.println("Respuesta de Cliente1 a Cliente2: " + respuestaCliente1);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
