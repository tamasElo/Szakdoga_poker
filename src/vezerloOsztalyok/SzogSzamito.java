package vezerloOsztalyok;

import java.awt.Point;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

public class SzogSzamito {
    private static double kx;
    private static double ky;
    private static double szog;
    private static double foSzog;

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
    public static List<Point> vegpontLista(int vegpontokSzama, int szelesseg, int magassag){
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
    public static Point vegpontSzamit(double szog, int szelesseg, int magassag) {
        RoundRectangle2D roundRectangle = new RoundRectangle2D.Double(szelesseg / 5.517, magassag / 4, szelesseg/1.569, magassag/2, magassag/2, magassag/2);
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
    public static double szogSzamit(int szelesseg, int magassag, double vx, double vy) {
        szog = 0;
        
        foSzogSzamit(szelesseg, magassag, vx, vy);
        if (foSzog <= 235 && foSzog >= 135) {
            kx = szelesseg / 2.726;
            ky = magassag / 2;
            szog = Math.atan2(vy - ky, vx - kx);
            szog = Math.toDegrees(szog);
        }
        if (foSzog <= 45 || foSzog >= 300) {
            kx = szelesseg / 1.579;
            ky = magassag / 2;
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
    public static double forgasSzogSzamit(int szelesseg, int magassag, double vx, double vy) {
        double kulonbseg;
        szog = 0;

        foSzogSzamit(szelesseg, magassag, vx, vy);

        if (foSzog <= 245 && foSzog >= 135) {
            kx = szelesseg / 2.726;
            ky = magassag / 2;
            szog = Math.atan2(vy - ky, vx - kx);
            szog = Math.toDegrees(szog);
            kulonbseg = (foSzog <= 180) ? -90 : 90;
            szog += kulonbseg;
        }
        if (foSzog <= 45 || foSzog >= 300) {
            kx = szelesseg / 1.579;
            ky = magassag / 2;
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
    public static double foSzogSzamit(int szelesseg, int magassag, double vx, double vy) {
        foSzog = Math.atan2(vy - magassag / 2, vx - szelesseg / 2);
        foSzog = Math.toDegrees(foSzog);
        if (foSzog < 0) {
            foSzog += 360;
        }
        return foSzog;
    }
}
