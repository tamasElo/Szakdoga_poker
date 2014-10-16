package vezerloOsztalyok;

import alapOsztalyok.Kartyalap;
import alapOsztalyok.PokerKez;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PokerKezKiertekelo {
    /*
     A Jatekos osztályban kell csinálni egy listát ami letárolja az aktuális 
     lapkombinációt illetve az öt legmagasabb lapot és egy String-et ami a kombináció neve pl:flöss.
     Így ha döntetlen helyzet van akkor pl collections.max() függvénnyel meg 
     lehet keresni hogy kinek van pl párja, drillje stb. ha egyeznek,
     akkor a kísérő lapoktól függ hogy ki nyer. Ha az is egyforma, akkor 
     döntetlen és osztoznak a nyertesek. Ha van maradék akkor az osztótól balra ülő játékos kapja.
     */
    private static List<Byte> nyertesJatekosokSorszama;
    private static Map<Byte, PokerKez> jatekosokPokerKeze;
    private static byte legmagasabbPokerKezErtek;
    private static byte aktPokerKezErtek;
            
    /**
     * Megkeresi a nyertes póker kezeket.
     * 
     * @param jatekosokKartyalapjai
     * @param leosztottKartyalapok 
     * @return  
     */
    public static Map<Byte, PokerKez> nyertesPokerKezKeres(Map<Byte, List<Kartyalap>> jatekosokKartyalapjai, List<Kartyalap> leosztottKartyalapok){
        jatekosokPokerKeze = new HashMap<>();
        nyertesJatekosokSorszama = new ArrayList<>();
        HashMap<Byte, PokerKez> nyertesJatekosokPokerKeze = new HashMap<>();
        PokerKez pokerKez;
        
        legmagasabbPokerKezErtek = 0;
        aktPokerKezErtek = 0;
                
        for (Map.Entry<Byte, List<Kartyalap>> entry : jatekosokKartyalapjai.entrySet()) {
            Byte key = entry.getKey();
            List<Kartyalap> value = entry.getValue();            
            pokerKez = Lapkombinaciok.lapKombinacioKeres(value, leosztottKartyalapok);
            jatekosokPokerKeze.put(key, pokerKez);
        }
        
        for (Map.Entry<Byte, PokerKez> entrySet : jatekosokPokerKeze.entrySet()) {
            Byte key = entrySet.getKey();
            PokerKez value = entrySet.getValue();
            aktPokerKezErtek = value.getPokerKezErtek();
            pokerKezErtekVizsgal(key);
        }
        
        for (Byte jatekosSorszam : nyertesJatekosokSorszama) {
            pokerKez = jatekosokPokerKeze.get(jatekosSorszam);
            nyertesJatekosokPokerKeze.put(jatekosSorszam, pokerKez);
        }
        return nyertesJatekosokPokerKeze;
    }

    /**
     * Megvizsgálja hogy kinek van a legmagasabb értékű póker keze. Ha több
     * ilyen is van, akkor meghívja a dontetlenPokerKezekVizsgal() metódust.
     * 
     * @param jatekosSorszam 
     */
    private static void pokerKezErtekVizsgal(byte jatekosSorszam){
        if (legmagasabbPokerKezErtek < aktPokerKezErtek) {
            nyertesJatekosokSorszama.clear();
            nyertesJatekosokSorszama.add(jatekosSorszam);
            legmagasabbPokerKezErtek = aktPokerKezErtek;
        } else if (legmagasabbPokerKezErtek == aktPokerKezErtek) {
            nyertesJatekosokSorszama.add(jatekosSorszam);
            dontetlenPokerKezekVizsgal();
        }
        
    }
    
    /**
     * Ha egynél több játékosnak is ugyanolyan értékű póker keze van, akkor ez a metódus
     * összehasonlítja a lapkombinációkat kártyaértékek szerint.
     */
    private static void dontetlenPokerKezekVizsgal(){
        List<Byte> ertekek = new ArrayList<>();
        byte legnagyobbErtek;
        int listaMeret = jatekosokPokerKeze.get(nyertesJatekosokSorszama.get(0)).getPokerKezKartyalapok().size();//A nyertesJatekosokSorszama lista 0-ik elemének értéke szerinti játékos sorszámának a póker kéz kártyalapjaniak a mérete lesz a listaMeret.
        
        for (byte i = 0; i < listaMeret; i++) {
            for (Byte jatekosSorszam : nyertesJatekosokSorszama) {
                ertekek.add(jatekosokPokerKeze.get(jatekosSorszam).getPokerKezKartyalapok().get(i).getKartyaErtek());
            }
            
            legnagyobbErtek = Collections.max(ertekek);
            
            /*Összehasonlítja a játékosok pókerkezének i-ik értékeit. 
              Ha nem egyezik a legnagyobb értékkel, akkor törli a 
              nyertesJatekosokSorszama listából a játékos sorszámát.*/
            for (byte j = 0; j < ertekek.size(); j++) {
                if (ertekek.get(j) != legnagyobbErtek) {
                    nyertesJatekosokSorszama.remove(j);
                    ertekek.remove(j);
                    j--;
                }
            }
            
            ertekek.clear();
            
            if(nyertesJatekosokSorszama.size() == 1) break; //Ha a lista mérete 1, akkor megvan a nyertes játékos, mivel neki van a legmagasabb értékű póker keze.
        }

        if (nyertesJatekosokSorszama.size() > 1) { //Ha még mindig egynél több nyertes játékos van, akkor ugyanazokat a műveleteket mint a felső ciklus végrehajtja a kísérőlapokon.
            listaMeret = jatekosokPokerKeze.get(nyertesJatekosokSorszama.get(0)).getKiseroKartyalapok().size();
            
            for (byte i = 0; i < listaMeret; i++) {
                for (Byte jatekosSorszam : nyertesJatekosokSorszama) {
                    ertekek.add(jatekosokPokerKeze.get(jatekosSorszam).getKiseroKartyalapok().get(i).getKartyaErtek());
                }

                legnagyobbErtek = Collections.max(ertekek);

                for (byte j = 0; j < ertekek.size(); j++) {
                    if (ertekek.get(j) != legnagyobbErtek) {
                        nyertesJatekosokSorszama.remove(j);
                        ertekek.remove(j);
                        j--;
                    }
                }

                ertekek.clear();

                if (nyertesJatekosokSorszama.size() == 1) break;
            }
        }
    }
}
