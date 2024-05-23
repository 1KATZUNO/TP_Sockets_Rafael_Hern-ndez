package Cliente;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import javax.imageio.*;
import java.awt.image.*;

public class ImagenCliente extends JFrame {
    private Socket socket;

    public ImagenCliente(String SERVER_ADDRESS, int SERVER_PORT) throws IOException {
        socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        setTitle("Image Client");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lbArrastrar = new JLabel("Drag and drop an image here", SwingConstants.CENTER);
        lbArrastrar.setFont(new Font("Serif", Font.BOLD, 20));
        add(lbArrastrar, BorderLayout.CENTER);

        new DropTarget(lbArrastrar, new DropTargetListener() {
            @Override
            public void dragEnter(DropTargetDragEvent dtde) {
            }

            @Override
            public void dragOver(DropTargetDragEvent dtde) {
            }

            @Override
            public void dropActionChanged(DropTargetDragEvent dtde) {
            }

            @Override
            public void dragExit(DropTargetEvent dte) {
            }

            @Override
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
                                    DisplayImagen(file); // Display the image locally
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

    private boolean ImagenFile(File Archivo) {
        String[] imageExtensions = new String[] { "jpg", "jpeg", "png", "gif", "bmp" };
        String fileName = Archivo.getName().toLowerCase();
        for (String ext : imageExtensions) {
            if (fileName.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    private void EnviarImagen_Servidor(File file) {
        try {
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

    private void DisplayImagen(File file) {
        try {
            BufferedImage image = ImageIO.read(file);
            if (image != null) {
                JLabel imageLabel = new JLabel(new ImageIcon(image));
                JFrame imageFrame = new JFrame("Local Image Preview");
                imageFrame.add(new JScrollPane(imageLabel));
                imageFrame.setSize(500, 500);
                imageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                imageFrame.setVisible(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String serverAddress = "localhost";
        int serverPort = 8000;
        try {
            new ImagenCliente(serverAddress, serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
