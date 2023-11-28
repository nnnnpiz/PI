package main;

import javax.swing.*;

public class GameWindow {
    private JFrame jframe;

    public GameWindow(GamePanel gamePanel){
        jframe=new JFrame();


        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jframe.add(gamePanel);
        jframe.setLocationRelativeTo(null); //spawn window in the centre of screen
        jframe.setResizable(false); //p n podermos dar resize na wind
        jframe.pack(); //dar fit na window p o preferred size do Jpanel q definimos 1280x800
        jframe.setVisible(true);
    }

}
