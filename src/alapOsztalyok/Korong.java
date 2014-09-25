package alapOsztalyok;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;

public abstract class Korong {
    private Image korongKep;
    private double kx;
    private double ky;
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
        
        g2D.drawImage(korongKep, (int)(kx-korongKepSzelesseg/2), (int) (ky-korongKepMagassag/2), korongKepSzelesseg, korongKepMagassag, o);
        
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
    
    public void setKorongKepSzelesseg(int korongKepSzelesseg) {
        this.korongKepSzelesseg = korongKepSzelesseg;
    }

    public void setKorongKepMagassag(int korongKepMagassag) {
        this.korongKepMagassag = korongKepMagassag;
    }

    public void setForgat(double forgat) {
        this.forgat = forgat;
    }

    public double getKx() {
        return kx;
    }

    public double getKy() {
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
