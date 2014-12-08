package hu.szakdolgozat.poker.vezerloOsztalyok.szalak;

import hu.szakdolgozat.poker.burkoloOsztalyok.PokerKez;
import hu.szakdolgozat.poker.vezerloOsztalyok.Lapkombinaciok;
import hu.szakdolgozat.poker.vezerloOsztalyok.PokerKezKiertekelo;
import hu.szakdolgozat.poker.vezerloOsztalyok.SzalVezerlo;
import hu.szakdolgozat.poker.vezerloOsztalyok.ZsetonKezelo;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Mi extends Thread{

    private JatekVezerlo jatekVezerlo;
    private SzalVezerlo szalVezerlo;
    private byte jatekosSorszam;
    private byte outok;
    private double odds;
    private double potOdds;
    private int potOsszeg;
    private int megadandoOsszeg;
    private int maxOsszeg;
    private int emelendoOsszeg;
    private double maxPokerKezErtek;
    private int pokerKezErtek;
    private PokerKez pokerKez;
    private double bedobasValoszinuseg;
    private double megadasValoszinuseg;
    private double emelesValoszinuseg;
    private double dontes;
    private boolean szalStop;

    public Mi(JatekVezerlo jatekVezerlo, SzalVezerlo szalVezerlo) {
        this.jatekVezerlo = jatekVezerlo;
        this.szalVezerlo = szalVezerlo;
    }
    
    /**
     * Eldönti, hogy mi legyen az aktuális játékos reakciója.
     */
    public void dontes() {     
        jatekosSorszam = jatekVezerlo.getAktJatekosSorszam();
        potOsszeg = ZsetonKezelo.zsetonokOsszege(szalVezerlo.getPot());
        megadandoOsszeg = jatekVezerlo.getOsszeg() - jatekVezerlo.getJatekosokTetje()[jatekosSorszam];
        pokerKez = PokerKezKiertekelo.pokerKezKeres(szalVezerlo.getJatekosokKartyalapjai().get(jatekosSorszam),
                szalVezerlo.getLeosztottKartyalapok());
        bedobasValoszinuseg = 0.5;
        megadasValoszinuseg = 0.9;
        emelesValoszinuseg = 1;
        outok = pokerKez.getOutok();
        maxPokerKezErtek = Lapkombinaciok.maxPokerkezMeghataroz(szalVezerlo.getJatekosokKartyalapjai().get(jatekosSorszam),
                szalVezerlo.getLeosztottKartyalapok());
        pokerKezErtek = pokerKez.getPokerKezErtek();
        
        if (megadandoOsszeg > 0) {
            if (outok > 0) {
                oddsSzamit();
            } else {
                maxOsszegSzamit();

                if (megadandoOsszeg > maxOsszeg) {
                    bedobasValoszinuseg += megadandoOsszeg / (double) maxOsszeg - 1;
                } else {
                    bedobasValoszinuseg -= 0.4;
                }
            }
        } else {
            bedobasValoszinuseg = 0f;
        }
        
        megadasValoszinuseg -= (pokerKezErtek / maxPokerKezErtek) * 0.3;
        emelesValoszinuseg -= (pokerKezErtek / maxPokerKezErtek) * 0.1;

        if (bedobasValoszinuseg >= megadasValoszinuseg) {
            megadasValoszinuseg = bedobasValoszinuseg;
        }

        if (bedobasValoszinuseg >= emelesValoszinuseg) {
            emelesValoszinuseg = bedobasValoszinuseg;
        }
        
        dontes = Math.random();
        
        if (dontes < bedobasValoszinuseg) {
            jatekVezerlo.bedob();
        } else if (dontes < megadasValoszinuseg) {
            if (jatekVezerlo.isMegadhat()) {
                jatekVezerlo.megad();
            } else if (jatekVezerlo.isPasszolhat()){
                jatekVezerlo.passzol();
            } else jatekVezerlo.allIn();
        } else if (dontes < emelesValoszinuseg) {
            maxOsszegSzamit();
            
            dontes = Math.random();
            emelendoOsszeg = (int) (maxOsszeg * dontes);
            emelendoOsszeg -= emelendoOsszeg % jatekVezerlo.getKisVakErtek();
            
            if (emelendoOsszeg <= megadandoOsszeg && megadandoOsszeg != 0) {
                emelendoOsszeg = megadandoOsszeg;
            } else if (emelendoOsszeg <= jatekVezerlo.getNagyVakErtek()) {
                emelendoOsszeg = jatekVezerlo.getNagyVakErtek();
            }
            
            if (emelendoOsszeg >= szalVezerlo.getJatekosZsetonOsszeg(jatekosSorszam)) {
                jatekVezerlo.allIn();
            } else if (jatekVezerlo.isEmelhet()) {
                jatekVezerlo.emel(emelendoOsszeg);
            } else {
                jatekVezerlo.nyit(emelendoOsszeg);
            }
        } else {
            jatekVezerlo.allIn();
        }
    }

    /**
     * Kiszámítja az odds-ot.
     */
    private void oddsSzamit() {
        if (szalVezerlo.leosztottKartyalapokSzama() == 3) {
            odds = 1 - ((47 - outok) / 47f) * ((46 - outok) / 46f);
        }

        if (szalVezerlo.leosztottKartyalapokSzama() == 4) {
            odds = outok / 46f;
        }

        if (megadandoOsszeg != 0) {
            potOdds = megadandoOsszeg / (double) potOsszeg;
        }

        if (odds < potOdds) {
            bedobasValoszinuseg += 0.3;
        } else {
            bedobasValoszinuseg -= 0.4;
        }
    }

    /**
     * A megfelelő valószínűségek mellett kiszámítja a maximum összeget.
     */
    private void maxOsszegSzamit() {
        double oszto;
        double negyedelesValoszinuseg = 0f, felezesValoszinuseg = 0f, harmadolasValoszinuseg = 0f;
        dontes = Math.random();
        
        switch (szalVezerlo.leosztottKartyalapokSzama()) {
            case 0:
                oszto = 5f;
                negyedelesValoszinuseg = 1 - 1 / oszto;
                felezesValoszinuseg = 1 - (1 - negyedelesValoszinuseg) / oszto;
                harmadolasValoszinuseg = 1 - (1 - felezesValoszinuseg) / oszto;
                break;
            case 3:
                oszto = 3f;
                negyedelesValoszinuseg = 1 - 1 / oszto;
                felezesValoszinuseg = 1 - (1 - negyedelesValoszinuseg) / oszto;
                harmadolasValoszinuseg = 1 - (1 - felezesValoszinuseg) / oszto;
                break;
            case 4:
                oszto = 3f;
                harmadolasValoszinuseg = 1 / oszto;
                felezesValoszinuseg = harmadolasValoszinuseg - (harmadolasValoszinuseg) / oszto;
                negyedelesValoszinuseg = felezesValoszinuseg - (felezesValoszinuseg) / oszto;
                break;
            case 5:
                oszto = 5f;
                harmadolasValoszinuseg = 1 / oszto;
                felezesValoszinuseg = harmadolasValoszinuseg - (harmadolasValoszinuseg) / oszto;
                negyedelesValoszinuseg = felezesValoszinuseg - (felezesValoszinuseg) / oszto;
        }
        
        maxOsszeg = (int) (szalVezerlo.getJatekosZsetonOsszeg(jatekosSorszam) * (pokerKezErtek / maxPokerKezErtek));
        
        if(dontes < negyedelesValoszinuseg){
            maxOsszeg /= 4;
        } else if(dontes < felezesValoszinuseg){
            maxOsszeg /= 2;
        }else if(dontes < harmadolasValoszinuseg){
            maxOsszeg /= 3;
        }        
    }

    /**
     * Várakozó állapotba állítja a szálat.
     */
    private synchronized void megallit() {
        try {
            wait();
        } catch (InterruptedException ex) {
            Logger.getLogger(Mi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Felébreszti a szálat.
     */
    public synchronized void folytat() {
        notify();
    }

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {
        long ido;

        while (true) {
            try {
                ido = (long) (1000 + Math.random() * 1500);

                sleep(ido);

                if (szalStop) {
                    break;
                }

                dontes();
                megallit();
            } catch (InterruptedException ex) {
                Logger.getLogger(Mi.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setSzalStop(boolean szalStop) {
        this.szalStop = szalStop;
    }
}
