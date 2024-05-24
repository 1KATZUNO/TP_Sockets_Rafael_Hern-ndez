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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla

        // Botón de regresar a menú principal
        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Recuerda salir antes con Cliente");
                GUI_Servidor g = new GUI_Servidor();
                g.setVisible(true);
                dispose();
            }
        });
        JPanel panelBtnRegresar = new JPanel();
        panelBtnRegresar.add(btnRegresar);
        add(panelBtnRegresar, BorderLayout.NORTH); // Coloca el botón en la parte superior de la ventana

        txtChat = new JTextArea();
        txtChat.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtChat);
        add(scrollPane, BorderLayout.CENTER); // Ajusta el panel de chat al centro

        txtEnviar = new JTextField(30); // Campo de texto y su tamaño
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

        new ServidorHilo().start(); // Inicia un hilo para manejar las conexiones de clientes
    }

    private void EnviarMensaje() {
        // Método para enviar mensajes del servidor a los clientes
        String message = "Servidor: " + txtEnviar.getText();
        txtChat.append(message + "\n");
        synchronized (EscritorCliente) {
            for (PrintWriter writer : EscritorCliente) {
                writer.println(message);
            }
        }
        txtEnviar.setText(""); // Limpia el campo de texto después de enviar el mensaje
    }

    private class ServidorHilo extends Thread {
        public void run() {
            // Hilo para manejar las conexiones entrantes de los clientes
            try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
                while (true) {
                    new ManejadorCliente(serverSocket.accept()).start(); // Acepta nuevas conexiones y las maneja en hilos separados
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ManejadorCliente extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ManejadorCliente(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                synchronized (EscritorCliente) {
                    EscritorCliente.add(out); // Agrega el escritor del cliente al conjunto de escritores
                }

                String message;
                while ((message = in.readLine()) != null) {
                    txtChat.append(message + "\n"); // Muestra el mensaje en el área de chat del servidor
                    synchronized (EscritorCliente) {
                        for (PrintWriter writer : EscritorCliente) {
                            writer.println(message); // Envía el mensaje a todos los clientes conectados
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close(); // Cierra el socket del cliente
                } catch (IOException e) {
                    e.printStackTrace();
                }
                synchronized (EscritorCliente) {
                    EscritorCliente.remove(out); // Elimina el escritor del cliente del conjunto de escritores
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
