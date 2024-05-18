package Servidor;

import javax.swing.JFrame;

public class GUI_Servidor extends JFrame{
    private javax.swing.JPanel PanelPrincipal; 
    private javax.swing.JButton btnFuncion1;



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
        btnFuncion1.setText("Chat");


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
            .addGap(0, 400, Short.MAX_VALUE)
        );
        PanelPrincipalLayout.setVerticalGroup(
            PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack(); // Ajustar el tamaño de la ventana para que se ajuste al contenido
    }




    public static void main(String[] args) {
   
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            new GUI_Servidor().setVisible(true);
        }
    });
}

}