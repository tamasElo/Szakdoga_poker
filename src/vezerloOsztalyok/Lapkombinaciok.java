package vezerloOsztalyok;

import alapOsztalyok.Kartyalap;
import alapOsztalyok.KartyalapRendezSzinSzerint;
import alapOsztalyok.PokerKez;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public final class Lapkombinaciok { //Azért final hogy ne lehessen belőle származtatni.

    private static String pokerKezNev;
    private static byte pokerKezErtek;
    private static boolean sor;
    private static boolean szin;
    private static boolean szinsor;
    private static boolean azonossag;
    private static byte sorHossz;
    private static int vegIndex;
    private static final int POKER_KEZ_KARTYALAPOK_SZAMA = 5;
    private static List<Kartyalap> leosztottKartyalapok;
    private static List<Kartyalap> jatekosKartyalapok;
    private static List<Kartyalap> szinKartyalapok;
    private static List<Kartyalap> rendezettKartyalapok;
    private static List<Kartyalap> pokerKezKartyalapok;
    private static List<Kartyalap> kiseroKartyalapok;
    
    private Lapkombinaciok(){}; //Azért privát hogy ne lehessen példányosítani.
    
    /**
     * Megvizsgálja, hogy van-e a játékosnak sora.
     */
    private static void sorKeres() {

        rendezettKartyalapok = new ArrayList<>();
        TreeSet<Kartyalap> rendezettKartyaHalmaz = new TreeSet<>(); //Kiveszi az ismétlődéseket és automatikusan rendezi.
        rendezettKartyaHalmaz.addAll(jatekosKartyalapok);
        rendezettKartyaHalmaz.addAll(leosztottKartyalapok);

        byte[] legkisebbSor = {5, 4, 3, 2, 14};
        Kartyalap elozoKartyalap, kovetkezokaKartyalap, kartyalap;

        Iterator<Kartyalap> itr = rendezettKartyaHalmaz.iterator();
        while (itr.hasNext()) {
            rendezettKartyalapok.add(itr.next());
        }
        
        /*ha i kisebb egyenlő mint 3 akkor hajtja végre mivel utánna már 
          nincs értelme keresni ezért kilép a ciklusból*/
        for (byte i = 1; i < rendezettKartyalapok.size(); i++) {
            elozoKartyalap = rendezettKartyalapok.get(i - 1);
            kovetkezokaKartyalap = rendezettKartyalapok.get(i);

            if (elozoKartyalap.getKartyaErtek() + 1 == kovetkezokaKartyalap.getKartyaErtek()) {
                sorHossz++;
                if (sorHossz >= POKER_KEZ_KARTYALAPOK_SZAMA) {
                    vegIndex = i;
                }
            } else if (i <= 3) { 
                sorHossz = 1;
            } else {
                break;
            }
        }

        if (vegIndex != 0) {
            pokerKezNev = "Sor";
            pokerKezErtek = 5;
            pokerKezListaba(rendezettKartyalapok);
            sor = true;
        } else {
            /*Megvizsgálja hogy van e legkisebb sora a játékosnak*/
            for (byte i = 0; i < legkisebbSor.length; i++) {
                for (byte j = 0; j < rendezettKartyalapok.size(); j++) {
                    kartyalap = rendezettKartyalapok.get(j);
                    if (legkisebbSor[i] == kartyalap.getKartyaErtek()) {
                        pokerKezKartyalapok.add(kartyalap);
                    }
                }
            }
            if (pokerKezKartyalapok.size() == POKER_KEZ_KARTYALAPOK_SZAMA) {
                pokerKezNev = "Sor";
                pokerKezErtek = 5;
                sor = true;
            }
        }
    }
    
    /**
     * Megvizsgálja hogy van-e a játékosnak flösse.
     */
    private static void szinKeres(){                   

        szinKartyalapok = new ArrayList<>();
        String elozoKartyaSzin, kovetkezoKartyaSzin, kartyaSzin;
        byte szinSzamol = 1;
        
        kartyakListaba();
        Collections.sort(rendezettKartyalapok, new KartyalapRendezSzinSzerint());
        
        for (byte i = 1; i < rendezettKartyalapok.size(); i++) {
            elozoKartyaSzin = rendezettKartyalapok.get(i-1).getKartyaSzin();
            kovetkezoKartyaSzin = rendezettKartyalapok.get(i).getKartyaSzin();
            
            szinSzamol = elozoKartyaSzin.equals(kovetkezoKartyaSzin) ? szinSzamol += 1 : 1;
            if (szinSzamol == POKER_KEZ_KARTYALAPOK_SZAMA) {
                kartyaSzin = kovetkezoKartyaSzin;
                pokerKezNev = "Flöss";
                pokerKezErtek = 6;
                
                for (Kartyalap kartyalap : rendezettKartyalapok) {
                    if (kartyalap.getKartyaSzin().equals(kartyaSzin)) {
                        szinKartyalapok.add(kartyalap);
                    }
                }                 
                
                Collections.sort(szinKartyalapok);
                vegIndex = szinKartyalapok.size()-1;
                pokerKezListaba(szinKartyalapok);
                szin = true;
                break;
            }
        }
    }

    /**
     * Megvizsgálja, hogy van-e a játékosnak színsora.
     */
    private static void szinsorKeres() {        
        
        for (byte i = 1; i < szinKartyalapok.size(); i++) {            
            Kartyalap elozoKartyalap = (Kartyalap)szinKartyalapok.get(i-1);
            Kartyalap kovetkezokaKartyalap = (Kartyalap)szinKartyalapok.get(i);
            if (elozoKartyalap.getKartyaErtek() + 1 == kovetkezokaKartyalap.getKartyaErtek()) {
                sorHossz++;
                if (sorHossz >= POKER_KEZ_KARTYALAPOK_SZAMA) vegIndex = i;
            }            
            else if (i<= 3) sorHossz = 1;
            else break;  
        }
        
        if (vegIndex != 0) {
            
            if (Collections.min(pokerKezKartyalapok).getKartyaErtek() == 10 && Collections.max(pokerKezKartyalapok).getKartyaErtek() == 14) {
                pokerKezNev = "Royal flöss";
                pokerKezErtek = 10;
            } else {
                pokerKezNev = "Színsor";
                pokerKezErtek = 9;
            }
            pokerKezListaba(szinKartyalapok);
            szinsor = true;
        }
    }

    /**
     * Kártyaegyezéseket keres és eldönti hogy ha van, akkor milyen
     * lapkombinációja van a játékosnak (póker, full, drill, párok).
     */
    private static void azonossagKeres() {          
        List<Byte> keresendoErtekek = new ArrayList<>();        
        List<Byte> par = new ArrayList<>(); 
        Kartyalap poker = null, drill = null;
        byte egyezesSzamol = 1;        
        
        kartyakListaba();
        Collections.sort(rendezettKartyalapok);
        
        for (byte i = 1; i < rendezettKartyalapok.size(); i++) {
            if (rendezettKartyalapok.get(i).getKartyaErtek() == rendezettKartyalapok.get(i-1).getKartyaErtek()) {
                egyezesSzamol++;
                switch(egyezesSzamol){
                    case 2 : 
                        par.add(rendezettKartyalapok.get(i).getKartyaErtek());
                        break;
                    case 3 : 
                        drill = rendezettKartyalapok.get(i);                        
                        break;
                    case 4 : 
                        poker = rendezettKartyalapok.get(i);
                }
            } else {
                egyezesSzamol = 1;
            }
        }
        
        /*Megvizsgálja hogy a pár és a drill értéke egyezik-e,
          ha igen akkor törli az elemet a par listából. A full vizsgálat miatt
          van rá szükség*/
        if (drill != null) {
            for (byte i = 0; i < par.size(); i++) {
                if (par.get(i) == drill.getKartyaErtek()) {
                    par.remove(i);
                }
            }
        }
        
        if (poker != null) {
            keresendoErtekek.add(poker.getKartyaErtek());
            pokerKezNev = "Póker";
            pokerKezErtek = 8;
        } else if (!par.isEmpty() && drill != null) {            
            keresendoErtekek.add(drill.getKartyaErtek());
            keresendoErtekek.add(par.get(par.size() - 1));
            pokerKezNev = "Full";
            pokerKezErtek = 7;
        } else if (drill != null) {
            keresendoErtekek.add(drill.getKartyaErtek());
            pokerKezNev = "Drill";
            pokerKezErtek = 4;
        } else if (par.size() >= 2) {
            keresendoErtekek.add(par.get(par.size() - 1));
            keresendoErtekek.add(par.get(par.size() - 2));
            pokerKezNev = "Két pár";
            pokerKezErtek = 3;
        }else if (!par.isEmpty()){
            keresendoErtekek.add(par.get(0));
            pokerKezNev = "Pár";
            pokerKezErtek = 2;
        }
        
        if(!keresendoErtekek.isEmpty()){
            pokerKezKartyalapok.clear();
            for (Byte ertek : keresendoErtekek) {
                for (Kartyalap kartyalap : rendezettKartyalapok) {
                    if (kartyalap.getKartyaErtek() == ertek) {
                        pokerKezKartyalapok.add(kartyalap);
                        if(pokerKezKartyalapok.size() == POKER_KEZ_KARTYALAPOK_SZAMA) break;
                    }
                }
            }         
            
            while (pokerKezKartyalapok.size() + kiseroKartyalapok.size() != POKER_KEZ_KARTYALAPOK_SZAMA) {
                if (pokerKezKartyalapok.contains(Collections.max(rendezettKartyalapok))) {
                    rendezettKartyalapok.remove(rendezettKartyalapok.size() - 1);
                } else {
                    kiseroKartyalapok.add(rendezettKartyalapok.get(rendezettKartyalapok.size() - 1));
                    rendezettKartyalapok.remove(rendezettKartyalapok.size() - 1);
                }
            }
            
            azonossag = true;
        }
    }
    
    /**
     * A sorKeres() metódusban feltöltött rendezettKartyalapok listából
     * Kiválasztja az öt legnagyobbat. Csak akkor fut le ha a többi 
     * lapkombináció kereső metódus nem teljesül és kizárásos alapon a 
     * sorKeres() metódusban feltöltött listát tudja használni.
     */
    private static void magasLapokKeres(){        
        pokerKezNev = "Magas lap";
        pokerKezErtek = 1;
        vegIndex = rendezettKartyalapok.size()-1;        
        pokerKezListaba(rendezettKartyalapok);
    }

    /**
     * A játékos kártyalapjait és a leosztott kártyalapoket a 
     * rendezettKartyalapok listába rakja.
     */
    private static void kartyakListaba(){
        if(rendezettKartyalapok != null)rendezettKartyalapok.clear();
        rendezettKartyalapok.addAll(jatekosKartyalapok);
        rendezettKartyalapok.addAll(leosztottKartyalapok);
    }
    
    /**
     * A játékos lapkombinációját állítja be.
     * 
     * @param kartyalapok 
     */
    private static void pokerKezListaba(List<Kartyalap> kartyalapok) {        
        pokerKezKartyalapok.clear();
        for (int i = vegIndex; i > vegIndex - POKER_KEZ_KARTYALAPOK_SZAMA; i--) {
            pokerKezKartyalapok.add(kartyalapok.get(i));
        }
    }

    /**
     * Kiértékeli a játékos lapjait és eldönti hogy a játékosnak milyen 
     * lapkombinációja van.
     *
     * @param jatekosKartyalapok
     * @param leosztottKartyalapok 
     * @return  
     */
    public static PokerKez lapKombinacioKeres(List<Kartyalap> jatekosKartyalapok, List<Kartyalap> leosztottKartyalapok){
        sor = false;
        szin = false;
        szinsor = false;
        azonossag = false;
        vegIndex = 0;
        sorHossz = 1;
        pokerKezErtek = 0;
        pokerKezKartyalapok = new ArrayList<>();
        kiseroKartyalapok = new ArrayList<>();
        Lapkombinaciok.jatekosKartyalapok = jatekosKartyalapok;
        Lapkombinaciok.leosztottKartyalapok = leosztottKartyalapok;
        
        sorKeres();
        szinKeres();
        
        if(sor && szin) szinsorKeres();
        
        if(!sor && !szin && !szinsor) azonossagKeres();
        
        if(!sor && !szin && !szinsor && !azonossag) magasLapokKeres();
        
        return new PokerKez(pokerKezNev, pokerKezErtek, pokerKezKartyalapok, kiseroKartyalapok);
    }
}
