package vezerloOsztalyok;

import alapOsztalyok.Zseton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.ImageIcon;

public final class ZsetonKezelo {
    private static byte[] aranySzamok;
    private static List<Zseton> zsetonLista;
    
    private ZsetonKezelo(){};
    
    /**
     * Egy aránynak megfelelően felbontja az összeget megfelelő értékű zsetonokra.
     * @param osszeg
     * @return 
     */
     public static List<Zseton> zsetonKioszt(int osszeg) {
         zsetonLista = new ArrayList<>();         
         byte[] zsetonErtekek = {100, 25, 10, 5, 1};
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
                            getResource("/adatFajlok/zsetonok/zseton"
                                    + zsetonErtekek[i] + ".png")).getImage()));
                }
            } else {
                for (byte j = 0; j < zsetonErtekek.length; j++) {
                    hanyados = osszeg / zsetonErtekek[j];
                    osszeg %= zsetonErtekek[j];

                    /*Hozzá adja a zsetonlistához az akutális zseton értékre felbontott zsetonokat.*/
                    for (int k = 0; k < hanyados; k++) {
                        zsetonLista.add(new Zseton(zsetonErtekek[j], new ImageIcon(ZsetonKezelo.class.
                                getResource("/adatFajlok/zsetonok/zseton"
                                        + zsetonErtekek[j] + ".png")).getImage()));
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
     * @param jatekosZsetonok
     * @param osszeg 
     * @return  
     */
    public static List<Zseton> pot(List<Zseton> jatekosZsetonok, int osszeg){
        int osszeg2;
        List<Zseton> potZsetonok = new ArrayList<>();
        /*Ez azért kell mert ennek az új arányosságnak megfelelően 
        bontja fel a zsetonkioszt metódus az összeget.*/
        aranySzamok = new byte[] {1, 1, 1, 1, 1};
        
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
}