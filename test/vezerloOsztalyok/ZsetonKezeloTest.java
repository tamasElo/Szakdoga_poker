package vezerloOsztalyok;

import alapOsztalyok.Zseton;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ZsetonKezeloTest {
    private List<Zseton> zsetonok;
    private int osszeg;
    private int osszeg2;
    
    public ZsetonKezeloTest() {
    }
    
    @Before
    public void setUp() {
        osszeg = 200;
        zsetonok = ZsetonKezelo.zsetonKioszt(osszeg);
    }

    /**
     * A zsetonKioszt metódus tesztelése.
     */
    @Test
    public void testZsetonKioszt() {        
        for (Zseton zseton : zsetonok) {
            osszeg2 += zseton.getErtek();
        }
        
        assertEquals(osszeg, osszeg2);
    }

    /**
     * A pot metódust teszteli.
     */
    @Test
    public void testPot() {
        List<Zseton> pot;
        osszeg2 = 100;
        
        pot = ZsetonKezelo.pot(zsetonok, osszeg2);
        for (Zseton zseton : pot) {
            osszeg2+=zseton.getErtek();
        }
        assertEquals(osszeg, osszeg2);
    }
}
