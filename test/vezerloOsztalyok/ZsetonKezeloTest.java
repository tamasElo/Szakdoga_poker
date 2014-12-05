package vezerloOsztalyok;

import hu.szakdolgozat.poker.vezerloOsztalyok.ZsetonKezelo;
import hu.szakdolgozat.poker.alapOsztalyok.Zseton;
import java.util.List;
import java.util.Map;
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

    /**
     * A pot nyertesek közötti szétválogatását teszteli.
     */
    @Test
    public void testPotSzetvalogat() {
        Map<Byte, List<Zseton>> szetvalogatottZsetonok = ZsetonKezelo.potSzetvalogat((byte)2, zsetonok);
        osszeg = ZsetonKezelo.zsetonokOsszege(szetvalogatottZsetonok.get((byte)0));
        osszeg2 = ZsetonKezelo.zsetonokOsszege(szetvalogatottZsetonok.get((byte)1));
        assertTrue(osszeg == osszeg2);
    }

    /**
     * Azt nézi hogy helyesen állípítja-e meg a zsetonok összegét.
     */
    @Test
    public void testZsetonokOsszege() {        
        assertTrue(ZsetonKezelo.zsetonokOsszege(zsetonok) == osszeg);
    }
}
