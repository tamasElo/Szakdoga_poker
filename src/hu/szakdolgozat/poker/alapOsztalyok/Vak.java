package hu.szakdolgozat.poker.alapOsztalyok;

import javax.swing.ImageIcon;

public class Vak extends Korong {

    private int ertek;

    public Vak(ImageIcon korongKep, ImageIcon elmosodottKorongKep) {
        super(korongKep, elmosodottKorongKep);
    }

    public void setErtek(int ertek) {
        this.ertek = ertek;
    }

    public int getErtek() {
        return ertek;
    }    
}
