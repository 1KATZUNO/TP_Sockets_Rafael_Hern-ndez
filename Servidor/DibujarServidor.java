package Servidor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class DibujarServidor extends JFrame {
    private static Set<PrintWriter> clientWriters = new HashSet<>();
    private DibujoArea AreaDibujo;

    public DibujarServidor() {
        setTitle("Dibujo Servidor");
        AreaDibujo = new DibujoArea();
        add(AreaDibujo, BorderLayout.CENTER);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JButton clearButton = new JButton("Borrar Todo");
        clearButton.addActionListener(e -> {
            AreaDibujo.Limpiar();
            broadcast("BORRAR");
        });
        panel.add(clearButton);

        JButton regresarButton = new JButton("Regresar");
        regresarButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Recuerda salir antes con Cliente");
            GUI_Servidor g = new GUI_Servidor();
            g.setVisible(true);
            dispose();
        });
        panel.add(regresarButton);

        add(panel, BorderLayout.SOUTH);

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(12349)) {
                System.out.println("Servidor escuchando en puerto 12349");

                while (true) {
                    Socket socket = serverSocket.accept();
                    new Thread(new ManejadorCliente(socket)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void broadcast(String mensaje) {
        synchronized (clientWriters) {
            for (PrintWriter writer : clientWriters) {
                writer.println(mensaje);
            }
        }
    }

    private class ManejadorCliente implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ManejadorCliente(Socket socket) {
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

                String mensaje;
                while ((mensaje = in.readLine()) != null) {
                    if (mensaje.equals("BORRAR")) {
                        AreaDibujo.Limpiar();
                    } else {
                        broadcast(mensaje);
                        procesarMensaje(mensaje);
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

        private void procesarMensaje(String mensaje) {
            String[] parts = mensaje.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            int x2 = Integer.parseInt(parts[2]);
            int y2 = Integer.parseInt(parts[3]);
            Color color = new Color(Integer.parseInt(parts[4]));
            AreaDibujo.LineaDibujo(x, y, x2, y2, color);
        }
    }

    private class DibujoArea extends JPanel {
        private int x, y;

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
                    int x2 = e.getX();
                    int y2 = e.getY();
                    LineaDibujo(x, y, x2, y2, Color.RED);
                    String message = x + "," + y + "," + x2 + "," + y2 + "," + Color.RED.getRGB();
                    broadcast(message);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DibujarServidor::new);
    }
}


