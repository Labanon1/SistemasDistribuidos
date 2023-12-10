package org.example;

import java.io.*;
import java.net.Socket;

public class ClienteJuegoThread implements Runnable {
    private Socket clienteEnvia;
    private Socket clienteRecibe;
    private String palabraAdivinar;

    public ClienteJuegoThread(Socket clienteEnvia, Socket clienteRecibe, String palabraAdivinar) {
        this.clienteEnvia = clienteEnvia;
        this.clienteRecibe = clienteRecibe;
        this.palabraAdivinar = palabraAdivinar;
    }

    @Override
    public void run() {
        try {
            DataInputStream inFromClienteEnvia = new DataInputStream(clienteEnvia.getInputStream());
            DataOutputStream outToClienteEnvia = new DataOutputStream(clienteEnvia.getOutputStream());
            DataOutputStream outToClienteRecibe = new DataOutputStream(clienteRecibe.getOutputStream());

            while (true) {
                String respuesta = inFromClienteEnvia.readUTF();

                if (respuesta.equalsIgnoreCase("adivinar")) {
                    String palabraClientePregunta = inFromClienteEnvia.readUTF();

                    if (palabraClientePregunta.equalsIgnoreCase(palabraAdivinar)) {
                        outToClienteEnvia.writeUTF("FELICIDADES, HAS ACERTADO");
                        Thread.sleep(110);
                        // Sale del bucle y termina el programa
                        System.exit(0);
                    } else {
                        outToClienteEnvia.writeUTF("Has fallado. Puedes hacer más preguntas o intentar adivinar de nuevo.");
                    }
                } else {
                    outToClienteRecibe.writeUTF( respuesta);
                }

                Thread.sleep(110);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

