package Inicio;
import Cliente.GUI_Cliente;
import Servidor.GUI_Servidor;
public class InicioTodo {
    
    public static void main(String[] args) {
        GUI_Cliente g = new GUI_Cliente();
        GUI_Servidor a = new GUI_Servidor();
        a.setVisible(true);
        g.setVisible(true);


    }

}
