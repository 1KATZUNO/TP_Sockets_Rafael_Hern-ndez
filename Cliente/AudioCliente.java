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
        setSize(300, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton recordButton = new JButton("Grabar");
        JButton stopButton = new JButton("Detener");

        recordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EmpezarGrabar();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PararGrabar();
                EnvioAudio("localhost", 12346); // Acordarse de poner otros puerts
            }
        });

        JPanel panel = new JPanel();
        panel.add(recordButton);
        panel.add(stopButton);
        add(panel);

        setVisible(true);
    }

    private void EmpezarGrabar() {
        Grabador = true;
        new Thread(() -> {
            try {
                AudioFormat format = new AudioFormat(44100, 16, 2, true, true);
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(format);
                line.start();

                out = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
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

    private void PararGrabar() {
        Grabador = false;
    }

    private void EnvioAudio(String SERVER_ADDRESS, int SERVER_PORT) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
            oos.writeObject(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AudioCliente::new);
    }
}
