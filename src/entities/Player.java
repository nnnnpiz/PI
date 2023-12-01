package entities;

import main.Game;
import utils.LoadSave;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static utils.Constants.Direction.*;
import static utils.Constants.Direction.DOWN;
import static utils.Constants.PlayerConstants.*;
import static utils.HelpMethods.*;

public class Player extends Entity{

    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 15; //120 FPS / 4 animaçoes por segundo = 30. Mas ficava mt lento animaçao por isso mudei p 15.
    private int playerAction = IDLE;
    private boolean left,up,right,down, jump;
    //private int playerDir = -1; //pq Iddle = -1. Moving = 0,1,2,3
    private boolean moving = false, attacking=false;
    private float playerSpeed = 1.0f * Game.SCALE;
    private int[][] lvlData;
    private float xDrawOffSet=21* Game.SCALE; //hitbox começa no jogador mm nao no sprite
    private float yDrawOffSet=4*Game.SCALE;

    //Jumping / Gravity:
    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE; //qt menor, mais alto o player pode pular
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE; //se atinjir o telhado
    private boolean inAir = false;

    public Player(float x, float y, int width, int height) {
        super(x, y,width,height);
        loadAnimations();
        initHitbox(x,y, (int)(20*Game.SCALE), (int)(27*Game.SCALE));
    }

    public void update(){

        updatePos();
        updateAnimationTick();
        setAnimation();

    }

    public void render(Graphics g){
        g.drawImage(animations[playerAction][aniIndex],(int)(hitbox.x-xDrawOffSet),(int)(hitbox.y -yDrawOffSet),width,height, null);
        //drawHitbox(g); to debug hitbox
    }


    private void updateAnimationTick() {

        aniTick++;
        if(aniTick >= aniSpeed){
            aniTick=0; //reset no tick
            aniIndex++;
            if(aniIndex >= GetSpriteAmount(playerAction)){ //dependendo da playerAction que esta a ser corrida nos vamos ate ao ultimo index dessa linha da matriz (neste caso IDLE =5)
                aniIndex=0;
                attacking=false;//in case its true
            }
        }


    }

    private void setAnimation() {
        int startAni = playerAction;

        if(moving)
            playerAction= RUNNING;
        else
            playerAction = IDLE;

        if (inAir){
            if(airSpeed < 0)
                playerAction = JUMP;
            else
                playerAction = FALLING;
        }

        if(attacking)
            playerAction=ATTACK_1; //qd paro o attack animation? qd chega ao ultimo index=> no updateAnimationtick final

        if(startAni!=playerAction) //mudamos a playeraction, pq isso significa q foi pedida uma nova animaçao e por isso precisamos de reiniciar o index das Animations de forma a percorrer uma nova parte da matriz p mostrar a ani corr
            resetAniTick();
    }

    private void resetAniTick() {
        aniTick=0;
        aniIndex=0;
    }

    private void updatePos() {

        moving=false; //default unless q um dos d baixo seja true... se for true damos render no playerAction RUNNING que esta no metoodo acima "setAnimation"

        if(jump)
            jump();
        if(!left && !right && !inAir)
            return;

        float xSpeed=0;

        //SE PRESSIONAR LEFT + RIGHT NAO MEXEMOS O X
        if(left) //so pressionamos a left
            xSpeed-=playerSpeed;

        if (right) //so pressionamos right
            xSpeed+=playerSpeed;

        if(!inAir){
            if(!IsEntityOnFloor(hitbox, lvlData)){
                inAir=true;
            }
        }

        if(inAir){

            if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)){
                hitbox.y+=airSpeed;
                airSpeed +=gravity;
                updateXPos(xSpeed);
            } else { //hitting telhado ou chao
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                //check se tocamos no chao:
                if(airSpeed > 0){
                    resetInAir();
                } else {
                    airSpeed = fallSpeedAfterCollision;
                }
                updateXPos(xSpeed);
            }

        }else
            updateXPos(xSpeed);

        moving=true;
    }

    private void jump() {
        if(inAir)
            return;
        inAir=true;
        airSpeed=jumpSpeed;
    }

    private void resetInAir() {
        inAir=false;
        airSpeed=0;
    }

    private void updateXPos(float xSpeed) {
        if(CanMoveHere(hitbox.x+xSpeed, hitbox.y, hitbox.width, hitbox.height,lvlData)){
                hitbox.x += xSpeed;
        } else { //colisao:
            hitbox.x =GetEntityXPosNextToWall(hitbox, xSpeed);
        }
    }

    private void loadAnimations() {
            BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS); //decidimos o tipo de atlas a carreagr

            animations = new BufferedImage[9][6]; //tamnho do array´2 vai ser consoante oq temos na img c o conj de Sprites!
            for(int j=0; j<animations.length; j++) {
                for (int i = 0; i < animations[j].length; i++) {
                    //x=i*64 pq é o x d cada sprite. y=0 pq vamos começar na 1a linha dos sprites, os outros sao p tamanho do sprite em si
                    animations[j][i] = img.getSubimage(i * 64, j*40, 64, 40);
                }
            }
    }

    public void loadLvlData(int[][] lvlData){
        this.lvlData=lvlData;
        if(!IsEntityOnFloor(hitbox,lvlData))
            inAir=true;
    }

    public void resetDirBooleans(){
        left=false;
        right=false;
        up=false;
        down=false;
    }

    public void setAttacking(boolean attacking){
        this.attacking=attacking;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setJump(boolean jump){
        this.jump=jump;
    }
}
