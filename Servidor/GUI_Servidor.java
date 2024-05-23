package Servidor;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GUI_Servidor extends JFrame{
    private javax.swing.JPanel PanelPrincipal; 
    private javax.swing.JButton btnFuncion1;
    private javax.swing.JButton btnAudios;
    private javax.swing.JButton btnImagenes;
    private javax.swing.JButton btnJuegos;
    private javax.swing.JButton btnDibujar;
    private javax.swing.JButton btnArchivos;
    private javax.swing.JLabel lbBienvenute;



public GUI_Servidor(){
    setLocationRelativeTo(null);
initComponents();



}


// initComponents es todo lo que va a ser la ventana, y se guarda en un apartado con ese nombre 
// igual al de netbeans, para no olvidar su función
private void initComponents(){
    // se le pone por defecto el exit and close(pa abrir y cerrar)

    // to esto son los nombres de los botones y el label
    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    PanelPrincipal = new javax.swing.JPanel();
    btnFuncion1 = new javax.swing.JButton();
    btnFuncion1.setText("Chat");
    btnAudios = new javax.swing.JButton();
    btnAudios.setText("Audios");
    btnImagenes = new javax.swing.JButton();
    btnImagenes.setText("Envío Imagenes");
    btnJuegos = new javax.swing.JButton();
    btnJuegos.setText("Juego");
    btnDibujar = new javax.swing.JButton();
    btnDibujar.setText("Dibujar");
    btnArchivos = new javax.swing.JButton();
    btnArchivos.setText("Recibir Files");
    lbBienvenute = new javax.swing.JLabel();
    lbBienvenute.setText("Bienvenido a Servidor");


// to esto es para la funcion de los botones
    btnFuncion1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnFuncion1ActionPerformed(evt);
        }
    });
    btnAudios.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnAudiosActionPerformed(evt);
        }
    });
    btnImagenes.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnImagenesActionPerformed(evt);
        }
    });
    btnJuegos.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnJuegosActionPerformed(evt);
        }
    });
    btnDibujar.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnDibujarActionPerformed(evt);
        }
    });
    btnArchivos.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnArchivosActionPerformed(evt);
        }
    });

//ACÁ INICIAMOS CONFIGURANDO EL FRAME

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
// permite tener un control preciso sobre la disposición de los componentes dentro de un contenedor
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 400, Short.MAX_VALUE)
        .addComponent(PanelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    // alineación de componentes horizontalmente
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 300, Short.MAX_VALUE)
        .addComponent(PanelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    // y acá pues para alinearlos pero de una manera vertical


//ACÁ PONEMOS EL PANEL PRINCIPAL Y SU ALINEACIÓN

    javax.swing.GroupLayout PanelPrincipalLayout = new javax.swing.GroupLayout(PanelPrincipal);
    PanelPrincipal.setLayout(PanelPrincipalLayout);
    PanelPrincipalLayout.setHorizontalGroup(
        PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(PanelPrincipalLayout.createSequentialGroup()
        .addGap(26, 26, 26)
        .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
            .addComponent(btnJuegos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnImagenes, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
            .addComponent(btnAudios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnFuncion1, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
        .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
            .addComponent(btnDibujar, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
            .addComponent(btnArchivos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addGap(28, 28, 28))
    .addGroup(PanelPrincipalLayout.createSequentialGroup()
        .addGap(113, 113, 113)
        .addComponent(lbBienvenute, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        
    );
    PanelPrincipalLayout.setVerticalGroup(
        PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(PanelPrincipalLayout.createSequentialGroup()
        .addGap(16, 16, 16)
        .addComponent(lbBienvenute)
        .addGap(27, 27, 27)
        .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
            .addGroup(PanelPrincipalLayout.createSequentialGroup()
                .addComponent(btnFuncion1)
                .addGap(30, 30, 30)
                .addComponent(btnAudios))
            .addComponent(btnDibujar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addGap(27, 27, 27)
        .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
            .addGroup(PanelPrincipalLayout.createSequentialGroup()
                .addComponent(btnImagenes)
                .addGap(28, 28, 28)
                .addComponent(btnJuegos))
            .addComponent(btnArchivos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap(57, Short.MAX_VALUE))
    );

    pack(); // Ajustar el tamaño de la ventana para que se ajuste al contenido
}

// estos son los botones, las acciones de ellos
private void btnFuncion1ActionPerformed(java.awt.event.ActionEvent evt) {  
                                              
    ChatServidor chat = new ChatServidor();
    chat.setVisible(true);
    dispose();
} 
private void btnAudiosActionPerformed(java.awt.event.ActionEvent evt) {  
                                              
    AudioServidor audio = new AudioServidor();
    audio.setVisible(true);
    dispose();
} 
private void btnImagenesActionPerformed(java.awt.event.ActionEvent evt) {  
                                              
    ImagenServidor imagen = new ImagenServidor();
    imagen.setVisible(true);
    dispose();
} 
private void btnJuegosActionPerformed(java.awt.event.ActionEvent evt) {  
                                              
    JuegoServidor juego = new JuegoServidor();
    juego.setVisible(true);
    dispose();
} 
private void btnDibujarActionPerformed(java.awt.event.ActionEvent evt) {  
                                              
    DibujarServidor dibujo = new DibujarServidor();
    dibujo.setVisible(true);
    dispose();
} 
private void btnArchivosActionPerformed(java.awt.event.ActionEvent evt) {  
                                              
    ArchivoServidor archivo = new ArchivoServidor();
    archivo.setVisible(true);
    dispose();
} 

public static void main(String[] args) {

java.awt.EventQueue.invokeLater(new Runnable() {
    public void run() {
        new GUI_Servidor().setVisible(true);
        // pa que corra
    }
});
}

}
