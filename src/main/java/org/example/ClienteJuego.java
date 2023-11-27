package org.example;

import java.io.*;
import java.net.Socket;

public class ClienteJuego {

    public static void main(String[] args) {
        try {
            // Conectar al servidor
            Socket socket = new Socket("localhost", 666);

            // Configurar flujos de entrada/salida para el cliente
            DataOutputStream outToServidor = new DataOutputStream(socket.getOutputStream());
            DataInputStream inFromServidor = new DataInputStream(socket.getInputStream());

            // Recibir el mensaje inicial del servidor
            String mensajeInicial = inFromServidor.readUTF();
            System.out.println(mensajeInicial);

            // Si este cliente es cliente1, recibir la palabra secreta del servidor
            if (mensajeInicial.contains("cliente1")) {
                String palabra = inFromServidor.readUTF();
                System.out.println("Palabra secreta recibida del servidor: " + palabra);

                // Comunicarse con el servidor
                new Thread(() -> hacerPreguntas(outToServidor, true)).start();
            } else {
                // Comunicarse con el servidor
                new Thread(() -> hacerPreguntas(outToServidor, false)).start();
            }

            // Se crea un nuevo hilo para recibir y mostrar las respuestas del ClienteJuego que responde (clientepregunta)
            new Thread(() -> recibirRespuestas(inFromServidor)).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void hacerPreguntas(DataOutputStream outToServidor, boolean esCliente1) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                // Leer la pregunta desde la consola
                String indicacion = esCliente1 ? "Tu pregunta o escribe 'adivinar' para adivinar la palabra: " : "";
                System.out.print(indicacion);
                String pregunta = reader.readLine();

                // Enviar la pregunta al servidor
                outToServidor.writeUTF(pregunta);

                // Añadir una pausa breve para permitir que las operaciones de E/S se completen
                Thread.sleep(110);
            }

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void recibirRespuestas(DataInputStream inFromServidor) {
        try {
            while (true) {
                // Recibir y mostrar la respuesta del ClienteJuego que responde (clientepregunta)
                String respuesta = inFromServidor.readUTF();
                System.out.println("Respuesta de ClienteRespuesta: " + respuesta);

                // Añadir una pausa breve para permitir que las operaciones de E/S se completen
                Thread.sleep(110);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
