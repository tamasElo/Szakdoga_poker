package alapOsztalyok;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.util.List;

public class Jatekos {
    private String nev;
    private Color szin;
    private boolean aktiv;
    private static byte index;
    private byte sorszam;
    private double x;
    private double y;
    private Font font;    
    private String pokerKezNev;
    private List<Kartyalap> pokerKezLapok;
    private List<Zseton> jatekosZsetonok;  

    public Jatekos(String nev) {
        this.nev = nev;
        szin = Color.white;
        sorszam = index++;
    }
    
    public void rajzol(Graphics2D g2D, ImageObserver o){
        int szovegSzelesseg;
        int szovegMagassag;
        g2D.setColor(szin);
        g2D.setFont(font);
        szovegSzelesseg = (int) g2D.getFontMetrics().getStringBounds(nev, g2D).getWidth();
        szovegMagassag = (int) g2D.getFontMetrics().getStringBounds(nev, g2D).getHeight();
        g2D.drawString(nev, (int) x - szovegSzelesseg / 2, (int) y + szovegMagassag / 2);
    }
    
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
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

    public void setPokerKezLapok(List<Kartyalap> pokerKezLapok) {
        this.pokerKezLapok = pokerKezLapok;
    }

    public void setJatekosZsetonok(List<Zseton> jatekosZsetonok) {
        this.jatekosZsetonok = jatekosZsetonok;
    }

    public String getNev() {
        return nev;
    }

    public boolean isAktiv() {
        return aktiv;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public byte getSorszam() {
        return sorszam;
    }

    public Font getFont() {
        return font;
    }

    public List<Zseton> getJatekosZsetonok() {
        return jatekosZsetonok;
    }

    public List<Kartyalap> getPokerKezLapok() {
        return pokerKezLapok;
    }

    public String getPokerKezNev() {
        return pokerKezNev;
    }
}