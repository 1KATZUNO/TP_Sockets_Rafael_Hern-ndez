package Cliente;

import javax.swing.*;

import Servidor.ImagenServidor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Cliente {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Cliente");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);

        JButton button = new JButton("Iniciar Servidor de Imagen");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {
                    ImagenServidor servidor = new ImagenServidor();
                    servidor.iniciarServidor();
                }).start();
            }
        });

        frame.getContentPane().add(button, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
