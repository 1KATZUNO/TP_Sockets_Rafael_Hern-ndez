package Servidor;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.*;
import javax.imageio.*;

public class ImagenServidor extends JFrame {
    private JLabel lbImagen;

    public ImagenServidor() {
        setTitle("Image Server"); // Establece el título del marco
        setSize(500, 500); // Establece el tamaño del marco
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); // Evita que el marco se cierre automáticamente
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Muestra un mensaje y cierra la aplicación cuando se intenta cerrar el marco
                JOptionPane.showMessageDialog(null, "Adiós");
                System.exit(0);
            }
        });
        setLocationRelativeTo(null); // Centra el marco en la pantalla

        lbImagen = new JLabel(); // Crea una etiqueta para mostrar la imagen
        add(new JScrollPane(lbImagen)); // Agrega la etiqueta en un panel con barra de desplazamiento
        setVisible(true); // Hace visible el marco

        // Botón para regresar al menú principal del servidor
        JButton regresarButton = new JButton("Regresar");
        regresarButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Recuerda salir antes con Cliente"); // Muestra un mensaje al usuario
            GUI_Servidor g = new GUI_Servidor(); // Crea una instancia del menú principal del servidor
            g.setVisible(true); // Muestra el menú principal del servidor
            dispose(); // Cierra el marco actual
        });
        add(regresarButton, BorderLayout.SOUTH); // Agrega el botón en la parte inferior del marco
    }

    // Método para iniciar el servidor
    public void iniciarServidor() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(12347)) {
                System.out.println("Servidor escuchando en puerto 12347"); // Muestra un mensaje en la consola

                while (true) {
                    Socket socket = serverSocket.accept(); // Espera a que un cliente se conecte
                    new Thread(() -> {
                        try (InputStream inputStream = socket.getInputStream()) {
                            DataInputStream dis = new DataInputStream(inputStream);
                            int length = dis.readInt(); // Lee la longitud de los datos de la imagen
                            if (length > 0) {
                                byte[] imageBytes = new byte[length];
                                dis.readFully(imageBytes); // Lee los datos de la imagen
                                ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
                                BufferedImage image = ImageIO.read(bais); // Convierte los datos en una imagen
                                if (image != null) {
                                    lbImagen.setIcon(new ImageIcon(image)); // Establece la imagen en la etiqueta
                                    pack(); // Ajusta el tamaño del marco para que se ajuste a la imagen
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start(); // Inicia un nuevo hilo para manejar la conexión con el cliente
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start(); // Inicia el hilo del servidor
    }

    public static void main(String[] args) {
        ImagenServidor servidor = new ImagenServidor(); // Crea una instancia del servidor de imágenes
        servidor.iniciarServidor(); // Inicia el servidor
    }
}

