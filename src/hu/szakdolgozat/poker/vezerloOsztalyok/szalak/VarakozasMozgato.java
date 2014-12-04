package hu.szakdolgozat.poker.vezerloOsztalyok.szalak;

import hu.szakdolgozat.poker.alapOsztalyok.Toltes;
import hu.szakdolgozat.poker.felulet.JatekterPanel;
import hu.szakdolgozat.poker.vezerloOsztalyok.SzalVezerlo;
import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

public class VarakozasMozgato extends Thread {
    
    private SzalVezerlo szalVezerlo;
    private boolean szalStop;

    public VarakozasMozgato(SzalVezerlo szalVezerlo) {
        this.szalVezerlo = szalVezerlo;
    }
    
    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {
        Toltes toltes = szalVezerlo.getToltes();
        Image kep;
        while (!szalStop) {
            for (int i = 0; i < 24; i++) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    Logger.getLogger(JatekterPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                kep = new ImageIcon("src/hu/szakdolgozat/poker/adatFajlok/varakozas/tolt_" + i + ".png").getImage();
                toltes.setToltoKep(kep);
            }
        }
    }

    public void setSzalStop(boolean szalStop) {
        this.szalStop = szalStop;
    }    
}
