package alapOsztalyok;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;

public abstract class Korong {
    private Image korongKep;
    private int kx;
    private int ky;
    private int korongKepSzelesseg;
    private int korongKepMagassag;
    private double forgat;
    
    public Korong(Image korongKep){
        this.korongKep = korongKep;        
    }

    public void rajzol(Graphics2D g2D, ImageObserver o) {
        AffineTransform at = g2D.getTransform();

        if (forgat != 0) {
            g2D.rotate(Math.toRadians(forgat), kx, ky);
        }
        
        g2D.drawImage(korongKep, kx-korongKepSzelesseg/2, ky-korongKepMagassag/2, korongKepSzelesseg, korongKepMagassag, o);
        
        if (forgat != 0) {
            g2D.setTransform(at);
        }
    }
    
    public void setKx(int kx) {
        this.kx = kx;
    }

    public void setKy(int ky) {
        this.ky = ky;
    }

    public void setKorongKepSzelesseg(int korongKepSzelesseg) {
        this.korongKepSzelesseg = korongKepSzelesseg;
    }

    public void setKorongKepMagassag(int korongKepMagassag) {
        this.korongKepMagassag = korongKepMagassag;
    }

    public void setForgat(double forgat) {
        this.forgat = forgat;
    }

    public int getKx() {
        return kx;
    }

    public int getKy() {
        return ky;
    }

    public int getKorongKepSzelesseg() {
        return korongKepSzelesseg;
    }

    public int getKorongKepMagassag() {
        return korongKepMagassag;
    }

    public double getForgat() {
        return forgat;
    }
}
