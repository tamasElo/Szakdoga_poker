package hu.szakdolgozat.poker.alapOsztalyok;

import java.awt.Image;

public class Vak extends Korong{
    private int ertek;

    public Vak(Image korongKep, Image elmosodottKorongKep) {
        super(korongKep, elmosodottKorongKep);
    }

    public void setErtek(int ertek) {
        this.ertek = ertek;
    }

    public int getErtek() {
        return ertek;
    }    
}
