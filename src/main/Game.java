package main;

public class Game implements Runnable{

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET=120;

    public Game(){
        gamePanel = new GamePanel();
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus(); //request input focus p ler INPUT
        startGameLoop();

    }

    private void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();

    }

    //Game Thread vai correr aqui
    @Override
    public void run() {
        //qt tempo cada frame dura em nanoSec p ser mais accurate possivel
        double timePerFrame = 1000000000.0/FPS_SET;
        long lastFrame = System.nanoTime(); //nanoTime deovlve Long
        long now = System.nanoTime(); //criamos now para so chamar nanoTime uma vez
        int frames =0;
        long lastCheck = System.currentTimeMillis();

        while(true){
            now = System.nanoTime(); //atualizar o valor de now sempre que faço o loop d novo

            if(now - lastFrame >= timePerFrame){ //se o "agora" - o ultimo update do frame for >= à duraçao  //qye qyeremos q o frame dure, entao:
                gamePanel.repaint();
                lastFrame = now;
                frames++;
            }

            if(System.currentTimeMillis() - lastCheck >= 1000){
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: "+frames);
                frames=0;
            }
        }

    }
}
