package Cliente;

import javax.swing.*;

import Servidor.GUI_Servidor;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.net.*;
import java.util.List;

public class ArchivoCliente extends JFrame {
    private JTextArea txtArchivos;
    private JButton btnRegresar;

    public ArchivoCliente() {
        setTitle("Transferencia de Archivos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel lbArrastra = new JLabel("Arrastra y suelta archivos aquí:");
        txtArchivos = new JTextArea();
        txtArchivos.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtArchivos);

        btnRegresar = new JButton("Regresar");
        btnRegresar.addActionListener(e -> {
         GUI_Cliente g = new GUI_Cliente();
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
                    List<File> fileList = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                    for (File file : fileList) {
                        txtArchivos.append("Enviando archivo: " + file.getName() + "\n");
                        EnviarArchivo(file);
                    }
                    return true;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return false;
                }
            }
        });

        setVisible(true);
    }

    private void EnviarArchivo(File file) {
        try (Socket socket = new Socket("localhost", 12350);
             OutputStream outputStream = socket.getOutputStream();
             FileInputStream fileInputStream = new FileInputStream(file);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
             PrintWriter writer = new PrintWriter(outputStream, true)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            writer.println("FIN");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error enviando archivo al servidor.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ArchivoCliente());
    }
}


