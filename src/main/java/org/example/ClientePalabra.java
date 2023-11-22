package org.example;

import java.io.*;
import java.net.*;

import java.io.*;
import java.net.*;

public class ClientePalabra {

    public static void main(String[] args) {
        try {
            // Conectar al servidor
            Socket socket = new Socket("localhost", 12345);

            // Configurar flujos de entrada/salida para el cliente
            ObjectInputStream inFromServidor = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outToServidor = new ObjectOutputStream(socket.getOutputStream());

            // Recibir la palabra secreta del servidor
            String palabraSecreta = (String) inFromServidor.readObject();
            System.out.println("Palabra secreta recibida del servidor: " + palabraSecreta);

            // Comunicarse con Cliente2
            new Thread(() -> comunicarseConCliente2(socket)).start();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void comunicarseConCliente2(Socket socket) {
        try {
            ObjectOutputStream outToCliente2 = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inFromCliente2 = new ObjectInputStream(socket.getInputStream());

            // Comunicación con Cliente2
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                // Leer pregunta de Cliente2
                String preguntaCliente2 = (String) inFromCliente2.readObject();
                System.out.println("Pregunta recibida de Cliente2: " + preguntaCliente2);

                // Responder con sí o no
                System.out.print("Tu respuesta (Sí/No): ");
                String respuesta = reader.readLine();
                outToCliente2.writeObject(respuesta);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}


