package entities;
import gamestates.Playing;
import main.Game;

import java.awt.geom.Rectangle2D;

import static utils.Constants.ANI_SPEED;
import static utils.Constants.EnemyConstants.*;
import static utils.Constants.GRAVITY;
import static utils.HelpMethods.*;
import static utils.Constants.Direction.*;

public abstract class Enemy extends  Entity{


    //2 enemies por enquanto
    protected int enemyType; //idle attacking wtv para o enemyState

    protected boolean firstUpdate=true;

    protected int walkDir = LEFT;
    protected int tileY;
    protected float attackDistance = Game.TILES_SIZE;
    protected boolean active=true; //enemy esta ativo
    protected boolean attackChecked;


    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType=enemyType;
        maxHealth = GetMaxHealth(enemyType); //vida dod crab
        currentHealth = maxHealth;
        walkSpeed = Game.SCALE * 0.35f; //TODO MUDAR ISTO PARA VA, speed do inimigo
    }

    protected void inAirChecks(int[][] lvlData, Playing playing) {
        if (state != HIT && state != DEAD) {
            updateInAir(lvlData);
            playing.getObjectManager().checkSpikesTouched(this);
        }
    }

    protected void firstUpdateCheck(int[][] lvlData){
        if (!IsEntityOnFloor(hitbox, lvlData)) //n estamos no floor
            inAir = true;
        firstUpdate = false;
    }

    protected void updateInAir(int[][] lvlData){
        if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)){
            hitbox.y +=airSpeed;
            airSpeed +=GRAVITY;
        } else {
            inAir = false;
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
            tileY = (int)(hitbox.y / Game.TILES_SIZE);
        }
    }

    protected void move(int[][] lvlData){
        float xSpeed=0;

        if(walkDir == LEFT)
            xSpeed =-walkSpeed;
        else
            xSpeed =walkSpeed;

        if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
            if(IsFloor(hitbox, xSpeed,lvlData)) { //check se esta no chao ! tem q haver chao
                hitbox.x += xSpeed;
                return;
            }
        changeWalkDir();
    }

    protected void turnTowardsPlayer(Player player){
        if(player.hitbox.x > hitbox.x) //significa q o player esta a direita do Crab
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    //conseguimos ver  o jogador?
    protected  boolean canSeePlayer(int[][] lvlData, Player player){
        //teem q estar no mm YPos. o Y do inimigo nnc vai mudar!!
        int playerTileY = (int)(player.getHitbox().y /Game.TILES_SIZE);
        if(playerTileY == tileY)
            if(isPlayerInRange(player)){
                if(IsSightClear(lvlData, hitbox, player.hitbox, tileY)) //se csg ver o jogador e se nao tem obstacles na frente dele
                    return true;
            }
        return false;

    }

    protected boolean isPlayerInRange(Player player) {
        int absValue =(int) Math.abs(player.hitbox.x - hitbox.x); //valor absoluto p dar a distancia
        return absValue <= attackDistance*5; //true logo esta in Range!
    }

    protected boolean isPlayerCloseForAttack(Player player){
        int absValue =(int) Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackDistance; //diferença no RANGE!
    }

    protected void newState(int enemyState){
        this.state = enemyState;
        aniTick=0;
        aniIndex=0; //smp q mudamos o state temos uma nova animation do 0
    }

    public void hurt(int amount) {
        currentHealth -=amount;
        if(currentHealth<=0)
            newState(DEAD);
        else
            newState(HIT);
    }

    protected void checkEnemyHit(Rectangle2D.Float attackBox,Player player) {
        if(attackBox.intersects(player.hitbox))
            player.changeHealth(-GetEnemyDmg(enemyType));
        attackChecked = true;
    }

    protected void updateAnimationTick(){
        aniTick++;
        if(aniTick>=ANI_SPEED){
            aniTick=0;
            aniIndex++;
            if(aniIndex >= GetSpriteAmount(enemyType, state)){
                aniIndex=0;

                switch(state){
                    case ATTACK, HIT -> state = IDDLE;//mudamos o state p iddle mal acabamos o ATTACK!
                    case DEAD -> active=false; //mal um morreu deixo de dar update nele nem dar draw logo isto serve de flag!
                }

            }
        }

    }

    protected void changeWalkDir() {
        if(walkDir == LEFT)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    public void resetEnemy(){
        hitbox.x=x;
        hitbox.y=y;
        firstUpdate=true;
        currentHealth=maxHealth;
        newState(IDDLE);
        active=true;
        airSpeed=0;
    }

    public boolean isActive(){
        return active;
    }


}
