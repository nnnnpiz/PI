package entities;

import gamestates.Playing;
import main.Game;
import utils.LoadSave;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static utils.Constants.ANI_SPEED;
import static utils.Constants.Direction.*;
import static utils.Constants.Direction.DOWN;
import static utils.Constants.GRAVITY;
import static utils.Constants.PlayerConstants.*;
import static utils.HelpMethods.*;

public class Player extends Entity{

    private BufferedImage[][] animations;

    private boolean left,right, jump;
    //private int playerDir = -1; //pq Iddle = -1. Moving = 0,1,2,3
    private boolean moving = false, attacking=false;

    private int[][] lvlData;
    private float xDrawOffSet=21* Game.SCALE; //hitbox começa no jogador mm nao no sprite
    private float yDrawOffSet=4*Game.SCALE;

    //Jumping / Gravity:
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE; //se atinjir o telhado

    //STATUSBARUI:
    private BufferedImage statusBarImg;

    private int statusBarWidth = (int) (192*Game.SCALE);
    private int statusBarHeight= (int) ( 58*Game.SCALE);
    private int statusBarX = (int) ( 10*Game.SCALE);
    private int statusBarY = (int) (10*Game.SCALE);

    private int healthBarWidth = (int)(150*Game.SCALE);
    private int healthBarHeight = (int)(4*Game.SCALE);
    private int healthBarXStart = (int)(34*Game.SCALE);
    private int healthBarYStart = (int)(14*Game.SCALE);

  //  private int maxHealth=100; //health do jogador inicial
    //private int currentHealth = maxHealth;
    private int healthWidth = healthBarWidth;


    private int flipX = 0;
    private int flipW = 1;

    private boolean attackChecked;
    private Playing playing;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y,width,height);
        this.playing=playing;
        this.state = IDLE;
        this.maxHealth=100;
        this.currentHealth=maxHealth;
        this.walkSpeed=Game.SCALE * 1.0f;
        loadAnimations();
        initHitbox(20, 27);
        initAttackBox();
    }

    public void setSpawn(Point spawn){
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x= x;
        hitbox.y=y;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x,y, (int)(20*Game.SCALE), (int)(20*Game.SCALE));
    }

    public void update() {
        updateHealthBar();
        if (currentHealth <= 0){
            playing.setGameOver(true);
            return;
        }

        updateAttackBox();

        updatePos();
        if(attacking)
            checkAttack();
        updateAnimationTick();
        setAnimation();

    }

    private void checkAttack() {
        if(attackChecked || aniIndex != 1) //atack é no 1
            return;
        attackChecked=true;
        playing.checkEnemyHit(attackBox);
    }

    private void updateAttackBox() {
        //left ou right
        if(right){
            attackBox.x = hitbox.x + hitbox.width+ (int)(Game.SCALE*10); //attack box follows a hitbox do player + um offset
        }else if(left) {
            attackBox.x = hitbox.x - hitbox.width- (int)(Game.SCALE*10);
        }
        attackBox.y = hitbox.y + (Game.SCALE*10);
    }

    private void updateHealthBar() {
        healthWidth = (int)((currentHealth /(float)maxHealth) * healthBarWidth);
    }

    public void render(Graphics g, int lvlOffset){
        g.drawImage(animations[state][aniIndex],
                (int)(hitbox.x-xDrawOffSet) - lvlOffset + flipX,
                (int)(hitbox.y -yDrawOffSet),
                width * flipW, height, null); //se formos p ESQuerda, flipw= -1; e assim a construçao da imagem vai ser virada p esquerda pois width fica negativo!

       // drawHitbox(g, lvlOffset); to debug hitbox and errors
        //drawAttackBox(g, lvlOffset); HITBOX D ATAQUE DEBUGGER
        drawUI(g);
    }


    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight,null);
        g.setColor(Color.red);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
    }


    private void updateAnimationTick() {
        aniTick++;
        if(aniTick >= ANI_SPEED){
            aniTick=0; //reset no tick
            aniIndex++;
            if(aniIndex >= GetSpriteAmount(state)){ //dependendo da playerAction que esta a ser corrida nos vamos ate ao ultimo index dessa linha da matriz (neste caso IDLE =5)
                aniIndex=0;
                attacking=false;//in case its true
                attackChecked=false; //reset p false
            }
        }


    }

    private void setAnimation() {
        int startAni = state;

        if(moving)
            state= RUNNING;
        else
            state = IDLE;

        if (inAir){
            if(airSpeed < 0)
                state = JUMP;
            else
                state = FALLING;
        }

        if(attacking) {
            state = ATTACK; //qd paro o attack animation? qd chega ao ultimo index=> no updateAnimationtick final
            if(startAni != ATTACK){
                aniIndex=1;
                aniTick=0;
                return;
            }

        }
        if(startAni!=state) //mudamos a playeraction, pq isso significa q foi pedida uma nova animaçao e por isso precisamos de reiniciar o index das Animations de forma a percorrer uma nova parte da matriz p mostrar a ani corr
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

       // if(!left && !right && !inAir)
       //     return;
        if(!inAir)
            if((!left && !right) || (right &&left))
                return;


        float xSpeed=0;

        //SE PRESSIONAR LEFT + RIGHT NAO MEXEMOS O X
        if(left) { //so pressionamos a left
            xSpeed -= walkSpeed;
            flipX = width; //para invertermos a img se precisarmos de atacar ou mover p esq
            flipW = -1;
        }
        if (right) { //so pressionamos right
            xSpeed += walkSpeed;
            flipX=0;
            flipW=1;
        }
        if(!inAir){
            if(!IsEntityOnFloor(hitbox, lvlData)){
                inAir=true;
            }
        }

        if(inAir){

            if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)){
                hitbox.y+=airSpeed;
                airSpeed +=GRAVITY;
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

    public void changeHealth(int value){
        currentHealth +=value;

        if(currentHealth <=0){
            currentHealth =0;
            //gameOver();
        } else if(currentHealth >= maxHealth)
            currentHealth = maxHealth;
    }

    private void loadAnimations() {
            BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS); //decidimos o tipo de atlas a carreagr

            animations = new BufferedImage[7][8]; //tamnho do array´2 vai ser consoante oq temos na img c o conj de Sprites!
            for(int j=0; j<animations.length; j++) {
                for (int i = 0; i < animations[j].length; i++) {
                    //x=i*64 pq é o x d cada sprite. y=0 pq vamos começar na 1a linha dos sprites, os outros sao p tamanho do sprite em si
                    animations[j][i] = img.getSubimage(i * 64, j*40, 64, 40);
                }
            }
            statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
    }

    public void loadLvlData(int[][] lvlData){
        this.lvlData=lvlData;
        if(!IsEntityOnFloor(hitbox,lvlData))
            inAir=true;
    }

    public void resetDirBooleans(){
        left=false;
        right=false;
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

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJump(boolean jump){
        this.jump=jump;
    }

    public void resetAll(){
        resetDirBooleans();
        inAir=false;
        attacking=false;
        moving=false;
        state=IDLE;
        currentHealth=maxHealth;

        hitbox.x=x;
        hitbox.y=y;

        if(!IsEntityOnFloor(hitbox,lvlData))
            inAir=true;

    }
}
