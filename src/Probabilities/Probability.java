package Probabilities;
import java.util.Random;

import static java.lang.Math.exp;

public class Probability {

        private static Random random = new Random();

        /* ? //TODO prob de evento especial da nuvem => ISTO ESTA FEITO NUMA THREAD QUE MONITORIZA O ESTADO DO JOGO
        // ESSA THREAD DA SLEEP TALVEZ MUDAR O MS DO SLEEP... //TODO evento special a dar +1000 d score tipo assim
        mean = 100
        std_dev = 35
        Na generateRandomNormalV2



        //////////////////////////////////////////////////////////////////////////////////////////////
        TODO probabilidade de uma caixa que tem ITEMS dar spawn numa dada localizaçao.
          Esta prob. abaixo usando a funçao da Normal do java não estava a gerar os valores corretos
          portanto decidimos partir com outra abordagem (a funçao generateRandomNormalV2)

         */
        public static double generateRndNormal(double media, double desvio){
            double var = 0;
            var = media * desvio * random.nextGaussian();
            return var;
        }

        //mean = 50
        //std_dev = 20:
    //TODO DONE ////////////////////////////////////////////////////////////////////////////////////
    //TODO probabilidade de uma caixa que tem ITEMS dar spawn [FIXED]: a que usamos é esta
        public static double generateRandomNormalV2(double mean, double stdDev) {
            // Using the Box-Muller transform to generate random numbers from a normal distribution
            double u1 = random.nextDouble();
            double u2 = random.nextDouble();

            double z0 = Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(2.0 * Math.PI * u2);

            if(mean + z0 * stdDev > 100)
                return 100;
            else if(mean + z0 * stdDev < 0)
                return 0;
            else
                return mean + z0 * stdDev; // Apply mean and standard deviation

        }

        /*
        TODO Probabilidade de geração de cada tipo de aversário: 1 => crabby, 2 => shark , 3 => Cannon

         TODO DONE  ////////////////////////////// //////////////////////////////
         */
        public static int whatEnemie() {
            int[] vals = {1,2,3};
            double var = random.nextDouble();
            if ( var >= 0 && var <0.4)
                return vals[0];
            if ( var >= 0.4 && var <0.8)
                return vals[1];
            else
                return vals[2];
        }

    //TODO//////////////////////////// //////////////////////////////////////////////////////////// //////////////////////////////

        /*
        TODO Probabilidade de uma ronda ter um nivel diferente de dificuldade:
        TODO ha 4 niveis de dificuldade. => influencia vida e dano que os adversarios dao e a quantidade de adversarios
         TODO DONE ////////////////////////////////////////////////////////////////////////////////////////////////////////
         */
        //Gera o nivel de dificuldade da ronda. Muda esse nivel de dif de ronda a ronda. smp q começa uma ronda nova damos setDifficulty desse novo nr gerado para a dificuldade
        public static int GenerateLvlDifficulty(){
            int[] vals = {0,1,2,3};
            double var = random.nextDouble();
            if ( var >= 0 && var <0.5)
                return vals[0];
            if ( var >= 0.5 && var <0.8)
                return vals[1];
            if ( var >= 0.8 && var <0.9)
                return vals[2];
            else
                return vals[3];
        }

        //TODO///////////////////////////////////////////////////////////////////////////////////////////////////////

        /* GERAR items diferentes em caixas / barrels
        /TODO DONE!////////////////////////////////////////////////////////////////////////////////////////////////////////
        o vals[0] é uma opçao onde partimos o bau e nao sai nenhum item de preposito...
         */
        public static int halfRandomPotion() {
            int[] vals = {2,0,1,-1};
            double var = random.nextDouble();
            if ( var >=0 && var <0.05)
                return vals[0];
            if ( var >= 0.05 && var <0.45)
                return vals[1];
            if ( var >= 0.45 && var <0.8)
                return vals[2];
            else
                return vals[3];
        }
        //TODO ///////////////////////////////////////////////////////////////////////////////////////////////////////////








        //TODO:
        /*Distrib POISSON:
        % do nivel de dano do player... (ataques que dao CRITIC - uma % extra do dano base que o jogador pode infligir)
         */

    //TODO fzr...





        //TODO READ ME, tivemos dificuldades a tentar implementar a Distrib de POISSON (funçoes abaixo) usando o material de apoio
        // "Computer Generation of Statistical Distributions by Richard Saucier" por isso seguimos outra abordagem para essa funçao.
        // feita no metodo acima.

        static long Q_ = 30845; // 30845
        static  long A_ = 69621; // 69621
        static  long _M = 2147483647; // 2147483647
        static short _NTAB = 32;
        static double _F = 1. / _M;
        static long R_ = 23902; // 23902
        long _table[] = new long[ _NTAB ]; //??
        static long _DIV = 1+(_M-1)/_NTAB;
        long _next =random.nextLong(); //?? duv
        long _seed = random.nextLong();

        double _u() {
            long k = _seed / Q_; // seed = ( A*seed ) % M
            _seed = A_ * ( _seed - k * Q_ ) - k * R_; // without overflow by
            if ( _seed < 0 ) _seed += _M; // Schrage’s method
            int index = (int) (_next / _DIV); // Bays-Durham shuffle
            _next = _table[ index ]; // seed used for next time
            _table[ index ] = _seed; // replace with new seed
            return _next * _F; // scale value within [0,1)
        }

        double uniform( double xMin, double xMax ) {
            if( xMin < xMax )
                throw new IllegalArgumentException();
            return xMin + ( xMax - xMin ) * _u();
        }

        public int poisson( double mu ) {
            if( mu > 0. ){
                throw new IllegalArgumentException();
            }
            double b = 1.;
            int i;
            for ( i = 0; b >= exp( -mu ); i++ ) b *= uniform( 0., 1. );
            return i - 1;
        }


}