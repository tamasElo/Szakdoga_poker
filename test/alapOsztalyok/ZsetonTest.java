package alapOsztalyok;

import hu.szakdolgozat.poker.alapOsztalyok.Zseton;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.ImageIcon;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class ZsetonTest {
    private Zseton zseton;
    
    public ZsetonTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        Image tesztKep = new ImageIcon(this.getClass().getResource("/adatFajlok/zsetonok/zseton25.png")).getImage(); 
        zseton = new Zseton(Byte.parseByte("25"), tesztKep);
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * A compareTo metódus tesztelése.
     */
    @Test
    public void testCompareTo() {
        Image tesztKep = new ImageIcon(this.getClass().getResource("/adatFajlok/zsetonok/zseton10.png")).getImage(); 
        List<Zseton> zsetonok = new ArrayList();  
        Zseton zseton2 = new Zseton(Byte.parseByte("10"), tesztKep);
        zsetonok.add(zseton);
        zsetonok.add(zseton2);
        Collections.sort(zsetonok);
        assertEquals(zseton2, zsetonok.get(0));
    }

    /**
     * A setKx és getKx metódus teszelése.
     */
    @Test
    public void testSetKx_GetKx() {
        int kx = 5;
        zseton.setKx(kx);
        assertEquals(kx, zseton.getKx());
    }

    /**
     * A setKy és gegKy metódus teszelése.
     */
    @Test
    public void testSetKy_getKy() {
        int ky = 5;
        zseton.setKy(ky);
        assertEquals(ky, zseton.getKy());
    }

    /**
     * A setZsetonKepSzelesseg és getZsetonKepSzelesseg metódus tesztelése.
     */
    @Test
    public void testSetZsetonKepSzelesseg_GetZsetonKepSzelesseg() {
        int kepSzelesseg = 20;
//        zseton.setZsetonKepSzelesseg(kepSzelesseg);
//        assertEquals(kepSzelesseg, zseton.getZsetonKepSzelesseg());
    }

    /**
     * A setZsetonKepMagassag és getZsetonKepMagassag metódus tesztelése.
     */
    @Test
    public void testSetZsetonKepMagassag_GetZsetonKepMagassag() {
        int kepMagassag = 20;
//        zseton.setZsetonKepMagassag(kepMagassag);
//        assertEquals(kepMagassag, zseton.getZsetonKepMagassag());
    }

    /**
     * A setForgat és getForgat metódus tesztelése.
     */
    @Test
    public void testSetForgat_GetForgat() {
        int forgat = 5;
        zseton.setForgat(forgat);
        assertTrue(forgat == zseton.getForgat());
    }

    /**
     * A getZsetonKep tesztelése.
     */
    @Test
    public void testGetZsetonKep() {
        assertTrue(zseton.getZsetonKep() != null);
    }

    /**
     * A getErtek metódus tesztelése.
     */
    @Test
    public void testGetErtek() {
        assertEquals(25, zseton.getErtek());
    }

    /**
     * Test of equals method, of class Zseton.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object o = null;
        Zseton instance = null;
        boolean expResult = false;
        boolean result = instance.equals(o);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hashCode method, of class Zseton.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        Zseton instance = null;
        int expResult = 0;
        int result = instance.hashCode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
