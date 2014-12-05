package hu.szakdolgozat.poker.vezerloOsztalyok.szalak;

import hu.szakdolgozat.poker.burkoloOsztalyok.PokerKez;
import hu.szakdolgozat.poker.vezerloOsztalyok.SzalVezerlo;
import hu.szakdolgozat.poker.alapOsztalyok.Zseton;
import hu.szakdolgozat.poker.vezerloOsztalyok.AdatKezelo;
import hu.szakdolgozat.poker.vezerloOsztalyok.AudioLejatszo;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import hu.szakdolgozat.poker.vezerloOsztalyok.SzogSzamito;
import hu.szakdolgozat.poker.vezerloOsztalyok.ZsetonKezelo;
import java.awt.Dimension;
import java.util.Iterator;

public class ZsetonMozgato extends Thread {

    private SzalVezerlo szalVezerlo;
    private Map<String, List<Double>> zsetonMozgatoXmlAdatok;    
    private Map<String, List<Double>> zsetonXmlAdatok;
    private Iterator<Double> itr;
    private byte jatekosokSzama;
    private int jatekterSzelesseg;
    private int jatekterMagassag;
    private boolean jatekosTetMozgatasa;
    private boolean potNyertesekhezMozgatasa;
    private byte jatekosSorszam;
    private List<Point> vegpontLista;
    private Point vegpont;
    private int jatekosTetOsszege;
    private double x;
    private double y;
    private double kx;
    private double ky;
    private double vx;
    private double vy;
    private double aktx;
    private double akty;
    private double tavolsag;
    private double elteres;
    private double lepes;
    private double szoras;
    private double szorasMin;
    private double szorasMax;
    private double veletlenForgSzog;
    private double szog;    

    public ZsetonMozgato(SzalVezerlo szalVezerlo) {
        this.szalVezerlo = szalVezerlo;
        jatekterSzelesseg = szalVezerlo.jatekterPanelSzelesseg();
        jatekterMagassag = szalVezerlo.jatekterPanelMagassag();
        jatekosokSzama = szalVezerlo.getJatekosokSzama();
        vegpontLista = SzogSzamito.vegpontLista(jatekosokSzama, jatekterSzelesseg, jatekterMagassag);
        zsetonXmlAdatok =  AdatKezelo.aranyErtekekBetolt("GrafikaElemek", new Dimension(jatekterSzelesseg, jatekterMagassag));
        zsetonMozgatoXmlAdatok = AdatKezelo.aranyErtekekBetolt("ZsetonMozgato", new Dimension(jatekterSzelesseg, jatekterMagassag));
        itr = zsetonMozgatoXmlAdatok.get("Konstruktor").iterator();
        lepes = itr.next();
        szorasMin = itr.next();
        szorasMax = itr.next();
    }

    /**
     * Betölti a zsetonokat és megfelelően elrendezi a játéktéren.
     */
    public void zsetonokBetolt() {
        Map<Byte, List<Zseton>> jatekosokZsetonjai;
        List<Zseton> zsetonok;
        int zsetonOsszeg = 0;
        
        if ((jatekosokZsetonjai = szalVezerlo.getJatekosokZsetonjai()) == null) {
            jatekosokZsetonjai = new ConcurrentHashMap<>();
            zsetonOsszeg = szalVezerlo.getZsetonOsszeg();
        }
        
        for (byte i = 0; i < jatekosokSzama; i++) {
            if (zsetonOsszeg > 0) {
                zsetonok = ZsetonKezelo.zsetonKioszt(zsetonOsszeg);
                jatekosokZsetonjai.put(i, zsetonok);
            } else {
                zsetonok = jatekosokZsetonjai.get(i);
            }
            
            for (Zseton zseton : zsetonok) {    
                itr = zsetonMozgatoXmlAdatok.get("ZsetonokBetolt").iterator();
                veletlenForgSzog = Math.random() * 360;//Létrehoz egy véletlen szöget 0 és 360 fok között. A létrehozott értéknek megfelelően fognak elfordulni a zsetonok.                
                szoras = szorasMin + Math.random() * szorasMax;//Létrehoz egy véletlen számot. Ennyivel fognak a zsetonok eltérni az aktuális x,y középponttól.
                x = vegpontLista.get(i).x;
                y = vegpontLista.get(i).y;
                szog = SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y) + 90;//Kiszámolja hogy az adott x,y pozícióban lévő pont hány fokos szöget zár be. Ehhez a szöghöz hozzá ad még kilencven fokot.
                elteres = itr.next();
                x += elteres * Math.cos(Math.toRadians(szog));//Az x koordináta pozícióját eltolja az elteres értékkel a megadott szög irányba.
                y += elteres * Math.sin(Math.toRadians(szog));
                vegpont = SzogSzamito.vegpontSzamit(SzogSzamito.foSzogSzamit(jatekterSzelesseg, jatekterMagassag, x, y), jatekterSzelesseg, jatekterMagassag);//kiszámolja az új végontot.
                elteres = itr.next();
                x = vegpont.x + elteres * Math.cos(Math.toRadians(SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y)));//Az új végpont x értékét eltolja az eltérés értékkel a megadott irányba.
                y = vegpont.y + elteres * Math.sin(Math.toRadians(SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y)));
                szog = SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y);//Kiszámolja az x,y értékekhez tartozó szöget és hozzáad 90 fokot;
                jatekosZsetonPoziciok(zseton.getErtek());
                x += elteres * Math.cos(Math.toRadians(szog)) + szoras;
                y += elteres * Math.sin(Math.toRadians(szog)) + szoras;
                zseton.setKx(x);
                zseton.setKy(y);                
                itr = zsetonXmlAdatok.get("Zseton").iterator();
                zseton.setKorongKepSzelesseg(itr.next());
                zseton.setKorongKepMagassag(itr.next());
                zseton.setForgat(veletlenForgSzog);
            }
        }

        if (szalVezerlo.getJatekosokZsetonjai() == null) {
            szalVezerlo.setJatekosokZsetonjai(jatekosokZsetonjai);//Átadja a szálvezérlőnek a jatekosokZsetonjai HashMap-et.
        } else {
            for (Zseton zseton : szalVezerlo.getPot()) {
                itr = zsetonMozgatoXmlAdatok.get("JatekosTetMozgat").iterator();
                szoras = -jatekterSzelesseg / 800 + Math.random() * jatekterSzelesseg / 400;
                x = itr.next();
                y = itr.next();
                potZsetonPoziciok(zseton.getErtek());
                x += elteres * Math.cos(Math.toRadians(szog)) + szoras;
                y += elteres * Math.sin(Math.toRadians(szog)) + szoras;
                zseton.setKx(x);
                zseton.setKy(y);
                itr = zsetonXmlAdatok.get("Zseton").iterator();
                zseton.setKorongKepSzelesseg(itr.next());
                zseton.setKorongKepMagassag(itr.next());
            }
        }
    }

    /**
     * Újratölti a megadott játékos zsetonjait és elhelyezi a megfelelő pozícióban.
     */
    private void zsetonokUjratolt(List<Zseton> zsetonok) {        
        for (Zseton zseton : zsetonok) {
            itr = zsetonMozgatoXmlAdatok.get("ZsetonokUjratolt").iterator();
            veletlenForgSzog = Math.random() * 360;
            szoras = szorasMin + Math.random() * szorasMax;
            x = vegpontLista.get(jatekosSorszam).x;
            y = vegpontLista.get(jatekosSorszam).y;
            szog = SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y) + 90;
            elteres = itr.next();
            x += elteres * Math.cos(Math.toRadians(szog));
            y += elteres * Math.sin(Math.toRadians(szog));
            vegpont = SzogSzamito.vegpontSzamit(SzogSzamito.foSzogSzamit(jatekterSzelesseg, jatekterMagassag, x, y), jatekterSzelesseg, jatekterMagassag);//kiszámolja az új végontot.
            elteres = itr.next();
            x = vegpont.x + elteres * Math.cos(Math.toRadians(SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y)));
            y = vegpont.y + elteres * Math.sin(Math.toRadians(SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y)));
            szog = SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y);            
            jatekosZsetonPoziciok(zseton.getErtek());
            x += elteres * Math.cos(Math.toRadians(szog)) + szoras;
            y += elteres * Math.sin(Math.toRadians(szog)) + szoras;
            zseton.setKx(x);
            zseton.setKy(y);
            itr = zsetonXmlAdatok.get("Zseton").iterator();
            zseton.setKorongKepSzelesseg(itr.next());
            zseton.setKorongKepMagassag(itr.next());
            zseton.setForgat(veletlenForgSzog);
        }
    }
    
    /**
     * A játékos tétjét mozgatja a megfelelő pozícióba.
     */
    @SuppressWarnings("SleepWhileInLoop")
    private void jatekosTetMozgat() {               
        List<Point> vegPontok = new ArrayList<>();        
        List<Zseton> jatekosZsetonjai = szalVezerlo.getJatekosokZsetonjai().get(jatekosSorszam);        
        List<Zseton> jatekosTetje = ZsetonKezelo.pot(jatekosZsetonjai, jatekosTetOsszege);
        
        szalVezerlo.pothozAd(jatekosTetje);         
        szalVezerlo.setMenthet(false);
        
        for (Zseton zseton : jatekosZsetonjai) {
            if (zseton.getKx() == 0) {
                zsetonokUjratolt(jatekosZsetonjai);
                break;
            }
        }
     
        for (Zseton zseton : jatekosTetje) {
            if (zseton.getKx() == 0) {
                zsetonokUjratolt(jatekosTetje);
                break;
            }
        }
        
        for (Zseton zseton : jatekosTetje) { 
            itr = zsetonMozgatoXmlAdatok.get("JatekosTetMozgat").iterator();
            szoras = -jatekterSzelesseg / 800 + Math.random() * jatekterSzelesseg / 400;
            x = itr.next();
            y = itr.next();      
            potZsetonPoziciok(zseton.getErtek());
            x += elteres * Math.cos(Math.toRadians(szog)) + szoras;
            y += elteres * Math.sin(Math.toRadians(szog)) + szoras;
            vegPontok.add(new Point((int)x, (int)y));
        }
         
        double zsetonokVegpontban = 0;
        long ido = 3;
        Zseton zseton;
        AudioLejatszo.audioLejatszas(AudioLejatszo.ZSETON_CSORGES, false);
        
        while(zsetonokVegpontban != jatekosTetje.size()){
            zsetonokVegpontban = 0;
            
            for (int i = 0; i < jatekosTetje.size(); i++) {
                zseton = jatekosTetje.get(i);
                kx = zseton.getKx();
                ky = zseton.getKy();
                vx = vegPontok.get(i).getX();
                vy = vegPontok.get(i).getY();
                tavolsag = Math.sqrt((vy - ky) * (vy - ky) + (vx - kx) * (vx - kx));
                szog = Math.atan2(vy - ky, vx - kx);
                aktx = kx + lepes * Math.cos(szog);
                akty = ky + lepes * Math.sin(szog);
                
                if (lepes >= tavolsag) {
                    zsetonokVegpontban++;
                } else {
                    zseton.setKx(aktx);
                    zseton.setKy(akty);
                }
            }
            
            try {
                sleep(ido);
            } catch (InterruptedException ex) {
                Logger.getLogger(ZsetonMozgato.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * A nyertes játékos zsetonjaihoz mozgatja a potban lévő zsetonokat.
     */
    @SuppressWarnings("SleepWhileInLoop")
    private void potNyertesekhezMozgat() {
        List<Point> vegPontok = new ArrayList<>();
        List<Zseton> pot = szalVezerlo.getPot();
        List<Zseton> jatekosZsetonjai;
        Map<Byte, PokerKez> nyertesek = szalVezerlo.getNyertesPokerKezek();
        List<Byte> nyertesJatekosSroszamok = new ArrayList<>(nyertesek.keySet());
        Map<Byte, List<Zseton>> szetvalogatottZsetonok = ZsetonKezelo.potSzetvalogat((byte) nyertesJatekosSroszamok.size(), pot);

        if (nyertesek.size() < szetvalogatottZsetonok.size()) {
            byte utolsoJatekosSorszam = nyertesJatekosSroszamok.get(nyertesJatekosSroszamok.size() - 1);
            byte potNyertesJatekosSorszam;
            boolean nyertesTalalat = false;
            Set<Byte> korbenLevoJatekosSorszamok = szalVezerlo.getJatekosokKartyalapjai().keySet();

            while (!nyertesTalalat) {
                potNyertesJatekosSorszam = ++utolsoJatekosSorszam == jatekosokSzama ? 0 : utolsoJatekosSorszam;

                for (Byte sorszam : korbenLevoJatekosSorszamok) {
                    if (sorszam == potNyertesJatekosSorszam) {
                        nyertesJatekosSroszamok.add(potNyertesJatekosSorszam);
                        nyertesTalalat = true;
                    }
                }
            }
        }

        long ido = 3;
        Zseton aktZseton;
        int lastIndex;
        List<Zseton> jatekosZsetonNyeremeny;
        szalVezerlo.setKartyaGrafikaElore(false);
        
        for (byte i = 0; i < nyertesJatekosSroszamok.size(); i++) {
            jatekosSorszam = nyertesJatekosSroszamok.get(i);
            jatekosZsetonjai = szalVezerlo.getJatekosokZsetonjai().get(jatekosSorszam);
            jatekosZsetonNyeremeny = szetvalogatottZsetonok.get(i);
            
            for (Zseton zseton : jatekosZsetonNyeremeny) {
                itr = zsetonMozgatoXmlAdatok.get("PotNyertesekhezMozgat").iterator();
                szoras = szorasMin + Math.random() * szorasMax;
                x = vegpontLista.get(jatekosSorszam).x;
                y = vegpontLista.get(jatekosSorszam).y;
                szog = SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y) + 90;
                elteres = itr.next();
                x += elteres * Math.cos(Math.toRadians(szog));
                y += elteres * Math.sin(Math.toRadians(szog));
                vegpont = SzogSzamito.vegpontSzamit(SzogSzamito.foSzogSzamit(jatekterSzelesseg, jatekterMagassag, x, y), jatekterSzelesseg, jatekterMagassag);//kiszámolja az új végontot.
                elteres = itr.next();
                x = vegpont.x + elteres * Math.cos(Math.toRadians(SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y)));
                y = vegpont.y + elteres * Math.sin(Math.toRadians(SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y)));
                szog = SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y);
                jatekosZsetonPoziciok(zseton.getErtek());
                x += elteres * Math.cos(Math.toRadians(szog)) + szoras;
                y += elteres * Math.sin(Math.toRadians(szog)) + szoras;
                vegPontok.add(new Point((int) x, (int) y));
            }
            
            AudioLejatszo.audioLejatszas(AudioLejatszo.ZSETON_CSORGES, false);
            
            while (!jatekosZsetonNyeremeny.isEmpty()) {
                for (int j = 0; j < jatekosZsetonNyeremeny.size(); j++) {
                    aktZseton = jatekosZsetonNyeremeny.get(j);
                    kx = aktZseton.getKx();
                    ky = aktZseton.getKy();
                    vx = vegPontok.get(j).getX();
                    vy = vegPontok.get(j).getY();
                    tavolsag = Math.sqrt((vy - ky) * (vy - ky) + (vx - kx) * (vx - kx));
                    szog = Math.atan2(vy - ky, vx - kx);
                    aktx = kx + lepes * Math.cos(szog);
                    akty = ky + lepes * Math.sin(szog);

                    if (lepes >= tavolsag) {
                        lastIndex = jatekosZsetonjai.lastIndexOf(aktZseton) + 1;
                        vegPontok.remove(j);
                        if (lastIndex != 0) {
                            jatekosZsetonjai.add(lastIndex, jatekosZsetonNyeremeny.remove(j));
                            pot.remove(pot.indexOf(aktZseton));
                        } else {
                            jatekosZsetonjai.add(jatekosZsetonNyeremeny.remove(j));
                            pot.remove(pot.indexOf(aktZseton));
                        }

                        j--;
                    } else {
                        aktZseton.setKx(aktx);
                        aktZseton.setKy(akty);
                    }
                }

                try {
                    sleep(ido);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ZsetonMozgato.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        szalVezerlo.jatekVezerlesFolytat();
    }
    
    /**
     * Beállítja a játékosokhoz tartozó zsetonok elhelyezkedését.
     *
     * @param zsetonErtek
     */
    private void jatekosZsetonPoziciok(byte zsetonErtek) {
        switch (zsetonErtek) {
            case 1:
                itr = zsetonMozgatoXmlAdatok.get("JatekosZsetonPoziciok").iterator();
                elteres = itr.next();
                szog += 150;
                break;
            case 5:
                itr = zsetonMozgatoXmlAdatok.get("JatekosZsetonPoziciok").iterator();
                elteres = itr.next();
                szog += 90;
                break;
            case 10:
                elteres = 0;
                szog += 90;
                break;
            case 25:
                itr = zsetonMozgatoXmlAdatok.get("JatekosZsetonPoziciok").iterator();
                elteres = itr.next();
                szog += 270;
                break;
            case 100:
                itr = zsetonMozgatoXmlAdatok.get("JatekosZsetonPoziciok").iterator();
                elteres = itr.next();
                szog += 210;
        }
    }
    
    /**
     * Beállítja a játékosokhoz tartozó potba helyezendő zsetonok
     * elhelyezkedését.
     *
     * @param zsetonErtek
     */
    private void potZsetonPoziciok(byte zsetonErtek) {
        switch (zsetonErtek) {
            case 1:
                itr = zsetonMozgatoXmlAdatok.get("PotZsetonPoziciok").iterator();
                elteres = itr.next();
                szog = 225;
                break;
            case 5:
                itr = zsetonMozgatoXmlAdatok.get("PotZsetonPoziciok").iterator();
                elteres = itr.next();
                szog = 135;
                break;
            case 10:
                elteres = 0;
                szog = 0;
                break;
            case 25:
                itr = zsetonMozgatoXmlAdatok.get("PotZsetonPoziciok").iterator();
                elteres = itr.next();
                szog = 45;
                break;
            case 100:
                itr = zsetonMozgatoXmlAdatok.get("PotZsetonPoziciok").iterator();
                elteres = itr.next();
                szog = 315;
        }
    }
    
    @Override
    public void run() {
        if (jatekosTetMozgatasa) {
            jatekosTetMozgat();
        }

        if (potNyertesekhezMozgatasa) {
            potNyertesekhezMozgat();
        }
    }

    public void setJatekosTetMozgatasa(boolean jatekosTetMozgatasa) {
        this.jatekosTetMozgatasa = jatekosTetMozgatasa;
    }

    public void setPotNyertesekhezMozgatasa(boolean potNyertesekhezMozgatasa) {
        this.potNyertesekhezMozgatasa = potNyertesekhezMozgatasa;
    }
    
    public void setJatekosTetOsszege(int jatekosTetOsszege) {
        this.jatekosTetOsszege = jatekosTetOsszege;
    }

    public void setJatekosSorszam(byte jatekosSorszam) {
        this.jatekosSorszam = jatekosSorszam;
    }
}
