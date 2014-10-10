package vezerloOsztalyok.szalak;

import vezerloOsztalyok.SzalVezerlo;
import alapOsztalyok.Zseton;
import java.awt.Point;
import java.util.ArrayList;
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
    private byte jatekosSorszam;
    private List<Point> vegpontLista;
    private int jatekosTetOsszege;

    public ZsetonMozgato(SzalVezerlo szalVezerlo) {
        this.szalVezerlo = szalVezerlo;
        jatekterSzelesseg = szalVezerlo.jatekterPanelSzelesseg();
        jatekterMagassag = szalVezerlo.jatekterPanelMagassag();
        jatekosokSzama = szalVezerlo.jatekosokSzama();
        vegpontLista = SzogSzamito.vegpontLista(jatekosokSzama, jatekterSzelesseg, jatekterMagassag);
    }

    /**
     * Betölti a zsetonokat és megfelelően elrendezi a játéktéren.
     */
    public void zsetonokBetolt() {
        Map<Byte, List<Zseton>> jatekosokZsetonjai = new ConcurrentHashMap<>();
        List<Zseton> zsetonok;
        Point vegpont;
        double szog, elteres;
        int osszeg = 2500;
        double x, y, szoras;
        double veletlenForgSzog;

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
                
                /*Beállítja a játékosokhoz tartozó zsetonok elhelyezkedését.*/
                switch (zseton.getErtek()) {
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
        Point vegpont;
        double szog, elteres;
        double x, y, szoras;
        double veletlenForgSzog;
        
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

            switch (zseton.getErtek()) {
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
        Point vegpont;
        double szog, elteres;
        double x, y, szoras;        
        List<Point> kezdoPontok = new ArrayList<>();
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
            x = vegpontLista.get(jatekosSorszam).x;
            y = vegpontLista.get(jatekosSorszam).y;
            szog = SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y) + 90;
            elteres = jatekterMagassag / 8;
            x += elteres * Math.cos(Math.toRadians(szog));
            y += elteres * Math.sin(Math.toRadians(szog));
            vegpont = SzogSzamito.vegpontSzamit(SzogSzamito.foSzogSzamit(jatekterSzelesseg, jatekterMagassag, x, y), jatekterSzelesseg, jatekterMagassag);//kiszámolja az új végontot.
            elteres = -jatekterSzelesseg * 0.08125;
            x = vegpont.x + elteres * Math.cos(Math.toRadians(SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y)));
            y = vegpont.y + elteres * Math.sin(Math.toRadians(SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y)));
            szog = SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y);

            switch (zseton.getErtek()) {
                case 1:
                    elteres = jatekterSzelesseg * 0.05625;
                    szog += 90;
                    break;
                case 5:
                    elteres = jatekterMagassag * 0.0875;
                    szog += 57;
                    break;
                case 10:
                    elteres = jatekterSzelesseg * 0.040625;
                    szog += 64;
                    break;
                case 25:
                    elteres = jatekterSzelesseg * 0.040625;
                    szog += 26;
                    break;
                case 100:
                    elteres = jatekterMagassag / 40;
                    szog += 90;
            }

            x += elteres * Math.cos(Math.toRadians(szog)) + szoras;
            y += elteres * Math.sin(Math.toRadians(szog)) + szoras;
            
            kezdoPontok.add(new Point((int)zseton.getKx(), (int)zseton.getKy()));
            vegPontok.add(new Point((int)x, (int)y));
        }
        
        double vx, vy, kx, ky, aktx, akty, tavolsag; 
        double aktTav = 0, zsetonokVegpontban = 0;
        double lepes = jatekterMagassag*0.0025;
        long ido = 5;
        
        while(zsetonokVegpontban != jatekosTetje.size()){
            for (int i = 0; i < jatekosTetje.size(); i++) {
                kx = kezdoPontok.get(i).getX();
                ky = kezdoPontok.get(i).getY();
                vx = vegPontok.get(i).getX();
                vy = vegPontok.get(i).getY();
                tavolsag = Math.sqrt((vy - ky) * (vy - ky) + (vx - kx) * (vx - kx));
                szog = Math.atan2(vy - ky, vx - kx);
                aktx = kx + aktTav * Math.cos(szog);
                akty = ky + aktTav * Math.sin(szog);
                if (aktTav >= tavolsag) {
                    zsetonokVegpontban++;
                } else {
                    zsetonokVegpontban = 0;
                    jatekosTetje.get(i).setKx(aktx);
                    jatekosTetje.get(i).setKy(akty);
                }
            }
            aktTav += lepes;
            try {
                sleep(ido);
            } catch (InterruptedException ex) {
                Logger.getLogger(ZsetonMozgato.class.getName()).log(Level.SEVERE, null, ex);
            }
            szalVezerlo.frissit();
        }
    }
    
    @Override
    public void run() {
        if(jatekosTetMozgatasa)jatekosTetMozgat();
    }

    public void setJatekosTetMozgatasa(boolean jatekosTetMozgatasa) {
        this.jatekosTetMozgatasa = jatekosTetMozgatasa;
    }
    
    public void setJatekosTetOsszege(int jatekosTetOsszege) {
        this.jatekosTetOsszege = jatekosTetOsszege;
    }

    public void setJatekosSorszam(byte jatekosSorszam) {
        this.jatekosSorszam = jatekosSorszam;
    }
}
