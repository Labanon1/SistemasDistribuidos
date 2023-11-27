package org.example;
import java.io.*;
import java.net.Socket;

public class ClienteJuegoThread implements Runnable {
    private Socket clienteEnvia;
    private Socket clienteRecibe;

    public ClienteJuegoThread(Socket clienteEnvia, Socket clienteRecibe) {
        this.clienteEnvia = clienteEnvia;
        this.clienteRecibe = clienteRecibe;
    }

    @Override
    public void run() {
        try {
// Se crean los flujos de entrada y salida para comunicarse con los clientes
            DataInputStream inFromClienteEnvia = new DataInputStream(clienteEnvia.getInputStream());
            DataOutputStream outToClienteRecibe = new DataOutputStream(clienteRecibe.getOutputStream());

// El hilo se ejecutará continuamente, manejando la comunicación entre los clientes
            while (true) {
// Recibir y mostrar la pregunta del ClienteJuego que hace preguntas
                String pregunta = inFromClienteEnvia.readUTF();
                System.out.println("Pregunta de ClientePregunta: " + pregunta);

                // Enviar la pregunta al ClienteJuego que responde (clienteRecibe)
                outToClienteRecibe.writeUTF(pregunta);

                // Añadir una pausa breve para permitir que las operaciones de E/S se completen
                Thread.sleep(110);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
