package alapOsztalyok;

import java.awt.Image;

public class Zseton extends Korong implements Comparable<Zseton>{

    private byte ertek;

    public Zseton(byte ertek, Image korongKep, Image elmosodottKorongKep) {
        super(korongKep, elmosodottKorongKep);
        this.ertek = ertek;
    } 

    @Override
    public boolean equals(Object o) {
        Zseton zseton = (Zseton) o;        
        return o instanceof Zseton && this.ertek == zseton.getErtek();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + this.ertek;
        return hash;
    }
    
    @Override
    public int compareTo(Zseton t) {
        return this.ertek - t.ertek;
    }
  
    public byte getErtek() {
        return ertek;
    }  
}
