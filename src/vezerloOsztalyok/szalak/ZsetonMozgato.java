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
                veletlenForgSzog = Math.random()*360;
                szoras = (int)(-3 + Math.random()*6);
                x = vegpontLista.get(i).x;
                y = vegpontLista.get(i).y;
                szog = SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y)+90;
                elteres = 150;
                x += elteres * Math.cos(Math.toRadians(szog));
                y += elteres * Math.sin(Math.toRadians(szog));
                vegpont = SzogSzamito.vegpontSzamit(SzogSzamito.foSzogSzamit(jatekterSzelesseg, jatekterMagassag, x, y), jatekterSzelesseg, jatekterMagassag);
                x = (int) (vegpont.x + 50 * Math.cos(Math.toRadians(SzogSzamito.foSzogSzamit(jatekterSzelesseg, jatekterMagassag, x, y))));
                y = (int) (vegpont.y + 50 * Math.sin(Math.toRadians(SzogSzamito.foSzogSzamit(jatekterSzelesseg, jatekterMagassag, x, y))));
                szog = SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, vegpont.x, vegpont.y)+90;
                
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
                zseton.setZsetonKepSzelesseg(30);
                zseton.setZsetonKepMagassag(30);
                zseton.forgat(veletlenForgSzog);
            }
        }
        szalVezerlo.setJatekosokZsetonjai(jatekosokZsetonjai);
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
