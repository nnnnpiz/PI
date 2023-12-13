package tests;

import java.util.ArrayList;

import static Probabilities.Probability.*;

public class tests {

    public static void main(String[] args) {
        //System.out.println(generateRandomNormalV2(50,20));
        int i =0;
        ArrayList<Integer> a = new ArrayList<>();
        while (i<1000) {
            int aux = (int) generateRandomNormalV2(100,35);
            if(aux <5)
                a.add(aux);
            i++;
        }

        System.out.println(a);

    } //em media por cada 1000 chamadas sao gerados 1 ou 2 valores =0;

    //TODO fazer evento especial apenas se o valor gerado for =0 ! faço a nuvem do tiago
}
