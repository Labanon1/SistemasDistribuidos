package org.example;
import java.io.*;
import java.net.Socket;

public class ClienteJuegoThread implements Runnable {
    private Socket cliente1;
    private Socket cliente2;

    public ClienteJuegoThread(Socket cliente1, Socket cliente2) {
        this.cliente1 = cliente1;
        this.cliente2 = cliente2;
    }

    @Override
    public void run() {
        try {
            // Se crean los flujos de entrada y salida para comunicarse con los clientes
            ObjectInputStream inFromCliente1 = new ObjectInputStream(cliente1.getInputStream());
            ObjectOutputStream outToCliente1 = new ObjectOutputStream(cliente1.getOutputStream());

            ObjectInputStream inFromCliente2 = new ObjectInputStream(cliente2.getInputStream());
            ObjectOutputStream outToCliente2 = new ObjectOutputStream(cliente2.getOutputStream());

            // El hilo se ejecutará continuamente, manejando la comunicación entre los clientes
            while (true) {
                // Recibir y mostrar la pregunta del ClienteJuego que hace preguntas
                String pregunta = (String) inFromCliente1.readObject();
                System.out.println("Pregunta de ClienteJuego: " + pregunta);

                // Enviar la pregunta al ClienteJuego que responde
                outToCliente2.writeObject(pregunta);

                // Recibir y mostrar la respuesta del ClienteJuego que responde
                String respuesta = (String) inFromCliente2.readObject();
                System.out.println("Respuesta de ClienteJuego: " + respuesta);

                // Enviar la respuesta al ClienteJuego que hace preguntas
                outToCliente1.writeObject(respuesta);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
