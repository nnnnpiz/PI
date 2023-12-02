package entities;

import java.awt.*;
import java.awt.geom.Rectangle2D;

//abstract => nao posso criar um objeto dela. dou lhe extends e store values cm posiçao, vida etc (conceitos de POO)
public abstract class Entity {

    protected float x,y;
    protected int width,height; //protected pq se fosse private so podiamos usar x e y nesta classe mm que extendida. Protected => so as que dao extend nesta classe podem usar os atributos dela
    protected Rectangle2D.Float hitbox; //Hitbox

    public Entity(float x, float y, int width, int height){
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;

    }

    protected void drawHitbox(Graphics g){
        //for debugging the hitbox
        g.setColor(Color.PINK);
        g.drawRect((int)hitbox.x, (int)hitbox.y, (int)hitbox.width, (int)hitbox.height);
    }

    protected void initHitbox(float x, float y, int width, int height) {
        hitbox = new Rectangle2D.Float(x,y,width,height);
    }

   // protected void updateHitbox(){ //da update na hitbox pos
     //   hitbox.x = (int)x;
     //   hitbox.y = (int)y;
    //}

    public Rectangle2D.Float getHitbox(){
        return hitbox;
    }



}
