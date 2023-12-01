package ui;

import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import static utils.Constants.UI.PauseButtons.*;

public class SoundButton extends PauseButton{

    private BufferedImage[][] soundImgs; //pq temos um sprite de imgs c matrixx
    private boolean mouseOver, mousePressed;
    private boolean muted; //determina se estamos na 1a row ou na 2a row dos sprites (muted ou som)
    private int rowIndex, colIndex; //row e column index

    public SoundButton(int x, int y, int width, int height) {
        super(x, y, width, height);
        loadSoundImgs();
    }

    private void loadSoundImgs() {
        BufferedImage tmp  = LoadSave.GetSpriteAtlas(LoadSave.SOUND_BUTTONS);//tmp store
        soundImgs = new BufferedImage[2][3];
        for(int j=0 ; j<soundImgs.length; j++)
            for(int i =0 ; i<soundImgs[j].length; i++)
                soundImgs[j][i] = tmp.getSubimage(i*SOUND_SIZE_DEFAULT,  j*SOUND_SIZE_DEFAULT ,SOUND_SIZE_DEFAULT,SOUND_SIZE_DEFAULT);
    }

    public void update(){
        if(muted)
            rowIndex =1;
        else
            rowIndex=0;

        colIndex=0;
        if(mouseOver)
            colIndex=1;
        if(mousePressed)
            colIndex=2;

    }

    //p qd damos release button
    public void resetBools(){
        mouseOver=false;
        mousePressed=false;
    }

    public void draw(Graphics g){
        g.drawImage(soundImgs[rowIndex][colIndex], x,y , width, height,null);
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }
}
