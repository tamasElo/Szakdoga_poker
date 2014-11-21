package hu.szakdolgozat.poker.felulet;

import hu.szakdolgozat.poker.vezerloOsztalyok.FeluletKezelo;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class KepernyoKezelo {
    private GraphicsDevice vc;
    private List<DisplayMode> kepernyoModok;
    private FeluletKezelo feluletKezelo;
    private boolean teljesKepernyoBekapcsolva;
    public static final byte ABLAKOS_MOD = 0;
    public static final byte TELJES_KEPERNYO_MOD = 1;
    public static final byte NORMAL_KEPERNYO = 0;
    public static final byte SZELES_KEPERNYO = 1;

    public KepernyoKezelo(FeluletKezelo feluletKezelo) {
        this.feluletKezelo = feluletKezelo;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        vc = ge.getDefaultScreenDevice();
    }
    
    /**
     * Leelenőrzi, hogy támogatja-e a kijelző a képernyő módot.
     * 
     * @param dm
     * @return 
     */
    public boolean kepernyoModEllenorzes(DisplayMode dm){
        boolean tamogatott = false;
        kepernyoModok = new ArrayList<>();
        DisplayMode[] kModok = vc.getDisplayModes();
        int horizontalisFelbontas, vertikalisFelbontas;
        int szinMelyseg;

        for (DisplayMode dmode : kModok) {
            horizontalisFelbontas = dmode.getWidth();
            vertikalisFelbontas = dmode.getHeight();
            szinMelyseg = dmode.getBitDepth();

            if (feluletKezelo.getKepernyoAllapot() == TELJES_KEPERNYO_MOD) {
                if (((double) Math.round(10 * horizontalisFelbontas / vertikalisFelbontas) / 10 == 1.3 && szinMelyseg == 32
                        || (double) Math.round(10 * horizontalisFelbontas / vertikalisFelbontas) / 10 == 1.7 && szinMelyseg == 32)
                        && dmode.getHeight() >= 768) {
                    kepernyoModok.add(dmode);
                }
            } else if (((double) Math.round(10 * horizontalisFelbontas / vertikalisFelbontas) / 10 == 1.3 && szinMelyseg == 32
                    && dmode.getRefreshRate() == vc.getDisplayMode().getRefreshRate()
                    || (double) Math.round(10 * horizontalisFelbontas / vertikalisFelbontas) / 10 == 1.7 && szinMelyseg == 32
                    && dmode.getRefreshRate() == vc.getDisplayMode().getRefreshRate())
                    && dmode.getHeight() >= 768) {
                kepernyoModok.add(dmode);
            }
        }
       
        if (feluletKezelo.getKepernyoAllapot() == ABLAKOS_MOD) {
            feluletKezelo.setKepernyoMod(new DisplayMode(dm.getWidth(), 
                    dm.getHeight(), dm.getBitDepth(), 
                    vc.getDisplayMode().getRefreshRate()));
            dm = feluletKezelo.getKepernyoMod();
        }
        
        for (DisplayMode dmode : kepernyoModok) {
            if(dmode.equals(dm)){
                tamogatott = true;
            }
        }
        
        return tamogatott;
    }
    
    public static byte keparanySzamit(Dimension felbontas){
        if((double) Math.round(10 * felbontas.getWidth() / felbontas.getHeight()) / 10 == 1.3)
            return NORMAL_KEPERNYO;
        else return SZELES_KEPERNYO;
    }
    
    /**
     * Bekapcsolja a teljes képernyős üzemmódot.
     * 
     * @param dm
     * @param pokerFrame 
     */
    public void teljesKepernyoBe(DisplayMode dm, JFrame pokerFrame) {   
        pokerFrame.setUndecorated(true);
        pokerFrame.setResizable(false);
        vc.setFullScreenWindow(pokerFrame);

        if (dm != null && vc.isDisplayChangeSupported()) {
            try {
                vc.setDisplayMode(dm);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }

            teljesKepernyoBekapcsolva = true;
        }
    }

    /**
     * Kikapcsolja a teljes képernyős üzemmódot. 
     */
    public void teljesKepernyoKi() {
        Window w = vc.getFullScreenWindow();
        
        if (w != null) {
            w.dispose();
        }
        
        vc.setFullScreenWindow(null);
        teljesKepernyoBekapcsolva = false;
    }

    public List<DisplayMode> kepernyoModLista() {
        return kepernyoModok;
    }

    public boolean isTeljesKepernyoBekapcsolva() {
        return teljesKepernyoBekapcsolva;
    }
}
