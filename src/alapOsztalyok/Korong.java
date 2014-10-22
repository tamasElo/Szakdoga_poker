package alapOsztalyok;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;

public abstract class Korong {
    private Image korongKep;
    private Image elmosodottKorongKep;
    private double kx;
    private double ky;
    private double korongKepSzelesseg;
    private double korongKepMagassag;
    private double forgat;
    private boolean elmosas;

    public Korong(Image korongKep, Image elmosodottKorongKep) {
        this.korongKep = korongKep;
        this.elmosodottKorongKep = elmosodottKorongKep;
    }   

    public void rajzol(Graphics2D g2D, ImageObserver o) {
        AffineTransform at = g2D.getTransform();
        Image kep;
        
        kep = elmosas ? elmosodottKorongKep : korongKep;
        
        if (forgat != 0) {
            g2D.rotate(Math.toRadians(forgat), kx, ky);
        }
        
        g2D.drawImage(kep, (int)(kx-korongKepSzelesseg/2), (int) (ky-korongKepMagassag/2), (int)korongKepSzelesseg, (int)korongKepMagassag, o);
        
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

    public void setKorongKepSzelesseg(double korongKepSzelesseg) {
        this.korongKepSzelesseg = korongKepSzelesseg;
    }

    public void setKorongKepMagassag(double korongKepMagassag) {
        this.korongKepMagassag = korongKepMagassag;
    }

    public void setForgat(double forgat) {
        this.forgat = forgat;
    }

    public void setElmosas(boolean elmosas) {
        this.elmosas = elmosas;
    }

    public double getKx() {
        return kx;
    }

    public double getKy() {
        return ky;
    }

    public double getKorongKepSzelesseg() {
        return korongKepSzelesseg;
    }

    public double getKorongKepMagassag() {
        return korongKepMagassag;
    }

    public double getForgat() {
        return forgat;
    }
}
