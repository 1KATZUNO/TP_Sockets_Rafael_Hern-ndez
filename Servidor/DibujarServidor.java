package Servidor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class DibujarServidor {
    private static Set<PrintWriter> clientWriters = new HashSet<>();
    private static JFrame frame;
    private static DibujoArea AreaDibujo;

    public static void main(String[] args) throws IOException {
        frame = new JFrame("Dibujo Servidor");
        AreaDibujo = new DibujoArea();
        frame.add(AreaDibujo, BorderLayout.CENTER);
        
        JButton clearButton = new JButton("Borrar Todo");
        clearButton.addActionListener(e -> {
            AreaDibujo.Limpiar();
            broadcast("Borrar");
        });
        frame.add(clearButton, BorderLayout.SOUTH);

        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        ServerSocket serverSocket = new ServerSocket(9000);
        System.out.println("Servidor escuchando en puerto 9000");

        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(new MenejadorCliente(socket)).start();
        }
    }

    private static void broadcast(String Mensaje) {
        synchronized (clientWriters) {
            for (PrintWriter writer : clientWriters) {
                writer.println(Mensaje);
            }
        }
    }

    private static class MenejadorCliente implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public MenejadorCliente(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                synchronized (clientWriters) {
                    clientWriters.add(out);
                }

                String Mensajee;
                while ((Mensajee = in.readLine()) != null) {
                    if (Mensajee.equals("CLEAR")) {
                        AreaDibujo.Limpiar();
                    } else {
                        synchronized (clientWriters) {
                            for (PrintWriter writer : clientWriters) {
                                writer.println(Mensajee);
                            }
                        }
                        ProcesoMensaje(Mensajee);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                synchronized (clientWriters) {
                    clientWriters.remove(out);
                }
            }
        }

        private void ProcesoMensaje(String Mensaje) {
            String[] parts = Mensaje.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            int x2 = Integer.parseInt(parts[2]);
            int y2 = Integer.parseInt(parts[3]);
            Color color = new Color(Integer.parseInt(parts[4]));
            AreaDibujo.LineaDibujo(x, y, x2, y2, color);
        }
    }

    static class DibujoArea extends JPanel {
        private int x, y, x2, y2;

        public DibujoArea() {
            setDoubleBuffered(false);
            setBackground(Color.WHITE);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    x = e.getX();
                    y = e.getY();
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    x2 = e.getX();
                    y2 = e.getY();
                    LineaDibujo(x, y, x2, y2, Color.RED);
                    String message = x + "," + y + "," + x2 + "," + y2 + "," + Color.RED.getRGB();
                    synchronized (clientWriters) {
                        for (PrintWriter writer : clientWriters) {
                            writer.println(message);
                        }
                    }
                    x = x2;
                    y = y2;
                }
            });
        }

        public void LineaDibujo(int x, int y, int x2, int y2, Color color) {
            Graphics g = getGraphics();
            g.setColor(color);
            g.drawLine(x, y, x2, y2);
            g.dispose();
        }

        public void Limpiar() {
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
        }
    }
}