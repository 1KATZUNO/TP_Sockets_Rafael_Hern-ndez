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
    private boolean AudioRecibir;
    private byte[] RecibirAudioData;

    public AudioServidor() {
        setTitle("Servidor de Audio");
        setSize(300, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BotonPlay = new JButton("Reproducir");
        BotonPlay.setEnabled(false);

        BotonPlay.addActionListener(e -> {
            if (AudioRecibir) {
                playAudio();
            }
        });

        add(BotonPlay);
        setVisible(true);

        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(12346)) {
                while (true) {
                    try (Socket socket = serverSocket.accept();
                         ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
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
        }).start();
    }
// JOptionPane
    private void playAudio() {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(RecibirAudioData)) {
            AudioFormat format = new AudioFormat(44100, 16, 2, true, true);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bais.read(buffer)) != -1) {
                line.write(buffer, 0, bytesRead);
            }

            line.drain();
            line.close();
        } catch (LineUnavailableException | IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AudioServidor::new);
    }
}
//192.168.100.7