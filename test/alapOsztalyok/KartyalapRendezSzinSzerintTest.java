package alapOsztalyok;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.ImageIcon;
import org.junit.Test;
import static org.junit.Assert.*;

public class KartyalapRendezSzinSzerintTest {
    
    public KartyalapRendezSzinSzerintTest() {
    }

    /**
     * A testCompare metódus tesztelése.
     */
    @Test
    public void testCompare() {
        Image tesztKep = new ImageIcon(this.getClass().getResource("/adatFajlok/kartyaPakli/14_of_diamonds.png")).getImage(); 
        List<Kartyalap> kartyalapok = new ArrayList<>();
        Kartyalap kartyalap = new Kartyalap(tesztKep, "Király", "treff", Byte.valueOf("13"));
        Kartyalap kartyalap2 = new Kartyalap(tesztKep, "Dáma", "kőr", Byte.valueOf("12"));
        kartyalapok.add(kartyalap);
        kartyalapok.add(kartyalap2);
        Collections.sort(kartyalapok, new KartyalapRendezSzinSzerint());
        assertEquals(kartyalap2, kartyalapok.get(0));
    }    
}
