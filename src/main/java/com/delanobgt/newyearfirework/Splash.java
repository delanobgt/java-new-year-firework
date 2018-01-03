package com.delanobgt.newyearfirework;

import java.awt.Color;
import java.awt.Graphics2D;

public class Splash {
    private static final int radius = 1;
    private int maxR = Tool.getRandomIntegerInRange(50, 80);
    private double curR = 0;
    private double vel;
    private int degree;
    private String colorHex;
    
    public Splash(int degree, double vel, String colorHex) {
        this.degree = degree;
        this.vel = vel;
        this.colorHex = colorHex;
    }
    
    public void update() {
        if (!isAtMaxDistance()) {
            curR = Math.min(maxR, curR+vel);
        }
    }
    
    public boolean isAtMaxDistance() {
        return curR >= maxR;
    }
    
    public void draw(Graphics2D g) {
        if (!isAtMaxDistance()) {
            g.setColor(Color.decode(colorHex));
            g.fillOval(
                    (int)(Math.cos(Math.toRadians(degree))*curR)-radius,
                    (int)(Math.sin(Math.toRadians(degree))*curR)-radius,
                    2*radius,
                    2*radius
            );
        }
    }
}
