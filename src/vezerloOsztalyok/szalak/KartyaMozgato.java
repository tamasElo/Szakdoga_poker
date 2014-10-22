package vezerloOsztalyok.szalak;

import vezerloOsztalyok.SzalVezerlo;
import alapOsztalyok.Kartyalap;
import alapOsztalyok.PokerKez;
import java.awt.Point;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import vezerloOsztalyok.PakliKezelo;
import vezerloOsztalyok.PokerKezKiertekelo;
import vezerloOsztalyok.SzogSzamito;

public class KartyaMozgato extends Thread implements Runnable{    
    private SzalVezerlo szalVezerlo;
    private Map<Byte, List<Kartyalap>> jatekosokKartyalapjai;
    private List<Kartyalap> kartyalapok;    
    private List<Point> szorasok;
    private List<Point> vegpontok;
    private int jatekterSzelesseg;
    private int jatekterMagassag;
    private double kx;
    private double ky;
    private double vx;
    private double vy;
    private double aktx;
    private double akty;
    private double tavolsag;
    private double aktTav;    
    private double elteres;
    private double foSzog;    
    private double vegSzog;
    private double vegForgSzog;
    private double aktForgSzog;
    private boolean kartyalapokKiosztasa;
    private boolean kartyalapLeosztas;
    private boolean kartyalapokBedobasa;
    private boolean kartyalapokKiertekelese;
    private boolean osszesKartyalapLeosztas;
    private byte dealer;
    private double lepes;
    private int ido;
    private byte elojel;
    private byte jatekosokSzama;
    private byte jatekosSorszam;
    private static double laptavolsagokOsszege;

    public KartyaMozgato(SzalVezerlo szalVezerlo) {
        this.szalVezerlo = szalVezerlo;        
        jatekterSzelesseg = szalVezerlo.jatekterPanelSzelesseg();
        jatekterMagassag = szalVezerlo.jatekterPanelMagassag();
        jatekosokSzama = szalVezerlo.jatekosokSzama();
        ido = 2;
        lepes = jatekterSzelesseg * 0.001875;
    }

    /**
     * A PakliKezelo osztálytól lekéri a kevert pakli listát és ezt átadja a
     * szálvezérlönek.
     */
    private void pakliBetolt() { 
        Point aktSzoras, szorasHatar = new Point(jatekterSzelesseg / 400, jatekterMagassag / 600);            
        kx = jatekterSzelesseg / 2;//Beállítja a kezdöpontot középre, hogy onnan induljon az animácio.
        ky = jatekterMagassag / 2;
        szorasok = new ArrayList();
        /*Beállítja a minimális és maximális szóráspontokat.*/
        double minX = -szorasHatar.getX() - jatekterSzelesseg * 0.000625,
                maxX = jatekterSzelesseg * 0.00125 * szorasHatar.getX() + jatekterSzelesseg * 0.00125,
                minY = -szorasHatar.getY() - jatekterMagassag / 1200,
                maxY = jatekterMagassag / 600 * szorasHatar.getY() + jatekterMagassag / 600;
        
        kartyalapok = PakliKezelo.kevertPakli();
        
        for (Kartyalap kartyalap : kartyalapok) {
            kartyalap.setKartyaKepSzelesseg(jatekterSzelesseg/22.857);
            kartyalap.setKartyaKepMagassag(jatekterMagassag/11.765);    
            aktSzoras = new Point((int) (minX + Math.random() * maxX), (int) (minY + Math.random() * maxY));
            kartyalap.setKx(kx+aktSzoras.getX());
            kartyalap.setKy(ky+aktSzoras.getY());
            szorasok.add(aktSzoras);
        }
        szalVezerlo.setKartyalapok(kartyalapok);
        laptavolsagokOsszege = 0;//minden új körben lenullázza mivel a lapleosztást elölről kell kezdeni.
    }
    
    /**
     * Az egész kártyapaklit mozgatja középről a megadott szögbe és irányba.
     */
    @SuppressWarnings("SleepWhileInLoop")
    private void kartyaPakliMozgat() {       
        aktTav = 0;       
        aktForgSzog = 0;
        elteres = 0.09375 * jatekterSzelesseg;
        vegpontok = SzogSzamito.vegpontLista(jatekosokSzama, jatekterSzelesseg, jatekterMagassag);//Lekéri a végpontok listáját a játékosok száma alapján.
        vx = vegpontok.get(dealer).x;
        vy = vegpontok.get(dealer).y;       
        vegSzog = SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, vx, vy);//Kiszámítja a beállított végpontokhoz tartozó szöget.
        /*A végpontokból levon ellentétes szögirányba egy szélességgel és magassággal arányos értéket.*/
        vx += elteres * Math.cos(Math.toRadians(vegSzog+180));
        vy += elteres * Math.sin(Math.toRadians(vegSzog+180));
        foSzog = Math.atan2(vy - ky, vx - kx);               
        vegForgSzog = SzogSzamito.forgasSzogSzamit(jatekterSzelesseg, jatekterMagassag, vx ,vy);//Kiszámítja hogy mennyivel kell elforgatni a lapokat az asztal lekerekitett szélébez viszonyítva.
        tavolsag = Math.sqrt((vy - ky) * (vy - ky) + (vx - kx) * (vx - kx));
        
        while (aktTav <= tavolsag) {
                aktTav += lepes;
                aktx = kx + aktTav * Math.cos(foSzog);
                akty = ky + aktTav * Math.sin(foSzog);
                aktForgSzog += vegForgSzog / (tavolsag / lepes);//A lépésenkénti szögelfordulást hatàrozza meg.
            try {
                sleep(ido);
            } catch (InterruptedException ex) {
                Logger.getLogger(KartyaMozgato.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            /*Beállítja a pakli aktuális pozícióját.*/
            for (byte i = 0; i < kartyalapok.size(); i++) {                 
                kartyalapok.get(i).setKx(aktx + szorasok.get(i).getX());
                kartyalapok.get(i).setKy(akty + szorasok.get(i).getY());
                kartyalapok.get(i).setForgat(aktForgSzog);
            }
            szalVezerlo.frissit();
        }
    }
    
    /**
     * Kiosztja a kártyalapokat a pakli pozíciójától indulva a játékosoknak.
     */
    @SuppressWarnings("SleepWhileInLoop")
    private void kartyalapokKioszt() {  
        try {
            szalVezerlo.setKartyaGrafikaElore(true);//A kártyalapok elsőként való kirajzolását teszi lehetővé a játéktéren.        
            double vegpontSzog;
            elteres = jatekterSzelesseg/80;//A játékos lapjai közötti távolság értékét adja meg.
            byte j, k;
            jatekosSorszam = dealer;
            Kartyalap kartyalap;
            vegpontok.addAll(vegpontok);//Megduplázza a végpontokat. Mivel egy játékosnak két lapja van, ezért kétszer kell a ciklusban ugyanazzal a végponttal számolni.
            for (byte i = 0; i < vegpontok.size(); i++) {
                jatekosSorszam++;//Megnöveli egyel az jatekosSorszam változót hogy a megfelelő játékoshoz kerüljön a kiosztott kártyalap.
                if(jatekosSorszam == jatekosokSzama) jatekosSorszam = 0;//Ha az jatekosSorszam eléri a játékosok számát akkor a számláló kinullázódik.
                kartyalap = kartyalapok.remove(kartyalapok.size()-1);//A kártyalap hivatkozást ráállítja a kartyalapok lista utolsó elemére, törli a hivatkozást a listából és hozzá adja az aktuális játékos lapjaihoz a kártyát.
                szalVezerlo.jatekosKartyalapokhozAd(jatekosSorszam, kartyalap);
                
                aktTav = 0;
                aktForgSzog = 0;
                kx = kartyalap.getKx();
                ky = kartyalap.getKy();
                vx = vegpontok.get(jatekosSorszam).x;
                vy = vegpontok.get(jatekosSorszam).y;
                vegForgSzog = SzogSzamito.forgasSzogSzamit(jatekterSzelesseg, jatekterMagassag, vx, vy);
                foSzog = Math.atan2(vy - ky, vx - kx);
                tavolsag = Math.sqrt((vy - ky) * (vy - ky) + (vx - kx) * (vx - kx));
                
                while (aktTav <= tavolsag) {
                    aktTav += lepes;
                    aktx = kx + aktTav * Math.cos(foSzog);
                    akty = ky + aktTav * Math.sin(foSzog);
                    aktForgSzog += vegForgSzog / (tavolsag / lepes);
                    kartyalap.setKx(aktx);
                    kartyalap.setKy(akty);
                    kartyalap.setForgat(aktForgSzog);
                    sleep(ido);
                    szalVezerlo.frissit();
                }
            }   szalVezerlo.setKartyaGrafikaElore(false);//A zsetonokat lesz elsőként kirajzolva a játékpanelre.
            
            jatekosokKartyalapjai = szalVezerlo.getJatekosokKartyalapjai();//Lekéri a szálvezérlőtől a játékosok kártyalapjait.
            
            while (lepes <= tavolsag) {
                for (byte i = 0; i < vegpontok.size(); i++) {
                    /*A feltételnek megfelelöen előjelet vált. A j változó a játékos
                    kártyalapjainak sorszáma a k pedig az aktuális játékos sorszáma.*/
                    if (i < jatekosokSzama) {
                        j = 0;
                        k = i;
                        elojel = -1;
                    } else {
                        j = 1;
                        k = (byte) (i - jatekosokSzama);
                        elojel = 1;
                    }
                    
                    kartyalap = jatekosokKartyalapjai.get(k).get(j);//Beállítja a hivatkozást a k-ik játékos j-ik kártyalapját.
                    
                    if(k == JatekVezerlo.EMBER_JATEKOS_SORSZAM)
                        kartyalap.setMutat(true);//Megmutatja az ember játékos lapjait.
                    
                    kx = kartyalap.getKx();
                    ky = kartyalap.getKy();
                    vx = vegpontok.get(i).x;
                    vy = vegpontok.get(i).y;
                    aktForgSzog = kartyalap.getForgat();
                    vegpontSzog = SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, vx, vy);//Kiszámítja a végponthoz tartozó szöget.
                    vegpontSzog -= 90 * elojel;//Elöjelnek megfelelöen az ehhez a szöghöz viszonyítottan +- 90 fokban hozzáad egy eltérési értéket. Ebböl jön ki a játékos egyik kártyájának végpontja.*/
                    vx += elteres * Math.cos(Math.toRadians(vegpontSzog));
                    vy += elteres * Math.sin(Math.toRadians(vegpontSzog));
                    foSzog = Math.atan2(vy - ky, vx - kx);
                    tavolsag = Math.sqrt((vy - ky) * (vy - ky) + (vx - kx) * (vx - kx));
                    aktx = kx + lepes * Math.cos(foSzog);
                    akty = ky + lepes * Math.sin(foSzog);
                    aktForgSzog += 2 * elojel;
                    kartyalap.setKx(aktx);
                    kartyalap.setKy(akty);
                    kartyalap.setForgat(aktForgSzog);
                    sleep(ido);
                    szalVezerlo.frissit();
                }
            }
            szalVezerlo.jatekVezerlesFolytat();
        } catch (InterruptedException ex) {
            Logger.getLogger(KartyaMozgato.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * A játékos bedobott lapjait elmozgatja az aszal közepe fele.
     */
    @SuppressWarnings("SleepWhileInLoop")
    private void kartyalapokBedob(){
        jatekosokKartyalapjai = szalVezerlo.getJatekosokKartyalapjai();
        vegpontok = SzogSzamito.vegpontLista(jatekosokSzama, jatekterSzelesseg, jatekterMagassag);
        List<Kartyalap> jatekosKartyalapok = jatekosokKartyalapjai.get(jatekosSorszam);    
        
        do {
            elojel = 1;
            for (Kartyalap kartyalap : jatekosKartyalapok) {
                kx = kartyalap.getKx();
                ky = kartyalap.getKy();
                vx = vegpontok.get(jatekosSorszam).x;
                vy = vegpontok.get(jatekosSorszam).y;
                vegForgSzog = SzogSzamito.forgasSzogSzamit(jatekterSzelesseg, jatekterMagassag, vx, vy);
                foSzog = Math.atan2(vy - ky, vx - kx);
                tavolsag = Math.sqrt((vy - ky) * (vy - ky) + (vx - kx) * (vx - kx));
                aktx = kx + lepes * Math.cos(foSzog);
                akty = ky + lepes * Math.sin(foSzog);
                aktForgSzog = kartyalap.getForgat() + 2 * elojel;
                elojel *= -1;
                if(kartyalap.isMutat())kartyalap.setMutat(false);
                kartyalap.setKx(aktx);
                kartyalap.setKy(akty);
                kartyalap.setForgat(aktForgSzog);
                try {
                    sleep(ido);
                } catch (InterruptedException ex) {
                    Logger.getLogger(KartyaMozgato.class.getName()).log(Level.SEVERE, null, ex);
                }
                szalVezerlo.frissit();
            }
        } while (lepes <= tavolsag);
        
        /*Az esetleges forgatási eltéréseket korrigálja*/
        for (Kartyalap kartyalap : jatekosKartyalapok) {
             kartyalap.setForgat(vegForgSzog);
        }
        
        aktTav = 0;
        elteres = jatekterSzelesseg / 64;
        vegSzog = SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, vx, vy);
        vx += elteres * Math.cos(Math.toRadians(vegSzog+180));
        vy += elteres * Math.sin(Math.toRadians(vegSzog+180));
        vegForgSzog = SzogSzamito.forgasSzogSzamit(jatekterSzelesseg, jatekterMagassag, vx, vy);
        foSzog = Math.atan2(vy - ky, vx - kx);
        tavolsag = Math.sqrt((vy - ky) * (vy - ky) + (vx - kx) * (vx - kx));
        
        while (aktTav <= tavolsag) {
                aktTav += lepes;
                aktx = kx + aktTav * Math.cos(foSzog);
                akty = ky + aktTav * Math.sin(foSzog);
            try {
                sleep(ido);
            } catch (InterruptedException ex) {
                Logger.getLogger(KartyaMozgato.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            for (Kartyalap kartyalap : jatekosKartyalapok) {                 
                kartyalap.setKx(aktx);
                kartyalap.setKy(akty);
            }
            szalVezerlo.frissit();
        }
    }
    
    /**
     * Leosztja a lapokat a paklitól indulva az asztal közepére a megfelelő eltolással.
     */
    @SuppressWarnings("SleepWhileInLoop")
    private void kartyalapLeosztas(){
        Kartyalap kartyalap;   
        int leosztandoKartyalapokSzama;
        double veletlenX, veletlenY;
        double novekmeny;
        kartyalapok = szalVezerlo.getKartyalapok();

        if (szalVezerlo.leosztottKartyalapokSzama() == 0) {
            leosztandoKartyalapokSzama = 3;
            novekmeny = jatekterSzelesseg / 2.5;
        } else {
            leosztandoKartyalapokSzama = 1;
            novekmeny = laptavolsagokOsszege;//A növekményt beállítja a már leosztott lapoktól megfelelő távolság értékre.
        }
        
        if (osszesKartyalapLeosztas) {
            leosztandoKartyalapokSzama = JatekVezerlo.LEOSZTHATO_KARTYALAPOK_SZAMA - szalVezerlo.leosztottKartyalapokSzama();
        }        
        
        for (byte i = 0; i < leosztandoKartyalapokSzama; i++) {
            veletlenX = -jatekterSzelesseg/620 + Math.random() * jatekterSzelesseg/310;
            veletlenY = -jatekterMagassag/240 + Math.random() * jatekterMagassag/120;
            kartyalap = kartyalapok.remove(kartyalapok.size()-1);
            szalVezerlo.leosztottKartyalapokhozAd(kartyalap);            
            aktTav = 0;
            kx = kartyalap.getKx();
            ky = kartyalap.getKy();
            vx = novekmeny;
            vy = jatekterMagassag/2;
            aktForgSzog = 0;
            vegForgSzog = -3 + Math.random() * 6;
            foSzog = Math.atan2(vy - ky, vx - kx);
            tavolsag = Math.sqrt((vy - ky) * (vy - ky) + (vx - kx) * (vx - kx));
            novekmeny+=kartyalap.getKartyaKepSzelesseg()+jatekterSzelesseg/160;//A leosztott lapok közötti távolságot számolja ki
            
            while (aktTav <= tavolsag) {
                aktTav += lepes;
                aktx = kx + aktTav * Math.cos(foSzog);
                akty = ky + aktTav * Math.sin(foSzog);
                aktForgSzog += vegForgSzog / (tavolsag / lepes);
                kartyalap.setKx(aktx+veletlenX);
                kartyalap.setKy(akty+veletlenY);
                kartyalap.setForgat(aktForgSzog);
                try {
                    sleep(ido);
                } catch (InterruptedException ex) {
                    Logger.getLogger(KartyaMozgato.class.getName()).log(Level.SEVERE, null, ex);
                }
                szalVezerlo.frissit();
            }            
            kartyalap.setMutat(true); 
        }
        laptavolsagokOsszege = novekmeny;
        
        if (szalVezerlo.leosztottKartyalapokSzama() == JatekVezerlo.LEOSZTHATO_KARTYALAPOK_SZAMA) {
            jatekosokKartyalapjai = szalVezerlo.getJatekosokKartyalapjai();
            List<Kartyalap> leosztottKartyalapok = szalVezerlo.getLeosztottKartyalapok();
            szalVezerlo.setNyertesPokerKezek(PokerKezKiertekelo.nyertesPokerKezKeres(jatekosokKartyalapjai, leosztottKartyalapok));
        }
    }
    
    @SuppressWarnings("SleepWhileInLoop")
    private void kartyalapokNagyit(){
        List<Kartyalap> leosztottKartyalapok = szalVezerlo.getLeosztottKartyalapok();
        jatekosokKartyalapjai = szalVezerlo.getJatekosokKartyalapjai();
        vegpontok = SzogSzamito.vegpontLista(jatekosokSzama, jatekterSzelesseg, jatekterMagassag);        
        elteres = jatekterSzelesseg * 0.046875; 
        ido = 12;
        double kartyaKepSzelesseg, kartyaKepMagassag;
        double arany = 0;
        
        szalVezerlo.grafikaElmosas(true);
        szalVezerlo.setKartyaGrafikaElore(true);
        
        do {
            for (Map.Entry<Byte, List<Kartyalap>> entrySet : jatekosokKartyalapjai.entrySet()) {
                Byte sorszam = entrySet.getKey();
                List<Kartyalap> jatekosKartyalapok = entrySet.getValue();
                elojel = 1;

                for (Kartyalap kartyalap : jatekosKartyalapok) {
                    if(!kartyalap.isMutat())kartyalap.setMutat(true);
                    
                    kx = kartyalap.getKx();
                    ky = kartyalap.getKy();
                    vx = vegpontok.get(sorszam).x;
                    vy = vegpontok.get(sorszam).y;
                    vegSzog = SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, vx, vy);
                    vx += elteres * Math.cos(Math.toRadians(vegSzog + 90 * elojel));
                    vy += elteres * Math.sin(Math.toRadians(vegSzog + 90 * elojel));
                    foSzog = Math.atan2(vy - ky, vx - kx);
                    tavolsag = Math.sqrt((vy - ky) * (vy - ky) + (vx - kx) * (vx - kx));
                    aktx = kx + lepes * Math.cos(foSzog);
                    akty = ky + lepes * Math.sin(foSzog);
                    elojel *= -1;
                    kartyaKepSzelesseg = kartyalap.getKartyaKepSzelesseg();
                    kartyaKepMagassag = kartyalap.getKartyaKepMagassag();
                    arany = kartyaKepSzelesseg / kartyaKepMagassag;
                    kartyaKepSzelesseg += lepes * arany;
                    kartyaKepMagassag += lepes;
                    kartyalap.setKx(aktx);
                    kartyalap.setKy(akty);
                    kartyalap.setKartyaKepSzelesseg(kartyaKepSzelesseg);
                    kartyalap.setKartyaKepMagassag(kartyaKepMagassag);
                    kartyalap.setForgat(kartyalap.getForgat() - 0.5 * elojel);                    
                }
            }
            
            byte szorzo = 2;
            for (byte i = 0; i < leosztottKartyalapok.size(); i++) {
                Kartyalap kartyalap = leosztottKartyalapok.get(i);
                if (i < 2) {
                    kartyalap.setKx(kartyalap.getKx() - lepes * arany * szorzo);
                    szorzo--;
                } else if (i > 2) {
                    szorzo++;
                    kartyalap.setKx(kartyalap.getKx() + lepes * arany * szorzo);
                }
                
                kartyaKepSzelesseg = kartyalap.getKartyaKepSzelesseg();
                kartyaKepMagassag = kartyalap.getKartyaKepMagassag();
                arany = kartyaKepSzelesseg / kartyaKepMagassag;
                kartyaKepSzelesseg += lepes * arany;
                kartyaKepMagassag += lepes;
                kartyalap.setKartyaKepSzelesseg(kartyaKepSzelesseg);
                kartyalap.setKartyaKepMagassag(kartyaKepMagassag);
            }

            try {
                sleep(ido);
            } catch (InterruptedException ex) {
                Logger.getLogger(KartyaMozgato.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            szalVezerlo.frissit();
        } while (lepes < tavolsag);
    }
    
    @SuppressWarnings("SleepWhileInLoop")
    private void keretAnimacio(){
        Map<Byte, PokerKez> nyertesPokerKezek = szalVezerlo.getNyertesPokerKezek();
        ido = 12;
        double keretKepSzelesseg, keretKepMagassag;
        double ertek = 0;
                
        for(int i = 0; i < 600; i++) {
            ertek = ertek > 3 ? 0 : ertek + 0.1;
 
            for (Map.Entry<Byte, PokerKez> entrySet : nyertesPokerKezek.entrySet()) {
                PokerKez pokerKez = entrySet.getValue();
                List<Kartyalap> jatekosPokerKezKartyalapok = pokerKez.getPokerKezKartyalapok();
                
                for (Kartyalap kartyalap : jatekosPokerKezKartyalapok) {
                    if (!kartyalap.isKeretRajzol()) {
                        kartyalap.setKeretRajzol(true);
                    }

                    keretKepSzelesseg = kartyalap.getKartyaKepSzelesseg() + Math.sin(ertek) * 25;
                    keretKepMagassag = kartyalap.getKartyaKepMagassag() + Math.sin(ertek) * 25;
                    kartyalap.setKeretKepSzelesseg(keretKepSzelesseg);
                    kartyalap.setKeretKepMagassag(keretKepMagassag);
                }

                try {
                    sleep(ido);
                } catch (InterruptedException ex) {
                    Logger.getLogger(KartyaMozgato.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Ha valamelyik boolean változó igaz a metódusban, akkor meghívja a hozzá tartozó metódust.
     */
    @Override
    public void run() {
        if (kartyalapokKiosztasa) {
            pakliBetolt();
            kartyaPakliMozgat();
            kartyalapokKioszt();
        }
        if(kartyalapLeosztas)kartyalapLeosztas();
        if (kartyalapokKiertekelese) {
            kartyalapokNagyit();
            keretAnimacio();
        }
        if(kartyalapokBedobasa)kartyalapokBedob();
    }    

    public void setKartyalapLeosztas(boolean kartyalapLeosztas) {
        this.kartyalapLeosztas = kartyalapLeosztas;
    }

    public void setOsszesKartyalapLeosztas(boolean osszesKartyalapLeosztas) {
        this.osszesKartyalapLeosztas = osszesKartyalapLeosztas;
    }

    public void setKartyalapokKiosztasa(boolean kartyalapokKiosztasa) {
        this.kartyalapokKiosztasa = kartyalapokKiosztasa;
    }

    public void setKartyalapokBedobasa(boolean kartyalapokBedobasa) {
        this.kartyalapokBedobasa = kartyalapokBedobasa;
    }

    public void setKartyalapokKiertekelese(boolean kartyalapokKiertekelese) {
        this.kartyalapokKiertekelese = kartyalapokKiertekelese;
    }

    public void setJatekosSorszam(byte jatekosSorszam) {
        this.jatekosSorszam = jatekosSorszam;
    }

    public void setDealer(byte dealer) {
        this.dealer = dealer;
    }
}
