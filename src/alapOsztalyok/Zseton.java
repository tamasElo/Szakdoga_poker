package alapOsztalyok;

import java.awt.Image;

public class Zseton extends Korong implements Comparable<Zseton>{

    private byte ertek;

    public Zseton(byte ertek, Image korongKep) {
        super(korongKep);
        this.ertek = ertek;
    } 
    
    @Override
    public int compareTo(Zseton t) {
        return this.ertek - t.ertek;
    }
  
    public byte getErtek() {
        return ertek;
    }  
}
