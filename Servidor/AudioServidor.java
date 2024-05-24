package Servidor;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class AudioServidor extends JFrame {

    private JButton BotonPlay;
    private JButton BotonRegresar;
    private boolean AudioRecibir;
    private byte[] RecibirAudioData;

    public AudioServidor() {
        // Configuración de la ventana
        setTitle("Servidor de Audio");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Creación de botones
        BotonPlay = new JButton("Reproducir");
        BotonPlay.setEnabled(false);

        // Acción al hacer clic en el botón de reproducir
        BotonPlay.addActionListener(e -> {
            if (AudioRecibir) {
                playAudio(); // Llama al método para reproducir el audio
            }
        });

        // Botón para regresar
        BotonRegresar = new JButton("Regresar");
        BotonRegresar.addActionListener(e -> {
            // Mensaje al intentar regresar
            JOptionPane.showMessageDialog(null, "Recuerda salir antes con Cliente");
            // Vuelve a la interfaz de servidor
            GUI_Servidor g = new GUI_Servidor();
            g.setVisible(true);
            dispose(); // Cierra la ventana actual
        });

        // Configuración del panel
        JPanel panel = new JPanel();
        panel.add(BotonPlay);
        panel.add(BotonRegresar);
        add(panel); // Añade el panel a la ventana

        setVisible(true); // Hace visible la ventana

        // Hilo para recibir audio del cliente
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(12346)) {
                while (true) {
                    try (Socket socket = serverSocket.accept();
                         ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
                        // Lee el audio recibido y habilita el botón de reproducir
                        RecibirAudioData = (byte[]) ois.readObject();
                        AudioRecibir = true;
                        BotonPlay.setEnabled(true);
                    } catch (IOException | ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }).start(); // Inicia el hilo para la recepción de audio
    }

    // Método para reproducir el audio recibido
    private void playAudio() {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(RecibirAudioData)) {
            // Configuración del formato de audio
            AudioFormat format = new AudioFormat(44100, 16, 2, true, true);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bais.read(buffer)) != -1) {
                line.write(buffer, 0, bytesRead); // Reproduce el audio
            }

            line.drain();
            line.close();
        } catch (LineUnavailableException | IOException ex) {
            ex.printStackTrace();
        }
    }

    // Método principal
    public static void main(String[] args) {
        // Inicia la aplicación
        SwingUtilities.invokeLater(AudioServidor::new);
    }
}

//192.168.100.7