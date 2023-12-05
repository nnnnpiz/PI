package main;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class GameWindow {
    private JFrame jframe;

    public GameWindow(GamePanel gamePanel){
        jframe=new JFrame();


        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jframe.add(gamePanel);
        jframe.setResizable(false); //p n podermos dar resize na wind
        jframe.pack(); //dar fit na window p o preferred size do Jpanel q definimos 1280x800
        jframe.setLocationRelativeTo(null); //spawn window in the centre of screen
        jframe.setVisible(true);

        //detetar se janela perdeu o focus => ou seja, se der minimise na window, ela perde o focus dos inputs e se tiver a andar e minimizar a janela p. ex nao é detetado o release do input e o player ficaria a andar ate voltar a abrir a janela
        jframe.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {

            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                gamePanel.getGame().windowFocusLost();
            }
        });
    }

}
