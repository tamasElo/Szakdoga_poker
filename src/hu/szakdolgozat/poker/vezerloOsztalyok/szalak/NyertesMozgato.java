package hu.szakdolgozat.poker.vezerloOsztalyok.szalak;

import hu.szakdolgozat.poker.alapOsztalyok.Nyertes;
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

public class NyertesMozgato extends Thread {

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
            Map<String, List<Double>> xmlAdatok = AdatKezelo.aranyErtekekBetolt("GrafikaElemek",
                    new Dimension(szalVezerlo.jatekterPanelSzelesseg(), szalVezerlo.jatekterPanelMagassag()));
            Iterator<Double> itr = xmlAdatok.get("Nyertes").iterator();
            kx = itr.next();
            ky = itr.next();
            nyertesKepSzelesseg = itr.next();
            nyertesKepMagassag = itr.next();            
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
            
            nyertes.setKx(kx);
            nyertes.setKy(ky);
            AudioLejatszo.audioLejatszas(AudioLejatszo.JATEK_NYERTES, false);

            while (aktTav <= vegsoMeret) {
                aktTav += szorzo;
                nyertesKepSzelesseg += nyertesHorizontalisLepes;
                nyertesKepMagassag += nyertesVertikalisLepes;
                szovegMeret += szovegLepes;
                szog -= 0.1;
                nyertes.setFont(new Font("Arial", 3, (int) szovegMeret));
                nyertes.setNyertesKepSzelesseg(nyertesKepSzelesseg);
                nyertes.setNyertesKepMagassag(nyertesKepMagassag);
                nyertes.setForgat(szog);
                sleep(0, (int) ido);
            }

            ido = 4000;
            sleep(ido);
            szalVezerlo.kilepes();
        } catch (InterruptedException ex) {
            Logger.getLogger(NyertesMozgato.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
