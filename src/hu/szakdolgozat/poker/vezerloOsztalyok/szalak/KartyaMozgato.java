package hu.szakdolgozat.poker.vezerloOsztalyok.szalak;

import hu.szakdolgozat.poker.vezerloOsztalyok.SzalVezerlo;
import hu.szakdolgozat.poker.alapOsztalyok.Kartyalap;
import hu.szakdolgozat.poker.alapOsztalyok.PokerKez;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import hu.szakdolgozat.poker.vezerloOsztalyok.PakliKezelo;
import hu.szakdolgozat.poker.vezerloOsztalyok.PokerKezKiertekelo;
import hu.szakdolgozat.poker.vezerloOsztalyok.SzogSzamito;

public class KartyaMozgato extends Thread{    
    private SzalVezerlo szalVezerlo;
    private Map<Byte, List<Kartyalap>> jatekosokKartyalapjai;
    private Map<Byte, List<Kartyalap>> kiszalltJatekosokKartyalapjai;
    private List<Kartyalap> leosztottKartyalapok;
    private List<Kartyalap> kartyalapok;    
    private List<Point> vegpontok;
    private static List<Point> szorasok;
    private int panelSzelesseg;
    private int panelMagassag;    
    private double aktKartyaKepSzelesseg;
    private double aktKartyaKepMagassag;
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
    private boolean kartyalapLeosztasa;
    private boolean kartyalapokBedobasa;
    private boolean kartyalapokKiertekelese;
    private boolean osszesKartyalapLeosztasa;
    private boolean menuAnimacio;
    private boolean szalStop;
    private byte dealer;
    private double lepes;
    private int ido;
    private byte elojel;
    private byte jatekosokSzama;
    private byte jatekosSorszam;
    private static double laptavolsagokOsszege;

    public KartyaMozgato(SzalVezerlo szalVezerlo) {
        this.szalVezerlo = szalVezerlo;
        
        if (szalVezerlo.isJatekterPanelBetoltve()) {
            panelSzelesseg = szalVezerlo.jatekterPanelSzelesseg();
            panelMagassag = szalVezerlo.jatekterPanelMagassag();
            jatekosokSzama = szalVezerlo.getJatekosokSzama();
            vegpontok = SzogSzamito.vegpontLista(jatekosokSzama, panelSzelesseg, panelMagassag);//Lekéri a végpontok listáját a játékosok száma alapján.
        } else {
            panelSzelesseg = szalVezerlo.jatekMenuPanelSzelesseg();
            panelMagassag = szalVezerlo.jatekMenuPanelMagassag();
        }

        lepes = panelSzelesseg * 0.001875;
    }

    /**
     * A PakliKezelo osztálytól lekéri a kevert pakli listát és ezt átadja a
     * szálvezérlönek.
     */
    private void pakliBetolt() {         
        Point aktSzoras, szorasHatar = new Point(panelSzelesseg / 400, panelMagassag / 600);            
        kx = panelSzelesseg / 2;//Beállítja a kezdöpontot középre, hogy onnan induljon az animácio.
        ky = panelMagassag / 2;        
        szorasok = new ArrayList();
        /*Beállítja a minimális és maximális szóráspontokat.*/
        double minX = -szorasHatar.getX() - panelSzelesseg * 0.000625,
               maxX = panelSzelesseg * 0.00125 * szorasHatar.getX() + panelSzelesseg * 0.00125,
               minY = -szorasHatar.getY() - panelMagassag / 1200,
               maxY = panelMagassag / 600 * szorasHatar.getY() + panelMagassag / 600;
        
        kartyalapok = PakliKezelo.kevertPakli();
        
        for (Kartyalap kartyalap : kartyalapok) {
            kartyalap.setKartyaKepSzelesseg(panelSzelesseg/22.857);
            kartyalap.setKartyaKepMagassag(panelMagassag/11.765);    
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
    private void pakliMozgat() {  
        ido = 3;
        aktTav = 0;       
        aktForgSzog = 0;
        elteres = 0.09375 * panelSzelesseg;        
       
        if (kartyalapokKiertekelese) {
            kx = kartyalapok.get(0).getKx();
            ky = kartyalapok.get(0).getKy();
            vx = panelSzelesseg / 2;
            vy = panelMagassag / 2;
        } else {
            vx = vegpontok.get(dealer).x;
            vy = vegpontok.get(dealer).y;
            vegSzog = SzogSzamito.szogSzamit(panelSzelesseg, panelMagassag, vx, vy);//Kiszámítja a beállított végpontokhoz tartozó szöget.
            /*A végpontokból levon ellentétes szögirányba egy szélességgel és magassággal arányos értéket.*/
            vx += elteres * Math.cos(Math.toRadians(vegSzog + 180));
            vy += elteres * Math.sin(Math.toRadians(vegSzog + 180));
        }       
        
        foSzog = Math.atan2(vy - ky, vx - kx); 
        
        if (kartyalapokKiertekelese) {           
             aktForgSzog = kartyalapok.get(0).getForgat();
             vegForgSzog = aktForgSzog * -1; //Itt a vegForgSzog az aktForgSzog ellentétje mivel ennyivel akarjuk visszaforgatni a kártyákat.
        } else {
            vegForgSzog = SzogSzamito.forgasSzogSzamit(panelSzelesseg, panelMagassag, vx, vy);//Kiszámítja hogy mennyivel kell elforgatni a lapokat az asztal lekerekitett szélébez viszonyítva.
        }
        
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
            ido = 2;
            elteres = panelSzelesseg/80;//A játékos lapjai közötti távolság értékét adja meg.
            jatekosSorszam = dealer;   
            Kartyalap kartyalap;
            List<Byte> jatekosSorszamok = new ArrayList<>();
            double vegpontSzog;
            byte j;
            byte aktivJatekosokSzama = szalVezerlo.aktivJatekosokKeres();
            for (byte i = 0; i < aktivJatekosokSzama; i++) {
                jatekosSorszam++;//Megnöveli egyel az jatekosSorszam változót hogy a megfelelő játékoshoz kerüljön a kiosztott kártyalap.     
                jatekosSorszam = szalVezerlo.aktivJatekosSorszamKeres(jatekosSorszam);
                jatekosSorszamok.add(jatekosSorszam);
            }
            
            jatekosSorszamok.addAll(jatekosSorszamok);
            
            for (byte i = 0; i < jatekosSorszamok.size(); i++) {                               
                kartyalap = kartyalapok.remove(kartyalapok.size()-1);//A kártyalap hivatkozást ráállítja a kartyalapok lista utolsó elemére, törli a hivatkozást a listából.      
                aktTav = 0;
                aktForgSzog = 0;
                jatekosSorszam = jatekosSorszamok.get(i);
                kx = kartyalap.getKx();
                ky = kartyalap.getKy();
                vx = vegpontok.get(jatekosSorszam).x;
                vy = vegpontok.get(jatekosSorszam).y;
                vegForgSzog = SzogSzamito.forgasSzogSzamit(panelSzelesseg, panelMagassag, vx, vy);
                foSzog = Math.atan2(vy - ky, vx - kx);
                tavolsag = Math.sqrt((vy - ky) * (vy - ky) + (vx - kx) * (vx - kx));
                szalVezerlo.jatekosKartyalapokhozAd(jatekosSorszam, kartyalap);//Hozzá adja az aktuális játékos lapjaihoz a kártyát.         
                
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
            }   
            
            szalVezerlo.setKartyaGrafikaElore(false);//A zsetonokat lesz elsőként kirajzolva a játékpanelre.            
            jatekosokKartyalapjai = szalVezerlo.getJatekosokKartyalapjai();//Lekéri a szálvezérlőtől a játékosok kártyalapjait.
            
            while (lepes <= tavolsag) {
                for (byte i = 0; i < jatekosSorszamok.size(); i++) {
                    jatekosSorszam = jatekosSorszamok.get(i);   
                    
                    if (i < jatekosSorszamok.size()/2) {
                        j = 0;
                        elojel = -1;
                    } else {
                        j = 1;
                        elojel = 1;
                    }                    
                    
                    kartyalap = jatekosokKartyalapjai.get(jatekosSorszam).get(j);
                    
                    if(jatekosSorszam == JatekVezerlo.EMBER_JATEKOS_SORSZAM)
                        kartyalap.setMutat(true);//Megmutatja az ember játékos lapjait.
                    
                    kx = kartyalap.getKx();
                    ky = kartyalap.getKy();
                    vx = vegpontok.get(jatekosSorszam).x;
                    vy = vegpontok.get(jatekosSorszam).y;
                    aktForgSzog = kartyalap.getForgat();
                    vegpontSzog = SzogSzamito.szogSzamit(panelSzelesseg, panelMagassag, vx, vy);//Kiszámítja a végponthoz tartozó szöget.
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
        ido = 2;
        kiszalltJatekosokKartyalapjai = szalVezerlo.getKiszalltJatekosokKartyalapjai();        
        List<Kartyalap> jatekosKartyalapok = kiszalltJatekosokKartyalapjai.get(jatekosSorszam);    
        
        do {
            elojel = 1;
            for (Kartyalap kartyalap : jatekosKartyalapok) {
                kx = kartyalap.getKx();
                ky = kartyalap.getKy();
                vx = vegpontok.get(jatekosSorszam).x;
                vy = vegpontok.get(jatekosSorszam).y;
                vegForgSzog = SzogSzamito.forgasSzogSzamit(panelSzelesseg, panelMagassag, vx, vy);
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
                szalVezerlo.frissit();
                
                try {
                    sleep(ido);
                } catch (InterruptedException ex) {
                    Logger.getLogger(KartyaMozgato.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } while (lepes <= tavolsag);
        
        /*Az esetleges forgatási eltéréseket korrigálja*/
        for (Kartyalap kartyalap : jatekosKartyalapok) {
             kartyalap.setForgat(vegForgSzog);
        }
        
        aktTav = 0;
        elteres = panelSzelesseg / 64;
        vegSzog = SzogSzamito.szogSzamit(panelSzelesseg, panelMagassag, vx, vy);
        vx += elteres * Math.cos(Math.toRadians(vegSzog+180));
        vy += elteres * Math.sin(Math.toRadians(vegSzog+180));
        vegForgSzog = SzogSzamito.forgasSzogSzamit(panelSzelesseg, panelMagassag, vx, vy);
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
        ido = 2;
        kartyalapok = szalVezerlo.getKartyalapok();

        if (szalVezerlo.leosztottKartyalapokSzama() == 0) {
            leosztandoKartyalapokSzama = 3;
            novekmeny = panelSzelesseg / 2.5;
        } else {
            leosztandoKartyalapokSzama = 1;
            novekmeny = laptavolsagokOsszege;//A növekményt beállítja a már leosztott lapoktól megfelelő távolság értékre.
        }

        if (osszesKartyalapLeosztasa) {
            leosztandoKartyalapokSzama = JatekVezerlo.LEOSZTHATO_KARTYALAPOK_SZAMA - szalVezerlo.leosztottKartyalapokSzama();
        } else {
            szalVezerlo.jatekVezerlesFolytat();
        }
        
        for (byte i = 0; i < leosztandoKartyalapokSzama; i++) {
            veletlenX = -panelSzelesseg/620 + Math.random() * panelSzelesseg/310;
            veletlenY = -panelMagassag/240 + Math.random() * panelMagassag/120;
            kartyalap = kartyalapok.remove(kartyalapok.size()-1);
            szalVezerlo.leosztottKartyalapokhozAd(kartyalap);            
            aktTav = 0;
            kx = kartyalap.getKx();
            ky = kartyalap.getKy();
            vx = novekmeny;
            vy = panelMagassag/2;
            aktForgSzog = 0;
            vegForgSzog = -3 + Math.random() * 6;
            foSzog = Math.atan2(vy - ky, vx - kx);
            tavolsag = Math.sqrt((vy - ky) * (vy - ky) + (vx - kx) * (vx - kx));
            novekmeny += kartyalap.getKartyaKepSzelesseg() + panelSzelesseg / 160;//A leosztott lapok közötti távolságot számolja ki
            
            while (aktTav <= tavolsag) {
                aktTav += lepes;
                aktx = kx + aktTav * Math.cos(foSzog);
                akty = ky + aktTav * Math.sin(foSzog);
                aktForgSzog += vegForgSzog / (tavolsag / lepes);
                kartyalap.setKx(aktx+veletlenX);
                kartyalap.setKy(akty+veletlenY);
                kartyalap.setForgat(aktForgSzog);               
                szalVezerlo.frissit();
                
                try {
                    sleep(ido);
                } catch (InterruptedException ex) {
                    Logger.getLogger(KartyaMozgato.class.getName()).log(Level.SEVERE, null, ex);
                }
            }      
            
            kartyalap.setMutat(true); 
        }
        
        laptavolsagokOsszege = novekmeny;
        
        if (szalVezerlo.leosztottKartyalapokSzama() == JatekVezerlo.LEOSZTHATO_KARTYALAPOK_SZAMA) {
            jatekosokKartyalapjai = szalVezerlo.getJatekosokKartyalapjai();
            leosztottKartyalapok = szalVezerlo.getLeosztottKartyalapok();
            szalVezerlo.setNyertesPokerKezek(PokerKezKiertekelo.nyertesPokerKezKeres(jatekosokKartyalapjai, leosztottKartyalapok));
        }        
    }
    
    /**
     * Felnagyítja a leosztott kártyalapokat és a játékosok kártyalapjait.
     */
    @SuppressWarnings("SleepWhileInLoop")
    private void kartyalapokNagyit(){   
        elteres = panelSzelesseg * 0.046875; 
        ido = 12;
        double arany = 0;
        Kartyalap leosztottKartyalap;
        
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
                    vegSzog = SzogSzamito.szogSzamit(panelSzelesseg, panelMagassag, vx, vy);
                    vx += elteres * Math.cos(Math.toRadians(vegSzog + 90 * elojel));
                    vy += elteres * Math.sin(Math.toRadians(vegSzog + 90 * elojel));
                    foSzog = Math.atan2(vy - ky, vx - kx);
                    tavolsag = Math.sqrt((vy - ky) * (vy - ky) + (vx - kx) * (vx - kx));
                    aktx = kx + lepes * Math.cos(foSzog);
                    akty = ky + lepes * Math.sin(foSzog);
                    elojel *= -1;
                    aktKartyaKepSzelesseg = kartyalap.getKartyaKepSzelesseg();
                    aktKartyaKepMagassag = kartyalap.getKartyaKepMagassag();
                    arany = aktKartyaKepSzelesseg / aktKartyaKepMagassag;
                    aktKartyaKepSzelesseg += lepes * arany;
                    aktKartyaKepMagassag += lepes;
                    kartyalap.setKx(aktx);
                    kartyalap.setKy(akty);
                    kartyalap.setKartyaKepSzelesseg(aktKartyaKepSzelesseg);
                    kartyalap.setKartyaKepMagassag(aktKartyaKepMagassag);
                    kartyalap.setForgat(kartyalap.getForgat() - 0.5 * elojel);                    
                }
            }
            
            byte szorzo = 2;
            
            for (byte i = 0; i < leosztottKartyalapok.size(); i++) {
                leosztottKartyalap = leosztottKartyalapok.get(i);
                if (i < 2) {
                    leosztottKartyalap.setKx(leosztottKartyalap.getKx() - lepes * arany * szorzo);
                    szorzo--;
                } else if (i > 2) {
                    szorzo++;
                    leosztottKartyalap.setKx(leosztottKartyalap.getKx() + lepes * arany * szorzo);
                }
                
                aktKartyaKepSzelesseg = leosztottKartyalap.getKartyaKepSzelesseg();
                aktKartyaKepMagassag = leosztottKartyalap.getKartyaKepMagassag();
                arany = aktKartyaKepSzelesseg / aktKartyaKepMagassag;
                aktKartyaKepSzelesseg += lepes * arany;
                aktKartyaKepMagassag += lepes;
                leosztottKartyalap.setKartyaKepSzelesseg(aktKartyaKepSzelesseg);
                leosztottKartyalap.setKartyaKepMagassag(aktKartyaKepMagassag);
            }

            szalVezerlo.frissit();
            
            try {
                sleep(ido);
            } catch (InterruptedException ex) {
                Logger.getLogger(KartyaMozgato.class.getName()).log(Level.SEVERE, null, ex);
            }            
        } while (lepes < tavolsag);
    }
    
    /**
     * A nyertes kártyalapok körül egy keretet animál.
     */
    @SuppressWarnings("SleepWhileInLoop")
    private void keretAnimacio(){
        Map<Byte, PokerKez> nyertesPokerKezek = szalVezerlo.getNyertesPokerKezek();
        Set<Kartyalap> nyertesKartyalapok = new HashSet();
        ido = 12;
        double keretKepSzelesseg, keretKepMagassag;
        double ertek = 0;
        
        for (Map.Entry<Byte, PokerKez> entrySet : nyertesPokerKezek.entrySet()) {
            PokerKez pokerKez = entrySet.getValue();
            List<Kartyalap> jatekosPokerKezKartyalapok = pokerKez.getPokerKezKartyalapok();
            nyertesKartyalapok.addAll(jatekosPokerKezKartyalapok);
        }
        
        for (int i = 0; i < 600; i++) {
            ertek = ertek > 3 ? 0 : ertek + 0.1;

            for (Kartyalap kartyalap : nyertesKartyalapok) {
                if (!kartyalap.isKeretRajzol()) {
                    kartyalap.setKeretRajzol(true);
                }

                keretKepSzelesseg = kartyalap.getKartyaKepSzelesseg() + Math.sin(ertek) * panelSzelesseg / 64;
                keretKepMagassag = kartyalap.getKartyaKepMagassag() + Math.sin(ertek) * panelSzelesseg / 64;
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

    /**
     * Vissza mozgatja a paklit és a többi kártyalapot az asztal közepére.
     */
    @SuppressWarnings("SleepWhileInLoop")
    private void kartyalapokPakliba(){
        szalVezerlo.grafikaElmosas(false);
        List<Kartyalap> mozgatandoKartyalapok = new CopyOnWriteArrayList<>();  
        List<Kartyalap> nagyitottKartyalapok = new CopyOnWriteArrayList<>();
        kiszalltJatekosokKartyalapjai = szalVezerlo.getKiszalltJatekosokKartyalapjai();
        byte kartyalapokVegpontban = 0;
        double arany;
        ido = 3;
        
        mozgatandoKartyalapok.addAll(leosztottKartyalapok);
        nagyitottKartyalapok.addAll(leosztottKartyalapok);        
        
        for (Map.Entry<Byte, List<Kartyalap>> entrySet : jatekosokKartyalapjai.entrySet()) {
            List<Kartyalap> jatekosKartyalapok = entrySet.getValue();
            mozgatandoKartyalapok.addAll(jatekosKartyalapok);
            nagyitottKartyalapok.addAll(jatekosKartyalapok);
        }       

        if (kiszalltJatekosokKartyalapjai != null) {
            for (Map.Entry<Byte, List<Kartyalap>> entrySet : kiszalltJatekosokKartyalapjai.entrySet()) {
                List<Kartyalap> jatekosKartyalapok = entrySet.getValue();
                mozgatandoKartyalapok.addAll(jatekosKartyalapok);
            }            
        }
        
        try {           
            while (kartyalapokVegpontban != mozgatandoKartyalapok.size()) {
                kartyalapokVegpontban = 0;
                
                for (Kartyalap kartyalap : mozgatandoKartyalapok) {
                    if (kartyalap.isMutat()) {
                        kartyalap.setMutat(false);
                    }
                    
                    kx = kartyalap.getKx();
                    ky = kartyalap.getKy();
                    vx = panelSzelesseg / 2;
                    vy = panelMagassag / 2;
                    tavolsag = Math.sqrt((vy - ky) * (vy - ky) + (vx - kx) * (vx - kx));
                    foSzog = Math.atan2(vy - ky, vx - kx);
                    aktx = kx + lepes * Math.cos(foSzog);
                    akty = ky + lepes * Math.sin(foSzog);

                    if (lepes >= tavolsag) {
                        kartyalapokVegpontban++;
                    } else {
                        kartyalap.setKx(aktx);
                        kartyalap.setKy(akty);
                    }

                    aktForgSzog = Math.round(kartyalap.getForgat());
                    if (Math.abs(aktForgSzog) > 0) {

                        if (aktForgSzog > 0) {
                            aktForgSzog--;
                        }

                        if (aktForgSzog < 0) {
                            aktForgSzog++;
                        }

                        kartyalap.setForgat(aktForgSzog);
                    }
                }

                szalVezerlo.frissit();
                sleep(ido);
            }
            
            pakliMozgat();
            
            do {
                for (Kartyalap kartyalap : nagyitottKartyalapok) {
                    aktKartyaKepSzelesseg = kartyalap.getKartyaKepSzelesseg();
                    aktKartyaKepMagassag = kartyalap.getKartyaKepMagassag();
                    arany = aktKartyaKepSzelesseg / aktKartyaKepMagassag;
                    aktKartyaKepSzelesseg -= lepes * arany;
                    aktKartyaKepMagassag -= lepes;
                    kartyalap.setKartyaKepSzelesseg(aktKartyaKepSzelesseg);
                    kartyalap.setKartyaKepMagassag(aktKartyaKepMagassag);
                }

                sleep(ido);
                szalVezerlo.frissit();
            } while (aktKartyaKepSzelesseg > panelSzelesseg / 22.857);
        } catch (InterruptedException ex) {
            Logger.getLogger(KartyaMozgato.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    /**
     * A játékmenü háttérben beúsztatja a kártyalapokat.
     */
    @SuppressWarnings("SleepWhileInLoop")
    private void kartyalapokBeusztatasa(){   
        kartyalapok = PakliKezelo.pakliBetolt();
        szalVezerlo.setKartyalapok(kartyalapok);    
        elteres = 0.5;  
        boolean irany;
        Kartyalap kartyalap;
        
        while(!szalStop && !kartyalapok.isEmpty()){
            kartyalap = kartyalapok.get((int) (Math.random() * kartyalapok.size()));
            kartyalap.setMutat(true);
            kartyalap.setKartyaKepSzelesseg(panelSzelesseg * 0.06875);
            kartyalap.setKartyaKepMagassag(panelSzelesseg / 10);
            ido = (int) (15 + Math.random() * 10);
            irany = Math.random() < 0.5;
            kx = irany ? 0 : panelSzelesseg;
            ky = panelMagassag / 2;
            vx = irany ? panelSzelesseg : 0;
            vy = panelSzelesseg / 8 + Math.random() * panelSzelesseg / 2;
            foSzog = Math.atan2(vy - ky, vx - kx);
            tavolsag = Math.sqrt((vy - ky) * (vy - ky) + (vx - kx) * (vx - kx));
            aktTav = 0;
            aktForgSzog = 0;
            
            if(Math.random() < 0.5) elteres *= -1;
            
            while (aktTav <= tavolsag) {
                aktx = kx + aktTav * Math.cos(foSzog);
                akty = ky + aktTav * Math.sin(foSzog);
                aktForgSzog += elteres;
                aktTav += lepes;                
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

            kartyalapok.remove(kartyalap);
        }
    }
    
    /**
     * Ha valamelyik boolean változó igaz a metódusban, akkor meghívja a hozzá tartozó metódust.
     */
    @Override
    public void run() {
        if (kartyalapokKiosztasa) {
            pakliBetolt();
            pakliMozgat();
            kartyalapokKioszt();
        }
        
        if (kartyalapLeosztasa) {
            if (szalVezerlo.getJatekosokKartyalapjai().size() > 1) {
                kartyalapLeosztas();
            } else {
                jatekosokKartyalapjai = szalVezerlo.getJatekosokKartyalapjai();
                leosztottKartyalapok = szalVezerlo.getLeosztottKartyalapok();
                szalVezerlo.setNyertesPokerKezek(PokerKezKiertekelo.nyertesPokerKezKeres(jatekosokKartyalapjai, leosztottKartyalapok));
            }
        }

        if (kartyalapokKiertekelese) {
            if (szalVezerlo.getJatekosokKartyalapjai().size() > 1) {
                kartyalapokNagyit();
                keretAnimacio();
            }
            
            kartyalapokPakliba();
        }
        
        if(kartyalapokBedobasa)kartyalapokBedob();
        
        if(menuAnimacio){
            kartyalapokBeusztatasa();
        }
    }    

    public void setKartyalapLeosztasa(boolean kartyalapLeosztasa) {
        this.kartyalapLeosztasa = kartyalapLeosztasa;
    }

    public void setOsszesKartyalapLeosztasa(boolean osszesKartyalapLeosztasa) {
        this.osszesKartyalapLeosztasa = osszesKartyalapLeosztasa;
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

    public void setMenuAnimacio(boolean menuAnimacio) {
        this.menuAnimacio = menuAnimacio;
    }

    public void setSzalStop(boolean szalStop) {
        this.szalStop = szalStop;
    }

    public void setJatekosSorszam(byte jatekosSorszam) {
        this.jatekosSorszam = jatekosSorszam;
    }

    public void setDealer(byte dealer) {
        this.dealer = dealer;
    }
}
