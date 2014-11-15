package vezerloOsztalyok;

import hu.szakdolgozat.poker.vezerloOsztalyok.SzogSzamito;
import java.awt.Point;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SzogSzamitoTest {
    private double szog;
    private boolean egyezes;
    
    public SzogSzamitoTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * A végpont listaába tárolt végpontok megfelelő pozícióját vizsgálja.
     */
    @Test
    public void testVegpontLista() {
        egyezes = false;
        szog = 90;
        double vegpontSzog;
        List<Point> vegpontok = SzogSzamito.vegpontLista(3, 200, 100);
        
        for (Point point : vegpontok) {            
            vegpontSzog = SzogSzamito.foSzogSzamit(200, 100, point.getX(), point.getY());
            if(szog >= vegpontSzog-2 && szog <=vegpontSzog+2) egyezes = true;//Megvizsgálja a végpontokhoz tartozó szöget hogy megfelelő hlyen vannak-e.
            szog += 360/vegpontok.size();
        }
        assertTrue(egyezes);
    }

    /**
     * A vegpontSzamit metódus által visszadott végpont megfeleő pozícióját vizsgálja.
     */
    @Test
    public void testVegpontSzamit() {
        Point vegpont = SzogSzamito.vegpontSzamit(90, 200, 100);
        szog = SzogSzamito.foSzogSzamit(200, 100, vegpont.getX(), vegpont.getY());      
        
        assertTrue(90 >= szog-2 && 90 <=szog+2);        
    }

    /**
     * A szogSzamit metodust teszteli.
     */
    @Test
    public void testSzogSzamit() {
        egyezes = true;
        szog = SzogSzamito.szogSzamit(320, 240, 165, 5);
        
        if(szog != -90)egyezes = false;
        szog = SzogSzamito.szogSzamit(320, 240, 165, 235);
        if(szog != 90)egyezes = false;
        szog = SzogSzamito.szogSzamit(320, 240, 5, 110);
        if(szog == -90 || szog == 90)egyezes = false;
        szog = SzogSzamito.szogSzamit(320, 240, 315, 110);
        if(szog == -90 || szog == 90)egyezes = false;
        assertTrue(egyezes);
    }

    /**
     * A forgasSzogSzamit metodust teszteli.
     */
    @Test
    public void testForgasSzogSzamit() {
        egyezes = true;
        szog = SzogSzamito.forgasSzogSzamit(200, 100, 10, 45);
        if(szog > 0) egyezes = false;
         szog = SzogSzamito.forgasSzogSzamit(200, 100, 190, 45);
        if(szog < 0) egyezes = false;
         szog = SzogSzamito.forgasSzogSzamit(200, 100, 10, 55);
        if(szog < 0) egyezes = false;
         szog = SzogSzamito.forgasSzogSzamit(200, 100, 190, 55);
        if(szog > 0) egyezes = false;
        assertTrue(egyezes);
    }
}
