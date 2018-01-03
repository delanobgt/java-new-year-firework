package com.delanobgt.newyearfirework;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainFirework extends JPanel {
    private static final String[] colorHexes = new String[] {
        "#FF8548",
        "#FFD211",
        "#C477FF",
        "#00AAFF",
        "#7EFF4A"
    };
    private static final int ANIMATION_DELAY = 10;
    private static final int CANNON_DELAY = 150;
    private static final int lineR = 25;
    private int mouseX = 0;
    private int mouseY = 10;
    private volatile boolean alive = false;
    private final List<Particle> particles = new ArrayList<>();
    
    public MainFirework() {
        this.setPreferredSize(new Dimension(600, 600));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                alive = true;
                new Thread(() -> {
                    while (true) {
                        synchronized(particles) {
                            particles.add(new Particle(
                                mouseX,
                                mouseY,
                                lineR,
                                colorHexes[(int)(colorHexes.length*Math.random())]
                            ));
                        }
                        sleep(CANNON_DELAY);
                        if (!alive) break;
                    }
                }).start();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                alive = false;
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                mouseX = e.getX()-300;
                mouseY = -(e.getY()-600);
            }
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX()-300;
                mouseY = -(e.getY()-600);
            }
        });
    }
        
    @Override
    public void paint(Graphics oldG) {
//        super.paint(oldG);
        Graphics2D g = (Graphics2D)oldG;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
        
        //init the global coordinate to normal cartesian
        g.translate(300, 600);
        g.scale(1.0, -1.0);
        
        //paint background with some alpha value
        g.setColor(new Color(0, 0, 0, 50));
        g.fillRect(-300, 0, 600, 600);
        
        //set paint properties for painting particles and stuffs
        g.setColor(new Color(255, 255, 255));
        g.setStroke(new BasicStroke(14));
        
        //draw rotator
        final int radius = 20;
        g.fillOval(-radius, -radius, radius*2, radius*2);
        
        //calculate cannon axis
        double mouseR = Math.hypot(mouseX, mouseY);
        double lineX = (lineR/mouseR)*mouseX;
        double lineY = (lineR/mouseR)*mouseY;
        //draw cannon
        g.drawLine(0, 0, (int)lineX, (int)lineY);
        
        synchronized(particles) {
            for (Particle p : particles) {
                p.draw(g);
            }
        }
    }
    
    private void update() {
        synchronized(particles) {
            for (int i = particles.size()-1; i >= 0; i--) {
                if (particles.get(i).isDead()) {
                    particles.remove(i);
                } else {
                    particles.get(i).update();
                }
            }
        }
    }
    
    public void showUp() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        new Thread(() -> {
            while (true) {
                update();
                repaint();
                sleep(ANIMATION_DELAY);
            }
        }).start();
    }
    
    private static void sleep(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ex) {}
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFirework().showUp();
        });        
    }
}
