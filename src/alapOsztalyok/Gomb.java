package alapOsztalyok;

import java.awt.Graphics;
import java.awt.Image;

public class Gomb {
    private final String nev;
    private final Image alapHelyzet;
    private final Image rakattintva;
    private final Image letiltva;
    private int x;
    private int y;
    private int szelesseg;
    private int magassag;
    private int megjSorszam;

    public Gomb(String nev, Image alapHelyzet, Image rakattintva, Image letiltva, int x, int y, int szelesseg, int magassag) {
        this.nev = nev;
        this.alapHelyzet = alapHelyzet;
        this.rakattintva = rakattintva;
        this.letiltva = letiltva;
        this.x = x;
        this.y = y;
        this.szelesseg = szelesseg;
        this.magassag = magassag;
    }
    
    public void rajzol(Graphics g){
        switch(megjSorszam){
            case 1 : g.drawImage(alapHelyzet, x, y, szelesseg, magassag, null);
                break;
            case 2 : g.drawImage(rakattintva, x, y, szelesseg, magassag, null);
                break;
            case 3 : g.drawImage(letiltva, x, y, szelesseg, magassag, null);
                break;
        }
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setSzelesseg(int szelesseg) {
        this.szelesseg = szelesseg;
    }

    public void setMagassag(int magassag) {
        this.magassag = magassag;
    }

    public void setMegjSorszam(int megjSorszam) {
        this.megjSorszam = megjSorszam;
    }

    public String getNev() {
        return nev;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSzelesseg() {
        return szelesseg;
    }

    public int getMagassag() {
        return magassag;
    }

    public int getMegjSorszam() {
        return megjSorszam;
    }
}
