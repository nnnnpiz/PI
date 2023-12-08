package objects;

import main.Game;

import static utils.Constants.ObjectConstants.*;

public class GameContainer extends GameObject{//É A BOX OU O BARREL

    public GameContainer(int x, int y, int objType) {
        super(x, y, objType);
        createHitbox();
    }

    private void createHitbox() {

        if(objType == BOX){
            initHitbox(25,18);

            //drawoffset é diferente se for box ou se  for barrel logo:
            xDrawOffset = (int)(7* Game.SCALE);
            yDrawOffset = (int)(12*Game.SCALE);

        } else{
            initHitbox(23, 25);

            xDrawOffset = (int)(8* Game.SCALE);
            yDrawOffset = (int)(5*Game.SCALE);

        }

        hitbox.y += yDrawOffset + (int)(Game.SCALE*2); //para meter os objetos a desenhados no chao!
        hitbox.x += xDrawOffset /2; //para centrar
    }

    public void update(){
        if(doAnimation) //so damos update na animation se "doAnimation" essta a true!
            updateAnimationTick();

    }


}
