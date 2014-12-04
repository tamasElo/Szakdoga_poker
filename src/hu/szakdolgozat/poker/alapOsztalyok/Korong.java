package hu.szakdolgozat.poker.alapOsztalyok;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.io.Serializable;
import javax.swing.ImageIcon;

public abstract class Korong implements Serializable{

    private ImageIcon korongKep;
    private ImageIcon elmosodottKorongKep;
    private transient double kx;
    private transient double ky;
    private transient double korongKepSzelesseg;
    private transient double korongKepMagassag;
    private double forgat;
    private boolean elmosas;

    public Korong(ImageIcon korongKep, ImageIcon elmosodottKorongKep) {
        this.korongKep = korongKep;
        this.elmosodottKorongKep = elmosodottKorongKep;
    }   

    public void rajzol(Graphics2D g2D, ImageObserver o) {
        AffineTransform at = g2D.getTransform();
        ImageIcon kep;
        
        kep = elmosas ? elmosodottKorongKep : korongKep;
        
        if (forgat != 0) {
            g2D.rotate(Math.toRadians(forgat), kx, ky);
        }
        
        g2D.drawImage(kep.getImage(), (int)(kx-korongKepSzelesseg/2), (int) (ky-korongKepMagassag/2), (int)korongKepSzelesseg, (int)korongKepMagassag, o);
        
        if (forgat != 0) {
            g2D.setTransform(at);
        }
    }

    public void setKorongKep(ImageIcon korongKep) {
        this.korongKep = korongKep;
    }

    public void setElmosodottKorongKep(ImageIcon elmosodottKorongKep) {
        this.elmosodottKorongKep = elmosodottKorongKep;
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
