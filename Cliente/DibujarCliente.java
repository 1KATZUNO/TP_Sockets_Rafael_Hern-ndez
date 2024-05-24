package Cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class DibujarCliente extends JFrame {
    private static PrintWriter out; // Para enviar datos al servidor
    private DibujoArea AreaDibujo; // El área donde se dibuja

    public DibujarCliente() {
        setTitle("Dibujo Cliente"); // Establece el título del marco
        AreaDibujo = new DibujoArea(); // Crea el área de dibujo
        add(AreaDibujo, BorderLayout.CENTER); // Agrega el área de dibujo al centro del marco
        setLocationRelativeTo(null); // Centra el marco en la pantalla

        // Panel para botones
        JPanel panel = new JPanel();

        // Botón para borrar todo el dibujo
        JButton borrarButton = new JButton("Borrar Todo");
        borrarButton.addActionListener(e -> out.println("BORRAR")); // Envía un comando de borrar al servidor
        panel.add(borrarButton);

        // Botón para regresar al menú principal del cliente
        JButton regresarButton = new JButton("Regresar");
        regresarButton.addActionListener(e -> {
            GUI_Cliente g = new GUI_Cliente();
            g.setVisible(true);
            dispose(); // Cierra el marco actual
        });
        panel.add(regresarButton);

        add(panel, BorderLayout.SOUTH); // Agrega el panel de botones en la parte inferior del marco

        setSize(800, 600); // Establece el tamaño del marco
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Finaliza el programa cuando se cierra el marco
        setVisible(true); // Hace visible el marco

        try {
            // Establece una conexión con el servidor
            Socket socket = new Socket("localhost", 12349);
            // Crea un escritor para enviar datos al servidor
            out = new PrintWriter(socket.getOutputStream(), true);
            // Crea un lector para recibir datos del servidor
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Inicia un hilo para recibir mensajes del servidor
            new Thread(() -> {
                String mensaje;
                try {
                    // Lee los mensajes del servidor
                    while ((mensaje = in.readLine()) != null) {
                        if (mensaje.equals("BORRAR")) {
                            AreaDibujo.Limpiar(); // Llama al método para limpiar el dibujo
                        } else {
                            String[] parts = mensaje.split(",");
                            // Extrae los datos del mensaje y dibuja una línea en el área de dibujo
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

    // Clase interna que representa el área de dibujo personalizada
    private class DibujoArea extends JPanel {
        private int x, y; // Coordenadas del último punto

        public DibujoArea() {
            setDoubleBuffered(false); // Desactiva el doble búfer para evitar problemas de dibujo
            setBackground(Color.WHITE); // Establece el color de fondo del área de dibujo
            // Maneja el evento de presionar el ratón para iniciar una nueva línea
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    x = e.getX();
                    y = e.getY();
                }
            });
            // Maneja el evento de arrastrar el ratón para dibujar una línea
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    int x2 = e.getX();
                    int y2 = e.getY();
                    // Dibuja una línea entre el punto actual y el anterior
                    LineaDibujo(x, y, x2, y2, Color.BLUE);
                    // Envía las coordenadas y el color al servidor
                    String message = x + "," + y + "," + x2 + "," + y2 + "," + Color.BLUE.getRGB();
                    out.println(message);
                    // Actualiza las coordenadas del último punto
                    x = x2;
                    y = y2;
                }
            });
        }

        // Método para dibujar una línea en el área de dibujo
        public void LineaDibujo(int x, int y, int x2, int y2, Color color) {
            Graphics g = getGraphics();
            g.setColor(color);
            g.drawLine(x, y, x2, y2);
            g.dispose();
        }

        // Método para limpiar el dibujo en el área de dibujo
        public void Limpiar() {
            repaint(); // Vuelve a pintar el área de dibujo para borrar todo el contenido
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // Llama al método de la clase base para pintar los componentes
        }
    }

    public static void main(String[] args) {
        // Método principal, crea y muestra el marco del cliente de dibujo
        SwingUtilities.invokeLater(DibujarCliente::new);
    }
}
