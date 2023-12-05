package entities;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

//abstract => nao posso criar um objeto dela. dou lhe extends e store values cm posiçao, vida etc (conceitos de POO)
public abstract class Entity {

    protected float x,y;
    protected int width,height; //protected pq se fosse private so podiamos usar x e y nesta classe mm que extendida. Protected => so as que dao extend nesta classe podem usar os atributos dela
    protected Rectangle2D.Float hitbox; //Hitbox
    protected int aniTick, aniIndex; //120 FPS / 4 animaçoes por segundo = 30. Mas ficava mt lento animaçao por isso mudei p 15.
    protected int state;
    protected float airSpeed;
    protected boolean inAir=false;
    protected int maxHealth;
    protected int currentHealth;
    //ATTACKBOX:
    protected Rectangle2D.Float attackBox; //se o enemy tiver dentro disto eu ataco
    protected float walkSpeed = 1.0f * Game.SCALE;

    public Entity(float x, float y, int width, int height){
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;

    }

    //DRAW DE QQLR HITBOX!
    protected void drawAttackBox(Graphics g, int xLvlOffset){
        g.setColor(Color.red);
        g.drawRect((int)(attackBox.x - xLvlOffset),(int) attackBox.y, (int)attackBox.width, (int)attackBox.height);

    }

    protected void drawHitbox(Graphics g, int xLvlOffset){
        //for debugging the hitbox
        g.setColor(Color.PINK);
        g.drawRect((int)hitbox.x - xLvlOffset, (int)hitbox.y, (int)hitbox.width, (int)hitbox.height);
    }

    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x,y,(int)(width*Game.SCALE),(int)(height*Game.SCALE));
    }

   // protected void updateHitbox(){ //da update na hitbox pos
     //   hitbox.x = (int)x;
     //   hitbox.y = (int)y;
    //}

    public Rectangle2D.Float getHitbox(){
        return hitbox;
    }

    public int getState(){
        return state;
    }

    public int getAniIndex(){
        return aniIndex;
    }


}
