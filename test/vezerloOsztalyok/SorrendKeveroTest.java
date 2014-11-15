package vezerloOsztalyok;

import hu.szakdolgozat.poker.vezerloOsztalyok.SorrendKevero;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SorrendKeveroTest {
    
    public SorrendKeveroTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Leteszteli hogy kevert sorrendet hoz-e létre a kevertSorrend metódus.
     */
    @Test
    public void testKevertSorrend() {
        boolean kevert = false;
        List<Integer>sorrend = SorrendKevero.kevertSorrend();
        for (int i = 0; i < sorrend.size(); i++) {
            if(i!=sorrend.get(i)){
                kevert = true;
                break;
            }
        }
        assertTrue(kevert);
    }    
}
