package hu.szakdolgozat.poker.alapOsztalyok;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.io.Serializable;

public class Jatekos implements Serializable{

    private String nev;
    private Font font;
    private Color szin;
    private boolean aktiv;
    private static byte index;
    private byte sorszam;
    private transient double kx;
    private transient double ky;
    private String pokerKezNev;

    public Jatekos(String nev) {
        this.nev = nev;
        szin = Color.white;
        sorszam = index++;
    }
    
    public void rajzol(Graphics2D g2D, ImageObserver o){
        double nevSzelesseg, nevMagassag;
        g2D.setColor(szin);
        g2D.setFont(font);        
        nevSzelesseg = g2D.getFontMetrics().getStringBounds(nev, g2D).getWidth();
        nevMagassag = g2D.getFontMetrics().getStringBounds(nev, g2D).getHeight();
        g2D.drawString(nev, (int) (kx - nevSzelesseg / 2), (int) (ky + nevMagassag / 2));
    }
    
    public static void sorszamIndexNullaz(){
        index = 0;
    }
    
    public void setKx(double kx) {
        this.kx = kx;
    }

    public void setKy(double ky) {
        this.ky = ky;
    }

    public void setFont(Font font) {
        this.font = font;
    }  

    public void setPokerKezNev(String pokerKezNev) {
        this.pokerKezNev = pokerKezNev;
    }

    public void setAktiv(boolean aktiv) {
        this.aktiv = aktiv;
    }

    public String getNev() {
        return nev;
    }

    public boolean isAktiv() {
        return aktiv;
    }

    public double getKx() {
        return kx;
    }

    public double getKy() {
        return ky;
    }
    public byte getSorszam() {
        return sorszam;
    }

    public Font getFont() {
        return font;
    }

    public String getPokerKezNev() {
        return pokerKezNev;
    }
}
