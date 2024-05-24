package Cliente;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatCliente extends JFrame {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private JTextArea txtChat;
    private JTextField txtEnviar;
    private PrintWriter out;

    public ChatCliente() {
        setTitle("Cliente de Chat");
        setSize(500, 400); // Ajusta el tamaño de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla

        // Crear botón de regresar
        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Vuelve al menú principal del cliente
                GUI_Cliente g = new GUI_Cliente();
                g.setVisible(true);
                dispose(); // Cierra la ventana actual
            }
        });
        JPanel panelBtnRegresar = new JPanel();
        panelBtnRegresar.add(btnRegresar);
        add(panelBtnRegresar, BorderLayout.NORTH); // Coloca el botón en la parte superior de la ventana

        txtChat = new JTextArea();
        txtChat.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtChat);
        add(scrollPane, BorderLayout.CENTER); // Ajusta el panel de chat al centro

        txtEnviar = new JTextField(30); // Campo de texto más grande
        JButton btnEnviar = new JButton("Enviar");
        btnEnviar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage(); // Envia el mensaje al servidor cuando se hace clic en el botón "Enviar"
            }
        });

        JPanel panelEnviar = new JPanel();
        panelEnviar.add(txtEnviar);
        panelEnviar.add(btnEnviar);
        add(panelEnviar, BorderLayout.SOUTH); // Ajusta el panel de enviar al fondo

        new ClientThread().start(); // Inicia un hilo para manejar la comunicación con el servidor
    }

    private void sendMessage() {
        // Método para enviar mensajes al servidor
        String message = "Cliente: " + txtEnviar.getText();
        txtChat.append(message + "\n"); // Muestra el mensaje en el área de chat del cliente
        out.println(message); // Envía el mensaje al servidor
        txtEnviar.setText(""); // Limpia el campo de texto después de enviar el mensaje
    }

    private class ClientThread extends Thread {
        public void run() {
            try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
                // Configura la entrada y salida de datos del socket
                out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String message;
                while ((message = in.readLine()) != null) {
                    if (!message.startsWith("Cliente:")) { // Evita la duplicación de mensajes propios
                        txtChat.append(message + "\n"); // Muestra los mensajes recibidos del servidor en el área de chat del cliente
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // Método principal, crea y muestra la ventana del cliente
        SwingUtilities.invokeLater(() -> {
            ChatCliente client = new ChatCliente();
            client.setVisible(true);
        });
    }
}
