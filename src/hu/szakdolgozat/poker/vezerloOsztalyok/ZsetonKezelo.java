package hu.szakdolgozat.poker.vezerloOsztalyok;

import hu.szakdolgozat.poker.alapOsztalyok.Zseton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.ImageIcon;

public final class ZsetonKezelo {

    private static byte[] aranySzamok;
    private static List<Zseton> zsetonLista;
    private static final byte[] zsetonErtekek = {100, 25, 10, 5, 1};
    
    private ZsetonKezelo(){};
    
    /**
     * Egy aránynak megfelelően felbontja az összeget megfelelő értékű zsetonokra.
     * 
     * @param osszeg
     * @return 
     */
     public static List<Zseton> zsetonKioszt(int osszeg) {
         zsetonLista = new CopyOnWriteArrayList<>();        
         int maradek;
         int hanyados;
         int arany;
         
        if (aranySzamok == null) {
            aranySzamok = new byte[]{3, 3, 3, 3, 8};
        }

        /*Végig megy az aranySzamok tömbön és egy feltételt vizsgál a ciklustörzsben. 
          Ha i nem egyenlő az aranySzamok tömb méretével, akkor az arany változót 
          egyenlővé teszi a megadott összeg és az i-edik arányszám hányadosával,
          majd kiszámolja a hányadost és a maradékot az arányt elosztva az i-ik 
          zseton értékkel és az osszeg változóból levonja az arany változó értékét, 
          majd hozzá adja a maradek értékét. Így minden ciklusban csökken az összeg értéke.
          A feltétel else ágában a megmaradt összeget bontja fel zseton értékekre.*/
        for (byte i = 0; i <= aranySzamok.length; i++) {
            if (i != aranySzamok.length) {
                arany = osszeg / aranySzamok[i];
                hanyados = arany / zsetonErtekek[i];
                maradek = arany % zsetonErtekek[i];
                osszeg -= arany;
                osszeg += maradek;
                
                /*Hozzá adja a zsetonlistához az akutális zseton értékre felbontott zsetonokat.*/
                for (int j = 0; j < hanyados; j++) {
                    zsetonLista.add(new Zseton(zsetonErtekek[i], new ImageIcon(ZsetonKezelo.class.
                            getResource("/hu/szakdolgozat/poker/adatFajlok/zsetonok/zseton"
                                    + zsetonErtekek[i] + ".png")).getImage(), 
                            new ImageIcon(ZsetonKezelo.class.
                            getResource("/hu/szakdolgozat/poker/adatFajlok/zsetonok/zseton"
                                    + zsetonErtekek[i] + "_blur.png")).getImage()));
                }
            } else {
                for (byte j = 0; j < zsetonErtekek.length; j++) {
                    hanyados = osszeg / zsetonErtekek[j];
                    osszeg %= zsetonErtekek[j];

                    /*Hozzá adja a zsetonlistához az akutális zseton értékre felbontott zsetonokat.*/
                    for (int k = 0; k < hanyados; k++) {
                        zsetonLista.add(new Zseton(zsetonErtekek[j], new ImageIcon(ZsetonKezelo.class.
                                getResource("/hu/szakdolgozat/poker/adatFajlok/zsetonok/zseton"
                                        + zsetonErtekek[j] + ".png")).getImage(),
                                new ImageIcon(ZsetonKezelo.class.
                                getResource("/hu/szakdolgozat/poker/adatFajlok/zsetonok/zseton"
                                        + zsetonErtekek[j] + "_blur.png")).getImage()));
                    }
                }
            }
        }
        
        Collections.sort(zsetonLista);
        return zsetonLista;
    }
    
    /**
     * Kiválogatja a játékos zsetonjai közül az összegnek megfelelő zsetonokat
     * és hozzáadja a pothoz.
     * 
     * @param jatekosZsetonok
     * @param osszeg 
     * @return  
     */
    public synchronized static List<Zseton> pot(List<Zseton> jatekosZsetonok, int osszeg){
        int osszeg2;
        List<Zseton> potZsetonok = new CopyOnWriteArrayList<>();  
        aranySzamok = new byte[] {1, 1, 1, 1, 1};//Ez azért kell mert ennek az új arányosságnak megfelelően bontja fel a zsetonkioszt metódus az összeget.
        
        zsetonKioszt(osszeg);

        
        while (!zsetonLista.isEmpty()) {
            for (int i = 0; i < zsetonLista.size(); i++) {
                for (int j = jatekosZsetonok.size() - 1; j >= 0; j--) {
                    if (zsetonLista.get(i).getErtek() == jatekosZsetonok.get(j).getErtek()) {
                        potZsetonok.add(jatekosZsetonok.remove(j));
                        zsetonLista.remove(i);
                        i--;
                        break;
                    }
                }
            }
            
            /*Ha a zsetonLista nem üres akkor a megadott arányszámoknak megfelelően
              felbontja újabb értékekre az összegeket.*/
            if (!zsetonLista.isEmpty()) {
                aranySzamok = new byte[]{3, 3, 3, 3, 8};
                osszeg = 0;
                for (Zseton zseton : jatekosZsetonok) {
                    osszeg += zseton.getErtek();
                }

                osszeg2 = 0;
                for (Zseton zseton : zsetonLista) {
                    osszeg2 += zseton.getErtek();
                }

                jatekosZsetonok.clear();
                jatekosZsetonok.addAll(zsetonKioszt(osszeg));//A clear() és az addAll() metódus segítségével meg marad a hivatkozás a játékos objektumban lévő zsetonlistára.
                zsetonKioszt(osszeg2);
            }
        }
        return potZsetonok;
    }
    
    /**
     * Szétosztja a potban lévő zsetonokat a nyertes játékosok között.
     * 
     * @param nyertesekSzama
     * @param pot
     * @return 
     */
    public static Map<Byte, List<Zseton>> potSzetvalogat(byte nyertesekSzama, List<Zseton> pot){ 
        Map<Byte, List<Zseton>> szetvalogatottZsetonok = new HashMap<>();
        List<Zseton> egyformaZsetonok = new CopyOnWriteArrayList<>();   
        List<Zseton> maradekZsetonok = new CopyOnWriteArrayList<>();
        List<Zseton> potMasolat = new ArrayList<>(pot);
        Zseton keresendoZseton;
        Zseton zseton;     
        byte zsetonErtek;     
        int osszegek[] = new int[nyertesekSzama];    
        
        for (int i = 0; i < nyertesekSzama; i++) {
            osszegek[i] = zsetonokOsszege(potMasolat) / nyertesekSzama; //felosztja a pot összegét arányos értékekre.
        }        

        while (!potMasolat.isEmpty()) {            
            keresendoZseton = potMasolat.get(potMasolat.size()-1); //A zsetonlista utolsó elemét beállítja keresendő zsetonnak.
            zsetonErtek = keresendoZseton.getErtek();
            
            for (Zseton potMasolat1 : potMasolat) {
                zseton = potMasolat1;
                if (zseton.equals(keresendoZseton)) {
                    egyformaZsetonok.add(zseton);
                }
            }
            
            potMasolat.removeAll(egyformaZsetonok); //Kitörli a zsetonlistából az aktuálisan megkeresett értékű egyforma zsetonokat.

            for (int i = 0; i < egyformaZsetonok.size(); i++) {
                for (byte j = 0; j < nyertesekSzama; j++) {                    
                    if (osszegek[j] / zsetonErtek != 0 && !egyformaZsetonok.isEmpty()) {
                        if (szetvalogatottZsetonok.containsKey(j)) {
                            szetvalogatottZsetonok.get(j).add(egyformaZsetonok.remove(0));
                        } else {
                            List<Zseton> jatekosZsetonok = new CopyOnWriteArrayList<>();
                            jatekosZsetonok.add(egyformaZsetonok.remove(0));
                            szetvalogatottZsetonok.put(j, jatekosZsetonok);
                        }
                        
                        osszegek[j] -= zsetonErtek;
                        i = -1;
                    }
                }          
            }
            
            if (!egyformaZsetonok.isEmpty()) {
                maradekZsetonok.addAll(egyformaZsetonok);
                egyformaZsetonok.clear();
            }
        }
        
        if (!maradekZsetonok.isEmpty()) {
            szetvalogatottZsetonok.put(nyertesekSzama, maradekZsetonok);
        }
        
        return szetvalogatottZsetonok;
    }
    
    /**
     * Visszaadja a zsetonok összegét.
     * 
     * @param zsetonok
     * @return 
     */
    public synchronized static int zsetonokOsszege(List<Zseton> zsetonok){
        int osszeg = 0;
        for (Zseton zseton : zsetonok) {
            osszeg += zseton.getErtek();
        }
        return osszeg;
    }
}
