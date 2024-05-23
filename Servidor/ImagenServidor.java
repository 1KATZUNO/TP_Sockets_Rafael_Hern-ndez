package Servidor;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;

public class ImagenServidor extends JFrame{
    public static void main(String[] args) {
        JFrame frame = new JFrame("Image Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel lbImagen = new JLabel();
        frame.add(new JScrollPane(lbImagen));
        frame.setSize(500, 500);
        frame.setVisible(true);

        try (ServerSocket serverSocket = new ServerSocket(8000)) {
            System.out.println("Server is listening on port 8000");

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    try (InputStream inputStream = socket.getInputStream()) {
                        BufferedImage Imagen = ImageIO.read(inputStream);
                        if (Imagen != null) {
                            ImageIcon imageIcon = new ImageIcon(Imagen);
                            lbImagen.setIcon(imageIcon);
                            frame.pack(); // Adjust the frame size to fit the new image
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
}
