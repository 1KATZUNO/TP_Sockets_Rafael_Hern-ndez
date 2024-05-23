package Cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

public class JuegoCliente extends JFrame {
    private JButton[][] Botones = new JButton[3][3];
    private boolean[][] BotonSeleciionado = new boolean[3][3];
    private PrintWriter out;
    private BufferedReader in;
    private boolean Turno = false;

    public JuegoCliente() {
        setTitle("Tic Tac Toe - Client");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 3));

        try {
            Socket socket = new Socket("localhost", 5000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            IniciarBotones();
            ListaBotones();
            ReiniciarJuego();

            new Thread(new IncomingReader()).start(); // Thread para leer mensajes del servidor

            pack();
            setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void IniciarBotones() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(100, 100));
                Botones[i][j] = button;
                add(button);
            }
        }
    }

    private void ListaBotones() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton button = Botones[i][j];
                final int row = i;
                final int col = j;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (Turno && !BotonSeleciionado[row][col]) {
                            BotonSeleciionado[row][col] = true;
                            button.setBackground(Color.BLUE);
                            button.setEnabled(false); // Desactivar el botón después de seleccionarlo
                            out.println(row + " " + col); // Enviar coordenadas al servidor
                            Turno = false; // Cambiar el turno después de hacer un movimiento
                        }
                    }
                });
            }
        }
    }

    public void ColorBotonActualizar(int row, int col, Color color) {
        Botones[row][col].setBackground(color); // Cambiar el color del botón a azul o rojo
        Botones[row][col].setEnabled(false); // Desactivar el botón
        BotonSeleciionado[row][col] = true;
    }

    public void Ganador(String winner) {
        if (winner.equals("Empate")) {
            JOptionPane.showMessageDialog(this, "Empate");
        } else if (winner.equals("Cliente") || winner.equals("Servidor")) {
            JOptionPane.showMessageDialog(this, "Ganador: " + winner);
        } else if (winner.equals("restart")) {
            ReiniciarJuego();
        } else {
            JOptionPane.showMessageDialog(this, "Ganador: No especificado");
        }
    }

    public void ReiniciarJuego() {
        Turno = true; // Establecer que es el turno del cliente al reiniciar el juego
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Botones[i][j].setBackground(null);
                Botones[i][j].setEnabled(true);
                BotonSeleciionado[i][j] = false;
            }
        }
    }

    public class IncomingReader implements Runnable {
        public void run() {
            try {
                String input;
                while ((input = in.readLine()) != null) {
                    String[] tokens = input.split(" ");
                    if (tokens.length == 1) { // Mensaje de ganador
                        Ganador(tokens[0]);
                    } else { // Coordenadas del movimiento
                        int Fila = Integer.parseInt(tokens[0]);
                        int Columna = Integer.parseInt(tokens[1]);
                        ColorBotonActualizar(Fila, Columna, Color.RED); // Actualizar botón recibido del servidor
                        Turno = true; // Cambiar al turno del cliente después de recibir el movimiento del servidor
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(JuegoCliente::new);
    }
}
