package vezerloOsztalyok;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SorrendKevero {
    
    private static Random general;    
    private static List<Integer> kevertSorrendLista;

    private SorrendKevero() {
    }

    /**
     * Létrehoz egy véletlen számsorrendet és ezt a számsort egy ArrayListben visszadja
     * 
     * @return 
     */
    public static List<Integer> kevertSorrend() {
        
        general = new Random();
        kevertSorrendLista = new ArrayList<>();
        List<Integer> balOldaliSorrendLista = new ArrayList<>();
        List<Integer> jobbOldaliSorrendLista = new ArrayList<>();
        byte teljesPakliMeret = PakliKezelo.PAKLI_MERET;
        byte keveresekSzamaAlsoKorlat = 5, keveresekSzamaFelsoKorlat = 6;
        int keveresekSzama = general.nextInt(keveresekSzamaFelsoKorlat) + keveresekSzamaAlsoKorlat;
        byte balOldalAlsoKorlat = 20, balOldalFelsoKorlat = 7;
        int balOldalMeret;
        int kartyaSzam;

            /*Alap pakli keverés*/
            while (kevertSorrendLista.size() != teljesPakliMeret) {
                varakozasIdo();
                kartyaSzam = general.nextInt(teljesPakliMeret); //Generál 52 db számot
                if (!kevertSorrendLista.contains(kartyaSzam)) {
                    kevertSorrendLista.add(kartyaSzam);
                }
            }

            /* Pakli megkeverése annyiszor amennyi a keverések száma*/
            for (int i = 0; i < keveresekSzama; i++) {                
                varakozasIdo();
                balOldalMeret = general.nextInt(balOldalFelsoKorlat) + balOldalAlsoKorlat;

                /*Feltölti a szétválasztott paklikat*/
                for (byte j = 0; j < balOldalMeret; j++) {
                    balOldaliSorrendLista.add(kevertSorrendLista.get(j));
                }
                for (int j = balOldalMeret; j < teljesPakliMeret; j++) {
                    jobbOldaliSorrendLista.add(kevertSorrendLista.get(j));
                }

                /*
                 * paklit kinullázza és újra feltölti az összekevert bal és jobb
                 * pakliból
                 */
                kevertSorrendLista.clear();
                while (kevertSorrendLista.size() != teljesPakliMeret) {                    
                    varakozasIdo();
                    kevertSorrendListaFeltolt(balOldaliSorrendLista);     
                    varakozasIdo();
                    kevertSorrendListaFeltolt(jobbOldaliSorrendLista);
                }
            }
            
        return kevertSorrendLista;
    }
    
    /**
     * Feltölti a kevert sorrend listát úgy hogy a megfelelő oldalról tesz be 
     * a listába véletlenszerűen meghatározott számú elemet.
     * 
     * @param sorrendOldal 
     */
    private static void kevertSorrendListaFeltolt(List<Integer> sorrendOldal) {
        
        int elemekSzama;
        byte kevertElemekAlsoKorlat = 1, kevertElemekFelsoKorlat = 3;
        elemekSzama = general.nextInt(kevertElemekFelsoKorlat) + kevertElemekAlsoKorlat;
        
        if (elemekSzama > sorrendOldal.size()) {
            elemekSzama = sorrendOldal.size();
        }
        for (byte j = 0; j < elemekSzama; j++) {
            kevertSorrendLista.add(sorrendOldal.get(sorrendOldal.size() - 1));
            sorrendOldal.remove(sorrendOldal.size() - 1);
        }
    }
    
    /**
     * Megadott korlátok közötti véletlen ideig várakoztat.
     */
    private static void varakozasIdo() {
        
        int ido;
        byte alsoIdoKorlat = 5, felsoIdoKorlat = 15;
        ido = general.nextInt(felsoIdoKorlat) + alsoIdoKorlat; //Véletlen ideig várakozik hogy növelje a véletlenkártyaszám generálást.
        try {
            Thread.sleep(ido);
        } catch (InterruptedException ex) {
            Logger.getLogger(SorrendKevero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
