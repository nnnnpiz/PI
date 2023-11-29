package main;

import entities.Player;
import levels.LevelManager;

import java.awt.*;

public class Game implements Runnable{

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET=120;
    private final int UPS_SET=200; //updates per secon => TICKs. update pos jogador p ex

    private Player player;
    private LevelManager levelManager;

    public final static int TILES_DEFAULT_SIZE=32;
    public final static float SCALE = 2f; //manter cm round p o tiles_size dar 48!
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT =14;
    public final static int TILES_SIZE = (int)(TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE*TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE*TILES_IN_HEIGHT;


    public Game(){
        initClasses();

        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus(); //request input focus p ler INPUT

        startGameLoop();

    }

    private void initClasses() {
        player = new Player(200,200, (int) (64*SCALE), (int)(40*SCALE));
        levelManager = new LevelManager(this);
    }

    private void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();

    }

    //smp q quero fzr alguma mudança no jogo chamo este metodo. ex: mudar level = level.update(), mudar pos do jogador etc
    public void update(){
        player.update();
        levelManager.update();
    }

    public void render(Graphics g){
        levelManager.draw(g);
        player.render(g);


    }

    //Game Thread vai correr aqui
    @Override
    public void run() {
        //qt tempo cada frame dura em nanoSec p ser mais accurate possivel
        double timePerFrame = 1000000000.0/FPS_SET;
        double timePerUpdate = 1000000000.0/UPS_SET; //nao é o tempo q um frame demora a atualizar mas sim o tempo entre os dois... o tempo da frequencia entre os dois

        long previousTime = System.nanoTime();

        int frames =0;
        int updates=0;
        long lastCheck = System.currentTimeMillis();

        double deltaU =0;
        double deltaF = 0;

        while(true){
            long currentTime = System.nanoTime();

            deltaU += (currentTime-previousTime) / timePerUpdate; //chega a 1 ou mais qd a duraçao desde o ultimo update for = ou > q o timePerUpdate
            deltaF += (currentTime-previousTime) / timePerFrame;
            previousTime = currentTime;

            if(deltaU >= 1){ //update the game:
                update();
                updates++;
                deltaU--;
            }

            if(deltaF >=1){ //update/ render
                gamePanel.repaint();
                frames++;
                deltaF--;
            }
            /*
            if(now - lastFrame >= timePerFrame){ //se o "agora" - o ultimo update do frame for >= à duraçao  //qye qyeremos q o frame dure, entao: (se é altura de "pintar" o frame)
                gamePanel.repaint();
                lastFrame = now;
                frames++;
            }

             */

            if(System.currentTimeMillis() - lastCheck >= 1000){
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: "+frames + " | UPS: " + updates);
                frames=0;
                updates=0;
            }
        }

    }

    public void windowFocusLost(){
        player.resetDirBooleans();
    }

    public Player getPlayer(){
        return player;
    }
}
