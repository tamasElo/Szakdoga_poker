package vezerloOsztalyok.szalak;

import vezerloOsztalyok.SzalVezerlo;
import alapOsztalyok.Dealer;
import alapOsztalyok.Korong;
import alapOsztalyok.Vak;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import vezerloOsztalyok.SzogSzamito;

public class KorongMozgato extends Thread{
    private static boolean korongokBetoltve;
    private byte dealerJatekosSorszam;    
    private int jatekterSzelesseg;
    private int jatekterMagassag;      
    private byte jatekosokSzama;
    private SzalVezerlo szalVezerlo;
    
    public KorongMozgato(SzalVezerlo szalVezerlo){
        this.szalVezerlo = szalVezerlo;
        jatekterSzelesseg = szalVezerlo.jatekterPanelSzelesseg();
        jatekterMagassag = szalVezerlo.jatekterPanelMagassag();
        jatekosokSzama = szalVezerlo.jatekosokSzama();
    }

    private void korongokBetolt() {
        List<Korong> korongok = new ArrayList<>();
        List<Point> vegpontLista = SzogSzamito.vegpontLista(jatekosokSzama, jatekterSzelesseg, jatekterMagassag);
        Dealer dealer = new Dealer(new ImageIcon(this.getClass().getResource("/adatFajlok/korongok/dealer.png")).getImage());
        Vak kisVak = szalVezerlo.getKisVak();
        Vak nagyVak = szalVezerlo.getNagyVak();
        double meret;
        int i = dealerJatekosSorszam;
        double x, y;
        double szog;
        int elteres = szalVezerlo.jatekterPanelSzelesseg()/16;
        double veletlenForgSzog;
        
        korongok.add(dealer);
        korongok.add(kisVak);
        korongok.add(nagyVak);
        
        for (Korong korong : korongok) {
            veletlenForgSzog = Math.random()*360; //Létrehoz egy véletlen szöget 0 és 360 fok között. A létrehozott értéknek megfelelően fognak elfordulni a zsetonok.                

            if (korong instanceof Dealer) {
                meret = szalVezerlo.jatekterPanelSzelesseg()/32;
            } else {
                if(++i == jatekosokSzama) i=0;
                meret = szalVezerlo.jatekterPanelSzelesseg()*0.028125;
            }
            x = vegpontLista.get(i).x;
            y = vegpontLista.get(i).y;
            szog = SzogSzamito.szogSzamit(jatekterSzelesseg, jatekterMagassag, x, y) + 140; //Kiszámolja hogy az adott x,y pozícióban lévő pont hány fokos szöget zár be. Ehhez a szöghöz hozzá ad még 130 fokot.
            x += elteres * Math.cos(Math.toRadians(szog));//Az x koordináta pozícióját eltolja az elteres értékkel a megadott szög irányba.
            y += elteres * Math.sin(Math.toRadians(szog));
            korong.setKx(x);
            korong.setKy(y);
            korong.setKorongKepMagassag(meret);
            korong.setKorongKepSzelesseg(meret);
            korong.setForgat(veletlenForgSzog);
        }
        szalVezerlo.setKorongok(korongok);
    }

    @Override
    public void run() {
        if (!korongokBetoltve) {
            korongokBetolt();
        }
    }

    public void setDealer(byte dealer) {
        this.dealerJatekosSorszam = dealer;
    }
}
