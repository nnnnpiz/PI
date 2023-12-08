package gamestates;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import objects.ObjectManager;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import static utils.Constants.Environment.*;

import static main.Game.SCALE;

public class Playing extends State implements Statemethods{

    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    //OBJECTMANAGER:
    private ObjectManager objectManager;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private boolean paused=false;//mostrar pause screen ou n

    private int xLvlOffset;
    private int leftBorder = (int)(0.2*Game.GAME_WIDTH); //linha q o player esta beyond. 20% da gamewidth original p movermos a camera
    private int rightBorder = (int) (0.8*Game.GAME_WIDTH); //80%
    private int maxLvlOffsetX;

    private BufferedImage backgroundImg, bigCloud, smallCloud; //playing background img, bigcloud, smallcloud
    private int[] smallCloudsPos; //p randomizar as small clouds c differtn y values
    private Random rnd = new Random(); //talvez mudar isto para ser uma distribuiçao?

    private boolean gameOver;
    private boolean lvlCompleted=false;
    private boolean playerDying;

    public Playing(Game game){
        super(game);
        initClasses();

        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
        bigCloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
        smallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
        smallCloudsPos = new int[8];
        for(int i=0; i<smallCloudsPos.length; i++)
            smallCloudsPos[i] = (int) (90*Game.SCALE)+rnd.nextInt((int)(100* SCALE)); //90*2 = 180 é o min que pode ser gerado. GERADO => 180 + algo entre 0 e 100 (200 pq scale =2)

        calcLvlOffset();
        loadStartLevel();
    }

    public void loadNextLevel(){
        resetAll();
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn()); //new spawn position p novo level!
    }

    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
        objectManager.loadObjects(levelManager.getCurrentLevel());
    }

    //calc para o start level
    private void calcLvlOffset() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
    }


    private void initClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        objectManager = new ObjectManager(this);

        player = new Player(200,200, (int) (64*SCALE), (int)(40*SCALE), this);
        player.loadLvlData(levelManager.getCurrentLevel().getLvlData()); //player fica c lvldata stored
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());

        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
    }


    @Override
    public void update() {
        //enquanto esta PAUSED = TRUE o jogo nao da mais update. (para o jogo)
        if(paused){
            pauseOverlay.update();
        } else if(lvlCompleted){
            levelCompletedOverlay.update();
        } else if(gameOver){
            gameOverOverlay.update();
        } else if(playerDying){ //check se o jogador esta a morrer
            player.update();
        }//OU ESTA GAMEOVER OU O PLAYERDYING!
        else {
            levelManager.update();
            objectManager.update(levelManager.getCurrentLevel().getLvlData(), player);
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLvlData(), player);
            //metodo p ver se precisamos de mexer a camera
            checkCloseToBorder();
        }

    }

    //check nova posiçao
    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLvlOffset; //se esta diferença for > rightBorder entao o plaer esta mais p direita e precisamos d mexer o LVL para a direita!

        if(diff > rightBorder)
            xLvlOffset += diff - rightBorder; //se player tiver no 85 e o offset=0. 85-0=85. if(85>80) offset += 85-80 =>offset +=5;

        else if(diff < leftBorder)
            xLvlOffset += diff-leftBorder;

        //da reasure que o lvlOffset nao fica demasiado alto qd nao ha mais p dar render
        if(xLvlOffset > maxLvlOffsetX)
            xLvlOffset = maxLvlOffsetX;
        else if(xLvlOffset < 0)
            xLvlOffset=0;
    }

    @Override
    public void draw(Graphics g) {
        //background playing img:
        g.drawImage(backgroundImg, 0,0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

        drawClouds(g);

        levelManager.draw(g, xLvlOffset);
        player.render(g, xLvlOffset);
        enemyManager.draw(g, xLvlOffset);
        objectManager.draw(g, xLvlOffset);

        //so quero dar draw no pause overlay se PAUSED= TRUE
        if(paused) {
            //desenhar escuro atras para efeito de  "pausa"
            g.setColor(new Color(0, 0, 0, 150)); //mudar para preto com alfa = 100
            g.fillRect(0,0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        } else if(gameOver)
            gameOverOverlay.draw(g);
        else if(lvlCompleted)
            levelCompletedOverlay.draw(g);
    }

    private void drawClouds(Graphics g) {
        for(int i=0; i<3; i++)
            g.drawImage(bigCloud, i*BIG_CLOUD_WIDTH - (int)(xLvlOffset*0.3),(int)(204* SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);

        for(int i=0; i<smallCloudsPos.length; i++){
        g.drawImage(smallCloud, SMALL_CLOUD_WIDTH*4*i - (int)(xLvlOffset*0.7), smallCloudsPos[i], SMALL_CLOUD_WIDTH,SMALL_CLOUD_HEIGHT, null); //p cada index vamos adicionar 4 clouds em width entre cada
        }
    }

    public void resetAll(){
        //TODO: reset playing, enemy, lvl, etc.
        gameOver=false;
        paused=false;
        lvlCompleted=false;
        playerDying=false;
        player.resetAll();
        enemyManager.resetAllEnemies();
        objectManager.resetAllObjects();
    }

    public void setGameOver(boolean gameOver){
        this.gameOver = gameOver;
    }

    public void checkObjectHit(Rectangle2D.Float hitbox){
        objectManager.checkObjectHit(hitbox);
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox){
        enemyManager.checkEnemyHit(attackBox);
    }

    public void checkPotionTouched(Rectangle2D.Float hitbox){
        objectManager.checkObjectTouched(hitbox);
    }

    public void checkSpikesTouched(Player p) {
        objectManager.checkSpikesTouched(p);
    }

    public void mouseDragged(MouseEvent e){
        if(!gameOver)
            if(paused)
                pauseOverlay.mouseDragged(e); //do mouseinputs chama este e este chama o do pauseoverlay
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(!gameOver)
            if(e.getButton() == MouseEvent.BUTTON1)
                player.setAttacking(true);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(!gameOver){
            if(paused)
                pauseOverlay.mousePressed(e);
            else if(lvlCompleted)
                levelCompletedOverlay.mousePressed(e);
        } else {
            gameOverOverlay.mousePressed(e);
        }//gameOever
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(!gameOver){
            if(paused)
                pauseOverlay.mouseReleased(e);
            else if(lvlCompleted)
                levelCompletedOverlay.mouseReleased(e);
        } else
            gameOverOverlay.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(!gameOver){
            if(paused)
                pauseOverlay.mouseMoved(e);
            else if(lvlCompleted)
                levelCompletedOverlay.mouseMoved(e);
        } else
            gameOverOverlay.mouseMoved(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(gameOver)
            gameOverOverlay.keyPressed(e);
        else
            switch(e.getKeyCode()){
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_LEFT:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_RIGHT:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_ESCAPE: //ESC p ir p menu
                    paused = !paused; //damos flip no pause button p dar pause e unpause no ESC
                    break;
            }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(!gameOver)
            switch(e.getKeyCode()){
                case KeyEvent.VK_A:
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_LEFT:
                    player.setLeft(false);
                    break;

                case KeyEvent.VK_D:
                    player.setRight(false);
                    break;
                case KeyEvent.VK_RIGHT:
                    player.setRight(false);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(false);
                    break;
            }
    }

    public void setLevelCompleted(boolean levelCompleted){
        this.lvlCompleted = levelCompleted;
    }
    public void setMaxLvlOffset(int lvlOffset){
        this.maxLvlOffsetX = lvlOffset;
    }

    public void unpauseGame(){
        paused = false;

    }

    public void windowFocusLost(){
        player.resetDirBooleans();
    }

    public Player getPlayer(){
        return player;
    }

    public EnemyManager getEnemyManager(){
        return enemyManager;
    }

    public ObjectManager getObjectManager(){
        return objectManager;
    }

    public LevelManager getLevelManager(){
        return  levelManager;
    }


    public void setPlayerDying(boolean b) {
        this.playerDying = playerDying;
    }
}
