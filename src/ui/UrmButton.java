package ui;

import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import static utils.Constants.UI.URMButtons.*;

public class UrmButton extends PauseButton {

    private BufferedImage[] imgs;
    private int rowIndex, index;
    private boolean mouseOver, mousePressed; //eventos a testar nos botoes cm fizemos no Menu

    public UrmButton(int x, int y, int width, int height, int rowIndex) { //row index p percorrer a array de imgs
        super(x, y, width, height);
        this.rowIndex=rowIndex;
        loadImgs();

    }

    private void loadImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.URM_BUTTONS); //load do URM
        imgs = new BufferedImage[3];
        for (int i =0; i<imgs.length; i++)
            imgs[i] = temp.getSubimage(i*URM_DEFAULT_SIZE, rowIndex*URM_DEFAULT_SIZE ,URM_DEFAULT_SIZE,URM_DEFAULT_SIZE);

    }


    public void update(){
        index=0;
        if(mouseOver) //dependendo do mouse event indexs diferentes p buscar a img correta
            index =1;
        if(mousePressed)
            index=2;

    }

    public void draw(Graphics g){
        g.drawImage(imgs[index], x, y, URM_SIZE, URM_SIZE,null );

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
