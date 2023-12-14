package tests;

import java.util.ArrayList;

import static Probabilities.Probability.*;

public class tests {

    public static void main(String[] args) {
        //System.out.println(generateRandomNormalV2(50,20));
        int i =0;
        double exp_lambda = Math.exp(5);
        while (i<1000){
            int aux = funPoissonSingle()*6;

            System.out.println(aux);
            i++;
        }



    } //em media por cada 1000 chamadas sao gerados 1 ou 2 valores =0;


}
