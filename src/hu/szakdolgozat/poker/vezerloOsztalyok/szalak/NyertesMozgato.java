package hu.szakdolgozat.poker.vezerloOsztalyok.szalak;

import hu.szakdolgozat.poker.alapOsztalyok.Nyertes;
import java.awt.Font;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import hu.szakdolgozat.poker.vezerloOsztalyok.FeluletKezelo;
import hu.szakdolgozat.poker.vezerloOsztalyok.SzalVezerlo;

public class NyertesMozgato extends Thread{
    private SzalVezerlo szalVezerlo;     
    private double kx;
    private double ky;
    private double nyertesKepSzelesseg;
    private double nyertesKepMagassag;
    private Nyertes nyertes;
    private long ido;
    
    public NyertesMozgato(Nyertes nyertes, SzalVezerlo szalVezerlo) {
        this.nyertes = nyertes;
        this.szalVezerlo = szalVezerlo;
    }

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {
        try {
            nyertesKepSzelesseg = nyertes.getNyertesKepSzelesseg();
            nyertesKepMagassag = nyertes.getNyertesKepMagassag();
            double szovegMeret = 0;
            double aktTav = 0;
            double szorzo = 10;
            double szog = 0;
            double vegsoMeret = 800;
            double nyertesHorizontalisLepes = szorzo * nyertesKepSzelesseg / vegsoMeret,
                   nyertesVertikalisLepes = szorzo * nyertesKepMagassag / vegsoMeret;
            double szovegLepes = szorzo * (nyertesKepMagassag / 7.8) / vegsoMeret;
            ido = 10;
            nyertesKepSzelesseg = 0;
            nyertesKepMagassag = 0;

            while (aktTav <= vegsoMeret) {
                aktTav += szorzo;
                nyertesKepSzelesseg += nyertesHorizontalisLepes;
                nyertesKepMagassag += nyertesVertikalisLepes;
                szovegMeret += szovegLepes;
                szog += 0.1;
                nyertes.setFont(new Font("Arial", 3, (int) szovegMeret));
                nyertes.setNyertesKepSzelesseg(nyertesKepSzelesseg);
                nyertes.setNyertesKepMagassag(nyertesKepMagassag);
                nyertes.setForgat(szog);
                szalVezerlo.frissit();
                sleep(0, (int) ido);
            }
            
            ido = 4000;
            sleep(ido);
            FeluletKezelo.jatekMenuPanelBetolt();
        } catch (InterruptedException ex) {
            Logger.getLogger(NyertesMozgato.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
