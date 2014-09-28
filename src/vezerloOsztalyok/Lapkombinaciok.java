package vezerloOsztalyok;

import alapOsztalyok.Jatekos;
import alapOsztalyok.Kartyalap;
import alapOsztalyok.KartyalapRendezSzinSzerint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public final class Lapkombinaciok { //Azért final hogy ne lehessen belőle származtatni.

    private static Jatekos jatekos;
    private static String pokerKezNev;
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
    private static  List<Kartyalap> rendezettKartyalapok;
    private static List<Kartyalap> pokerKez;
    
    private Lapkombinaciok(){}; //Azért privát hogy le lehessen példányosítani.
    
    /**
     * Megvizsgálja, hogy van-e a játékosnak sora.
     */
    private static void sorKeres() {
        
        rendezettKartyalapok = new ArrayList<>();
        TreeSet<Kartyalap> rendezettKartyaHalmaz = new TreeSet<>(); //Kiveszi az ismétlődéseket és automatikusan rendezi.
        rendezettKartyaHalmaz.addAll(jatekosKartyalapok);
        rendezettKartyaHalmaz.addAll(leosztottKartyalapok);

        byte[] legkisebbSor = {14, 2, 3, 4, 5};
        Kartyalap elozoKartyalap, kovetkezokaKartyalap, kartyalap;
        sorHossz = 1;

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
            pokerKezListaba(rendezettKartyalapok);
            jatekos.setPokerKezNev(pokerKezNev);
            sor = true;
        } else {
            /*Megvizsgálja hogy van e legkisebb sora a játékosnak*/
            for (byte i = 0; i < legkisebbSor.length; i++) {
                for (byte j = 0; j < rendezettKartyalapok.size(); j++) {
                    kartyalap = rendezettKartyalapok.get(j);
                    if (legkisebbSor[i] == kartyalap.getKartyaErtek()) {
                        pokerKez.add(kartyalap);
                    }
                }
            }
            if (pokerKez.size() == POKER_KEZ_KARTYALAPOK_SZAMA) {
                pokerKezNev = "Sor";
                jatekos.setPokerKezLapok(pokerKez);
                jatekos.setPokerKezNev(pokerKezNev);
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
                
                for (Kartyalap kartyalap : rendezettKartyalapok) {
                    if (kartyalap.getKartyaSzin().equals(kartyaSzin)) {
                        szinKartyalapok.add(kartyalap);
                    }
                }                 
                
                Collections.sort(szinKartyalapok);
                vegIndex = szinKartyalapok.size()-1;
                pokerKezListaba(szinKartyalapok);
                jatekos.setPokerKezNev(pokerKezNev);
                szin = true;
                break;
            }
        }
    }

    /**
     * Megvizsgálja, hogy van-e a játékosnak színsora.
     */
    private static void szinsorKeres() {
        
        sorHossz = 1;
        vegIndex = 0;
        
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
            pokerKezNev = "Színsor";
            pokerKezListaba(szinKartyalapok);
            jatekos.setPokerKezNev(pokerKezNev);
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
        } else if (!par.isEmpty() && drill != null) {
            keresendoErtekek.add(drill.getKartyaErtek());
            keresendoErtekek.add(par.get(par.size() - 1));
            pokerKezNev = "Full";
        } else if (drill != null) {
            keresendoErtekek.add(drill.getKartyaErtek());
            pokerKezNev = "Drill";
        } else if (par.size() >= 2) {
            keresendoErtekek.add(par.get(par.size() - 1));
            keresendoErtekek.add(par.get(par.size() - 2));
            pokerKezNev = "Két pár";
        }else if (!par.isEmpty()){
            keresendoErtekek.add(par.get(0));
            pokerKezNev = "Pár";
        }
        
        if(!keresendoErtekek.isEmpty()){
            pokerKez.clear();
            for (Byte ertek : keresendoErtekek) {
                for (Kartyalap kartyalap : rendezettKartyalapok) {
                    if (kartyalap.getKartyaErtek() == ertek) {
                        pokerKez.add(kartyalap);
                        if(pokerKez.size() == POKER_KEZ_KARTYALAPOK_SZAMA) break;
                    }
                }
            }            
            while(pokerKez.size() != POKER_KEZ_KARTYALAPOK_SZAMA){
                if(pokerKez.contains(Collections.max(rendezettKartyalapok)))
                    rendezettKartyalapok.remove(rendezettKartyalapok.size()-1);
                else pokerKez.add(rendezettKartyalapok.get(rendezettKartyalapok.size()-1));
            }
            
            Collections.sort(pokerKez);
            jatekos.setPokerKezLapok(pokerKez);
            jatekos.setPokerKezNev(pokerKezNev);
            azonossag = true;
        }
    }
    
    /**
     * A sorKeres() metódusban feltöltött rendezettKartyalapok listából
       Kiválasztja az öt legnagyobbat. Csak akkor fut le ha a többi 
     * lapkombináció kereső metódus nem teljesül és kizárásos alapon a 
     * sorKeres() metódusban feltöltött listát tudja használni.
     */
    private static void magasLapokKeres(){
        
        pokerKezNev = "Magas lap";
        vegIndex = rendezettKartyalapok.size()-1;        
        pokerKezListaba(rendezettKartyalapok);
        jatekos.setPokerKezNev(pokerKezNev);
    }

    private static void kartyakListaba(){
        if(rendezettKartyalapok != null)rendezettKartyalapok.clear();
        rendezettKartyalapok.addAll(jatekosKartyalapok);
        rendezettKartyalapok.addAll(leosztottKartyalapok);
    }
    
    /**
     * A játékos lapkombinációját állítja be.
     * @param Kartyalapok 
     */
    private static void pokerKezListaba(List<Kartyalap> Kartyalapok) {
        
        pokerKez.clear();
        for (int i = vegIndex; i > vegIndex - POKER_KEZ_KARTYALAPOK_SZAMA; i--) {
            pokerKez.add(Kartyalapok.get(i));
        }
        Collections.sort(pokerKez);
        jatekos.setPokerKezLapok(pokerKez);
    }

    /**
     * Kiértékeli a játékos lapjait és eldönti hogy a játékosnak milyen 
     * lapkombinációja van.
     * @param jatekos
     * @param leosztottKartyalapok 
     */
    public static void lapKombinacioKeres(Jatekos jatekos, List<Kartyalap> leosztottKartyalapok){
        
        pokerKez = new ArrayList<>();
        Lapkombinaciok.jatekos = jatekos;
       // jatekosKartyalapok = jatekos.getJatekosKartyalapok();
        Lapkombinaciok.leosztottKartyalapok = leosztottKartyalapok;
        
        sorKeres();
        szinKeres();
        
        if(sor && szin) szinsorKeres();
        
        if(!sor && !szin && !szinsor) azonossagKeres();
        
        if(!sor && !szin && !szinsor && !azonossag) magasLapokKeres();
    }
}
