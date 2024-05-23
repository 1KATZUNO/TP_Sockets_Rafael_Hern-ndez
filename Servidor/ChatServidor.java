package Servidor;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatServidor extends JFrame {
    private static final int PUERTO = 12345;
    private static Set<PrintWriter> EscritorCliente = new HashSet<>();
    private JTextArea txtChat;
    private JTextField txtEnviar;
    private PrintWriter out;

    public ChatServidor() {
        setTitle("Servidor de Chat");
        setSize(500, 400);
         // Ajustamos  el tamaño de la ventana, de comos e va a ver
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        // Centra la ventana en la pantalla

        //botón de regresar a menú principal
        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               // acá hacemos el regresarrr
            }
        });
        JPanel panelBtnRegresar = new JPanel();
        panelBtnRegresar.add(btnRegresar);
        add(panelBtnRegresar, BorderLayout.NORTH); 
// Colocamos el botón en la parte superior de la ventana por algún dedaso

        txtChat = new JTextArea();
        txtChat.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtChat);
        add(scrollPane, BorderLayout.CENTER); 
        // Ajustamos el panel de chat al centro

        txtEnviar = new JTextField(30); 
        // campo de texto y su tamaaño
        JButton btnEnviar = new JButton("Enviar");
        btnEnviar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EnviarMensaje();
            }
        });

        JPanel panelEnviar = new JPanel();
        panelEnviar.add(txtEnviar);
        panelEnviar.add(btnEnviar);
        add(panelEnviar, BorderLayout.SOUTH); // Ajusta el panel de enviar al fondo

        new ServidorHilo().start();
    }

    private void EnviarMensaje() {
        // acá enviamos el mensaje del servidor al cliente, y así será como le aparece 
        String message = "Servidor: " + txtEnviar.getText();
        txtChat.append(message + "\n");
        synchronized (EscritorCliente) {
            for (PrintWriter writer : EscritorCliente) {
                writer.println(message);
            }
        }
        txtEnviar.setText("");
    }

    private class ServidorHilo extends Thread {
        // aquí un hilito para el envío
        public void run() {
            try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
                while (true) {
                    new ManejadorCliente(serverSocket.accept()).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ManejadorCliente extends Thread {
        //El manejador del cliente se sienta en el lado del servidor y maneja la comunicación entre el servidor y los clientes
        // es como una centralita
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ManejadorCliente(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            // cmo corre
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                synchronized (EscritorCliente) {
                    EscritorCliente.add(out);
                }

                String message;
                while ((message = in.readLine()) != null) {
                    txtChat.append(message + "\n");
                    synchronized (EscritorCliente) {
                        for (PrintWriter writer : EscritorCliente) {
                            writer.println(message);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                synchronized (EscritorCliente) {
                    EscritorCliente.remove(out);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatServidor server = new ChatServidor();
            server.setVisible(true);
        });
    }
}