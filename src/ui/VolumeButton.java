package ui;

import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import static utils.Constants.UI.VolumeButtons.*;



public class VolumeButton extends PauseButton{

    private BufferedImage[] imgs;
    private BufferedImage slider;
    private int index=0;
    private boolean mouseOver, mousePressed;
    private int buttonX; //botao do slider
    private int minX, maxX; //slider start / end positions

    public VolumeButton(int x, int y, int width, int height) {
        super(x+width/2, y, VOLUME_WIDTH, height); //x+width/2 p começar com o slider no meio!
        bounds.x -= VOLUME_WIDTH/2; //mover um pc p esquerda
        buttonX = x + width/2;
        this.x=x;
        this.width=width;
        minX=x + VOLUME_WIDTH/2;
        maxX= x+width - VOLUME_WIDTH/2;
        loadImgs();
    }

    private void loadImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.VOLUME_BUTTONS); //load do VB
        imgs = new BufferedImage[3];
        for(int i=0; i<imgs.length; i++)
            imgs[i] = temp.getSubimage(i*VOLUME_DEFAULT_WIDTH,0,VOLUME_DEFAULT_WIDTH,VOLUME_DEFAULT_HEIGHT);

        slider = temp.getSubimage(3*VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);

    }


    public void update(){
        index=0;
        if(mouseOver)
            index=1;
        if(mousePressed)
            index=2;

    }

    public void draw(Graphics g){
        g.drawImage(slider, x,y, width,height,null);
        //botao:
        g.drawImage(imgs[index], buttonX - VOLUME_WIDTH/2, y, VOLUME_WIDTH, height, null);
    }

    //para o slider mouseX
    public void changeX(int x){ //int x
        if(x < minX)
            buttonX = minX;
        else if(x > maxX)
            buttonX=maxX;
        else
            buttonX= x;

        bounds.x = buttonX-VOLUME_WIDTH/2;

    }

    public void resetBools(){ //qd sairmos do button p dar reset no mouseOver e pressed
        mouseOver=false;
        mousePressed=false;
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

}
