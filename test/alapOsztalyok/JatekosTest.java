package alapOsztalyok;

import hu.szakdolgozat.poker.alapOsztalyok.Jatekos;
import java.awt.Font;
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
     * A setKx és getX metódus tesztelése.
     */
    @Test
    public void testSetX_GetX() {
        int x = 5;
        jatekos.setKx(x);
        assertTrue(x == jatekos.getX());
    }

    /**
     * A setKy és getY metódus tesztelése.
     */
    @Test
    public void testSetY_Gety() {
        int y = 10;
        jatekos.setKy(y);
        assertTrue(y == jatekos.getY());
    }

    /**
     * A setPokerKezNev és getPokerKezNev metódus tesztelése.
     */
    @Test
    public void testSetPokerKezNev_GetPokerKezNev() {
        String pokerKezNev = "Sor";
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
        int sorszam = jatekos.getSorszam();
        assertEquals(sorszam+1, new Jatekos("Teszt_Geza").getSorszam());
    }   

    /**
     * A setFont és getFong metódusokat teszteli.
     */
    @Test
    public void testSetFont_getFont() {
        Font font = new Font("Arial", 1, 1);
        jatekos.setFont(font);
        assertEquals(font, jatekos.getFont());        
    }
}
