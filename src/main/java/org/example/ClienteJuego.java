package org.example;
import java.io.*;
import java.net.*;

public class ClienteJuego {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 666);
            DataOutputStream outToServidor = new DataOutputStream(socket.getOutputStream());
            DataInputStream inFromServidor = new DataInputStream(socket.getInputStream());

            String mensajeInicial = inFromServidor.readUTF();
            System.out.println(mensajeInicial);

            if (mensajeInicial.contains("cliente1")) {
                String palabra = inFromServidor.readUTF();
                System.out.println("Palabra secreta recibida del servidor: " + palabra);
                new Thread(() -> hacerPreguntas(outToServidor, mensajeInicial, palabra)).start();
            } else {
                new Thread(() -> hacerPreguntas(outToServidor, mensajeInicial, "")).start();
            }

            new Thread(() -> recibirRespuestas(inFromServidor)).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void hacerPreguntas(DataOutputStream outToServidor, String mensajeInicial, String palabra) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                if (mensajeInicial.contains("LA PALABRA SELECCIONADA ES:")) {
                    String respuestaPregunta;
                    do {
                        System.out.println("Responde con si o no: ");
                        respuestaPregunta = reader.readLine().toLowerCase();
                        if (!respuestaPregunta.equals("si") && !respuestaPregunta.equals("no")) {
                            System.out.println("Respuesta errónea.");
                        }
                    } while (!respuestaPregunta.equals("si") && !respuestaPregunta.equals("no"));

                    outToServidor.writeUTF(respuestaPregunta);
                } else {
                    System.out.println("Haz una pregunta o escribe 'adivinar' para resolver:");
                    String mensaje = reader.readLine();
                    outToServidor.writeUTF(mensaje);

                    if (mensaje.equalsIgnoreCase("adivinar")) {
                        System.out.print("Resuelve: ");
                        String palabraAdivinada = reader.readLine();
                        outToServidor.writeUTF(palabraAdivinada);
                        Thread.sleep(110);
                    }
                }

                Thread.sleep(110);
            }

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void recibirRespuestas(DataInputStream inFromServidor) {
        try {
            while (true) {
                try {
                    String respuesta = inFromServidor.readUTF();
                    System.out.println(respuesta);
                    Thread.sleep(110);
                } catch (EOFException e) {
                    // Se alcanzó el final del flujo de entrada, salimos del bucle
                    System.out.println("El servidor ha cerrado la conexión.");
                    break;
                }
                catch (SocketException e) {
                    System.out.println("Se ha cerrado la conexión.");
                    break;
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}