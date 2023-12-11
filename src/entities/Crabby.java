package entities;

import levels.Level;
import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utils.Constants.Direction.LEFT;
import static utils.Constants.Direction.RIGHT;
import static utils.Constants.EnemyConstants.*;
import static utils.HelpMethods.*;

public class Crabby extends Enemy{

    private int attackBoxOffsetX;

    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitbox(22,19); //size do hitbox q qurerrmos p crabby 22x19
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x,y, (int)(82*Game.SCALE),(int)(19*Game.SCALE)); //atackbox vai ser um retangulo pq qd ele ataca extende as maos
        attackBoxOffsetX = (int)(Game.SCALE*30);
    }

    public void update(int[][] lvlData,Player player){
        updateBehaviour(lvlData, player);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateAttackBox() {
        attackBox.x = hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y;
    }

    //calcular como o codigo faz o "patrol" e mexe-se
    private void updateBehaviour(int[][] lvlData, Player player){
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
