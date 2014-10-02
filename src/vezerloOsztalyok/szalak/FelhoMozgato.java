package vezerloOsztalyok.szalak;

import alapOsztalyok.Felho;
import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;
import vezerloOsztalyok.SzalVezerlo;

public class FelhoMozgato extends Thread {
    private double kx;
    private double ky;
    private double vx;
    private double vy;
    private double aktx;
    private double akty;
    private double felhoKepSzelesseg;
    private double felhoKepMagassag;
    private static final long IDO = 2000;
    private SzalVezerlo szalVezerlo;

    public FelhoMozgato(SzalVezerlo szalVezerlo) {
        this.szalVezerlo = szalVezerlo;
    }

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {
        try {           
            Felho felho = szalVezerlo.getFelho();
            kx = felho.getKx();
            ky = felho.getKy();
            felhoKepSzelesseg = felho.getFelhoKepSzelesseg();
            felhoKepMagassag = felho.getFelhoKepMagassag();
            vx = kx + felhoKepSzelesseg/4;
            vy = ky - felhoKepMagassag / 1.7;
            double szovegMeret = 0;            
            double aktTav = 0;
            double szog = Math.atan2(vy - ky, vx - kx);
            double tavolsag = Math.sqrt((vy - ky) * (vy - ky) + (vx - kx) * (vx - kx));
            double felhoHorizontalisLepes = felhoKepSzelesseg/tavolsag;
            double felhoVertikalisLepes = felhoKepMagassag/tavolsag;            
            double szovegLepes = (felhoKepMagassag/9.75)/tavolsag;
            felhoKepSzelesseg = 0;
            felhoKepMagassag = 0;                
            while (aktTav <= tavolsag) {
                aktTav ++;
                felhoKepSzelesseg+= felhoHorizontalisLepes;
                felhoKepMagassag+= felhoVertikalisLepes;
                szovegMeret += szovegLepes;
                felho.setFont(new Font("Arial", 2, (int) szovegMeret));
                aktx = kx + aktTav * Math.cos(szog);
                akty = ky + aktTav * Math.sin(szog);
                felho.setKx(aktx);
                felho.setKy(akty);
                felho.setFelhoKepSzelesseg(felhoKepSzelesseg);
                felho.setFelhoKepMagassag(felhoKepMagassag);
                sleep(2);
                szalVezerlo.frissit();
            }
            sleep(IDO);
        } catch (InterruptedException ex) {
            Logger.getLogger(FelhoMozgato.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
}
