package hu.szakdolgozat.poker.alapOsztalyok;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

public class Toltes {
    private Image toltoKep;
    private double kx;
    private double ky;
    private double toltoKepSzelesseg;
    private double toltoKepMagassag;
    
    public void rajzol(Graphics g, ImageObserver o) {
        if (toltoKep != null) {
            g.drawImage(toltoKep, (int) (kx - toltoKepSzelesseg / 2), (int) (ky - toltoKepMagassag / 2),
                    (int) toltoKepSzelesseg, (int) toltoKepMagassag, o);
        }
    }

    public void setToltoKep(Image toltoKep) {
        this.toltoKep = toltoKep;
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
}
