package Probabilities;
import java.util.Random;

import static java.lang.Math.exp;

public class Probability {
        private static Random random = new Random();

        /* Evento especial a dar +1000000 de score. Esta VA utiliza a funçao GenerateRandomNormalV2 e tem a utilidade de
        gerar um valor X que representa a marca (em rondas) em que vai ocorrer esse evento especial caracterizado
        no jogador receber +1M de score. Apos ser gerado X, esse evento só vai ocorrer apos essas X rondas, tendo o
        jogador que se manter vivo.

        Valores a utilizar na generateRandomNormalV2:
        mean = 100
        std_dev = 35
        */

        /* NOTA:
          Esta função a baixo, usando o metodo da Normal do java, não estava a gerar os valores corretos
          portanto decidimos partir com outra abordagem (a funçao generateRandomNormalV2), que simula o
          comportamento descrito nos histogramas de forma mais precisa!

        public static double generateRndNormal(double media, double desvio){
            double var = 0;
            var = media * desvio * random.nextGaussian();
            return var;
        }
        */

        /* VA que representa a prob de uma caixa que tem ITEMS dar spawn numa dada localizaçao, sendo o valor
        gerado a localiçao do X onde ela vai aparecer no mapa!

        Valores usados na generateRandomNormalV2:
        mean = 50
        std_dev = 20:
        */

        public static double generateRandomNormalV2(double mean, double stdDev) { //representa duas VAs consoante os valores dados como args.
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


        //Probabilidade de geração de cada tipo de adversário: 1 => crabby, 2 => shark , 3 => Cannon.
        public static int whatEnemie() { //(uma VA)
            int[] vals = {1,2,3};
            double var = random.nextDouble();
            if ( var >= 0 && var <0.4)
                return vals[0];
            if ( var >= 0.4 && var <0.8)
                return vals[1];
            else
                return vals[2];
        }


        /*
        Probabilidade de uma ronda ter um nivel diferente de dificuldade:
        ha 4 niveis de dificuldade. => cada um influencia a vida e dano que os adversarios teem/infligem, e tambem
        a quantidade de adversarios que vao dar spawn na ronda. Esse nivel de dificuldade é gerado de ronda a ronda
         */
        public static int GenerateLvlDifficulty(){ //uma VA.
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



        /* Prob. p GERAR items diferentes em cada caixa / barrel
        o vals[0] é uma opçao onde partimos o bau e nao sai nenhum item de preposito...
         */
        public static int halfRandomPotion() { //uma VA.
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


        /*Distrib POISSON:
        % do nivel de dano do player... (ataques que dao CRITIC - uma % extra do dano base que o jogador pode infligir)
        consoante o que a distrib de Poisson gerar.
         */
        public static int funPoissonSingle () { //Uma VA.
            double exp_lambda = Math.exp (-5); //constant for terminating loop
            double randUni; //uniform variable
            double prodUni = 1; //product of uniform variables
            int randPoisson= 1; //Poisson variable

            do {
                //randUni = funUniformSingle (); //generate uniform variable
                randUni = random.nextGaussian();
                prodUni = prodUni * randUni; //update product
                randPoisson++; // increase Poisson variable

            } while (prodUni > exp_lambda);

            return randPoisson;
        }



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