package org.example;

import java.io.*;
import java.net.*;

public class ClienteConcursante {

    public static void main(String[] args) {
        try {
            // Conectar al servidor
            Socket socket = new Socket("localhost", 12345);

            // Configurar flujos de entrada/salida para el cliente
            ObjectInputStream inFromServidor = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outToServidor = new ObjectOutputStream(socket.getOutputStream());

            // Comunicarse con Cliente1
            new Thread(() -> comunicarseConCliente1(socket)).start();

            // Empezar a hacer preguntas al servidor
            hacerPreguntas(outToServidor, inFromServidor);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void hacerPreguntas(ObjectOutputStream outToServidor, ObjectInputStream inFromServidor) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                // Leer la pregunta del jugador desde la consola
                System.out.print("Tu pregunta a Cliente1 (o escribe 'adivinar' para adivinar la palabra): ");
                String pregunta = reader.readLine();

                // Enviar la pregunta a Cliente1
                outToServidor.writeObject(pregunta);

                // Si el jugador quiere adivinar la palabra, esperar la respuesta del servidor y mostrar el resultado
                if (pregunta.equalsIgnoreCase("adivinar")) {
                    String resultadoAdivinanza = (String) inFromServidor.readObject();
                    System.out.println(resultadoAdivinanza);
                    break;  // Salir del bucle cuando el jugador ha adivinado
                }

                // Recibir la respuesta del servidor
                String respuesta = (String) inFromServidor.readObject();
                System.out.println("Respuesta de Cliente1: " + respuesta);
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error de conexión con el servidor: " + e.getMessage());
        }
    }

    private static void comunicarseConCliente1(Socket socket) {
        try {
            ObjectOutputStream outToCliente1 = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inFromCliente1 = new ObjectInputStream(socket.getInputStream());

            // Comunicación con Cliente1
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                // Escribir pregunta para Cliente1
                System.out.print("Tu pregunta a Cliente1: ");
                String preguntaCliente2 = reader.readLine();
                outToCliente1.writeObject(preguntaCliente2);

                // Recibir respuesta de Cliente1
                String respuestaCliente1 = (String) inFromCliente1.readObject();
                System.out.println("Respuesta de Cliente1: " + respuestaCliente1);
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error de conexión con Cliente1: " + e.getMessage());
        }
    }
}
