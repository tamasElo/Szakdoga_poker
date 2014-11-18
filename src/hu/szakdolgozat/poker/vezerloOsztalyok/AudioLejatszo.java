package hu.szakdolgozat.poker.vezerloOsztalyok;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;

public class AudioLejatszo {
    private static AudioInputStream ais;
    private static Clip clip;
    private static boolean menuZene;
    private static boolean hangok;
    private static boolean elinditva;
    public static final File MENU_ZENE = new File("src/hu/szakdolgozat/poker/adatFajlok/audio/menu_zene.wav");
    public static final File KARTYA_KEVERES = new File("src/hu/szakdolgozat/poker/adatFajlok/audio/kartya_keveres.wav");
    public static final File KARTYA_OSZTAS = new File("src/hu/szakdolgozat/poker/adatFajlok/audio/kartya_osztas.wav");
    public static final File ZSETON_CSORGES = new File("src/hu/szakdolgozat/poker/adatFajlok/audio/zseton_csorges.wav");
    public static final File JATEK_NYERTES = new File("src/hu/szakdolgozat/poker/adatFajlok/audio/jatek_nyertes.wav");
    public static final File KOR_NYERTES = new File("src/hu/szakdolgozat/poker/adatFajlok/audio/kor_nyertes.wav");
    public static final File ELOUGRO_FELHO = new File("src/hu/szakdolgozat/poker/adatFajlok/audio/elougro_felho.wav");
    public static final File MENUPONT_HANG = new File("src/hu/szakdolgozat/poker/adatFajlok/audio/menupont_hang.wav");

    /**
     * Hangok lejátszását teszi lehetővé
     * 
     * @param cim
     * @param ismetles
     */
    public synchronized static void audioLejatszas(File cim, boolean ismetles) {
        audioBeallitasokBetolt();

        try {
            if (menuZene && !elinditva && cim == MENU_ZENE) {
                audioMegallit();
                ais = AudioSystem.getAudioInputStream(cim);
                clip = AudioSystem.getClip();
                clip.open(ais);               
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                elinditva = true;
            }
            
            if (!menuZene) {
                audioMegallit();
            }
            
            if (hangok && (cim != MENU_ZENE)) {
                ais = AudioSystem.
                        getAudioInputStream(cim);
                clip = AudioSystem.getClip();
                clip.open(ais);

                if (ismetles) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    clip.start();
                }
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
      
    }
    
    public synchronized static void audioMegallit() {
        if (clip != null) {
            clip.stop();
            elinditva = false;
        }        
    }
    
    private static void audioBeallitasokBetolt(){
        List<String> adatok = AdatKezelo.beallitasBetolt(AdatKezelo.AUDIO);
        Iterator<String> itr = adatok.iterator();
        menuZene = Boolean.parseBoolean(itr.next());
        hangok = Boolean.parseBoolean(itr.next());
    }

    public static boolean isMenuZene() {
        return menuZene;
    }

    public static boolean isHangok() {
        return hangok;
    }   
}
