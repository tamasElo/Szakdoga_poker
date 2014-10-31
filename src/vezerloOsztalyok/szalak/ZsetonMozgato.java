package vezerloOsztalyok.szalak;

import alapOsztalyok.PokerKez;
import vezerloOsztalyok.SzalVezerlo;
import alapOsztalyok.Zseton;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import vezerloOsztalyok.SzogSzamito;
import vezerloOsztalyok.ZsetonKezelo;

public class ZsetonMozgato extends Thread {

    private SzalVezerlo szalVezerlo;
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
    private double veletlenForgSzog;
    private double szog;    

    public ZsetonMozgato(SzalVezerlo szalVezerlo) {
        this.szalVezerlo = szalVezerlo;
        jatekterSzelesseg = szalVezerlo.jatekterPanelSzelesseg();
        jatekterMagassag = szalVezerlo.jatekterPanelMagassag();
        jatekosokSzama = szalVezerlo.jatekosokSzama();
        vegpontLista = SzogSzamito.vegpontLista(jatekosokSzama, jatekterSzelesseg, jatekterMagassag);
        lepes = jatekterMagassag * 0.0025;
    }

    /**
     * Betölti a zsetonokat és megfelelően elrendezi a játéktéren.
     */
    public void zsetonokBetolt() {
        Map<Byte, List<Zseton>> jatekosokZsetonjai = new ConcurrentHashMap<>();
        List<Zseton> zsetonok;
        int osszeg = 2500;

        for (byte i = 0; i < jatekosokSzama; i++) {
            zsetonok = ZsetonKezelo.zsetonKioszt(osszeg);
            jatekosokZsetonjai.put(i, zsetonok);

            for (Zseton zseton : zsetonok) {                    
                veletlenForgSzog = Math.random() * 360;//Létrehoz egy véletlen szöget 0 és 360 fok között. A létrehozott értéknek megfelelően fognak elfordulni a zsetonok.                
                szoras = -jatekterSzelesseg / 800 + Math.random() * jatekterSzelesseg / 400;//Létrehoz egy véletlen számot. Ennyivel fognak a zsetonok eltérni az aktuális x,y középponttól.
                x = vegpontLista.get(i).x;
                y = vegpontLista.get(i).y;
                szog = SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y) + 90;//Kiszámolja hogy az adott x,y pozícióban lévő pont hány fokos szöget zár be. Ehhez a szöghöz hozzá ad még kilencven fokot.
                elteres = jatekterMagassag / 8;
                x += elteres * Math.cos(Math.toRadians(szog));//Az x koordináta pozícióját eltolja az elteres értékkel a megadott szög irányba.
                y += elteres * Math.sin(Math.toRadians(szog));
                vegpont = SzogSzamito.vegpontSzamit(SzogSzamito.foSzogSzamit(jatekterSzelesseg, jatekterMagassag, x, y), jatekterSzelesseg, jatekterMagassag);//kiszámolja az új végontot.
                elteres = jatekterMagassag / 30;
                x = vegpont.x + elteres * Math.cos(Math.toRadians(SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y)));//Az új végpont x értékét eltolja az eltérés értékkel a megadott irányba.
                y = vegpont.y + elteres * Math.sin(Math.toRadians(SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y)));
                szog = SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y);//Kiszámolja az x,y értékekhez tartozó szöget és hozzáad 90 fokot;
                jatekosZsetonPoziciok(zseton.getErtek());
                x += elteres * Math.cos(Math.toRadians(szog)) + szoras;
                y += elteres * Math.sin(Math.toRadians(szog)) + szoras;
                zseton.setKx(x);
                zseton.setKy(y);
                zseton.setKorongKepSzelesseg(jatekterSzelesseg / 53.5);
                zseton.setKorongKepMagassag(jatekterSzelesseg / 53.5);
                zseton.setForgat(veletlenForgSzog);
            }
        }
        szalVezerlo.setJatekosokZsetonjai(jatekosokZsetonjai);//Átadja a szálvezérlőnek a jatekosokZsetonjai HashMap-et.
        szalVezerlo.frissit();
    }

    /**
     * Újratölti a megadott játékos zsetonjait és elhelyezi a megfelelő pozícióban.
     */
    private void zsetonokUjratolt(List<Zseton> zsetonok) {
        for (Zseton zseton : zsetonok) {
            veletlenForgSzog = Math.random() * 360;
            szoras = -jatekterSzelesseg / 800 + Math.random() * jatekterSzelesseg / 400;
            x = vegpontLista.get(jatekosSorszam).x;
            y = vegpontLista.get(jatekosSorszam).y;
            szog = SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y) + 90;
            elteres = jatekterMagassag / 8;
            x += elteres * Math.cos(Math.toRadians(szog));
            y += elteres * Math.sin(Math.toRadians(szog));
            vegpont = SzogSzamito.vegpontSzamit(SzogSzamito.foSzogSzamit(jatekterSzelesseg, jatekterMagassag, x, y), jatekterSzelesseg, jatekterMagassag);//kiszámolja az új végontot.
            elteres = jatekterMagassag / 30;
            x = vegpont.x + elteres * Math.cos(Math.toRadians(SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y)));
            y = vegpont.y + elteres * Math.sin(Math.toRadians(SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y)));
            szog = SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y);            
            jatekosZsetonPoziciok(zseton.getErtek());
            x += elteres * Math.cos(Math.toRadians(szog)) + szoras;
            y += elteres * Math.sin(Math.toRadians(szog)) + szoras;
            zseton.setKx(x);
            zseton.setKy(y);
            zseton.setKorongKepSzelesseg(jatekterSzelesseg / 53.5);
            zseton.setKorongKepMagassag(jatekterSzelesseg / 53.5);
            zseton.setForgat(veletlenForgSzog);
        }
        
        szalVezerlo.frissit();
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
            szoras = -jatekterSzelesseg / 800 + Math.random() * jatekterSzelesseg / 400;
            x = 950;
            y = 750;      
            potZsetonPoziciok(zseton.getErtek());
            x += elteres * Math.cos(Math.toRadians(szog)) + szoras;
            y += elteres * Math.sin(Math.toRadians(szog)) + szoras;
            vegPontok.add(new Point((int)x, (int)y));
        }
         
        double zsetonokVegpontban = 0;
        long ido = 3;
        Zseton zseton;
        
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
            
            szalVezerlo.frissit();
            
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
        jatekosSorszam = 1;
        List<Point> vegPontok = new ArrayList<>();               
        List<Zseton> pot = szalVezerlo.getPot();
        List<Zseton> jatekosZsetonjai = szalVezerlo.getJatekosokZsetonjai().get(jatekosSorszam);
        List<Byte> jatekosSorszamok = new ArrayList<>();
        Map<Byte, PokerKez> nyertesek = szalVezerlo.getNyertesPokerKezek();
        
        for (Map.Entry<Byte, PokerKez> entrySet : nyertesek.entrySet()) {
            Byte key = entrySet.getKey();
            jatekosSorszamok.add(key);            
        }
                
        for (Zseton zseton : pot) { 
            szoras = -jatekterSzelesseg / 800 + Math.random() * jatekterSzelesseg / 400;
            x = vegpontLista.get(jatekosSorszam).x;
            y = vegpontLista.get(jatekosSorszam).y;
            szog = SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y) + 90;
            elteres = jatekterMagassag / 8;
            x += elteres * Math.cos(Math.toRadians(szog));
            y += elteres * Math.sin(Math.toRadians(szog));
            vegpont = SzogSzamito.vegpontSzamit(SzogSzamito.foSzogSzamit(jatekterSzelesseg, jatekterMagassag, x, y), jatekterSzelesseg, jatekterMagassag);//kiszámolja az új végontot.
            elteres = jatekterMagassag / 30;
            x = vegpont.x + elteres * Math.cos(Math.toRadians(SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y)));
            y = vegpont.y + elteres * Math.sin(Math.toRadians(SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y)));
            szog = SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y);            
            jatekosZsetonPoziciok(zseton.getErtek());            
            x += elteres * Math.cos(Math.toRadians(szog)) + szoras;
            y += elteres * Math.sin(Math.toRadians(szog)) + szoras;
            vegPontok.add(new Point((int)x, (int)y));
        }
        
        long ido = 5;
        Zseton zseton;
        int lastIndex;
        
        while(!pot.isEmpty()){
            for (int i = 0; i < pot.size(); i++) {
                zseton = pot.get(i);
                kx = zseton.getKx();
                ky = zseton.getKy();
                vx = vegPontok.get(i).getX();
                vy = vegPontok.get(i).getY();
                tavolsag = Math.sqrt((vy - ky) * (vy - ky) + (vx - kx) * (vx - kx));
                szog = Math.atan2(vy - ky, vx - kx);
                aktx = kx + lepes * Math.cos(szog);
                akty = ky + lepes * Math.sin(szog);
                
                if (lepes >= tavolsag) {
                    
                    
                        lastIndex = jatekosZsetonjai.lastIndexOf(zseton);
                        vegPontok.remove(i);
                        
                        if (lastIndex != -1) {
                            jatekosZsetonjai.add(lastIndex, pot.remove(i));
                        } else {
                            jatekosZsetonjai.add(pot.remove(i));
                        }
                        
                        i--;
                    
                } else {
                    zseton.setKx(aktx);
                    zseton.setKy(akty);
                }
            }
            
            szalVezerlo.frissit();
            
            try {
                sleep(ido);
            } catch (InterruptedException ex) {
                Logger.getLogger(ZsetonMozgato.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }    
    
    /**
     * Beállítja a játékosokhoz tartozó zsetonok elhelyezkedését.
     *
     * @param zsetonErtek
     */
    private void jatekosZsetonPoziciok(byte zsetonErtek) {
        switch (zsetonErtek) {
            case 1:
                elteres = jatekterMagassag / 30;
                szog += 150;
                break;
            case 5:
                elteres = jatekterMagassag / 30;
                szog += 90;
                break;
            case 10:
                elteres = 0;
                szog += 90;
                break;
            case 25:
                elteres = jatekterMagassag / 30;
                szog += 270;
                break;
            case 100:
                elteres = jatekterMagassag / 30;
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
                elteres = 40;
                szog = 225;
                break;
            case 5:
                elteres = 40;
                szog = 135;
                break;
            case 10:
                elteres = 0;
                szog = 0;
                break;
            case 25:
                elteres = 40;
                szog = 45;
                break;
            case 100:
                elteres = 40;
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
