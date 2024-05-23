package Cliente;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import javax.imageio.*;
import java.awt.image.*;

public class ImagenCliente extends JFrame {
    private Socket socket;
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12347;

    public ImagenCliente() {
        conectarServidor();

        setTitle("Image Client");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lbArrastrar = new JLabel("Drag and drop an image here", SwingConstants.CENTER);
        lbArrastrar.setFont(new Font("Serif", Font.BOLD, 20));
        add(lbArrastrar, BorderLayout.CENTER);

        new DropTarget(lbArrastrar, new DropTargetListener() {
            @Override
            public void dragEnter(DropTargetDragEvent dtde) {}

            @Override
            public void dragOver(DropTargetDragEvent dtde) {}

            @Override
            public void dropActionChanged(DropTargetDragEvent dtde) {}

            @Override
            public void dragExit(DropTargetEvent dte) {}

            @Override
            @SuppressWarnings("unchecked")
            public void drop(DropTargetDropEvent dtde) {
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    Transferable transferable = dtde.getTransferable();
                    DataFlavor[] flavors = transferable.getTransferDataFlavors();
                    for (DataFlavor flavor : flavors) {
                        if (flavor.isFlavorJavaFileListType()) {
                            java.util.List<File> files = (java.util.List<File>) transferable.getTransferData(flavor);
                            if (!files.isEmpty()) {
                                File file = files.get(0);
                                if (ImagenFile(file)) {
                                    EnviarImagen_Servidor(file);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Please drop an image file");
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        setVisible(true);
    }

    private boolean ImagenFile(File archivo) {
        String[] imageExtensions = new String[] { "jpg", "jpeg", "png", "gif", "bmp" };
        String fileName = archivo.getName().toLowerCase();
        for (String ext : imageExtensions) {
            if (fileName.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    private void EnviarImagen_Servidor(File file) {
        try {
            if (socket == null || socket.isClosed()) {
                conectarServidor();
            }
            
            BufferedImage image = ImageIO.read(file);
            if (image != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "png", baos);
                byte[] imageBytes = baos.toByteArray();

                OutputStream outputStream = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(outputStream);
                dos.writeInt(imageBytes.length);
                dos.write(imageBytes);
                dos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void conectarServidor() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se puede conectar al servidor. Asegúrese de que el servidor está ejecutándose.", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        new ImagenCliente();
    }
}



