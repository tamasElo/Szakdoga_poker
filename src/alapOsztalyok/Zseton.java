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
    private boolean forgat;
    private double fok;

    public Zseton(byte ertek, Image zsetonKep) {
        this.ertek = ertek;
        this.zsetonKep = zsetonKep;
    } 
    
    public void rajzol(Graphics2D g2D, ImageObserver o) {
        AffineTransform at = g2D.getTransform();

        if (forgat) {
            g2D.rotate(Math.toRadians(fok), kx, ky);
        }
        
        g2D.drawImage(zsetonKep, kx-zsetonKepSzelesseg/2, ky-zsetonKepMagassag/2, zsetonKepSzelesseg, zsetonKepMagassag, o);
        
        if (forgat) {
            g2D.setTransform(at);
        }
    }

    public void forgat(double fok) {
        forgat = (fok != 0);
        this.fok = fok;
    }
        
    @Override
    public String toString() {
        return String.valueOf(ertek);
    }

    @Override
    public int compareTo(Zseton t) {
        return this.ertek-t.ertek;
    }

    public byte getErtek() {
        return ertek;
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
}