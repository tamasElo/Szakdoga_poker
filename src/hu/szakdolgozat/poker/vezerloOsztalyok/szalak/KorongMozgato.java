package hu.szakdolgozat.poker.vezerloOsztalyok.szalak;

import hu.szakdolgozat.poker.vezerloOsztalyok.SzalVezerlo;
import hu.szakdolgozat.poker.alapOsztalyok.Dealer;
import hu.szakdolgozat.poker.alapOsztalyok.Korong;
import hu.szakdolgozat.poker.vezerloOsztalyok.AdatKezelo;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import hu.szakdolgozat.poker.vezerloOsztalyok.SzogSzamito;
import java.awt.Dimension;
import java.util.Iterator;
import java.util.Map;

public class KorongMozgato extends Thread {

    private static List<Point> vegpontLista;
    private static boolean korongokBetoltve;
    private Map<String, List<Double>> xmlAdatok;
    private Iterator<Double> itr;
    private List<Korong> korongok;
    private byte dealerJatekosSorszam;
    private int jatekterSzelesseg;
    private int jatekterMagassag;
    private byte jatekosokSzama;
    private double szog;
    private double elteres;
    private SzalVezerlo szalVezerlo;

    public KorongMozgato(SzalVezerlo szalVezerlo) {
        this.szalVezerlo = szalVezerlo;
        jatekterSzelesseg = szalVezerlo.jatekterPanelSzelesseg();
        jatekterMagassag = szalVezerlo.jatekterPanelMagassag();
        jatekosokSzama = szalVezerlo.getJatekosokSzama();
        xmlAdatok = AdatKezelo.aranyErtekekBetolt("KorongMozgato", new Dimension(jatekterSzelesseg, jatekterMagassag));
        itr = xmlAdatok.get("Konstruktor").iterator();
        elteres = itr.next();
    }

    private void korongokBetolt() {
        korongok = new ArrayList<>();
        vegpontLista = SzogSzamito.vegpontLista(jatekosokSzama, jatekterSzelesseg, jatekterMagassag);        
        double meret;
        byte i = dealerJatekosSorszam;
        double x, y;
        double veletlenForgSzog;
        
        korongok.add(szalVezerlo.getDealer());
        korongok.add(szalVezerlo.getKisVak());
        korongok.add(szalVezerlo.getNagyVak());
        
        for (Korong korong : korongok) {
            veletlenForgSzog = Math.random()*360; //Létrehoz egy véletlen szöget 0 és 360 fok között. A létrehozott értéknek megfelelően fognak elfordulni a zsetonok.                

            if (korong instanceof Dealer) {
                xmlAdatok = AdatKezelo.aranyErtekekBetolt("GrafikaElemek", new Dimension(jatekterSzelesseg, jatekterMagassag));
                itr = xmlAdatok.get("Dealer").iterator();
                meret = itr.next();
            } else {
                if (++i == jatekosokSzama) {
                    i = 0;
                }
                
                xmlAdatok = AdatKezelo.aranyErtekekBetolt("GrafikaElemek", new Dimension(jatekterSzelesseg, jatekterMagassag));
                itr = xmlAdatok.get("Vak").iterator();
                meret = itr.next();
            }
            
            x = vegpontLista.get(i).x;
            y = vegpontLista.get(i).y;
            szog = SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y) + 140; //Kiszámolja hogy az adott x,y pozícióban lévő pont hány fokos szöget zár be. Ehhez a szöghöz hozzá ad még 140 fokot.
            x += elteres * Math.cos(Math.toRadians(szog));//Az x koordináta pozícióját eltolja az elteres értékkel a megadott szög irányba.
            y += elteres * Math.sin(Math.toRadians(szog));
            korong.setKx(x);
            korong.setKy(y);
            korong.setKorongKepMagassag(meret);
            korong.setKorongKepSzelesseg(meret);
            korong.setForgat(veletlenForgSzog);
        }
        
        szalVezerlo.setKorongok(korongok);
        korongokBetoltve = true;
    }

    @SuppressWarnings("SleepWhileInLoop")
    private void korongokMozgat() {
        korongok = szalVezerlo.getKorongok();        
        double kx, ky, vx, vy, aktx, akty;
        byte sorszam;
        double foSzog;
        double tavolsag;
        xmlAdatok = AdatKezelo.aranyErtekekBetolt("KorongMozgato", new Dimension(jatekterSzelesseg, jatekterMagassag));
        itr = xmlAdatok.get("KorongokMozgat").iterator();
        double lepes = itr.next();
        long ido = 3;
        byte korongokVegpontban = 0;
        
        while (korongokVegpontban != korongok.size()) {
            korongokVegpontban = 0;
            sorszam = dealerJatekosSorszam;
            
            for (Korong korong : korongok) {
                kx = korong.getKx();
                ky = korong.getKy();                
                vx = vegpontLista.get(sorszam).getX();
                vy = vegpontLista.get(sorszam).getY();                
                sorszam = szalVezerlo.aktivJatekosSorszamKeres(++sorszam);                
                szog = SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, vx, vy) + 140;
                vx += elteres * Math.cos(Math.toRadians(szog));
                vy += elteres * Math.sin(Math.toRadians(szog));
                tavolsag = Math.sqrt((vy - ky) * (vy - ky) + (vx - kx) * (vx - kx));
                foSzog = Math.atan2(vy - ky, vx - kx);
                aktx = kx + lepes * Math.cos(foSzog);
                akty = ky + lepes * Math.sin(foSzog);
                if (lepes >= tavolsag) {
                    korongokVegpontban++;
                } else {
                    korong.setKx(aktx);
                    korong.setKy(akty);
                }
            }
      
            try {
                sleep(ido);
            } catch (InterruptedException ex) {
                Logger.getLogger(KorongMozgato.class.getName()).log(Level.SEVERE, null, ex);
            }

            szalVezerlo.frissit();
        }
    }
    
    @Override
    public void run() {
        if (!korongokBetoltve) {
            korongokBetolt();
        }else{
            korongokMozgat();
        }
    }    

    public static void setKorongokBetoltve(boolean korongokBetoltve) {
        KorongMozgato.korongokBetoltve = korongokBetoltve;
    }
    
    public void setDealer(byte dealer) {
        this.dealerJatekosSorszam = dealer;
    }
}
