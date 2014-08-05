package alapOsztalyok;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import javax.swing.ImageIcon;

public class Kartyalap implements Comparable<Kartyalap>{
    private Image elolap;
    private final Image hatlap = new ImageIcon(this.getClass().getResource
        ("/adatFajlok/kartyaPakli/hatlap.png")).getImage(); //A forgatást még le kell tesztelni és ha működik statikussal akkor úgy marad
    private double kx;
    private double ky;
    private int kartyaKepSzelesseg, kartyaKepMagassag;
    private final byte kartyaErtek;
    private final String kartyaNev;
    private final String kartyaSzin;
    private boolean mutat;
    private boolean forgat;
    private double fok;

    public Kartyalap(Image elolap, String kartyaNev, String kartyaSzin, byte kartyaErtek) {
        this.elolap = elolap;
        this.kartyaNev = kartyaNev;
        this.kartyaSzin = kartyaSzin;
        this.kartyaErtek = kartyaErtek;
    }
    
    public void rajzol(Graphics2D g2D, ImageObserver o){
        AffineTransform at = g2D.getTransform();          

        if (forgat) {
            g2D.rotate(Math.toRadians(fok), kx, ky);
        } 
        if (!mutat) {
            g2D.drawImage(hatlap, (int) kx-kartyaKepSzelesseg/2, (int) ky-kartyaKepMagassag/2, kartyaKepSzelesseg, kartyaKepMagassag, o);
        } else {
            g2D.drawImage(elolap, (int) kx-kartyaKepSzelesseg/2, (int) ky-kartyaKepMagassag/2, kartyaKepSzelesseg, kartyaKepMagassag, o);
        }

        if (forgat) {
            g2D.setTransform(at);
        }
    }
    
    public void forgat(double fok){
        forgat = (fok != 0);                    
        this.fok = fok;
    }       

    public Image getElolap() {
        return elolap;
    }

    public Image getHatlap() {
        return hatlap;
    }   

    public byte getKartyaErtek() {
        return kartyaErtek;
    }

    public String getKartyaSzin() {
        return kartyaSzin;
    }

    public double getKx() {
        return kx;
    }

    public double getKy() {
        return ky;
    }

    public void setKx(double kx) {
        this.kx = kx;
    }

    public void setKy(double ky) {
        this.ky = ky;
    }

    public boolean isMutat() {
        return mutat;
    }
    
    public void setKartyaKepSzelesseg(int kartyaKepSzelesseg) {
        this.kartyaKepSzelesseg = kartyaKepSzelesseg;
    }

    public void setKartyaKepMagassag(int kartyaKepMagassag) {
        this.kartyaKepMagassag = kartyaKepMagassag;
    }

    public void setMutat(boolean mutat) {
        this.mutat = mutat;
    }

    public int getKartyaKepSzelesseg() {
        return kartyaKepSzelesseg;
    }

    public int getKartyaKepMagassag() {
        return kartyaKepMagassag;
    }

    public double getFok() {
        return fok;
    }

    @Override
    public int compareTo(Kartyalap t) {
        return this.kartyaErtek-t.getKartyaErtek();
    }
    
}