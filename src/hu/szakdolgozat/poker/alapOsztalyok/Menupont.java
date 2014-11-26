package hu.szakdolgozat.poker.alapOsztalyok;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Menupont {

    private String nev;
    private String betuTipus;
    private int betuMeret;
    private Color betuszin;
    private boolean passziv;
    private double kx;
    private double ky;
    private double szovegSzelesseg;
    private double szovegMagassag;

    public Menupont(String nev) {
        this.nev = nev;
    }

    public void rajzol(Graphics2D g2D) {
        if (passziv) {
            betuszin = Color.GRAY;
        } else {
            betuszin = Color.BLACK;
        }
        
        Font font = new Font(betuTipus, 1, betuMeret);
        g2D.setFont(font);
        szovegSzelesseg = g2D.getFontMetrics().getStringBounds(nev, g2D).getWidth();
        szovegMagassag = g2D.getFontMetrics().getStringBounds(nev, g2D).getHeight();
        g2D.setColor(betuszin);
        g2D.drawString(nev, (int) (kx - szovegSzelesseg / 2), (int) (ky + szovegMagassag / 2)); //A szöveg rajzolásátnak y pontja alapesetben a bal alsó sarok, ezért hogy megkapjuk ky-t, hozzá kell adni a szöveg magasságának a felét.
    }

    public void setBetuTipus(String betuTipus) {
        this.betuTipus = betuTipus;
    }

    public void setBetuMeret(int betuMeret) {
        this.betuMeret = betuMeret;
    }

    public void setPassziv(boolean passziv) {
        this.passziv = passziv;
    }

    public void setKx(double kx) {
        this.kx = kx;
    }

    public void setKy(double ky) {
        this.ky = ky;
    }

    public String getNev() {
        return nev;
    }

    public double getKx() {
        return kx;
    }

    public double getKy() {
        return ky;
    }

    public int getBetuMeret() {
        return betuMeret;
    }

    public double getSzovegSzelesseg() {
        return szovegSzelesseg;
    }

    public double getSzovegMagassag() {
        return szovegMagassag;
    }

    public boolean isPassziv() {
        return passziv;
    }
}
