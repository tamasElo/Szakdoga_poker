package hu.szakdolgozat.poker.felulet;

import hu.szakdolgozat.poker.vezerloOsztalyok.FeluletKezelo;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class KepernyoKezelo {
    private GraphicsDevice vc;
    private List<DisplayMode> kepernyoModok;
    private FeluletKezelo feluletKezelo;
    private boolean teljesKepernyoBekapcsolva;

    public KepernyoKezelo(FeluletKezelo feluletKezelo) {
        this.feluletKezelo = feluletKezelo;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        vc = ge.getDefaultScreenDevice();
    }
    
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
            
            if (feluletKezelo.getKepernyoMod() == FeluletKezelo.TELJES_KEPERNYO_MOD) {
                if (((double) Math.round(10 * horizontalisFelbontas / vertikalisFelbontas) / 10 == 1.3 && szinMelyseg == 32
                        || (double) Math.round(10 * horizontalisFelbontas / vertikalisFelbontas) / 10 == 1.7 && szinMelyseg == 32)
                        && dmode.getHeight() >= 768) {
                    kepernyoModok.add(dmode);
                }
            } else {
                if (((double) Math.round(10 * horizontalisFelbontas / vertikalisFelbontas) / 10 == 1.3 && szinMelyseg == 32 
                        && dmode.getRefreshRate() == vc.getDisplayMode().getRefreshRate()
                        || (double) Math.round(10 * horizontalisFelbontas / vertikalisFelbontas) / 10 == 1.7 && szinMelyseg == 32 
                        && dmode.getRefreshRate() == vc.getDisplayMode().getRefreshRate())
                        && dmode.getHeight() >= 768) {
                    kepernyoModok.add(dmode);
                }
            }
        }
       
        if (feluletKezelo.getKepernyoMod() == FeluletKezelo.ABLAKOS_MOD) {
            feluletKezelo.setKepFrissites((byte) vc.getDisplayMode().getRefreshRate());
        }
        
        for (DisplayMode dmode : kepernyoModok) {
            if(dmode.getWidth() == dm.getWidth() && dmode.getHeight() == dm.getHeight() 
                    && dmode.getRefreshRate() == dm.getRefreshRate()){
                tamogatott = true;
            }
        }
        
        return tamogatott;
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

    public List<DisplayMode> getKepernyoModok() {
        return kepernyoModok;
    }

    public boolean isTeljesKepernyoBekapcsolva() {
        return teljesKepernyoBekapcsolva;
    }
}
