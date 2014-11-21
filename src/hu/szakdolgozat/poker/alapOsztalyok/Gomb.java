package hu.szakdolgozat.poker.alapOsztalyok;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

public class Gomb {

    private final String nev;
    private final Image alapHelyzet;
    private final Image rakattintva;
    private final Image letiltva;
    private double kx;
    private double ky;
    private double szelesseg;
    private double magassag;
    private int megjSorszam;

    public Gomb(String nev, Image alapHelyzet, Image rakattintva, Image letiltva, double kx, double ky, double szelesseg, double magassag) {
        this.nev = nev;
        this.alapHelyzet = alapHelyzet;
        this.rakattintva = rakattintva;
        this.letiltva = letiltva;
        this.kx = kx;
        this.ky = ky;
        this.szelesseg = szelesseg;
        this.magassag = magassag;
    }
    
    public void rajzol(Graphics g, ImageObserver o){
        switch(megjSorszam){
            case 1 : g.drawImage(alapHelyzet, (int)(kx - szelesseg / 2.0), (int)(ky - magassag / 2.0 + magassag/15.0), (int)szelesseg, (int)magassag, o);                
                break;
            case 2 : g.drawImage(rakattintva, (int)(kx - szelesseg / 2.0), (int)(ky - magassag / 2.0), (int)szelesseg, (int)magassag, o);
                break;
            case 3 : g.drawImage(letiltva, (int)(kx - szelesseg / 2.0), (int)(ky - magassag / 2.0 + magassag/15.0), (int)szelesseg, (int)magassag, o);
                break;
        }
    }

    public void setKx(double kx) {
        this.kx = kx;
    }

    public void setKy(double ky) {
        this.ky = ky;
    }

    public void setSzelesseg(double szelesseg) {
        this.szelesseg = szelesseg;
    }

    public void setMagassag(double magassag) {
        this.magassag = magassag;
    }

    public void setMegjSorszam(int megjSorszam) {
        this.megjSorszam = megjSorszam;
    }

    public String getNev() {
        return nev;
    }

    public double getKx() {
        return kx;
    }

    public double getKy() {
        return ky;
    }

    public double getSzelesseg() {
        return szelesseg;
    }

    public double getMagassag() {
        return magassag;
    }
    public int getMegjSorszam() {
        return megjSorszam;
    }
}
