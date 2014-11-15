package alapOsztalyok;

import hu.szakdolgozat.poker.alapOsztalyok.Kartyalap;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.ImageIcon;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class KartyalapTest {
     private Kartyalap kartyalap;
     private Image tesztKep;
    
    public KartyalapTest() {
    }
    
    @Before
    public void setUp() {
        tesztKep = new ImageIcon(this.getClass().getResource("/adatFajlok/kartyaPakli/14_of_diamonds.png")).getImage(); 
        kartyalap = new Kartyalap(tesztKep, "Király", "Bubi", Byte.valueOf("13"));
    }
   
    /**
     * A compareTo metódus tesztelése.
     */
    @Test
    public void testCompareTo() {
        Kartyalap kartyalap2 = new Kartyalap(tesztKep, "Dáma", "Bubi", Byte.valueOf("12"));
        List<Kartyalap> kartyalapok = new ArrayList<>();
        kartyalapok.add(kartyalap);
        kartyalapok.add(kartyalap2);        
        Collections.sort(kartyalapok);
        assertEquals(kartyalap2, kartyalapok.get(0));
    } 
    
    /**
     * A setKx és getKx metódus tesztelése.
     */
    @Test
    public void testSetKx_GetKx() {
        int kx = 10;
        kartyalap.setKx(kx);
        assertTrue(kx == kartyalap.getKx());
    }

    /**
     * A setKy és getKy metódus tesztelése.
     */
    @Test
    public void testSetKy_GetKy() {
        int ky = 15;
        kartyalap.setKy(ky);
        assertTrue(ky == kartyalap.getKy());
    }

    /**
     * A setKartyaKepSzelesseg és getKartyaKepSzelesseg metódus tesztelése.
     */
    @Test
    public void testSetKartyaKepSzelesseg_GetKartyaKepSzelesseg() {
        int kepSzelesseg = 100;
        kartyalap.setKartyaKepSzelesseg(kepSzelesseg);
        assertEquals(kepSzelesseg, kartyalap.getKartyaKepSzelesseg());
    }

    /**
     * A setKartyaKepMagassag és getKartyaKepMagassag metódus tesztelése.
     */
    @Test
    public void testSetKartyaKepMagassag_GetKartyaKepMagassag() {
        int kepMagassag = 200;
        kartyalap.setKartyaKepMagassag(kepMagassag);
        assertEquals(kepMagassag, kartyalap.getKartyaKepMagassag());
    }

    /**
     * A setMutat és isMutat metódus tesztelése.
     */
    @Test
    public void testSetMutat_IsMutat() {
        kartyalap.setMutat(true);
        assertTrue(kartyalap.isMutat());
    }

    /**
     * A setForgat és getForgat metódus tesztelése.
     */
    @Test
    public void testSetForgat_GetForgat() {
        int szog = 5;
        kartyalap.setForgat(szog);
        assertTrue(szog == kartyalap.getForgat());
    }

    /**
     * A getElolap metódus tesztelése.
     */
    @Test
    public void testGetElolap() {
        assertTrue(kartyalap.getElolap() != null);
    }

    /**
     * A getHatlap metódus tesztelése.
     */
    @Test
    public void testGetHatlap() {
        assertTrue(kartyalap.getHatlap() != null);
    }   
}
