package objects;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utils.Constants.ANI_SPEED;
import static utils.Constants.ObjectConstants.*;

public class GameObject {

    protected int x,y,objType;
    protected Rectangle2D.Float hitbox;
    protected boolean doAnimation, active=true; //se pegarmos no obj ou destruimos lhe ele fica unactive
    protected int aniTick,aniIndex;
    protected int xDrawOffset, yDrawOffset;

    public GameObject(int x, int y, int objType){
        this.x=x;
        this.y=y;
        this.objType=objType;
    }

    protected void updateAnimationTick(){
        aniTick++;
        if(aniTick>=ANI_SPEED){
            aniTick=0;
            aniIndex++;
            if(aniIndex >= GetSpriteAmount(objType)){ //SO ANIMAMOS O OBJETO QUANDO DESTROIMOS A CAIXA P EX , DPS TEMOS Q PARAR
                aniIndex=0;
                if(objType == BARREL || objType == BOX){
                    doAnimation = false;
                    active=false; //n damos draw dps de destruir
                } else if(objType == CANNON_LEFT || objType == CANNON_RIGHT){
                    doAnimation=false;
                }
            }
        }
    }

    //qd damos resetlevel tmb quermeos dar reset no gameObject
    public void reset(){
        aniIndex=0;
        aniTick=0;
        active=true;

        if(objType == BARREL || objType == BOX || objType == CANNON_LEFT || objType == CANNON_RIGHT) //se for barrel ou box n continuamos a animar
            doAnimation=false;
        else
            doAnimation = true;//se for uma poçao continuamos a dar update

    }

    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x,y,(int)(width* Game.SCALE),(int)(height*Game.SCALE));
    }

    public void drawHitbox(Graphics g, int xLvlOffset){
        //for debugging the hitbox
        g.setColor(Color.PINK);
        g.drawRect((int)hitbox.x - xLvlOffset, (int)hitbox.y, (int)hitbox.width, (int)hitbox.height);
    }

    public int getObjType() {
        return objType;
    }


    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active){
        this.active=active;
    }

    public void setAnimation(boolean doAnimation){
        this.doAnimation=doAnimation;

    }

    public int getxDrawOffset() {
        return xDrawOffset;
    }


    public int getyDrawOffset() {
        return yDrawOffset;
    }

    //pq n vamos desenhar o object aqui!
    public int getAniIndex(){
        return aniIndex;
    }

    public int getAniTick(){
        return aniTick;
    }


}
