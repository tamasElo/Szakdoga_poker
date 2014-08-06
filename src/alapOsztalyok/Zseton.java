package alapOsztalyok;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;

public class Zseton implements Comparable<Zseton>{

    private byte ertek;
    private Image zsetonKep;
    private int kx;
    private int ky;
    private int zsetonKepSzelesseg;
    private int zsetonKepMagassag;
    private double forgat;

    public Zseton(byte ertek, Image zsetonKep) {
        this.ertek = ertek;
        this.zsetonKep = zsetonKep;
    } 
    
    public void rajzol(Graphics2D g2D, ImageObserver o) {
        AffineTransform at = g2D.getTransform();

        if (forgat != 0) {
            g2D.rotate(Math.toRadians(forgat), kx, ky);
        }
        
        g2D.drawImage(zsetonKep, kx-zsetonKepSzelesseg/2, ky-zsetonKepMagassag/2, zsetonKepSzelesseg, zsetonKepMagassag, o);
        
        if (forgat != 0) {
            g2D.setTransform(at);
        }
    }

    @Override
    public int compareTo(Zseton t) {
        return this.ertek - t.ertek;
    }

    public void setKx(int kx) {
        this.kx = kx;
    }

    public void setKy(int ky) {
        this.ky = ky;
    }

    public void setZsetonKepSzelesseg(int zsetonKepSzelesseg) {
        this.zsetonKepSzelesseg = zsetonKepSzelesseg;
    }

    public void setZsetonKepMagassag(int zsetonKepMagassag) {
        this.zsetonKepMagassag = zsetonKepMagassag;
    }

    public void setForgat(double forgat) {
        this.forgat = forgat;
    }

    public Image getZsetonKep() {
        return zsetonKep;
    }

    public byte getErtek() {
        return ertek;
    }

    public int getKx() {
        return kx;
    }

    public int getKy() {
        return ky;
    }

    public int getZsetonKepSzelesseg() {
        return zsetonKepSzelesseg;
    }

    public int getZsetonKepMagassag() {
        return zsetonKepMagassag;
    }

    public double getForgat() {
        return forgat;
    }
}
