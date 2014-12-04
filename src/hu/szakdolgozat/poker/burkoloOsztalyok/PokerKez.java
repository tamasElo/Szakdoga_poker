package hu.szakdolgozat.poker.burkoloOsztalyok;

import hu.szakdolgozat.poker.alapOsztalyok.Kartyalap;
import java.util.List;

/*Ez az osztály egy burkoló osztály ami arra szolgál, hogy a póker kéz
  nevét, értékét, a kártyalapok és kisérőlapok listáját együtt kezelje.*/
public class PokerKez {

    private String pokerKezNev;
    private int pokerKezErtek;
    private List<Kartyalap> pokerKezKartyalapok;
    private List<Kartyalap> kiseroKartyalapok;
    private byte outok;

    public PokerKez(String pokerKezNev, int pokerKezErtek, List<Kartyalap> pokerKezKartyalapok, List<Kartyalap> kiseroKartyalapok) {
        this.pokerKezNev = pokerKezNev;
        this.pokerKezErtek = pokerKezErtek;
        this.pokerKezKartyalapok = pokerKezKartyalapok;
        this.kiseroKartyalapok = kiseroKartyalapok;
    }

    public PokerKez(String pokerKezNev, int pokerKezErtek, List<Kartyalap> pokerKezKartyalapok, List<Kartyalap> kiseroKartyalapok, byte outok) {
        this.pokerKezNev = pokerKezNev;
        this.pokerKezErtek = pokerKezErtek;
        this.pokerKezKartyalapok = pokerKezKartyalapok;
        this.kiseroKartyalapok = kiseroKartyalapok;
        this.outok = outok;
    }

    public String getPokerKezNev() {
        return pokerKezNev;
    }

    public int getPokerKezErtek() {
        return pokerKezErtek;
    }

    public List<Kartyalap> getPokerKezKartyalapok() {
        return pokerKezKartyalapok;
    }    

    public List<Kartyalap> getKiseroKartyalapok() {
        return kiseroKartyalapok;
    }

    public byte getOutok() {
        return outok;
    }
}
