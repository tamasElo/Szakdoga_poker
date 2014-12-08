package hu.szakdolgozat.poker.alapOsztalyok;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;

public class Toltes {
    private Image toltoKep;
    private double kx;
    private double ky;
    private double toltoKepSzelesseg;
    private double toltoKepMagassag;
    private double forgat;

    public Toltes(Image toltoKep) {
        this.toltoKep = toltoKep;
    }
    
    public void rajzol(Graphics2D g2D, ImageObserver o) {
        AffineTransform at = g2D.getTransform();
        
        if (forgat != 0) {
            g2D.rotate(Math.toRadians(forgat), kx, ky);
        }
        
        if (toltoKep != null) {
            g2D.drawImage(toltoKep, (int) (kx - toltoKepSzelesseg / 2), (int) (ky - toltoKepMagassag / 2),
                    (int) toltoKepSzelesseg, (int) toltoKepMagassag, o);
        }
        
        if (forgat != 0) {
            g2D.setTransform(at);
        }
    }

    public void setKx(double kx) {
        this.kx = kx;
    }

    public void setKy(double ky) {
        this.ky = ky;
    }

    public void setToltoKepSzelesseg(double toltoKepSzelesseg) {
        this.toltoKepSzelesseg = toltoKepSzelesseg;
    }

    public void setToltoKepMagassag(double toltoKepMagassag) {
        this.toltoKepMagassag = toltoKepMagassag;
    }    

    public void setForgat(double forgat) {
        this.forgat = forgat;
    }
}
