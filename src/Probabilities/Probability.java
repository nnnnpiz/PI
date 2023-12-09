package Probabilities;
import java.util.Random;

public class Probability {

        private static Random random = new Random();

        /* Probabilidade de ocorrer um evento especial: TODO "NUVEM" rara que mata todos os inimigos vivos (isActive());
        mean = 100
        std_dev = 35
        OU
        Probabilidade de um item/powerup aparecer => TODO probabilidade de uma caixa que tem ITEMS dar spawn
        mean = 50
        std_dev = 20
         */
        public static double generateRndNormal(double media, double desvio){
            double var = 0;
            var = media * desvio * random.nextGaussian();
            return var;
        }



        /*
        TODO Probabilidade de geração de cada tipo de aversário: 1 => crabby, 2 => shark , 3 => Cannon
         */
        public static int generateRndDiscrete() {
            int[] vals = {1,2,3};
            double var = random.nextDouble();
            if ( var >= 0 && var <0.6)
                return vals[0];
            if ( var >= 0.6 && var <0.9)
                return vals[1];
            else
                return vals[2];
        }


        /*
        TODO Probabilidade de uma ronda ter um nivel diferente de dificuldade:
        TODO ha 4 niveis de dificuldade. => influencia vida e dano que os adversarios dao e a quantidade de adversarios
         */
        public static int generateRndDiscrete2() {
            int[] vals = {0,1,2,3};
            double var = random.nextDouble();
            if ( var >= 0 && var <0.7)
                return vals[1];
            if ( var >= 0.7 && var <0.9)
                return vals[2];
            if ( var >= 0.9 && var <0.975)
                return vals[2];
            else
                return vals[3];
        }

        /*
        TODO Probabilidade de geração de um item de powerup:
        o vals[0] é uma opçao onde partimos o bau e nao sai nenhum item de preposito...
         */
        public static int generateRndDiscrete3() {
            int[] vals = {1,2,3,4};
            double var = random.nextDouble();
            if ( var >=0 && var <0.05)
                return vals[1];
            if ( var >= 0.05 && var <0.45)
                return vals[2];
            if ( var >= 0.45 && var <0.8)
                return vals[2];
            else
                return vals[3];
        }

        //TODO:
        /*FALTA A DE Distrib POISSON:
        % do nivel de dano do player... (ataques que dao CRITIC - uma % extra do dano base que o jogador pode infligir)
         */


}