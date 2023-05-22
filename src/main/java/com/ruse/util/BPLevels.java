package com.ruse.util;

public class BPLevels {

    public static void main(String[] args){
        double buff = 0.68f;
        double exp = 1000;
        for(int i = 1; i < 50; i++){
            exp += Math.floor(i + 962 * Math.pow(2, i / 9.0));
            double exp2 = Math.floor(exp / 4);
            double exp3 = Math.floor(exp2 * buff);
            System.out.println("Level: " + i + " Exp: " + exp + " Exp2: " + exp2 + " Exp3: " + exp3);
        }
    }
}
