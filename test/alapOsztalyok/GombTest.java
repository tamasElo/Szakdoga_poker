package alapOsztalyok;

import java.awt.Image;
import javax.swing.ImageIcon;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GombTest {
    private Gomb gomb;
    
    public GombTest() {
    }
    
    @Before
    public void setUp() {
        Image tesztKep = new ImageIcon(this.getClass().getResource("/adatFajlok/iranyitas/call_2.png")).getImage();
        gomb = new Gomb("test_gomb", tesztKep, tesztKep, tesztKep, 50, 100, 320, 240);
    }

    /**
     * A setX és getX metódus tesztelése.
     */
    @Test
    public void testSetX_GetX() {
        int x = 5;
        gomb.setX(x);
        assertEquals(x, gomb.getX());
    }

    /**
     * A setY és getY metódus tesztelése.
     */
    @Test
    public void testSetY_Gety() {
        int y = 10;
        gomb.setY(y);
        assertEquals(y, gomb.getY());
    }

    /**
     * A setSzelesseg és getSzelesseg metódus tesztelése.
     */
    @Test
    public void testSetSzelesseg_GetSzelesseg() {
        int szelesseg = 100;
        gomb.setSzelesseg(szelesseg);
        assertEquals(szelesseg, gomb.getSzelesseg());
    }

    /**
     * A setMagassag és getMagassag metódus tesztelése.
     */
    @Test
    public void testSetMagassag_GetMagassag() {
        int magassag = 200;
        gomb.setMagassag(magassag);
        assertEquals(magassag, gomb.getMagassag());
    }

    /**
     * A setMegjSorszam és getMegjSorszam metódus tesztelése.
     */
    @Test
    public void testSetMegjSorszam_GetMegjSorszam() {
        int megjSorszam = 3;
        gomb.setMegjSorszam(megjSorszam);
        assertEquals(megjSorszam, gomb.getMegjSorszam());
    }

    /**
     * A getNev tesztelése.
     */
    @Test
    public void testGetNev() {
        String gombNev = "test_gomb";
        assertEquals(gombNev, gomb.getNev());
    }    
}
