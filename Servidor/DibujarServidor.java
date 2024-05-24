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
        add(AreaDibujo, BorderLayout.CENTER); // Agrega el área de dibujo al centro del marco
        setLocationRelativeTo(null); // Centra el marco en la pantalla

        // Panel para botones
        JPanel panel = new JPanel();

        // Botón para borrar todo el dibujo
        JButton clearButton = new JButton("Borrar Todo");
        clearButton.addActionListener(e -> {
            AreaDibujo.Limpiar(); // Llama al método para limpiar el dibujo
            broadcast("BORRAR"); // Envia el comando de borrar a todos los clientes
        });
        panel.add(clearButton);

        // Botón para regresar al menú principal del servidor
        JButton regresarButton = new JButton("Regresar");
        regresarButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Recuerda salir antes con Cliente");
            GUI_Servidor g = new GUI_Servidor();
            g.setVisible(true);
            dispose(); // Cierra el marco actual
        });
        panel.add(regresarButton);

        add(panel, BorderLayout.SOUTH); // Agrega el panel de botones en la parte inferior del marco

        setSize(800, 600); // Establece el tamaño del marco
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Finaliza el programa cuando se cierra el marco
        setVisible(true); // Hace visible el marco

        // Inicia un hilo para manejar las conexiones de los clientes
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(12349)) {
                System.out.println("Servidor escuchando en puerto 12349");

                while (true) {
                    Socket socket = serverSocket.accept(); // Acepta nuevas conexiones de clientes
                    new Thread(new ManejadorCliente(socket)).start(); // Inicia un hilo para manejar cada cliente
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Método para enviar un mensaje a todos los clientes conectados
    private void broadcast(String mensaje) {
        synchronized (clientWriters) {
            for (PrintWriter writer : clientWriters) {
                writer.println(mensaje);
            }
        }
    }

    // Clase interna para manejar cada conexión de cliente en un hilo separado
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
                    clientWriters.add(out); // Agrega el escritor del cliente al conjunto de escritores
                }

                String mensaje;
                while ((mensaje = in.readLine()) != null) {
                    if (mensaje.equals("BORRAR")) {
                        AreaDibujo.Limpiar(); // Llama al método para limpiar el dibujo en todos los clientes
                    } else {
                        broadcast(mensaje); // Envia el mensaje a todos los clientes conectados
                        procesarMensaje(mensaje); // Procesa el mensaje para dibujar en el área de dibujo
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close(); // Cierra el socket del cliente
                } catch (IOException e) {
                    e.printStackTrace();
                }
                synchronized (clientWriters) {
                    clientWriters.remove(out); // Elimina el escritor del cliente del conjunto de escritores
                }
            }
        }

        // Método para procesar un mensaje y dibujar en el área de dibujo
        private void procesarMensaje(String mensaje) {
            String[] parts = mensaje.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            int x2 = Integer.parseInt(parts[2]);
            int y2 = Integer.parseInt(parts[3]);
            Color color = new Color(Integer.parseInt(parts[4]));
            AreaDibujo.LineaDibujo(x, y, x2, y2, color); // Dibuja la línea en el área de dibujo
        }
    }

    // Clase interna que representa el área de dibujo personalizada
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
                    LineaDibujo(x, y, x2, y2, Color.RED); // Dibuja la línea en el área de dibujo
                    String message = x + "," + y + "," + x2 + "," + y2 + "," + Color.RED.getRGB();
                    broadcast(message); // Envia el mensaje a todos los clientes conectados
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
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        // Método principal, crea y muestra el marco del servidor de dibujo
        SwingUtilities.invokeLater(DibujarServidor::new);
    }
}
