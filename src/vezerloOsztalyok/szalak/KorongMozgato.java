package vezerloOsztalyok.szalak;

import alapOsztalyok.Dealer;
import alapOsztalyok.Vak;
import javax.swing.ImageIcon;

public class KorongMozgato extends Thread{
    private static boolean korongokBetoltve;
    private byte dealer;
    
    public KorongMozgato(byte dealer){
        this.dealer = dealer;
    }

    private void korongokBetolt() {
        Dealer dealer = new Dealer(new ImageIcon(this.getClass().getResource("/adatFajlok/korongok/dealer.png")).getImage());
        Vak kisVak = new Vak(new ImageIcon(this.getClass().getResource("/adatFajlok/korongok/small_blind.png")).getImage());
        Vak nagyVak = new Vak(new ImageIcon(this.getClass().getResource("/adatFajlok/korongok/big_blind.png")).getImage());
    }

    @Override
    public void run() {
        if (!korongokBetoltve) {
            korongokBetolt();
        }
    }
}