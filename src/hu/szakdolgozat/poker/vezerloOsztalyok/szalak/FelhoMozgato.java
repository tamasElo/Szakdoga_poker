package hu.szakdolgozat.poker.vezerloOsztalyok.szalak;

import hu.szakdolgozat.poker.alapOsztalyok.Felho;
import hu.szakdolgozat.poker.vezerloOsztalyok.AdatKezelo;
import hu.szakdolgozat.poker.vezerloOsztalyok.AudioLejatszo;
import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;
import hu.szakdolgozat.poker.vezerloOsztalyok.SzalVezerlo;
import java.awt.Dimension;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FelhoMozgato extends Thread {

    private double kx;
    private double ky;
    private double vx;
    private double vy;
    private double aktx;
    private double akty;
    private double felhoKepSzelesseg;
    private double felhoKepMagassag;
    private Felho felho;
    private SzalVezerlo szalVezerlo;

    public FelhoMozgato(Felho felho, SzalVezerlo szalVezerlo) {
        this.felho = felho;
        this.szalVezerlo = szalVezerlo;
    }

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {
        try {
            kx = felho.getKx();
            ky = felho.getKy();
            Map<String, List<Double>> xmlAdatok = AdatKezelo.aranyErtekekBetolt("GrafikaElemek", 
                    new Dimension(szalVezerlo.jatekterPanelSzelesseg(), szalVezerlo.jatekterPanelMagassag()));
            Iterator<Double> itr = xmlAdatok.get("Felho").iterator();
            felhoKepSzelesseg = itr.next();
            felhoKepMagassag = itr.next();
            vx = kx + felhoKepSzelesseg / 4;
            vy = ky - felhoKepMagassag / 1.7;
            double betuMeret = 0;           
            double aktTav = 0;
            double szog = Math.atan2(vy - ky, vx - kx);
            double tavolsag = Math.sqrt((vy - ky) * (vy - ky) + (vx - kx) * (vx - kx));
            double felhoHorizontalisLepes = felhoKepSzelesseg / tavolsag;
            double felhoVertikalisLepes = felhoKepMagassag / tavolsag;
            double szovegLepes = (felhoKepMagassag / 7.8) / tavolsag;
            long ido = 2;
            felhoKepSzelesseg = 0;
            felhoKepMagassag = 0;     
            AudioLejatszo.audioLejatszas(AudioLejatszo.ELOUGRO_FELHO, false);
            
            while (aktTav <= tavolsag) {
                aktTav ++;
                felhoKepSzelesseg+= felhoHorizontalisLepes;
                felhoKepMagassag+= felhoVertikalisLepes;
                betuMeret += szovegLepes;
                felho.setFont(new Font("Arial", 2, (int) betuMeret));
                aktx = kx + aktTav * Math.cos(szog);
                akty = ky + aktTav * Math.sin(szog);
                felho.setKx(aktx);
                felho.setKy(akty);
                felho.setFelhoKepSzelesseg(felhoKepSzelesseg);
                felho.setFelhoKepMagassag(felhoKepMagassag);                
                sleep(ido);
            }       
            
            ido = 1500;            
            sleep(ido);
        } catch (InterruptedException ex) {
            Logger.getLogger(FelhoMozgato.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
