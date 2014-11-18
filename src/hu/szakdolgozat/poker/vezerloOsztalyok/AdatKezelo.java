package hu.szakdolgozat.poker.vezerloOsztalyok;

import java.awt.DisplayMode;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public final class AdatKezelo {

    public static final File GRAFIKA = new File("src/hu/szakdolgozat/poker/adatFajlok/beallitasok/grafika.txt");
    public static final File AUDIO = new File("src/hu/szakdolgozat/poker/adatFajlok/beallitasok/audio.txt");
    public static final File JATEKMENET = new File("src/hu/szakdolgozat/poker/adatFajlok/beallitasok/jatekmenet.txt");
    
    private AdatKezelo(){}
    
    /**
     * Kiírja fájlba a grafikus beállítások paramétereit.
     * 
     * @param kepernyoMod
     * @param kepernyoAllapot
     * @param elsimitas 
     */
    public static void grafikaBeallitasMent(DisplayMode kepernyoMod, byte kepernyoAllapot, boolean elsimitas) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(GRAFIKA))) {
            writer.write(String.valueOf(kepernyoMod.getWidth()) + "\n");
            writer.write(String.valueOf(kepernyoMod.getHeight()) + "\n");            
            writer.write(String.valueOf(kepernyoMod.getBitDepth()) + "\n");
            writer.write(String.valueOf(kepernyoMod.getRefreshRate()) + "\n");
            writer.write(String.valueOf(kepernyoAllapot) + "\n");
            writer.write(String.valueOf(elsimitas) + "\n");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    /**
     * Kiírja fájlba az audió beállítások paramétereit
     * 
     * @param menuZene
     * @param hangok 
     */
    public static void audioBeallitasMent(boolean menuZene, boolean hangok) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(AUDIO))) {
            writer.write(String.valueOf(menuZene) + "\n");
            writer.write(String.valueOf(hangok) + "\n");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    /**
     * Kiírja fájlba a játékmenet beállítások paramétereit.
     * 
     * @param emberJatekosNev
     * @param jatekosokSzama
     * @param zsetonOsszeg
     * @param nagyVakErtek
     * @param vakErtekEmeles 
     */
    public static void jatekmenetBeallitasMent(String emberJatekosNev, byte jatekosokSzama,
            int zsetonOsszeg, byte nagyVakErtek, byte vakErtekEmeles) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(JATEKMENET))) {
            writer.write(emberJatekosNev + "\n");
            writer.write(String.valueOf(jatekosokSzama) + "\n");
            writer.write(String.valueOf(zsetonOsszeg) + "\n");
            writer.write(String.valueOf(nagyVakErtek) + "\n");
            writer.write(String.valueOf(vakErtekEmeles) + "\n");          
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

    }
    
    public static void jatekAllasMent(){
    }   
    
    /**
     * Beolvassa fájlból a elérési útként megadott beállítások paramétereit.
     * 
     * @param eleresiUt
     * @return 
     */
    public static List<String> beallitasBetolt(File eleresiUt){  
        String adat;
        List<String> adatok = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(eleresiUt))) {            
            while ((adat = reader.readLine()) != null) {    
                adatok.add(adat);
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
        return adatok;
    }
    
    public static void jatekAllasBetolt(){
    
    }
}
