package Servidor;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.net.*;
import java.util.List;

public class ArchivoServidor extends JFrame {
    private JTextArea textArea;
    private JButton btnRegresar;

    public ArchivoServidor() {
        setTitle("File Transfer Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel lbArrastra = new JLabel("Arrastra y suelta archivos aquí:");
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        btnRegresar = new JButton("Regresar");
        btnRegresar.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Recuerda salir antes con Cliente");
         GUI_Servidor g = new GUI_Servidor();
         g.setVisible(true);
         dispose();
        });

        panel.add(lbArrastra, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnRegresar, BorderLayout.SOUTH);

        getContentPane().add(panel);

        panel.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            @Override
            public boolean importData(TransferSupport support) {
                if (!canImport(support)) {
                    return false;
                }

                Transferable transferable = support.getTransferable();
                try {
                    List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                    for (File file : files) {
                        textArea.append("Recibiendo archivo: " + file.getName() + "\n");
                        handleFile(file);
                    }
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        });

        new Thread(this::startServer).start();
    }

    private void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(12350)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> handleClient(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket socket) {
        try (InputStream inputStream = socket.getInputStream();
             FileOutputStream fileOutputStream = new FileOutputStream("received_" + System.currentTimeMillis() + ".txt")) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
            textArea.append("Archivo recibido y guardado.\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleFile(File file) {
        try (Socket socket = new Socket("localhost", 9000);
             OutputStream outputStream = socket.getOutputStream();
             FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            textArea.append("Archivo enviado: " + file.getName() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ArchivoServidor().setVisible(true));
    }
}

