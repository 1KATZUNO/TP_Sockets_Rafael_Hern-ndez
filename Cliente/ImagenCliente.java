package Cliente;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import javax.imageio.*;
import java.awt.image.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ImagenCliente extends JFrame {
    private Socket socket;
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12347;

    public ImagenCliente() {
        setTitle("Image Client"); // Establece el título del marco
        setSize(400, 400); // Establece el tamaño del marco
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); // Evita que el marco se cierre automáticamente
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose(); // Cierra el marco
            }
        });
        setLocationRelativeTo(null); // Centra el marco en la pantalla

        JLabel lbArrastrar = new JLabel("Drag and drop an image here", SwingConstants.CENTER); // Etiqueta para arrastrar y soltar una imagen
        lbArrastrar.setFont(new Font("Serif", Font.BOLD, 20)); // Establece el tamaño y estilo de fuente de la etiqueta
        add(lbArrastrar, BorderLayout.CENTER); // Agrega la etiqueta al centro del marco

        JButton regresarButton = new JButton("Regresar"); // Botón para regresar al menú principal del cliente
        regresarButton.addActionListener(e -> {
           GUI_Cliente g = new GUI_Cliente();
           g.setVisible(true);
           dispose(); // Cierra el marco actual
        });
        add(regresarButton, BorderLayout.SOUTH); // Agrega el botón en la parte inferior del marco

        // Configuración del comportamiento de arrastrar y soltar para la etiqueta
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
                    dtde.acceptDrop(DnDConstants.ACTION_COPY); // Acepta la acción de copiar
                    Transferable transferable = dtde.getTransferable(); // Obtiene los datos transferidos
                    DataFlavor[] flavors = transferable.getTransferDataFlavors(); // Obtiene los sabores de datos transferidos
                    for (DataFlavor flavor : flavors) {
                        if (flavor.isFlavorJavaFileListType()) { // Verifica si el tipo de datos es una lista de archivos Java
                            java.util.List<File> files = (java.util.List<File>) transferable.getTransferData(flavor); // Obtiene la lista de archivos
                            if (!files.isEmpty()) {
                                File file = files.get(0); // Obtiene el primer archivo de la lista
                                if (isImageFile(file)) { // Verifica si el archivo es una imagen
                                    sendImageToServer(file); // Envía la imagen al servidor
                                } else {
                                    JOptionPane.showMessageDialog(null, "Please drop an image file"); // Muestra un mensaje si el archivo no es una imagen
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        setVisible(true); // Hace visible el marco
    }

    // Método para verificar si el archivo es una imagen
    private boolean isImageFile(File file) {
        String[] imageExtensions = new String[] { "jpg", "jpeg", "png", "gif", "bmp" }; // Extensiones de archivo de imagen admitidas
        String fileName = file.getName().toLowerCase(); // Obtiene el nombre del archivo en minúsculas
        for (String ext : imageExtensions) {
            if (fileName.endsWith(ext)) { // Comprueba si el nombre del archivo tiene una de las extensiones admitidas
                return true; // Devuelve verdadero si el archivo es una imagen
            }
        }
        return false; // Devuelve falso si el archivo no es una imagen
    }

    // Método para enviar la imagen al servidor
    private void sendImageToServer(File file) {
        try {
            if (socket == null || socket.isClosed()) { // Verifica si el socket está cerrado o no inicializado
                connectToServer(); // Conecta al cliente con el servidor
            }
            
            BufferedImage image = ImageIO.read(file); // Lee la imagen del archivo
            if (image != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream(); // Crea un flujo de salida de bytes en memoria
                ImageIO.write(image, "png", baos); // Escribe la imagen en formato PNG en el flujo de salida
                byte[] imageBytes = baos.toByteArray(); // Convierte la imagen en bytes

                OutputStream outputStream = socket.getOutputStream(); // Obtiene el flujo de salida del socket
                DataOutputStream dos = new DataOutputStream(outputStream); // Crea un flujo de salida de datos sobre el flujo de salida del socket
                dos.writeInt(imageBytes.length); // Escribe la longitud de los datos de imagen en el flujo de salida
                dos.write(imageBytes); // Escribe los datos de la imagen en el flujo de salida
                dos.flush(); // Vacía el búfer de salida
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para conectar al cliente con el servidor
    private void connectToServer() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT); // Crea un nuevo socket y lo conecta al servidor
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se puede conectar al servidor. Asegúrese de que el servidor está ejecutándose.", "Error de Conexión", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error si no se puede conectar al servidor
            System.exit(1); // Sale del programa con un código de error
        }
    }

    public static void main(String[] args) {
        new ImagenCliente(); // Crea una instancia del cliente de imágenes
    }
}





