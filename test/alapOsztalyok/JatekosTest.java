package alapOsztalyok;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class JatekosTest {
    private Jatekos jatekos;
    
    public JatekosTest() {
    }
    
    @Before
    public void setUp() {
        jatekos = new Jatekos("Teszt_Jani");
    }

    /**
     * A setX és getX metódus tesztelése.
     */
    @Test
    public void testSetX_GetX() {
        int x = 5;
        jatekos.setX(x);
        assertTrue(x == jatekos.getX());
    }

    /**
     * A setY és getY metódus tesztelése.
     */
    @Test
    public void testSetY_Gety() {
        int y = 10;
        jatekos.setY(y);
        assertTrue(y == jatekos.getY());
    }

    /**
     * A setPokerKezNev és getPokerKezNev metódus tesztelése.
     */
    @Test
    public void testSetPokerKezNev_GetPokerKezNev() {
        String pokerKezNev = "Ász";
        jatekos.setPokerKezNev(pokerKezNev);
        assertEquals(pokerKezNev, jatekos.getPokerKezNev());
    }


    /**
     * A getNev metódus tesztelése.
     */
    @Test
    public void testGetNev() {
        String nev = "Teszt_Jani";
        assertEquals(nev, jatekos.getNev());
    }
  
    /**
     * A getSorszam metódus tesztelése.
     */
    @Test
    public void testGetSorszam() {
        int sorszam = 0;
        assertEquals(sorszam, jatekos.getSorszam());
    }   

    /**
     * Test of rajzol method, of class Jatekos.
     */
    @Test
    public void testRajzol() {
    }

    /**
     * Test of setX method, of class Jatekos.
     */
    @Test
    public void testSetX() {
    }

    /**
     * Test of setY method, of class Jatekos.
     */
    @Test
    public void testSetY() {
    }

    /**
     * Test of setFont method, of class Jatekos.
     */
    @Test
    public void testSetFont() {
    }

    /**
     * Test of setPokerKezNev method, of class Jatekos.
     */
    @Test
    public void testSetPokerKezNev() {
    }

    /**
     * Test of setPokerKezLapok method, of class Jatekos.
     */
    @Test
    public void testSetPokerKezLapok() {
    }

    /**
     * Test of setJatekosZsetonok method, of class Jatekos.
     */
    @Test
    public void testSetJatekosZsetonok() {
    }

    /**
     * Test of getX method, of class Jatekos.
     */
    @Test
    public void testGetX() {
    }

    /**
     * Test of getY method, of class Jatekos.
     */
    @Test
    public void testGetY() {
    }

    /**
     * Test of getJatekosZsetonok method, of class Jatekos.
     */
    @Test
    public void testGetJatekosZsetonok() {
    }

    /**
     * Test of getPokerKezLapok method, of class Jatekos.
     */
    @Test
    public void testGetPokerKezLapok() {
    }

    /**
     * Test of getPokerKezNev method, of class Jatekos.
     */
    @Test
    public void testGetPokerKezNev() {
    }
}
