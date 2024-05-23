package Cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class DibujarCliente extends JFrame {
    private static PrintWriter out;
    private static DibujoArea AreaDibujo;

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Dibujo Cliente");
        AreaDibujo = new DibujoArea();
        frame.add(AreaDibujo, BorderLayout.CENTER);
        
        JButton Borrar = new JButton("Borrar Todo");
        Borrar.addActionListener(e -> out.println("BORRAR"));
        frame.add(Borrar, BorderLayout.SOUTH);

        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        Socket socket = new Socket("localhost", 9000);
        out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        new Thread(() -> {
            String mensaje;
            try {
                while ((mensaje = in.readLine()) != null) {
                    if (mensaje.equals("CLEAR")) {
                        AreaDibujo.Borreishon();
                    } else {
                        String[] parts = mensaje.split(",");
                        int x = Integer.parseInt(parts[0]);
                        int y = Integer.parseInt(parts[1]);
                        int x2 = Integer.parseInt(parts[2]);
                        int y2 = Integer.parseInt(parts[3]);
                        Color color = new Color(Integer.parseInt(parts[4]));
                        AreaDibujo.LineaDibujo(x, y, x2, y2, color);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
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
                    LineaDibujo(x, y, x2, y2, Color.BLUE);
                    String message = x + "," + y + "," + x2 + "," + y2 + "," + Color.BLUE.getRGB();
                    out.println(message);
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

        public void Borreishon() {
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
        }
    }
}

