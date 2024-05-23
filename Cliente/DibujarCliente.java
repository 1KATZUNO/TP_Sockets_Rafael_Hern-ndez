package Cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class DibujarCliente extends JFrame {
    private static PrintWriter out;
    private DibujoArea AreaDibujo;

    public DibujarCliente() {
        setTitle("Dibujo Cliente");
        AreaDibujo = new DibujoArea();
        add(AreaDibujo, BorderLayout.CENTER);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JButton borrarButton = new JButton("Borrar Todo");
        borrarButton.addActionListener(e -> out.println("BORRAR"));
        panel.add(borrarButton);

        JButton regresarButton = new JButton("Regresar");
        regresarButton.addActionListener(e -> {
            GUI_Cliente g = new GUI_Cliente();
         g.setVisible(true);
         dispose();
        });
        panel.add(regresarButton);

        add(panel, BorderLayout.SOUTH);

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        try {
            Socket socket = new Socket("localhost", 12349);
            out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(() -> {
                String mensaje;
                try {
                    while ((mensaje = in.readLine()) != null) {
                        if (mensaje.equals("BORRAR")) {
                            AreaDibujo.Limpiar();
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
        } catch (IOException e) {
            e.printStackTrace();
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

        public void Limpiar() {
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DibujarCliente::new);
    }
}
