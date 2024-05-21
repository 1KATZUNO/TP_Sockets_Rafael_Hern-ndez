package Servidor;

import javax.swing.JFrame;

public class GUI_Servidor extends JFrame{
    private javax.swing.JPanel PanelPrincipal; 
    private javax.swing.JButton btnFuncion1;
    private javax.swing.JButton btnAudios;
    private javax.swing.JButton btnImagenes;
    private javax.swing.JButton btnVideos;



public GUI_Servidor(){
    setLocationRelativeTo(null);
initComponents();


}


// initComponents es todo lo que va a ser la ventana, y se guarda en un apartado con ese nombre 
// igual al de netbeans, para no olvidar su función
    private void initComponents(){
        // se le pone por defecto el exit and close(pa abrir y cerrar)
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        PanelPrincipal = new javax.swing.JPanel();
        btnFuncion1 = new javax.swing.JButton();
        btnFuncion1.setText("Chat");
        btnAudios = new javax.swing.JButton();
        btnAudios.setText("Audios");
        btnImagenes = new javax.swing.JButton();
        btnImagenes.setText("Envío Imagenes");


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
                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnImagenes, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnAudios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnFuncion1, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)))
                .addContainerGap(257, Short.MAX_VALUE))
            
        );
        PanelPrincipalLayout.setVerticalGroup(
            PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelPrincipalLayout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(btnFuncion1)
                .addGap(30, 30, 30)
                .addComponent(btnAudios)
                .addGap(27, 27, 27)
                .addComponent(btnImagenes)
                .addContainerGap(112, Short.MAX_VALUE))
        );

        pack(); // Ajustar el tamaño de la ventana para que se ajuste al contenido
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