package inputs;

import main.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static utils.Constants.Direction.*;

public class KeyboardInputs implements KeyListener {

    private GamePanel gamePanel;

    public KeyboardInputs(GamePanel gamePanel){
        this.gamePanel=gamePanel; //gamePanel access dentro do keyboardInput p mudar valores dentro do gp
        //isto p mudar os valores do xDelta e yDelta do gamePanel
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }


    @Override
    public void keyReleased(KeyEvent e) {
        //qd damos release na key temos q dar reset na dir
        switch(e.getKeyCode()){
            case KeyEvent.VK_W:
                gamePanel.getGame().getPlayer().setUp(false);
                break;
            case KeyEvent.VK_A:
                gamePanel.getGame().getPlayer().setLeft(false);
                break;
            case KeyEvent.VK_S:
                gamePanel.getGame().getPlayer().setDown(false);
                break;
            case KeyEvent.VK_D:
                gamePanel.getGame().getPlayer().setRight(false);
                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_W:
                gamePanel.getGame().getPlayer().setUp(true);
                break;
            case KeyEvent.VK_A:
                gamePanel.getGame().getPlayer().setLeft(true);
                break;
            case KeyEvent.VK_S:
                gamePanel.getGame().getPlayer().setDown(true);
                break;
            case KeyEvent.VK_D:
                gamePanel.getGame().getPlayer().setRight(true);
                break;
        }

    }

}
