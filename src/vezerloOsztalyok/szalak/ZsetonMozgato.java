package vezerloOsztalyok.szalak;

import alapOsztalyok.Zseton;
import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import vezerloOsztalyok.SzogSzamito;
import vezerloOsztalyok.ZsetonKezelo;

public class ZsetonMozgato extends Thread {

    private SzalVezerlo szalVezerlo;
    private byte jatekosokSzama;
    private boolean zsetonokBetolt;
    private int jatekterSzelesseg;
    private int jatekterMagassag;

    ZsetonMozgato(SzalVezerlo szalVezerlo) {
        this.szalVezerlo = szalVezerlo;
        jatekterSzelesseg = szalVezerlo.jatekterPanelSzelesseg();
        jatekterMagassag = szalVezerlo.jatekterPanelMagassag();
        jatekosokSzama = szalVezerlo.jatekosokSzama();
    }

    /**
     * Betölti a zsetonokat és megfelelően elrendezi a játéktéren.
     */
    private void zsetonokBetolt() {
        Map<Byte, List<Zseton>> jatekosokZsetonjai = new HashMap<>();
        List<Zseton> zsetonok;
        List<Point> vegpontLista = SzogSzamito.vegpontLista(jatekosokSzama, jatekterSzelesseg, jatekterMagassag);
        Point vegpont;
        double szog, elteres;
        int osszeg = 2500;
        int x, y;
        int szoras;
        double veletlenForgSzog;

        for (byte i = 0; i < jatekosokSzama; i++) {
            zsetonok = ZsetonKezelo.zsetonKioszt(osszeg);
            jatekosokZsetonjai.put(i, zsetonok);

            for (Zseton zseton : zsetonok) {                    
                veletlenForgSzog = Math.random()*360;//Létrehoz egy véletlen szöget 0 és 360 fok között. A létrehozott értéknek megfelelően fognak elfordulni a zsetonok.                
                szoras = (int)(-3 + Math.random()*6);//Létrehoz egy véletlen számot -2 és +4 között. Ennyivel fognak a zsetonok eltérni az aktuális x középponttól.
                x = vegpontLista.get(i).x;
                y = vegpontLista.get(i).y;
                szog = SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y)+90;//Kiszámolja hogy az adott x,y pozícióban lévő pont hány fokos szöget zár be. Ehhez a szöghöz hozzá ad még kilencven fokot.
                elteres = 150;
                x += elteres * Math.cos(Math.toRadians(szog));//Az x koordináta pozícióját eltolja az elteres értékkel a megadott szög irányba.
                y += elteres * Math.sin(Math.toRadians(szog));
                vegpont = SzogSzamito.vegpontSzamit(SzogSzamito.foSzogSzamit(jatekterSzelesseg, jatekterMagassag, x, y), jatekterSzelesseg, jatekterMagassag);//kiszámolja az új végontot.
                elteres = 40;
                x = (int) (vegpont.x + elteres * Math.cos(Math.toRadians(SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y))));//Az új végpont x értékét eltolja az eltérés értékkel a megadott irányba.
                y = (int) (vegpont.y + elteres * Math.sin(Math.toRadians(SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y))));
                szog = SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y)+90;//Kiszámolja az x,y értékekhez tartozó szöget és hozzáad 90 fokot;
                
                /*Beállítja a játékosokhoz tartozó zsetonok elhelyezkedését.*/
                switch (zseton.getErtek()) {
                    case 1:
                        elteres = 40;
                        szog += 60;
                        break;
                    case 5:
                        elteres = 40;
                        break;
                    case 10:
                        elteres = 0;
                        break;
                    case 25:
                        elteres = 40;
                        szog += 180;
                        break;
                    case 100:
                        elteres = 40;
                        szog += 110;
                }                
                
                x += elteres * Math.cos(Math.toRadians(szog)) + szoras;
                y += elteres * Math.sin(Math.toRadians(szog)) + szoras;
                zseton.setKx(x);
                zseton.setKy(y);
                zseton.setKorongKepSzelesseg(30);
                zseton.setKorongKepMagassag(30);
                zseton.setForgat(veletlenForgSzog);
            }
        }
        szalVezerlo.setJatekosokZsetonjai(jatekosokZsetonjai);//Átadja a szálvezérlőnek a jatekosokZsetonjai HashMap-et.
        szalVezerlo.frissit();
    }

    @Override
    public void run() {
        if (zsetonokBetolt) {
            zsetonokBetolt();
        }
    }

    public void setZsetonokBetolt(boolean zsetonokBetolt) {
        this.zsetonokBetolt = zsetonokBetolt;
    }
}
