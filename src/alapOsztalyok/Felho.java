package alapOsztalyok;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

public class Felho {
    private Image felhoKep;
    private String nev;
    private Color szin;    
    private Font font;
    private double kx;
    private double ky;
    private double felhoKepSzelesseg;
    private double felhoKepMagassag;

    public Felho(Image felhoKep, String nev) {
        this.felhoKep = felhoKep;
        this.nev = nev;        
        szin = Color.black;
    }
    
    public void rajzol(Graphics g, ImageObserver o){
        double szovegSzelesseg, szovegMagassag;
        g.setColor(szin);
        g.setFont(font);
        szovegSzelesseg = g.getFontMetrics().getStringBounds(nev, g).getWidth();
        szovegMagassag = g.getFontMetrics().getStringBounds(nev, g).getHeight();        
        g.drawImage(felhoKep, (int)(kx - felhoKepSzelesseg / 2), (int) (ky - felhoKepMagassag / 2), (int)felhoKepSzelesseg, (int) felhoKepMagassag, o);
        g.drawString(nev, (int) (kx - szovegSzelesseg / 2), (int) (ky - (szovegMagassag / 1.5)));
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setKx(double kx) {
        this.kx = kx;
    }

    public void setKy(double ky) {
        this.ky = ky;
    }

    public void setFelhoKepSzelesseg(double felhoKepSzelesseg) {
        this.felhoKepSzelesseg = felhoKepSzelesseg;
    }

    public void setFelhoKepMagassag(double felhoKepMagassag) {
        this.felhoKepMagassag = felhoKepMagassag;
    }

    public double getKx() {
        return kx;
    }

    public double getKy() {
        return ky;
    }

    public double getFelhoKepSzelesseg() {
        return felhoKepSzelesseg;
    }

    public double getFelhoKepMagassag() {
        return felhoKepMagassag;
    }
}
