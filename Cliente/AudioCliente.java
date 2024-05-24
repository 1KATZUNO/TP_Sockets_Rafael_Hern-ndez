package Cliente;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AudioCliente extends JFrame {

    private boolean Grabador;
    private ByteArrayOutputStream out;

    public AudioCliente() {
        setTitle("Cliente de Audio");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Creación de botones
        JButton recordButton = new JButton("Grabar");
        JButton stopButton = new JButton("Detener");
        JButton regresarButton = new JButton("Regresar");

        // Acción al hacer clic en el botón de grabar
        recordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EmpezarGrabar();
            }
        });

        // Acción al hacer clic en el botón de detener
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PararGrabar();
                EnvioAudio("localhost", 12346); // Debes recordar cambiar el puerto si es necesario
            }
        });

        // Acción al hacer clic en el botón de regresar
        regresarButton.addActionListener(e -> {
            GUI_Cliente g = new GUI_Cliente();
            g.setVisible(true);
            dispose(); // Cierra la ventana actual
        });

        // Configuración del panel
        JPanel panel = new JPanel();
        panel.add(recordButton);
        panel.add(stopButton);
        panel.add(regresarButton);
        add(panel); // Añade el panel a la ventana

        setVisible(true); // Hace visible la ventana
    }

    // Método para iniciar la grabación de audio
    private void EmpezarGrabar() {
        Grabador = true;
        new Thread(() -> {
            try {
                // Configuración del formato de audio
                AudioFormat format = new AudioFormat(44100, 16, 2, true, true);
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(format);
                line.start();

                out = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                // Bucle para grabar audio mientras el botón de detener no se haya presionado
                while (Grabador) {
                    int count = line.read(buffer, 0, buffer.length);
                    if (count > 0) {
                        out.write(buffer, 0, count);
                    }
                }
                line.close();
                out.close();
            } catch (LineUnavailableException | IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    // Método para detener la grabación de audio
    private void PararGrabar() {
        Grabador = false;
    }

    // Método para enviar audio al servidor
    private void EnvioAudio(String SERVER_ADDRESS, int SERVER_PORT) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
            oos.writeObject(out.toByteArray()); // Envía los datos de audio al servidor
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método principal
    public static void main(String[] args) {
        // Inicia la aplicación
        SwingUtilities.invokeLater(AudioCliente::new);
    }
}
