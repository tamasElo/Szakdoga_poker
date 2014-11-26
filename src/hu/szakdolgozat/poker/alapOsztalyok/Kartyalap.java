package hu.szakdolgozat.poker.alapOsztalyok;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class Kartyalap implements Serializable, Comparable<Kartyalap> {

    private ImageIcon elolap;
    private final ImageIcon hatlap;
    private final ImageIcon elmosodottHatlap;
    private final ImageIcon keret;
    private double kx;
    private double ky;
    private double kartyaKepSzelesseg;
    private double kartyaKepMagassag;
    private double keretKepSzelesseg;
    private double keretKepMagassag;
    private final byte kartyaErtek;
    private final String kartyaNev;
    private final String kartyaSzin;
    private boolean mutat;
    private boolean keretRajzol;
    private boolean elmosas;
    private double forgat;

    public Kartyalap(ImageIcon elolap, String kartyaNev, String kartyaSzin, byte kartyaErtek) {
        this.elolap = elolap;
        this.kartyaNev = kartyaNev;
        this.kartyaSzin = kartyaSzin;
        this.kartyaErtek = kartyaErtek;
        hatlap = new ImageIcon(this.getClass().getResource("/hu/szakdolgozat/poker/adatFajlok/kartyaPakli/hatlap.png"));
        elmosodottHatlap = new ImageIcon(this.getClass().getResource("/hu/szakdolgozat/poker/adatFajlok/kartyaPakli/hatlap_blur.png"));
        keret = new ImageIcon(this.getClass().getResource("/hu/szakdolgozat/poker/adatFajlok/kartyaPakli/keret.png"));
    }
    
    public void rajzol(Graphics2D g2D, ImageObserver o){
        AffineTransform at = g2D.getTransform();          
        ImageIcon kep;
        kep = elmosas ? elmosodottHatlap : hatlap;
        
        if (forgat != 0) {
            g2D.rotate(Math.toRadians(forgat), kx, ky);
        } 
        
        if (mutat) {
            if (keretRajzol) {
                g2D.drawImage(keret.getImage(), (int) (kx - keretKepSzelesseg / 2),
                        (int) (ky - keretKepMagassag / 2),
                        (int) keretKepSzelesseg, (int) keretKepMagassag, o);
            }

            g2D.drawImage(elolap.getImage(), (int) (kx - kartyaKepSzelesseg / 2),
                    (int) (ky - kartyaKepMagassag / 2),
                    (int) kartyaKepSzelesseg, (int) kartyaKepMagassag, o);
        } else {

            g2D.drawImage(kep.getImage(), (int) (kx - kartyaKepSzelesseg / 2),
                    (int) (ky - kartyaKepMagassag / 2),
                    (int) kartyaKepSzelesseg, (int) kartyaKepMagassag, o);
        }

        if (forgat != 0) {
            g2D.setTransform(at);
        }
    }   
    
    @Override
    public int compareTo(Kartyalap t) {
        return this.kartyaErtek-t.getKartyaErtek();
    }

    public void setElolap(ImageIcon elolap) {
        this.elolap = elolap;
    }
    
    public void setKx(double kx) {
        this.kx = kx;
    }

    public void setKy(double ky) {
        this.ky = ky;
    }

    public void setKartyaKepSzelesseg(double kartyaKepSzelesseg) {
        this.kartyaKepSzelesseg = kartyaKepSzelesseg;
    }

    public void setKartyaKepMagassag(double kartyaKepMagassag) {
        this.kartyaKepMagassag = kartyaKepMagassag;
    }

    public void setKeretKepSzelesseg(double keretKepSzelesseg) {
        this.keretKepSzelesseg = keretKepSzelesseg;
    }

    public void setKeretKepMagassag(double keretKepMagassag) {
        this.keretKepMagassag = keretKepMagassag;
    }
    public void setMutat(boolean mutat) {
        this.mutat = mutat;
    }

    public void setKeretRajzol(boolean keretRajzol) {
        this.keretRajzol = keretRajzol;
    }

    public void setElmosas(boolean elmosas) {
        this.elmosas = elmosas;
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

    public double getKartyaKepSzelesseg() {
        return kartyaKepSzelesseg;
    }

    public double getKartyaKepMagassag() {
        return kartyaKepMagassag;
    }

    public double getKeretKepSzelesseg() {
        return keretKepSzelesseg;
    }

    public double getKeretKepMagassag() {
        return keretKepMagassag;
    }

    public byte getKartyaErtek() {
        return kartyaErtek;
    }

    public String getKartyaNev() {
        return kartyaNev;
    }

    public String getKartyaSzin() {
        return kartyaSzin;
    }

    public boolean isMutat() {
        return mutat;
    }

    public boolean isKeretRajzol() {
        return keretRajzol;
    }

    public double getForgat() {
        return forgat;
    }    
}
