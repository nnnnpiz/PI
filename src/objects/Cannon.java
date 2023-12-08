package objects;

import main.Game;

public class Cannon extends GameObject{

    private int tileY; //checkar se o cannon esta no mesmo Y tile que o player para fzr o seePlayer
    public Cannon(int x, int y, int objType) {
        super(x, y, objType);
        tileY = y/ Game.TILES_SIZE;
        initHitbox(40, 26);
        hitbox.x -= (int) (4*Game.SCALE);
        hitbox.y +=(int)(6*Game.SCALE); //para centrar o canon no chao e centro
    }

    public void update(){
        if(doAnimation)
            updateAnimationTick();
    }

    public int getTileY(){
        return tileY;
    }
}
