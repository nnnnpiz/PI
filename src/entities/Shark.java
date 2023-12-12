package entities;


import static utils.Constants.Direction.LEFT;
import static utils.Constants.Direction.RIGHT;
import static utils.Constants.EnemyConstants.*;
import static utils.HelpMethods.CanMoveHere;
import static utils.HelpMethods.IsFloor;
import gamestates.Playing;
import main.Game;

import java.awt.geom.Rectangle2D;

public class Shark extends Enemy {

    private int attackBoxOffsetX;

    public Shark(float x, float y) {
        super(x, y, SHARK_WIDTH, SHARK_HEIGHT, SHARK);
        initHitbox(18, 22);
        initAttackBox(); //w, h, attackBBoxOffset
    }

    private void initAttackBox(){
        attackBox = new Rectangle2D.Float(x,y, (int)(20* Game.SCALE),(int)(20* Game.SCALE));
        attackBoxOffsetX = (int)(Game.SCALE*20);
    }

    public void update(int[][] lvlData, Player player) {
        updateBehavior(lvlData, player);
        updateAnimationTick();
        updateAttackBoxFlip();
    }

    private void updateAttackBox() {
        attackBox.x = hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y;
    }

    //p shark mudar a attack box consoante a direçao q esta virado
    protected void updateAttackBoxFlip() {
        if (walkDir == RIGHT)
            attackBox.x = hitbox.x + hitbox.width;
        else
            attackBox.x = hitbox.x - attackBoxOffsetX;

        attackBox.y = hitbox.y;
    }


    private void updateBehavior(int[][] lvlData, Player player) {
        if(firstUpdate)
            firstUpdateCheck(lvlData);

        if(inAir)
            updateInAir(lvlData);
        else { //PATROL: (se nao estou no ar)
            switch(state){
                case IDDLE:
                    newState(RUNNING); //isto pq o enemy esta smp a correr
                    break;
                case RUNNING:
                    if(canSeePlayer(lvlData, player)) {
                        turnTowardsPlayer(player);
                        if (isPlayerCloseForAttack(player))
                            newState(ATTACK);
                    }

                    move(lvlData);
                    break;
                case ATTACK:
                    if(aniIndex ==0)
                        attackChecked=false; //reset no atack checked smp q dar a volta nas animaçoes

                    if(aniIndex==3 && !attackChecked) //index=3 é o sprite onde o inimigo demonstra o ataque e é ai q vamos verificar se atinje o player
                        checkEnemyHit(attackBox,player);
                    break;
                case HIT:
                    break;
            }
        }
    }

    //TESTAR p futura implementaçao um ataque diferente
    protected void attackMove(int[][] lvlData, Playing playing) {
        float xSpeed = 0;

        if (walkDir == LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;

        if (CanMoveHere(hitbox.x + xSpeed * 4, hitbox.y, hitbox.width, hitbox.height, lvlData))
            if (IsFloor(hitbox, xSpeed * 4, lvlData)) {
                hitbox.x += xSpeed * 4;
                return;
            }
        newState(IDDLE);
    }

    public int flipX(){
        if(walkDir == RIGHT)
            return width;
        else
            return 0;
    }

    public int flipW(){
        if(walkDir == RIGHT)
            return -1;
        else
            return 1;
    }



}

