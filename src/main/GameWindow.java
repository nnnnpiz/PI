package main;

import javax.swing.*;

public class GameWindow {
    private JFrame jframe;

    public GameWindow(GamePanel gamePanel){
        jframe=new JFrame();

        jframe.setSize(400,400);
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jframe.add(gamePanel);
        jframe.setLocationRelativeTo(null); //spawn window in the centre of screen
        jframe.setVisible(true);
    }

}
