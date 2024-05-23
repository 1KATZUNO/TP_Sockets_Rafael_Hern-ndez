package Servidor;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;

public class ImagenServidor extends JFrame {
    public ImagenServidor() {
        setTitle("Image Server");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lbImagen = new JLabel();
        add(new JScrollPane(lbImagen));
        setVisible(true);

        try (ServerSocket serverSocket = new ServerSocket(12347)) {
            System.out.println("Servidor escuchando en puerto 12347");

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    try (InputStream inputStream = socket.getInputStream()) {
                        DataInputStream dis = new DataInputStream(inputStream);
                        int length = dis.readInt();
                        if (length > 0) {
                            byte[] imageBytes = new byte[length];
                            dis.readFully(imageBytes);
                            ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
                            BufferedImage image = ImageIO.read(bais);
                            if (image != null) {
                                lbImagen.setIcon(new ImageIcon(image));
                                pack(); // Adjust the frame size to fit the new image
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ImagenServidor();
    }
}

