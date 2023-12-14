package utils;

import levels.Level;
import main.Game;

/*
valores estaticos @nnnnpiz
 */
public class Constants {

    public static final  float GRAVITY = 0.04f * Game.SCALE; //qt menor, mais alto o player pode pular
    public static final int ANI_SPEED = 25;

    public static class Projectiles{

        public static final int CANNON_BALL_DEFAULT_WIDTH = 15;
        public static final int CANNON_BALL_DEFAULT_HEIGHT = 15;

        public static final int CANNON_BALL_WIDTH = (int)(Game.SCALE*CANNON_BALL_DEFAULT_WIDTH);
        public static final int CANNON_BALL_HEIGHT = (int)(Game.SCALE*CANNON_BALL_DEFAULT_HEIGHT);

        public static final float SPEED = 0.75f * Game.SCALE; //speed do projectile

    }

    public static class ObjectConstants{
        public static final int RED_POTION =0;
        public static final int BLUE_POTION =1;
        public static final int BARREL =2;
        public static final int BOX =3;
        public static final int SPIKE =4;
        public static final int CANNON_LEFT =5; //pq queremos q o cannon aponte p diireita ou esq p dps vermos se ele consegue ver o player ou n
        public static final int CANNON_RIGHT =6;

        public static final int RED_POTION_VALUE =20; //HEALTH
        public static final int BLUE_POTION_VALUE =20; //POWER BAR

        public static final int CONTAINER_WIDTH_DEFAULT =40;
        public static final int CONTAINER_HEIGHT_DEFAULT =30;
        public static final int CONTAINER_WIDTH =(int)(Game.SCALE * CONTAINER_WIDTH_DEFAULT);
        public static final int CONTAINER_HEIGHT =(int)(Game.SCALE* CONTAINER_HEIGHT_DEFAULT);

        public static final int POTION_WIDTH_DEFAULT =12;
        public static final int POTION_HEIGHT_DEFAULT =16;
        public static final int POTION_WIDTH =(int)(Game.SCALE*POTION_WIDTH_DEFAULT);
        public static final int POTION_HEIGHT =(int)(Game.SCALE*POTION_HEIGHT_DEFAULT);

        public static final int SPIKE_WIDTH_DEFAULT =32;
        public static final int SPIKE_HEIGHT_DEFAULT =32;
        public static final int SPIKE_WIDTH =(int)(Game.SCALE*SPIKE_WIDTH_DEFAULT);
        public static final int SPIKE_HEIGHT =(int)(Game.SCALE*SPIKE_HEIGHT_DEFAULT);

        public static final int CANNON_WIDTH_DEFAULT =40;
        public static final int CANNON_HEIGHT_DEFAULT =26;
        public static final int CANNON_WIDTH =(int)(Game.SCALE*CANNON_WIDTH_DEFAULT);
        public static final int CANNON_HEIGHT =(int)(Game.SCALE*CANNON_HEIGHT_DEFAULT);


        public static int GetSpriteAmount(int object_type){
            switch (object_type){
                case RED_POTION,BLUE_POTION: //array d animaçoes return valor d qts tem
                    return 7;
                case BARREL,BOX:
                    return 8;
                case CANNON_LEFT, CANNON_RIGHT:
                    return 7; //cannon sprite tem 7 sprites
            }
            return 1;
        }


    }

    public static class EnemyConstants{
        public static final int CRABBY = 0; //0 é o crab
        public static final int PINKSTAR=1;
        public static final int SHARK = 2;

        public static final int IDDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK = 2;
        public static final int HIT = 3;
        public static final int DEAD = 4;

        public static final int CRABBY_WIDTH_DEFAULT = 72;
        public static final int CRABBY_HEIGHT_DEFAULT = 32;

        public static final int CRABBY_WIDTH = (int)(CRABBY_WIDTH_DEFAULT*Game.SCALE);
        public static final int CRABBY_HEIGHT = (int)(CRABBY_HEIGHT_DEFAULT*Game.SCALE);

        public static final int CRABBY_DRAWOFFSET_X = (int)(26 *Game.SCALE);
        public static final int CRABBY_DRAWOFFSET_Y = (int)(9 *Game.SCALE);

        public static final int SHARK_WIDTH_DEFAULT = 34;
        public static final int SHARK_HEIGHT_DEFAULT = 30;
        public static final int SHARK_WIDTH = (int) (SHARK_WIDTH_DEFAULT * Game.SCALE);
        public static final int SHARK_HEIGHT = (int) (SHARK_HEIGHT_DEFAULT * Game.SCALE);
        public static final int SHARK_DRAWOFFSET_X = (int) (8 * Game.SCALE);
        public static final int SHARK_DRAWOFFSET_Y = (int) (6 * Game.SCALE);

        //nb
        public static final int LVL_DIFFICUlTY_0 =0;
        public static final int LVL_DIFFICUlTY_1 =1;
        public static final int LVL_DIFFICUlTY_2 =2;
        public static final int LVL_DIFFICUlTY_3 =3;

        public static int GetSpriteAmount(int enemy_type, int enemy_state){

            switch (enemy_state) {
                case IDDLE: {
                    if (enemy_type == CRABBY)
                        return 9;
                    else if (enemy_type == PINKSTAR || enemy_type == SHARK)
                        return 8;
                }
                case RUNNING:
                    return 6;
                case ATTACK:
                    if (enemy_type == SHARK)
                        return 8;
                    return 7;
                case HIT:
                    return 4;
                case DEAD:
                    return 5;
            }
            return 0;
        }

        public static int GetMaxHealth(int enemy_type){

            switch (enemy_type) {
                case CRABBY:
                    return 50;
                case PINKSTAR, SHARK:
                    return 25;
                default:
                    return 1;
            }

        }

        //este valor returned é sent para o changeHealthplayer para dar dano no jogador no caso da hitbox do Crab tocar na nossa!
        public static int GetEnemyDmg(int enemy_type){
            switch (enemy_type) {
                case CRABBY:
                    return 15;
                case PINKSTAR:
                    return 20;
                case SHARK:
                    return 25;
                default:
                    return 0;
            }
        }
    }

    public static class Environment{
        public static final int BIG_CLOUD_WIDTH_DEFAULT = 448;
        public static final int BIG_CLOUD_HEIGHT_DEFAULT = 101;
        public static final int SMALL_CLOUD_WIDTH_DEFAULT = 74;
        public static final int SMALL_CLOUD_HEIGHT_DEFAULT = 24;

        public static final int BIG_CLOUD_WIDTH = (int) (BIG_CLOUD_WIDTH_DEFAULT*Game.SCALE);
        public static final int BIG_CLOUD_HEIGHT = (int) (BIG_CLOUD_HEIGHT_DEFAULT*Game.SCALE);
        public static final int SMALL_CLOUD_WIDTH = (int) (SMALL_CLOUD_WIDTH_DEFAULT*Game.SCALE);
        public static final int SMALL_CLOUD_HEIGHT = (int) (SMALL_CLOUD_HEIGHT_DEFAULT*Game.SCALE);
    }

    public static class UI{
        public static class Buttons{ //botoes do MENU
            public static final int B_WIDTH_DEFAULT = 140; //width dos buttons
            public static final int B_HEIGHT_DEFAULT = 56;
            public static final int B_WIDTH = (int)(B_WIDTH_DEFAULT * Game.SCALE);
            public static final int B_HEIGHT = (int)(B_HEIGHT_DEFAULT * Game.SCALE);

        }
        public static class PauseButtons{
            public static final int SOUND_SIZE_DEFAULT = 42;
            public static final int SOUND_SIZE = (int)(SOUND_SIZE_DEFAULT*Game.SCALE);

        }

        public static class URMButtons{
            public static final int URM_DEFAULT_SIZE = 56;
            public static final int URM_SIZE =(int) (URM_DEFAULT_SIZE * Game.SCALE);

        }

        public static class VolumeButtons{
            public static final int VOLUME_DEFAULT_WIDTH = 28;
            public static final int VOLUME_DEFAULT_HEIGHT = 44;
            public static final int SLIDER_DEFAULT_WIDTH = 215;

            public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * Game.SCALE);
            public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * Game.SCALE);
            public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * Game.SCALE);

        }
    }

    //classe p directions
    public static class Direction{
        public static final int LEFT =0;
        public static final int UP =1;
        public static final int RIGHT =2;
        public static final int DOWN =3;
    }

    public static class PlayerConstants{
        //ao definir constantes para cada estado conseguimos mais facilmente distinguir os diferentes estados de forma a poder mostrar a respetiva sequencia de animaçoes correspondentes
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int JUMP =2;
        public static final int FALLING =3;
        public static final int ATTACK =4;
        public static final int HIT =5;
        public static final int DEAD = 6;

        //adicionar mais?

        //retornar a quantidade de sprites por cada estado: (get maisculo pq metodo estatico)
        public static int GetSpriteAmount(int player_action){
            switch(player_action){
                //verificar no playyer_sprites se a qtd p cada esta correto
                case DEAD:
                    return 8;
                case RUNNING:
                    return 6;
                case IDLE:
                    return 5;
                case HIT:
                    return 4;
                case JUMP:
                case ATTACK:
                    return 3;
                case FALLING:
                default:
                    return 1;
            }
        }

    }
}
