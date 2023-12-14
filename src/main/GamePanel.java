package main;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;
import static utils.Constants.Direction.*;
import static utils.Constants.PlayerConstants.*;

/**
 private float xDelta=100, yDelta=100;
 //private Random random;
 private BufferedImage img; //usamos BufferedImage para desta forma termos numa so imagem o conjunto de sprites das animaçoes e assim podermos selecionar qual sprite usar com uma so imagem!
 private BufferedImage[][] animations; //esta e as seguintes serao as arrays c as imagens de cada "estado" do jogador de forma a percorrermos o array e mudar o sprite drawn de forma a simular movement
 //mudamos p matriz p poder ter todas as animaçoes num so load
 private int aniTick, aniIndex, aniSpeed = 15; //120 FPS / 4 animaçoes por segundo = 30. Mas ficava mt lento animaçao por isso mudei p 15.
 private int playerAction = IDLE;
 private int playerDir = -1; //pq Iddle = -1. Moving = 0,1,2,3
 private boolean moving = false;
 **/

public class GamePanel extends JPanel {
    private MouseInputs mouseInputs; //crio este atributo de class de forma a nao passar como MouseListener dois objetos diferentes
    private Game game;

    public GamePanel(Game game){
        mouseInputs = new MouseInputs(this);
        this.game=game;
        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs); //clicks
        addMouseMotionListener(mouseInputs); //drags
    }


    private void setPanelSize() {
        Dimension size = new Dimension(GAME_WIDTH,GAME_HEIGHT); //imgs de 32 pixeis por 32 => 1280/32 = 40 wide. 800/32 = 25 height
        setPreferredSize(size);
        System.out.println("size : " + GAME_WIDTH + " : " + GAME_HEIGHT);
    }

    public void updateGame(){

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        //subImg = img.getSubimage(1*64, 8*40, 64, 40); //vamos buscar outro sprite da matriz o (1,8)
        //ISTO PASSOU P CLASSE PLAYER p ficar em cada
        //g.drawImage(animations[playerAction][aniIndex], (int)xDelta,(int)yDelta, 256, 160, null); //4º arg. ImageObserver usado p monitorizar o estado da imagem antes de ser FULLY drawn nao vai ser usado por questoes de simplicidade de codigo
        //nb q width x height faz de "factor" e multiplica o tamanho do sprite deste q seja o dobro p ficar prop.

        game.render(g);
    }

    public Game getGame(){
        return game;
    }





}