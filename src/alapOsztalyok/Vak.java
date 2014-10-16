package alapOsztalyok;

import java.awt.Image;

public class Vak extends Korong{
    private int ertek;

    public Vak(Image korongKep) {
        super(korongKep);
    }

    public void setErtek(int ertek) {
        this.ertek = ertek;
    }

    public int getErtek() {
        return ertek;
    }    
}
