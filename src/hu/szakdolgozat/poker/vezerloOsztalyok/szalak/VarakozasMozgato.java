package hu.szakdolgozat.poker.vezerloOsztalyok.szalak;

import hu.szakdolgozat.poker.alapOsztalyok.Toltes;
import hu.szakdolgozat.poker.felulet.JatekterPanel;
import hu.szakdolgozat.poker.vezerloOsztalyok.SzalVezerlo;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        
        double forgSzog = 0;
        
        while (!szalStop) {
            for (int i = 0; i < 24; i++) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    Logger.getLogger(JatekterPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                forgSzog -= 15;
                
                if (forgSzog <= -360) {
                    forgSzog = 0;
                }
                
                toltes.setForgat(forgSzog);
            }
        }
    }

    public void setSzalStop(boolean szalStop) {
        this.szalStop = szalStop;
    }    
}
