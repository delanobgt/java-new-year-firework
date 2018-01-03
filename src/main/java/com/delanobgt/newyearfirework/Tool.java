package com.delanobgt.newyearfirework;

public class Tool {
    
    public static int getRandomIntegerInRange(int a, int b) {
        return ( (int)(Math.random()*(b-a+1)) ) + a;
    }
    
}
