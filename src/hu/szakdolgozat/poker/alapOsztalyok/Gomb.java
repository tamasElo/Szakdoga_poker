package hu.szakdolgozat.poker.alapOsztalyok;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

public class Gomb {
    private final String nev;
    private final Image alapHelyzet;
    private final Image rakattintva;
    private final Image letiltva;
    private double x;
    private double y;
    private double szelesseg;
    private double magassag;
    private int megjSorszam;

    public Gomb(String nev, Image alapHelyzet, Image rakattintva, Image letiltva, double x, double y, double szelesseg, double magassag) {
        this.nev = nev;
        this.alapHelyzet = alapHelyzet;
        this.rakattintva = rakattintva;
        this.letiltva = letiltva;
        this.x = x;
        this.y = y;
        this.szelesseg = szelesseg;
        this.magassag = magassag;
    }
    
    public void rajzol(Graphics g, ImageObserver o){
        switch(megjSorszam){
            case 1 : g.drawImage(alapHelyzet, (int)x, (int)(y+magassag/15), (int)szelesseg, (int)magassag, o);
                break;
            case 2 : g.drawImage(rakattintva, (int)x, (int)y, (int)szelesseg, (int)magassag, o);
                break;
            case 3 : g.drawImage(letiltva, (int)x, (int)(y+magassag/15), (int)szelesseg, (int)magassag, o);
                break;
        }
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
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

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
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
