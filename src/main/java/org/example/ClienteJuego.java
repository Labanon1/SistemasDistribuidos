package org.example;

import java.io.*;
import java.net.*;

public class ClienteJuego {

    public static void main(String[] args) {
        try {
            // Conectar al servidor
            Socket socket = new Socket("localhost", 12345);

            // Configurar flujos de entrada/salida para el cliente
            ObjectOutputStream outToServidor = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inFromServidor = new ObjectInputStream(socket.getInputStream());

            // Recibir la palabra secreta del servidor
            String palabraSecreta = (String) inFromServidor.readObject();
            System.out.println("Palabra secreta recibida del servidor: " + palabraSecreta);

            // Comunicarse con el servidor
            new Thread(() -> hacerPreguntas(outToServidor)).start();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void hacerPreguntas(ObjectOutputStream outToServidor) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                // Leer la pregunta desde la consola
                System.out.print("Tu pregunta o escribe 'adivinar' para adivinar la palabra: ");
                String pregunta = reader.readLine();

                // Enviar la pregunta al servidor
                outToServidor.writeObject(pregunta);

                // Añadir una pausa breve para permitir que las operaciones de E/S se completen
                Thread.sleep(100);
            }

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
