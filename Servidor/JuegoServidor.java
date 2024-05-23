package Servidor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class JuegoServidor extends JFrame {
    private JButton[][] Botones = new JButton[3][3];
    private boolean[][] BotonSelecionado = new boolean[3][3];
    private PrintWriter out;
    private BufferedReader in;
    private boolean GanadorJuego = false;
    private boolean Turno = false; // Indica si es el turno del cliente

    public JuegoServidor() {
        setTitle("Tic Tac Toe - Server");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 3));

        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Servidor Iniciado. Esperando cliente...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado.");
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            IniciarBotones();
            ListaBotones();

            new Thread(new IncomingReader()).start(); // Thread para leer mensajes del cliente

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
                        if (Turno && !BotonSelecionado[row][col] && !GanadorJuego) {
                            BotonSelecionado[row][col] = true;
                            button.setBackground(Color.RED);
                            out.println(row + " " + col); // Enviar coordenadas al cliente
                            Ganador();
                            RevisarEmpate();
                            Turno = false; // Cambiar turno al servidor
                        }
                    }
                });
            }
        }
    }
private void RevisarEmpate() {
    // Verificar si todos los botones están seleccionados, en cuyo caso hay un empate
    boolean BotonesSeleccionados = true;
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            if (!BotonSelecionado[i][j]) {
                BotonesSeleccionados = false;
                break;
            }
        }
        if (!BotonesSeleccionados) {
            break;
        }
    }
    if (BotonesSeleccionados && !GanadorJuego) {
        AnunciarGanador("Empate");
    }
}

    private void Ganador() {
    // Verificar si hay un ganador en filas
    
    
    

    // Verificar si hay un ganador en filas para el cliente
    for (int i = 0; i < 3; i++) {
        if (Botones[i][0].getBackground().equals(Color.BLUE) &&
            Botones[i][1].getBackground().equals(Color.BLUE) &&
            Botones[i][2].getBackground().equals(Color.BLUE)) {
            GanadorJuego = true;
            AnunciarGanador("Cliente");
            return;
        }
    }

    // Verificar si hay un ganador en columnas para el cliente
    for (int j = 0; j < 3; j++) {
        if (Botones[0][j].getBackground().equals(Color.BLUE) &&
            Botones[1][j].getBackground().equals(Color.BLUE) &&
            Botones[2][j].getBackground().equals(Color.BLUE)) {
            GanadorJuego = true;
            AnunciarGanador("Cliente");
            return;
        }
    }

    // Verificar si hay un ganador en la diagonal principal para el cliente
    if (Botones[0][0].getBackground().equals(Color.BLUE) &&
        Botones[1][1].getBackground().equals(Color.BLUE) &&
        Botones[2][2].getBackground().equals(Color.BLUE)) {
        GanadorJuego = true;
        AnunciarGanador("Cliente");
        return;
    }

    // Verificar si hay un ganador en la diagonal secundaria para el cliente
    if (Botones[0][2].getBackground().equals(Color.BLUE) &&
        Botones[1][1].getBackground().equals(Color.BLUE) &&
        Botones[2][0].getBackground().equals(Color.BLUE)) {
        GanadorJuego = true;
        AnunciarGanador("Cliente");
        return;
    }
    for (int i = 0; i < 3; i++) {
        if (Botones[i][0].getBackground().equals(Color.RED) &&
            Botones[i][1].getBackground().equals(Color.RED) &&
            Botones[i][2].getBackground().equals(Color.RED)) {
            GanadorJuego = true;
            AnunciarGanador("Servidor");
            return;
        }
    }

    // Verificar si hay un ganador en columnas
    for (int j = 0; j < 3; j++) {
        if (Botones[0][j].getBackground().equals(Color.RED) &&
            Botones[1][j].getBackground().equals(Color.RED) &&
            Botones[2][j].getBackground().equals(Color.RED)) {
            GanadorJuego = true;
            AnunciarGanador("Servidor");
            return;
        }
    }

    // Verificar si hay un ganador en la diagonal principal
    if (Botones[0][0].getBackground().equals(Color.RED) &&
        Botones[1][1].getBackground().equals(Color.RED) &&
        Botones[2][2].getBackground().equals(Color.RED)) {
        GanadorJuego = true;
        AnunciarGanador("Servidor");
        return;
    }

    // Verificar si hay un ganador en la diagonal secundaria
    if (Botones[0][2].getBackground().equals(Color.RED) &&
        Botones[1][1].getBackground().equals(Color.RED) &&
        Botones[2][0].getBackground().equals(Color.RED)) {
        GanadorJuego = true;
        AnunciarGanador("Servidor");
        return;
    }



    // Verificar si todos los botones están seleccionados, en cuyo caso hay un empate
    boolean BotonesSeleccionados = true;
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            if (!BotonSelecionado[i][j]) {
                BotonesSeleccionados = false;
                break;
            }
        }
        if (!BotonesSeleccionados) {
            break;
        }
    }
    if (BotonesSeleccionados) {
        AnunciarGanador("Empate");
    }

}
    private void AnunciarGanador(String winner) {
        if (winner.equals("Empate")) {
            JOptionPane.showMessageDialog(this, "Empate");
        } else {
            JOptionPane.showMessageDialog(this, "Ganador: " + winner);
        }
        ReiniciarJuego();
    }

    private void ReiniciarJuego() {
        // Reiniciar el estado del juego
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Botones[i][j].setBackground(null);
                BotonSelecionado[i][j] = false;
            }
        }
        GanadorJuego = false;
        Turno = true; // Establecer el turno del cliente
        out.println("restart"); // Enviar mensaje de reinicio al cliente
    }

    public void ColorBotonActualizar(int row, int col, Color color) {
        Botones[row][col].setBackground(color);
        BotonSelecionado[row][col] = true;
    }

    public void setTurnoCliente(boolean turn) {
        Turno = turn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(JuegoServidor::new);
    }

public class IncomingReader implements Runnable {
    public void run() {
        try {
            String input;
            while ((input = in.readLine()) != null) {
                String[] tokens = input.split(" ");
                if (tokens.length == 1 && tokens[0].equals("Cliente")) {
                    AnunciarGanador("Cliente");
                    return;
                } else {
                    int row = Integer.parseInt(tokens[0]);
                    int col = Integer.parseInt(tokens[1]);
                    ColorBotonActualizar(row, col, Color.BLUE); // Actualizar botón recibido del cliente
                    RevisarEmpate();
                    setTurnoCliente(true); // Cambiar turno al cliente
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }
}
