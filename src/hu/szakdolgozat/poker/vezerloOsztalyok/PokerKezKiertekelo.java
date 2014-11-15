package hu.szakdolgozat.poker.vezerloOsztalyok;

import hu.szakdolgozat.poker.alapOsztalyok.Kartyalap;
import hu.szakdolgozat.poker.alapOsztalyok.PokerKez;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PokerKezKiertekelo {
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
            Byte jatekosSorszam = entry.getKey();
            List<Kartyalap> jatekosKartyalapjai = entry.getValue();            
            pokerKez = Lapkombinaciok.lapKombinacioKeres(jatekosKartyalapjai, leosztottKartyalapok);
            jatekosokPokerKeze.put(jatekosSorszam, pokerKez);
        }
        
        for (Map.Entry<Byte, PokerKez> entrySet : jatekosokPokerKeze.entrySet()) {
            Byte jatekosSorszam = entrySet.getKey();
            pokerKez = entrySet.getValue();
            aktPokerKezErtek = pokerKez.getPokerKezErtek();
            pokerKezErtekVizsgal(jatekosSorszam);
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
