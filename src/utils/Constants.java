package utils;

import main.Game;

/*
valores estaticos @nnnnpiz
 */
public class Constants {

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
        public static final int GROUND =4;
        public static final int HIT =5;
        public static final int ATTACK_1 =6;
        public static final int ATTACK_JUMP_1 =7;
        public static final int ATTACK_JUMP_2 =8;
        //adicionar mais?

        //retornar a quantidade de sprites por cada estado: (get maisculo pq metodo estatico)
        public static int GetSpriteAmount(int player_action){
            switch(player_action){
                //verificar no playyer_sprites se a qtd p cada esta correto
                case RUNNING:
                    return 6;
                case IDLE:
                    return 5;
                case HIT:
                    return 4;
                case JUMP:
                case ATTACK_1:
                case ATTACK_JUMP_1:
                case ATTACK_JUMP_2:
                    return 3; //as 4 anim de cima devolvem todas 3 &tds c mm output!
                case GROUND:
                    return 2;
                case FALLING:
                default:
                    return 1;
            }
        }

    }
}
