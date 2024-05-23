package Servidor;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ArchivoServidor extends JFrame{
    public static void main(String[] args) {
        JFrame frame = new JFrame("File Transfer Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel lbArrastra = new JLabel("Arrastra y suelta archivos aquí:");
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        panel.add(lbArrastra, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        frame.getContentPane().add(panel);

        frame.setVisible(true);

        panel.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                return true;
            }

            @Override
            public boolean importData(TransferSupport support) {
                try {
                    Transferable Transferible = support.getTransferable();
                    if (Transferible.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        java.util.List<File> fileList = (java.util.List<File>) Transferible.getTransferData(DataFlavor.javaFileListFlavor);
                        for (File file : fileList) {
                            textArea.append("Recibiendo archivo: " + file.getName() + "\n");
                            handleFile(file, textArea);
                        }
                        return true;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return false;
            }
        });

        try (ServerSocket serverSocket = new ServerSocket(9000)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> handleClient(socket, textArea)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket, JTextArea textArea) {
        try (
            InputStream inputStream = socket.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream("received_" + System.currentTimeMillis() + ".txt");
        ) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
            textArea.append("Archivo recibido y guardado.\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void handleFile(File file, JTextArea textArea) {
        try (
            Socket socket = new Socket("localhost", 9000);
            OutputStream outputStream = socket.getOutputStream();
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        ) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            textArea.append("Archivo enviado: " + file.getName() + "\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}