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
                GUI_Cliente g = new GUI_Cliente();
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

        txtEnviar = new JTextField(30); // Campo de texto más grande
        JButton btnEnviar = new JButton("Enviar");
        btnEnviar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        JPanel panelEnviar = new JPanel();
        panelEnviar.add(txtEnviar);
        panelEnviar.add(btnEnviar);
        add(panelEnviar, BorderLayout.SOUTH); // Ajusta el panel de enviar al fondo

        new ClientThread().start();
    }

    private void sendMessage() {
        String message = "Cliente: " + txtEnviar.getText();
        txtChat.append(message + "\n");
        out.println(message);
        txtEnviar.setText("");
    }

    private class ClientThread extends Thread {
        public void run() {
            try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
                out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String message;
                while ((message = in.readLine()) != null) {
                    if (!message.startsWith("Cliente:")) { // Evita la duplicación
                        txtChat.append(message + "\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatCliente client = new ChatCliente();
            client.setVisible(true);
        });
    }
}
