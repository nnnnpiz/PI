package objects;

import main.Game;

import java.awt.geom.Rectangle2D;
import static utils.Constants.Projectiles.*;

public class Projectile {

    private Rectangle2D.Float hitbox;
    private int dir; //direction da bola left ou right e multiplicamos pelo speed e aplicamos a mudança da ball d pos
    private boolean active=true; //se nao tiver active nao vamos dar draw

    public Projectile(int x, int y, int dir){ //x e y é a pos do spawn
        int xOffset = (int)(-3* Game.SCALE);//default p se tiver facing p LEFT.
        int yOffset = (int)(5*Game.SCALE); //estes offsets servem para mover o projectile p ficar mais realista e parecer q esta a sair da parte de tras do cannon consoante esta p direita ou p esquerda facing!

        if(dir == 1) //facing to the left
            xOffset =(int)(29*Game.SCALE);

        hitbox = new Rectangle2D.Float(x + xOffset, y + yOffset, CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT);
        this.dir=dir;

    }

    public void updatePos(){ //dir vai ser 1 ou -1. se for 1 esta indo p direita se ofr -1 esta indo p esq
        hitbox.x += dir*SPEED;

    }

    public void setPos(int x, int y){
        hitbox.x =x;
        hitbox.y=y;
    }

    //p dar check dps se deu hit no player ou n, o projectile!
    public Rectangle2D.Float getHitbox(){
        return hitbox;
    }

    public void setActive(boolean active){
        this.active=active;
    }

    public boolean isActive(){
        return active;
    }


}
