package vezerloOsztalyok;

import alapOsztalyok.Kartyalap;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.ImageIcon;

public final class PakliKezelo {

    public static final int PAKLI_MERET = 52;
    private static List<Kartyalap> pakli;

    private PakliKezelo() {
    }

    /**
     * Létrehozza a kártyapaklit
     * @return 
     */
    public static List<Kartyalap> pakliBetolt() {
        
        byte j = 0, k = 0, l;
        String [][] adatok = {{"clubs", "diamonds", "hearts", "spades"},
                              {"2", "3", "4", "5", "6", "7", "8", "9", "10",
                               "Jumbo", "Dáma", "Király", "Ász"}};
        pakli = new CopyOnWriteArrayList<>();

        for (byte i = 0; i < PAKLI_MERET; i++) {

            if (i != 0 && i % 4 == 0) {
                k++;
                j = 0;
            }
            
            l = (byte) (k + 2);
            
            pakli.add(new Kartyalap(new ImageIcon(PakliKezelo.class.
                    getResource("/adatFajlok/kartyaPakli/" + String.valueOf(l) 
                            + "_of_"+ adatok[0][j] +".png")).getImage(), 
                    adatok[1][k],adatok[0][j], l));                
            j++;
        }
        
        return pakli;
    }
    
    /**
     * Megkeveri a kártyapaklit és visszadja a lapokat tartalmazó ArrayList-et
     * 
     * @return 
     */
    public static List<Kartyalap> kevertPakli() {
        pakliBetolt();

        List<Kartyalap> ujPakli = new CopyOnWriteArrayList<>();
        List<Integer> sorrend = SorrendKevero.kevertSorrend();

        for (byte i = 0; i < PAKLI_MERET; i++) {
            ujPakli.add(pakli.get(sorrend.get(i)));
        }

        return ujPakli;
    }
}
