package vezerloOsztalyok;

import alapOsztalyok.Kartyalap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.*;
import org.junit.Test;

public class PakliKezeloTest {
    
    public PakliKezeloTest() {
    }

    /**
     * Leellenőrzi hogy a pakli minden lapja különbözik-e.
     */
    @Test
    public void testKevertPakli() {
        List<Kartyalap>pakli = PakliKezelo.kevertPakli();
        Set<Kartyalap>halmaz = new HashSet<>(pakli);//Ha át rakom halmazba a listát akkor az azonos elemek közül kitöröl egyet
        assertTrue(pakli.size() == halmaz.size());                 
   }    
}
