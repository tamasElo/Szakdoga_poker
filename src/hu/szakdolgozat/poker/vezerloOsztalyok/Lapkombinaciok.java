package hu.szakdolgozat.poker.vezerloOsztalyok;

import hu.szakdolgozat.poker.alapOsztalyok.Kartyalap;
import hu.szakdolgozat.poker.alapOsztalyok.KartyalapRendezSzinSzerint;
import hu.szakdolgozat.poker.burkoloOsztalyok.PokerKez;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public final class Lapkombinaciok { //Azért final hogy ne lehessen belőle származtatni.

    private static String pokerKezNev;
    private static String lehetsegesSzin;
    private static int pokerKezErtek;
    private static boolean sor;
    private static boolean szin;
    private static boolean szinsor;
    private static boolean azonossag;
    private static byte sorHossz;
    private static byte outok;
    private static int vegIndex;
    private static final byte POKER_KEZ_KARTYALAPOK_SZAMA = 5;
    private static int maxKartyalapokSzama;
    private static final byte[] legkisebbSor = {5, 4, 3, 2, 14};
    private static final String MAGAS_LAP = "Magas lap";
    private static final String PAR = "Pár";    
    private static final String KET_PAR = "Két pár";
    private static final String DRILL = "Drill";
    private static final String SOR = "Sor";
    private static final String FLOSS = "Flöss";
    private static final String FULL = "Full";
    private static final String POKER = "Póker";
    private static final String SZINSOR = "Színsor";
    private static final String ROYAL_FLOSS = "Royal flöss";   
    private static final byte SZIN_SZERINT = 0;
    private static final byte ERTEK_SZERINT = 1;
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
        rendezettKartyaHalmaz.addAll(leosztottKartyalapok);
        rendezettKartyaHalmaz.addAll(jatekosKartyalapok);
        
        Kartyalap elozoKartyalap, kovetkezokaKartyalap, kartyalap;

        Iterator<Kartyalap> itr = rendezettKartyaHalmaz.iterator();
        while (itr.hasNext()) {
            rendezettKartyalapok.add(itr.next());
        }
        
        vegIndex = 0;
        sorHossz = 1;
        
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
            pokerKezListaba(rendezettKartyalapok);
            sor = true;
        } else {
            /*Megvizsgálja hogy van-e legkisebb sora a játékosnak*/
            for (byte i = 0; i < legkisebbSor.length; i++) {
                for (byte j = 0; j < rendezettKartyalapok.size(); j++) {
                    kartyalap = rendezettKartyalapok.get(j);
                    if (legkisebbSor[i] == kartyalap.getKartyaErtek()) {
                        pokerKezKartyalapok.add(kartyalap);
                    }
                }
            }
            
            if (pokerKezKartyalapok.size() == POKER_KEZ_KARTYALAPOK_SZAMA) {
                sor = true;
            } else if (pokerKezKartyalapok.size() == 4) { // Ha a kártyalapok száma 4, akkor az outok száma 4 lesz mivel vagy lyuk van vagy zárt sor(14 vagy 2-vel kezdődik).
                outok = 4;
            }
        }
 
        if (sor) {
            pokerKezNev = SOR;
            pokerKezErtek = 80 + pokerKezKartyalapok.get(0).getKartyaErtek();
        } else {
            Map<Byte, List<Byte>> lehetsegesSorok = new HashMap<>();
            List<Byte> lehetsegesSor = new ArrayList<>();
            byte kovetkezoErtek = 0, aktErtek, lyukErtek = 0, lyukIndex = 0, lyukSzamlal = 0;
            boolean lyukTalalat = false;
            sorHossz = 1;

            /*Lehetséges sorok keresésée.*/
            for (byte i = 0; i < rendezettKartyalapok.size(); i++) {
                aktErtek = rendezettKartyalapok.get(i).getKartyaErtek();
                if (i + 1 < rendezettKartyalapok.size()) {
                    kovetkezoErtek = rendezettKartyalapok.get(i + 1).getKartyaErtek();
                }

                if (lehetsegesSor.isEmpty()) {
                    lehetsegesSor.add(aktErtek);
                }

                if (++aktErtek <= kovetkezoErtek) {
                    while (aktErtek != kovetkezoErtek) {
                        sorHossz = 1;
                        lyukSzamlal++;
                        aktErtek++;
                    }

                    if (lyukSzamlal == 0) {
                        sorHossz++;

                        lehetsegesSor.add(kovetkezoErtek);

                        if (sorHossz == 4) {
                            lehetsegesSorok.put(lyukErtek, new ArrayList<>(lehetsegesSor));
                        }
                    } else if (lyukSzamlal == 1 && !lyukTalalat) {
                        lyukTalalat = true;
                        lyukIndex = i;
                        lyukErtek = (byte) (kovetkezoErtek - 1);
                        lehetsegesSor.add(lyukErtek);
                        lehetsegesSor.add(kovetkezoErtek);
                        lehetsegesSorok.put(lyukErtek, lehetsegesSor);
                        lyukSzamlal = 0;
                    } else if (lyukSzamlal == 1 && lyukTalalat) {
                        lehetsegesSor = new ArrayList<>();
                        lyukTalalat = false;
                        i = lyukIndex;
                        lyukSzamlal = 0;
                    } else if (lyukSzamlal >= 2) {
                        lehetsegesSor = new ArrayList<>();
                        lyukSzamlal = 0;
                    }
                }
            }

            /*Outok számának megállapítása.*/
            Object[] lyukErtekek = lehetsegesSorok.keySet().toArray();

            if (lyukErtekek.length != 0) {
                lyukErtek = (byte) lyukErtekek[lyukErtekek.length - 1];
                lehetsegesSor = lehetsegesSorok.get(lyukErtek);

                if (lyukErtek == 0) {
                    if (Collections.max(lehetsegesSor) == 14 || Collections.min(lehetsegesSor) == 2) {
                        outok = 4;
                    } else {
                        outok = 8;
                    }
                } else {
                    if (lehetsegesSor.size() > 4) {
                        lehetsegesSor = lehetsegesSor.subList(lehetsegesSor.size() - 4, lehetsegesSor.size());

                        if (lehetsegesSor.contains(lyukErtek)) {
                            outok = 4;
                        } else if (Collections.max(lehetsegesSor) == 14 || Collections.min(lehetsegesSor) == 2) {
                            outok = 4;
                        } else {
                            outok = 8;
                        }
                    }
                }
            }
        }
    }

    /**
     * Megvizsgálja hogy van-e a játékosnak flösse.
     */
    private static void szinKeres(){    
        szinKartyalapok = new ArrayList<>();
        String elozoKartyaSzin, kovetkezoKartyaSzin;
        byte szinSzamol = 1;
        boolean szinOutokHozzaad = false;

        kartyakRendezettListaba(SZIN_SZERINT);

        for (byte i = 1; i < rendezettKartyalapok.size(); i++) {
            elozoKartyaSzin = rendezettKartyalapok.get(i - 1).getKartyaSzin();
            kovetkezoKartyaSzin = rendezettKartyalapok.get(i).getKartyaSzin();
            szinSzamol = elozoKartyaSzin.equals(kovetkezoKartyaSzin) ? szinSzamol += 1 : 1;

            if (szinSzamol == POKER_KEZ_KARTYALAPOK_SZAMA) {
                lehetsegesSzin = kovetkezoKartyaSzin;
                pokerKezNev = FLOSS;
                pokerKezErtek = 100;

                for (Kartyalap kartyalap : rendezettKartyalapok) {
                    if (kartyalap.getKartyaSzin().equals(lehetsegesSzin)) {
                        szinKartyalapok.add(kartyalap);
                    }
                }

                Collections.sort(szinKartyalapok);
                vegIndex = szinKartyalapok.size() - 1;
                pokerKezListaba(szinKartyalapok);
                szin = true;
                szinOutokHozzaad = false;
                break;
            } else if (szinSzamol == 4) {
                szinOutokHozzaad = true;
            }
        }
        
        if (szinOutokHozzaad) {
            outok -= outok == 4 ? 1 : 0;
            outok -= outok == 8 ? 2 : 0;
            outok += 9;
        }
    }

    /**
     * Megvizsgálja, hogy van-e a játékosnak színsora.
     */
    private static void szinsorKeres() {
        if (sor && szin) {
            vegIndex = 0;
            sorHossz = 1;

            for (byte i = 1; i < szinKartyalapok.size(); i++) {
                Kartyalap elozoKartyalap = (Kartyalap) szinKartyalapok.get(i - 1);
                Kartyalap kovetkezokaKartyalap = (Kartyalap) szinKartyalapok.get(i);

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
                if (Collections.min(pokerKezKartyalapok).getKartyaErtek() == 10 && Collections.max(pokerKezKartyalapok).getKartyaErtek() == 14) {
                    pokerKezNev = ROYAL_FLOSS;
                } else {
                    pokerKezNev = SZINSOR;
                }

                pokerKezListaba(szinKartyalapok);
                szinsor = true;
            } else {
                sorHossz = 0;

                for (byte kartyaErtek : legkisebbSor) {
                    for (Kartyalap kartyalap : szinKartyalapok) {
                        if (kartyaErtek == kartyalap.getKartyaErtek()) {
                            sorHossz++;
                        }
                    }
                }

                if (sorHossz == POKER_KEZ_KARTYALAPOK_SZAMA) {
                    pokerKezNev = SZINSOR;
                    szinsor = true;

                    pokerKezKartyalapok.clear();

                    for (byte kartyaErtek : legkisebbSor) {
                        for (Kartyalap kartyalap : szinKartyalapok) {
                            if (kartyaErtek == kartyalap.getKartyaErtek()) {
                                pokerKezKartyalapok.add(kartyalap);
                            }
                        }
                    }
                }
            }

            switch (pokerKezNev) {
                case ROYAL_FLOSS:
                    pokerKezErtek = 180;
                    break;
                case SZINSOR:
                    pokerKezErtek = 160;

            }
        }
    }

    /**
     * Kártyaegyezéseket keres és eldönti hogy ha van, akkor milyen
     * lapkombinációja van a játékosnak (póker, full, drill, párok).
     */
    private static void azonossagKeres() {            
        if (!sor && !szin && !szinsor) {
            List<Byte> keresendoErtekek = new ArrayList<>();
            List<Byte> par = new ArrayList<>();
            Kartyalap poker = null, drill = null;
            byte egyezesSzamol = 1;
            boolean szinEgyezes = false;

            kartyakRendezettListaba(ERTEK_SZERINT);

            for (byte i = 1; i < rendezettKartyalapok.size(); i++) {
                if (rendezettKartyalapok.get(i).getKartyaErtek() == rendezettKartyalapok.get(i - 1).getKartyaErtek()) {
                    egyezesSzamol++;
                    switch (egyezesSzamol) {
                        case 2:
                            par.add(rendezettKartyalapok.get(i).getKartyaErtek());
                            break;
                        case 3:
                            drill = rendezettKartyalapok.get(i);
                            break;
                        case 4:
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
                pokerKezNev = POKER;
                pokerKezErtek = 140 + poker.getKartyaErtek();
            } else if (!par.isEmpty() && drill != null) {
                keresendoErtekek.add(drill.getKartyaErtek());
                keresendoErtekek.add(par.get(par.size() - 1));
                pokerKezNev = FULL;
                pokerKezErtek = 120 + drill.getKartyaErtek();
            } else if (drill != null) {
                keresendoErtekek.add(drill.getKartyaErtek());
                pokerKezNev = DRILL;
                pokerKezErtek = 60 + drill.getKartyaErtek();
            } else if (par.size() >= 2) {
                keresendoErtekek.add(par.get(par.size() - 1));
                keresendoErtekek.add(par.get(par.size() - 2));
                pokerKezNev = KET_PAR;
                pokerKezErtek = 40 + Collections.max(par);
            } else if (!par.isEmpty()) {
                keresendoErtekek.add(par.get(0));
                pokerKezNev = PAR;
                pokerKezErtek = 20 + Collections.max(par);
            }

            if (!keresendoErtekek.isEmpty()) {
                pokerKezKartyalapok.clear();
                for (Byte ertek : keresendoErtekek) {
                    for (Kartyalap kartyalap : rendezettKartyalapok) {
                        if (kartyalap.getKartyaErtek() == ertek) {
                            pokerKezKartyalapok.add(kartyalap);

                            if (pokerKezNev.equals(PAR) && kartyalap.getKartyaSzin().equals(lehetsegesSzin)) {
                                szinEgyezes = true;
                            }

                            if (pokerKezKartyalapok.size() == POKER_KEZ_KARTYALAPOK_SZAMA) {
                                break;
                            }
                        }
                    }
                }

                kiseroKartyalapokListaba();

                azonossag = true;
            }
            
            switch (pokerKezNev) {
                case FULL:
                    outok += 1;
                    break;
                case DRILL:
                    outok += (leosztottKartyalapok.size() - 1) * 3 + 1;
                    break;
                case KET_PAR:
                    outok += 4;
                    break;
                case PAR:
                    outok += 2;
                    outok -= szinEgyezes ? 1 : 0;
            }
        }
    }

    /**
     * A sorKeres() metódusban feltöltött rendezettKartyalapok listából
     * Kiválasztja az öt legnagyobbat. Csak akkor fut le ha a többi 
     * lapkombináció kereső metódus nem teljesül és kizárásos alapon a 
     * sorKeres() metódusban feltöltött listát tudja használni.
     */
    private static void magasLapokKeres() {
        if (!sor && !szin && !szinsor && !azonossag) {
            pokerKezNev = MAGAS_LAP;
            pokerKezKartyalapok.clear();
            pokerKezKartyalapok.add(Collections.max(rendezettKartyalapok));
            pokerKezErtek = pokerKezKartyalapok.get(0).getKartyaErtek();
            kiseroKartyalapokListaba();
            outok += (jatekosKartyalapok.size() + leosztottKartyalapok.size()) * 3;
            outok -= !lehetsegesSzin.equals("") ? leosztottKartyalapok.size() + jatekosKartyalapok.size() - 4 : 0;
        }
    }

    /**
     * A játékos kártyalapjait és a leosztott kártyalapoket a 
     * rendezettKartyalapok listába rakja és a megadott szempont szerint
     * rendezi a listát.
     */
    private static void kartyakRendezettListaba(byte rendezesSzempont){
        if(rendezettKartyalapok != null)rendezettKartyalapok.clear();
        rendezettKartyalapok.addAll(leosztottKartyalapok);
        rendezettKartyalapok.addAll(jatekosKartyalapok);
        
        switch (rendezesSzempont) {
            case SZIN_SZERINT:
                Collections.sort(rendezettKartyalapok, new KartyalapRendezSzinSzerint());
                break;
            case ERTEK_SZERINT:
                Collections.sort(rendezettKartyalapok);
        }
    }
    
    /**
     * A játékos lapkombinációját állítja be.
     * 
     * @param kartyalapok 
     */
    private static void pokerKezListaba(List<Kartyalap> kartyalapok) {        
        pokerKezKartyalapok.clear();     
        
        for (int i = vegIndex; i > vegIndex - maxKartyalapokSzama; i--) {
            pokerKezKartyalapok.add(kartyalapok.get(i));
        }
    }

    /**
     * Feltölti a kísérő kártyalapok listát a megfelelő elemekkel.
     */
    private static void kiseroKartyalapokListaba() {
        while (pokerKezKartyalapok.size() + kiseroKartyalapok.size() != maxKartyalapokSzama) {
            if (pokerKezKartyalapok.contains(Collections.max(rendezettKartyalapok))) {
                rendezettKartyalapok.remove(rendezettKartyalapok.size() - 1);
            } else {
                kiseroKartyalapok.add(rendezettKartyalapok.get(rendezettKartyalapok.size() - 1));
                rendezettKartyalapok.remove(rendezettKartyalapok.size() - 1);
            }
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
        lehetsegesSzin = "";
        pokerKezNev = "";
        pokerKezErtek = 0;
        pokerKezKartyalapok = new ArrayList<>();
        kiseroKartyalapok = new ArrayList<>();
        outok = 0;
        Lapkombinaciok.jatekosKartyalapok = jatekosKartyalapok;
        Lapkombinaciok.leosztottKartyalapok = leosztottKartyalapok;
        maxKartyalapokSzama = jatekosKartyalapok.size()+leosztottKartyalapok.size() < 5 ? 
                              jatekosKartyalapok.size()+leosztottKartyalapok.size() : POKER_KEZ_KARTYALAPOK_SZAMA;
         
        sorKeres();
        szinKeres();
        szinsorKeres();
        azonossagKeres();
        magasLapokKeres();

        if (leosztottKartyalapok.size() == POKER_KEZ_KARTYALAPOK_SZAMA || leosztottKartyalapok.isEmpty()) {
            return new PokerKez(pokerKezNev, pokerKezErtek, pokerKezKartyalapok, kiseroKartyalapok);
        } else {
            return new PokerKez(pokerKezNev, pokerKezErtek, pokerKezKartyalapok, kiseroKartyalapok, outok);
        }
    }

    /**
     * Meghatározza az aktuális leosztott lapokhoz, hogy mekkora a lehetséges legnagyobb pókerkéz.
     * 
     * @param jatekosKartyalapok
     * @param leosztottKartyalapok
     * @return 
     */
    public static int maxPokerkezMeghataroz(List<Kartyalap> jatekosKartyalapok, List<Kartyalap> leosztottKartyalapok) {
        List<Kartyalap> kartyaPakli = PakliKezelo.pakliBetolt();
        kartyaPakli.removeAll(jatekosKartyalapok);
        kartyaPakli.removeAll(leosztottKartyalapok);
        int maxPokerkezErtek = 2;
        PokerKez pokerKez;
        for (Kartyalap kartyalap : kartyaPakli) {
            for (Kartyalap kartyalap2 : kartyaPakli) {
                jatekosKartyalapok = new ArrayList<>();
                jatekosKartyalapok.add(kartyalap);
                jatekosKartyalapok.add(kartyalap2);
                pokerKez = lapKombinacioKeres(jatekosKartyalapok, leosztottKartyalapok);
                
                if (pokerKez.getPokerKezErtek() > maxPokerkezErtek) {
                    maxPokerkezErtek = pokerKez.getPokerKezErtek();
                }
            }
        }
        
        return maxPokerkezErtek;
    }
}
