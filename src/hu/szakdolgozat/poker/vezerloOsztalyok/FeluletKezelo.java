package hu.szakdolgozat.poker.vezerloOsztalyok;

import hu.szakdolgozat.poker.felulet.JatekMenuPanel;
import hu.szakdolgozat.poker.felulet.JatekterPanel;
import hu.szakdolgozat.poker.felulet.KepernyoKezelo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFrame;

public class FeluletKezelo {

    private DisplayMode kepernyoMod;
    private Dimension felbontas;
    private JatekMenuPanel jatekMenuPanel;
    private JatekterPanel jatekterPanel;
    private SzalVezerlo szalVezerlo;
    private JFrame pokerFrame;
    private KepernyoKezelo kepernyoKezelo;
    private byte kepernyoAllapot;
    private boolean elsimitas;
    private boolean felbontasValtozott;

    public static void main(String[] args) {
        FeluletKezelo fk = new FeluletKezelo();
        fk.kepernyoKezelo = new KepernyoKezelo(fk);
        fk.beallitasokAlkalmaz();
    }

    /**
     * Betölti a poker frame-et
     */
    public void pokerFrameBetolt() {
        if (pokerFrame != null) {
            pokerFrame.dispose();
        }
        
        pokerFrame = new JFrame();
        pokerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pokerFrame.setTitle("Póker");
        pokerFrame.setBackground(Color.black);
        pokerFrame.getContentPane().setPreferredSize(felbontas);
    }

    /**
     * Betölti a játékmenü panelt.
     *
     * @param feluletKezelo
     */
    public void jatekMenuPanelBetolt(FeluletKezelo feluletKezelo) {
        if (jatekMenuPanel != null) {
            szalVezerlo.jatekMenuAnimacioMegallit();
        }
        
        szalVezerlo = new SzalVezerlo();
        jatekMenuPanel = new JatekMenuPanel(kepernyoAllapot, kepernyoKezelo.kepernyoModLista(), elsimitas);
        jatekMenuPanel.setLayout(null);
        jatekMenuPanel.setBackground(Color.black);
        jatekMenuPanel.setSize(felbontas);
        jatekMenuPanel.setFeluletKezelo(feluletKezelo);
        jatekMenuPanel.setSzalVezerlo(szalVezerlo);
        szalVezerlo.setJatekMenuPanel(jatekMenuPanel);    
        
        if (jatekterPanel != null) {
            pokerFrame.remove(jatekterPanel);
        }
        
        pokerFrame.getContentPane().add(jatekMenuPanel);
        pokerFrame.setResizable(false);
        pokerFrame.pack();
        pokerFrame.setLocationRelativeTo(null);
        AudioLejatszo.audioLejatszas(AudioLejatszo.MENU_ZENE, true);
    }
    
    /**
     * Betölti a játék tér panelt.
     */
    public void jatekTerPanelBetolt() {
        AudioLejatszo.audioMegallit();
        szalVezerlo = new SzalVezerlo();
        jatekterPanel = new JatekterPanel();
        jatekterPanel.setBackground(Color.black);
        jatekterPanel.setSize(felbontas);
        jatekterPanel.setElsimitas(elsimitas);
        jatekterPanel.setSzalVezerlo(szalVezerlo);
        szalVezerlo.setJatekTerPanel(jatekterPanel);
        szalVezerlo.setFeluletKezelo(this);
        pokerFrame.remove(jatekMenuPanel);
        pokerFrame.getContentPane().add(BorderLayout.CENTER, jatekterPanel);
        pokerFrame.pack();
    }

    /**
     * Alkalmazza a betöltött beállításokat.
     */
    public void beallitasokAlkalmaz() {
        beallitasokBetolt();                
        
        if (!kepernyoKezelo.kepernyoModEllenorzes(kepernyoMod)) {
            kepernyoMod = new DisplayMode(1024, 768, 32, 60);
            felbontas = new Dimension(kepernyoMod.getWidth(), kepernyoMod.getHeight());
        }

        if (kepernyoAllapot == KepernyoKezelo.ABLAKOS_MOD && kepernyoKezelo.isTeljesKepernyoBekapcsolva()) {
            kepernyoKezelo.teljesKepernyoKi();
            pokerFrameBetolt();
            jatekMenuPanelBetolt(this);
            pokerFrame.setVisible(true);
        } else if (kepernyoAllapot == KepernyoKezelo.TELJES_KEPERNYO_MOD && !kepernyoKezelo.isTeljesKepernyoBekapcsolva()) {//bezárja a póker frame-et és létrehoz egy újat majd betölti a menü panelt.           
            pokerFrameBetolt();
            kepernyoKezelo.teljesKepernyoBe(kepernyoMod, pokerFrame);
            jatekMenuPanelBetolt(this);
        } else if (kepernyoAllapot == KepernyoKezelo.TELJES_KEPERNYO_MOD && kepernyoKezelo.isTeljesKepernyoBekapcsolva() && felbontasValtozott) {            
            kepernyoKezelo.teljesKepernyoKi();
            pokerFrameBetolt();
            kepernyoKezelo.teljesKepernyoBe(kepernyoMod, pokerFrame);
            jatekMenuPanelBetolt(this);
        } else if (kepernyoAllapot == KepernyoKezelo.ABLAKOS_MOD && !kepernyoKezelo.isTeljesKepernyoBekapcsolva() && felbontasValtozott || pokerFrame == null) {
            pokerFrameBetolt();
            jatekMenuPanelBetolt(this);
            pokerFrame.setVisible(true);
        } else {
            AudioLejatszo.audioLejatszas(AudioLejatszo.MENU_ZENE, true);
        }
    }

    /**
     * Betölti a beállításokat.
     */
    private void beallitasokBetolt() {
        List<String> adatok = AdatKezelo.beallitasBetolt(AdatKezelo.GRAFIKA);
        Iterator<String> itr = adatok.iterator();
        kepernyoMod = new DisplayMode(Integer.parseInt(itr.next()), Integer.parseInt(itr.next()),
                Integer.parseInt(itr.next()), Integer.parseInt(itr.next()));
        kepernyoAllapot = Byte.parseByte(itr.next());
        elsimitas = Boolean.parseBoolean(itr.next());
        felbontas = new Dimension(kepernyoMod.getWidth(), kepernyoMod.getHeight());
    }

    public void setKepernyoMod(DisplayMode kepernyoMod) {
        this.kepernyoMod = kepernyoMod;
    }

    public DisplayMode getKepernyoMod() {
        return kepernyoMod;
    }

    public byte getKepernyoAllapot() {
        return kepernyoAllapot;
    }

    public void setFelbontasValtozott(boolean felbontasValtozott) {
        this.felbontasValtozott = felbontasValtozott;
    }
}
