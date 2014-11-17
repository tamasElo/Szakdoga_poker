package hu.szakdolgozat.poker.vezerloOsztalyok;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class AdatKezelo {
    private AdatKezelo(){}
    
    public static void grafikaBeallitasMent(Dimension felbontas, byte kepFrissites, byte kepernyoMod, boolean elsimitas) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/hu/szakdolgozat/poker/adatFajlok/beallitasok/grafika.txt"))) {
            writer.write(String.valueOf(felbontas.width) + "\n");
            writer.write(String.valueOf(felbontas.height) + "\n");
            writer.write(String.valueOf(kepFrissites) + "\n");
            writer.write(String.valueOf(kepernyoMod) + "\n");
            writer.write(String.valueOf(elsimitas) + "\n");
        } catch (IOException ex) {
            Logger.getLogger(AdatKezelo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void audioBeallitasMent(boolean menuZene, boolean hangok) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/hu/szakdolgozat/poker/adatFajlok/beallitasok/hangok.txt"))) {
            writer.write(String.valueOf(menuZene) + "\n");
            writer.write(String.valueOf(hangok) + "\n");
        } catch (IOException ex) {
            Logger.getLogger(AdatKezelo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void jatekmenetBeallitasMent(String emberJatekosNev, byte jatekosokSzama,
            int zsetonOsszeg, byte nagyVakErtek, byte vakErtekEmeles) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/hu/szakdolgozat/poker/adatFajlok/beallitasok/jatekmenet.txt"))) {
            writer.write(emberJatekosNev + "\n");
            writer.write(String.valueOf(jatekosokSzama) + "\n");
            writer.write(String.valueOf(zsetonOsszeg) + "\n");
            writer.write(String.valueOf(nagyVakErtek) + "\n");
            writer.write(String.valueOf(vakErtekEmeles) + "\n");          
        } catch (IOException ex) {
            Logger.getLogger(AdatKezelo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public static void jatekAllasMent(){
    }   
    
    public static List<String> grafikaBeallitasBetolt(){  
        String adat;
        List<String> adatok = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("src/hu/szakdolgozat/poker/adatFajlok/beallitasok/grafika.txt"))) {            
            while ((adat = reader.readLine()) != null) {    
                adatok.add(adat);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AdatKezelo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdatKezelo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return adatok;
    }
    
    public static void audioBeallitasBetolt(){
    }
    
    public static void jatekMenetBeallitasBetolt(){
    }
    
    public static void jatekAllasBetolt(){
    
    }
}
