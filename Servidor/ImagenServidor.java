package Servidor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ImagenServidor extends JFrame {
    public ImagenServidor() {
        setTitle("Image Server");
        setSize(500, 500);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                GUI_Servidor g = new GUI_Servidor();
                g.setVisible(true);
                dispose();
            }
        });
        setLocationRelativeTo(null);

        JLabel lbImagen = new JLabel();
        add(new JScrollPane(lbImagen));
        setVisible(true);

        JButton regresarButton = new JButton("Regresar");
        regresarButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Recuerda salir antes con Cliente");
            GUI_Servidor g = new GUI_Servidor();
            g.setVisible(true);
            dispose();
        });
        add(regresarButton, BorderLayout.SOUTH);

        try {
            ServerSocket serverSocket = new ServerSocket(12347);
            System.out.println("Servidor escuchando en puerto 12347");

            while (true) {
                Socket socket = serverSocket.accept();
                procesarSolicitud(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void procesarSolicitud(Socket socket) {
        new Thread(() -> {
            try {
                InputStream inputStream = socket.getInputStream();
                DataInputStream dis = new DataInputStream(inputStream);
                int length = dis.readInt();
                if (length > 0) {
                    byte[] imageBytes = new byte[length];
                    dis.readFully(imageBytes);
                    ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
                    BufferedImage image = ImageIO.read(bais);
                    if (image != null) {
                        mostrarImagen(image);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void mostrarImagen(BufferedImage image) {
        SwingUtilities.invokeLater(() -> {
            JLabel lbImagen = new JLabel(new ImageIcon(image));
            getContentPane().removeAll();
            add(new JScrollPane(lbImagen));
            revalidate();
            repaint();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ImagenServidor::new);
    }
}
