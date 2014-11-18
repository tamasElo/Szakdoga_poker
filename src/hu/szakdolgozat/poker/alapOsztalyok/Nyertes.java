package hu.szakdolgozat.poker.alapOsztalyok;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;

public class Nyertes {

    private String nyertesNeve;
    private Image nyertesKep;
    private Color szin;
    private Font font;
    private double kx;
    private double ky;
    private double nyertesKepSzelesseg;
    private double nyertesKepMagassag;
    private double forgat;

    public Nyertes(String nyertesNeve, Image nyertesKep) {
        this.nyertesNeve = nyertesNeve;
        this.nyertesKep = nyertesKep;
        szin = Color.black;
    }

    public void rajzol(Graphics2D g2D, ImageObserver o) {
        AffineTransform at = g2D.getTransform();
        double szovegSzelesseg, szovegMagassag;

        if (forgat != 0) {
            g2D.rotate(Math.toRadians(forgat), kx, ky);
        }
        
        String szoveg = nyertesNeve + " nyert!";
        g2D.setColor(szin);
        g2D.setFont(font);
        szovegSzelesseg = g2D.getFontMetrics().getStringBounds(szoveg, g2D).getWidth();
        szovegMagassag = g2D.getFontMetrics().getStringBounds(szoveg, g2D).getHeight();
        g2D.drawImage(nyertesKep, (int) (kx - nyertesKepSzelesseg / 2), (int) (ky - nyertesKepMagassag / 2), (int) nyertesKepSzelesseg, (int) nyertesKepMagassag, o);
        g2D.drawString(szoveg, (int) (kx - szovegSzelesseg / 2), (int) (ky + szovegMagassag / 2));
        
        if (forgat != 0) {
            g2D.setTransform(at);
        }
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setKx(double kx) {
        this.kx = kx;
    }

    public void setKy(double ky) {
        this.ky = ky;
    }

    public void setNyertesKepSzelesseg(double nyertesKepSzelesseg) {
        this.nyertesKepSzelesseg = nyertesKepSzelesseg;
    }

    public void setNyertesKepMagassag(double nyertesKepMagassag) {
        this.nyertesKepMagassag = nyertesKepMagassag;
    }

    public double getKx() {
        return kx;
    }

    public double getKy() {
        return ky;
    }

    public double getNyertesKepSzelesseg() {
        return nyertesKepSzelesseg;
    }

    public double getNyertesKepMagassag() {
        return nyertesKepMagassag;
    }

    public void setForgat(double forgat) {
        this.forgat = forgat;
    }
}
