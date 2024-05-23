package Cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ArchivoCliente {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Transferencia de Archivos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel lbArrastra = new JLabel("Arrastra y suelta archivos aquí:");
        JTextArea txtArchivos = new JTextArea();
        txtArchivos.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtArchivos);

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
                    Transferable transferable = support.getTransferable();
                    if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        java.util.List<File> fileList = (java.util.List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                        for (File file : fileList) {
                            txtArchivos.append("Enviando archivo: " + file.getName() + "\n");
                            EnviarArchivo(file);
                        }
                        return true;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return false;
            }
        });
    }

    private static void EnviarArchivo(File file) {
        try (Socket socket = new Socket("localhost", 9000);
             OutputStream outputStream = socket.getOutputStream();
             FileInputStream fileInputStream = new FileInputStream(file);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
             PrintWriter writer = new PrintWriter(outputStream, true);
        ) {
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
}
