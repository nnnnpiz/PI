package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utils.Constants.UI.PauseButtons.SOUND_SIZE;
import static utils.Constants.UI.URMButtons.URM_SIZE;
import static utils.Constants.UI.VolumeButtons.SLIDER_WIDTH;
import static utils.Constants.UI.VolumeButtons.VOLUME_HEIGHT;

public class PauseOverlay {

    private Playing playing;
    private BufferedImage backgroundImg;
    private int bgX, bgY, bgW, bgH; //BG = BACKGROUND
    private UrmButton menuB, replayB, unpauseB; //tipos de botoes p a img URM_BUTTONS.
    private AudioOptions audioOptions;

    public PauseOverlay(Playing playing){
        this.playing = playing;
        loadBackground();
        audioOptions = playing.getGame().getAudioOptions(); //pois criamos o audioOptions na classe Game
        createUrmButtons();

    }

    private void createUrmButtons() {
        int menuX = (int) (313*Game.SCALE);
        int replayX = (int) (387*Game.SCALE);
        int unpauseX = (int )(462*Game.SCALE); //so fzms X p cada, pq sao x's diferentes p cada mas y mantem -se no ecra
        int bY = (int)(325*Game.SCALE);

        //inicializar os botoes:
        menuB = new UrmButton(menuX, bY, URM_SIZE, URM_SIZE, 2); //ultimo argumento é o row index
        replayB = new UrmButton(replayX, bY, URM_SIZE, URM_SIZE, 1);
        unpauseB = new UrmButton(unpauseX, bY, URM_SIZE, URM_SIZE, 0);
    }



    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bgW = (int)(backgroundImg.getWidth()* Game.SCALE);
        bgH = (int)(backgroundImg.getHeight()* Game.SCALE);
        bgX = Game.GAME_WIDTH /2 - bgW/2;
        bgY = (int)(25 * Game.SCALE);
    }

    public void update(){
        //URM:
        menuB.update();
        replayB.update();
        unpauseB.update();

        audioOptions.update();
    }

    public void draw(Graphics g){
        //background:
        g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);

        //URM Buttons:
        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);

        audioOptions.draw(g);
    }

    public void mouseDragged(MouseEvent e){
        audioOptions.mouseDragged(e); //SENDO ASSIM ja podemos chamar o mouseDragged da classe audioOptions!
    }

    public void mousePressed(MouseEvent e) {
        if(isIn(e,menuB))
            menuB.setMousePressed(true);
        else if(isIn(e,replayB))
            replayB.setMousePressed(true);
        else if(isIn(e,unpauseB))
            unpauseB.setMousePressed(true);
        else
            audioOptions.mousePressed(e);
    }


    public void mouseReleased(MouseEvent e) {
         if(isIn(e,menuB)){
            if(menuB.isMousePressed()) {
                Gamestate.state = Gamestate.MENU;
                playing.unpauseGame(); //assim se voltar ao menu e tocar PLAY nao começa paused
            }
        }
        else if(isIn(e,replayB)){
            if(replayB.isMousePressed()) {
                playing.resetAll();
                playing.unpauseGame();
            }
        }
        else if(isIn(e,unpauseB)){
            if(unpauseB.isMousePressed())
                playing.unpauseGame();
        } else
            audioOptions.mouseReleased(e);

        menuB.resetBools();
        replayB.resetBools();
        unpauseB.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(false);
        replayB.setMouseOver(false);
        unpauseB.setMouseOver(false);

        if(isIn(e, menuB))
            menuB.setMouseOver(true);
        else if(isIn(e, replayB))
            replayB.setMouseOver(true);
        else if(isIn(e, unpauseB))
            unpauseB.setMouseOver(true);
        else
            audioOptions.mouseMoved(e);
    }

    //parecido ao state class mas o pauseOverlay nnao é uma state class ent faço de novo
    private boolean isIn(MouseEvent e, PauseButton b){ //usamos PauseButton p darmos check em tds os botoes
        return (b.getBounds().contains(e.getX(),e.getY()));
    }



}
