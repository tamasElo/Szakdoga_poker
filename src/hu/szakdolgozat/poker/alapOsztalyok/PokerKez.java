package hu.szakdolgozat.poker.alapOsztalyok;

import java.util.List;

/*Ez az osztály egy burkoló osztály ami arra szolgál, hogy a póker kéz
  nevét, értékét, a kártyalapok és kisérőlapok listáját együtt kezelje.*/
public class PokerKez {

    private String pokerKezNev;
    private byte pokerKezErtek;
    private List<Kartyalap> pokerKezKartyalapok;
    private List<Kartyalap> kiseroKartyalapok;

    public PokerKez(String pokerKezNev, byte pokerKezErtek, List<Kartyalap> pokerKezKartyalapok, List<Kartyalap> kiseroKartyalapok) {
        this.pokerKezNev = pokerKezNev;
        this.pokerKezErtek = pokerKezErtek;
        this.pokerKezKartyalapok = pokerKezKartyalapok;
        this.kiseroKartyalapok = kiseroKartyalapok;
    }

    public String getPokerKezNev() {
        return pokerKezNev;
    }

    public byte getPokerKezErtek() {
        return pokerKezErtek;
    }

    public List<Kartyalap> getPokerKezKartyalapok() {
        return pokerKezKartyalapok;
    }    

    public List<Kartyalap> getKiseroKartyalapok() {
        return kiseroKartyalapok;
    }
}
