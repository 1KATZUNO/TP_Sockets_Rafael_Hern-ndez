package Cliente;

import javax.swing.*;
import Servidor.GUI_Servidor;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.net.*;
import java.util.List;

public class ArchivoCliente extends JFrame {
    // Área de texto para mostrar la lista de archivos que se están enviando
    private JTextArea txtArchivos;
    // Botón para regresar a la GUI principal del cliente
    private JButton btnRegresar;

    public ArchivoCliente() {
        // Establecer el título de la ventana
        setTitle("Transferencia de Archivos");
        // Establecer la operación de cierre predeterminada para salir de la aplicación
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Establecer el tamaño de la ventana
        setSize(400, 250);
        // Centrar la ventana en la pantalla
        setLocationRelativeTo(null);

        // Crear un panel para contener los componentes
        JPanel panel = new JPanel();
        // Establecer el diseño del panel en un diseño de borde
        panel.setLayout(new BorderLayout());

        // Crear una etiqueta para instruir al usuario a arrastrar y soltar archivos
        JLabel lbArrastra = new JLabel("Arrastra y suelta archivos aquí:");
        // Crear un área de texto para mostrar la lista de archivos que se están enviando
        txtArchivos = new JTextArea();
        // Establecer el área de texto para que no sea editable
        txtArchivos.setEditable(false);
        // Crear un panel de desplazamiento para contener el área de texto
        JScrollPane scrollPane = new JScrollPane(txtArchivos);

        // Crear un botón para regresar a la GUI principal del cliente
        btnRegresar = new JButton("Regresar");
        // Agregar un listener de acción al botón
        btnRegresar.addActionListener(e -> {
            // Crear una nueva instancia de la GUI principal del cliente
            GUI_Cliente g = new GUI_Cliente();
            // Hacer la nueva GUI visible
            g.setVisible(true);
            // Eliminar la ventana actual
            dispose();
        });

        // Agregar los componentes al panel
        panel.add(lbArrastra, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnRegresar, BorderLayout.SOUTH);

        // Agregar el panel a la ventana
        getContentPane().add(panel);

        // Establecer un controlador de transferencia para manejar los eventos de arrastrar y soltar
        panel.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                // Verificar si los datos que se están transfiriendo son una lista de archivos
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            @Override
            public boolean importData(TransferSupport support) {
                // Verificar si los datos que se están transfiriendo son una lista de archivos
                if (!canImport(support)) {
                    return false;
                }

                // Obtener los datos transferibles
                Transferable transferable = support.getTransferable();
                try {
                    // Obtener la lista de archivos que se están transfiriendo
                    List<File> fileList = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                    // Iterar sobre la lista de archivos
                    for (File file : fileList) {
                        // Agregar el nombre del archivo al área de texto
                        txtArchivos.append("Enviando archivo: " + file.getName() + "\n");
                        // Enviar el archivo al servidor
                        EnviarArchivo(file);
                    }
                    return true;
                } catch (Exception ex) {
                    // Imprimir el seguimiento de la pila de la excepción
                    ex.printStackTrace();
                    return false;
                }
            }
        });

        // Hacer la ventana visible
        setVisible(true);
    }

    // Método para enviar un archivo al servidor
    private void EnviarArchivo(File file) {
        try (Socket socket = new Socket("localhost", 12350);
             OutputStream outputStream = socket.getOutputStream();
             FileInputStream fileInputStream = new FileInputStream(file);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
             PrintWriter writer = new PrintWriter(outputStream, true)) {
            // Crear un búfer para leer el archivo
            byte[] buffer = new byte[8192];
            int bytesRead;
            // Leer el archivo y enviarlo al servidor
            while ((bytesRead = bufferedInputStream.read(buffer))!= -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            // Enviar un mensaje al servidor para indicar el final del archivo
            writer.println("FIN");
        } catch (IOException ex) {
            // Imprimir el seguimiento de la pila de la excepción
            ex.printStackTrace();
            //Mostrar un mensaje de error al usuario
            JOptionPane.showMessageDialog(null, "Error enviando archivo al servidor.");
        }
    }

    public static void main(String[] args) {
        // Crear una nueva instancia de la GUI en el hilo de eventos
        SwingUtilities.invokeLater(() -> new ArchivoCliente());
    }
}
