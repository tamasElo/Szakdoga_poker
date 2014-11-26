package hu.szakdolgozat.poker.vezerloOsztalyok;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class SzogSzamito {

    private static double kx;
    private static double ky;
    private static double szog;
    private static double foSzog;
    private static int elozoSzelesseg;
    private static int elozoMagassag;
    private static Map<String, List<Double>> xmlAdatok;
    private static Iterator<Double> itr;
    
    private SzogSzamito() {
    }

    /**
     * A 360 fokot flosztja a végpontok számának megfelelő részekre és ezekhez a
     * szögekhez tartozó végpont listát ad vissza.
     *
     * @param vegpontokSzama
     * @param szelesseg
     * @param magassag
     * @return
     */
    public synchronized static List<Point> vegpontLista(int vegpontokSzama, int szelesseg, int magassag){
        List<Point> vegpontok = new ArrayList<>();
        double aktSzog = 90;
        int szogNovekmeny = 360 / vegpontokSzama;
        for (int i = 0; i < vegpontokSzama; i++) {
            vegpontok.add(vegpontSzamit(aktSzog, szelesseg, magassag));
            aktSzog += szogNovekmeny;
        }
        return vegpontok;
    }
  
    /**
     * A paraméterként megaadott szöghöz tartozó végpontot adja vissza.
     *
     * @param szog
     * @param szelesseg
     * @param magassag
     * @return
     */
    public synchronized static Point vegpontSzamit(double szog, int szelesseg, int magassag) {
        xmlAdatokBetolt(szelesseg, magassag);
        
        itr = xmlAdatok.get("KerekitettTeglalap").iterator();
        RoundRectangle2D roundRectangle = new RoundRectangle2D.Double(itr.next(), itr.next(), 
                itr.next(), itr.next(), itr.next(), itr.next());
        kx = szelesseg / 2;
        ky = magassag / 2;
        double lepes = 1;
        double aktx, akty;
        double tav = 0;
        
        do {
            tav += lepes;
            aktx = kx + tav * Math.cos(Math.toRadians(szog));
            akty = ky + tav * Math.sin(Math.toRadians(szog));
        } while (roundRectangle.contains(aktx, akty));
       
        Point vegpont = new Point((int)aktx, (int)akty);
        return vegpont;
    }
    
    /**
     * Kiszámítja a végpontokhoz tartozó szöget(Ha a végpont valamelyik köríven van, akkor a körív középpontjához viszonyítva számol).
     *
     * @param szelesseg
     * @param magassag
     * @param vx
     * @param vy
     * @return
     */
    public synchronized static double szogSzamit(int szelesseg, int magassag, double vx, double vy) {        
        xmlAdatokBetolt(szelesseg, magassag);
                
        szog = 0;
        
        foSzogSzamit(szelesseg, magassag, vx, vy);
        
        if (foSzog <= 235 && foSzog >= 135) {
            itr = xmlAdatok.get("BalKor").iterator();
            kx = itr.next();
            ky = itr.next();
            szog = Math.atan2(vy - ky, vx - kx);
            szog = Math.toDegrees(szog);
        }
        
        if (foSzog <= 45 || foSzog >= 300) {
            itr = xmlAdatok.get("JobbKor").iterator();
            kx = itr.next();
            ky = itr.next();
            szog = Math.atan2(vy - ky, vx - kx);
            szog = Math.toDegrees(szog);
        }
        
        if (foSzog < 300 && foSzog > 235) {
            kx = szelesseg / 2;
            ky = magassag / 2;
            szog = -90;
        }
        
        if (foSzog > 45 && foSzog < 135) {
            kx = szelesseg / 2;
            ky = magassag / 2;
            szog = 90;
        }

        return szog;
    }

    /**
     * A forgatandó kártyalap végpozíciójához tartozó forgatási szöget számítja
     * ki.
     *
     * @param szelesseg
     * @param magassag
     * @param vx
     * @param vy
     * @return
     */
    public synchronized static double forgasSzogSzamit(int szelesseg, int magassag, double vx, double vy) {        
        xmlAdatokBetolt(szelesseg, magassag);
        
        double kulonbseg;
        szog = 0;

        foSzogSzamit(szelesseg, magassag, vx, vy);
        
        if (foSzog <= 245 && foSzog >= 135) {
            itr = xmlAdatok.get("BalKor").iterator();
            kx = itr.next();
            ky = itr.next();
            szog = Math.atan2(vy - ky, vx - kx);
            szog = Math.toDegrees(szog);
            
            if (szog < -175) {//Ez azért kell, mert néha a bal oldali koordinátához számolt szog nem pont 180 fok lesz, hanem -180 vagy annál kisebb.
                szog += 360;
            }
            
            kulonbseg = (foSzog <= 180) ? -90 : 90;
            szog += kulonbseg;
        }
        
        if (foSzog <= 45 || foSzog >= 300) {
            itr = xmlAdatok.get("JobbKor").iterator();
            kx = itr.next();
            ky = itr.next();
            szog = Math.atan2(vy - ky, vx - kx);
            szog = Math.toDegrees(szog);
            kulonbseg = (foSzog <= 360 && foSzog> 45) ? 90 : -90;
            szog += kulonbseg;
        }
        
        return szog;
    }
    
    /**
     * Kiszámítja a végponthoz tartozó főszöget.
     *
     * @param szelesseg
     * @param magassag
     * @param vx
     * @param vy
     * @return
     */
    public synchronized static double foSzogSzamit(int szelesseg, int magassag, double vx, double vy) {
        foSzog = Math.atan2(vy - magassag / 2, vx - szelesseg / 2);
        foSzog = Math.toDegrees(foSzog);
        
        if (foSzog < 0) {
            foSzog += 360;
        }
        
        return foSzog;
    }
    
    private synchronized static void xmlAdatokBetolt(int szelesseg, int magassag) {
        if (xmlAdatok == null) {
            xmlAdatok = AdatKezelo.aranyErtekekBetolt("Alakzatok", new Dimension(szelesseg, magassag));
        } else if (szelesseg != elozoSzelesseg || magassag != elozoMagassag) {
            xmlAdatok = AdatKezelo.aranyErtekekBetolt("Alakzatok", new Dimension(szelesseg, magassag));
        }

        elozoSzelesseg = szelesseg;
        elozoMagassag = magassag;
    }
}
