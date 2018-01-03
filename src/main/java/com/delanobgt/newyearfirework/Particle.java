package com.delanobgt.newyearfirework;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class Particle {
    private static final int radius = 5;
    private final double oneX;
    private final double oneY;
    private final double acc = -0.05;
    private double vel = Tool.getRandomIntegerInRange(4, 7);
    private double posR;
    private boolean exploding = false;
    private String colorHex;
    private List<Splash> splashes = new ArrayList<>();
    
    public Particle(int mouseX, int mouseY, int posR, String colorHex) {
        double mouseR = Math.hypot(mouseX, mouseY);
        this.oneX = (1.0/mouseR)*mouseX;
        this.oneY = (1.0/mouseR)*mouseY;
        this.posR = posR;
        this.colorHex = colorHex;
    }
    
    public void update() {
        if (exploding) {
            for (Splash s : splashes) {
                s.update();
            }
        } else if (this.vel <= 1) { //firework reach peak
            exploding = true;
            int denom = Tool.getRandomIntegerInRange(5, 10);
            for (int i = 0; i < 360; i += 22) {
                splashes.add(new Splash(
                        i,
                        5.0/denom,
                        colorHex
                ));
            }
        } else { //firework ascending to the sky
            this.vel += acc;
            this.posR += vel;
        }
    }
    
    public void draw(Graphics2D g) {
        if (exploding) {
            g.translate((int)(posR*oneX), (int)(posR*oneY));
            for (Splash s : splashes) {
                s.draw(g);
            }
            g.translate(-(int)(posR*oneX), -(int)(posR*oneY));
        } else {
            g.setColor(Color.decode(colorHex));
            g.fillOval(
                    (int)(posR*oneX-radius),
                    (int)(posR*oneY-radius),
                    2*radius,
                    2*radius
            );
        }
    }
    
    public boolean isDead() {
        return exploding && splashes.get(0).isAtMaxDistance();
    }
    
}
