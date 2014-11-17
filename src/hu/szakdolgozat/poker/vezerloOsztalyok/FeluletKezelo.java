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
    private DisplayMode dm;
    private JatekMenuPanel jatekMenuPanel;
    private JatekterPanel jatekterPanel;
    private SzalVezerlo szalVezerlo;
    private JFrame pokerFrame;
    private Dimension felbontas;
    private KepernyoKezelo kepernyoKezelo; 
    private byte kepFrissites;
    private byte kepernyoMod;
    private byte keparany;
    private boolean elsimitas;
    public static final byte ABLAKOS_MOD = 0;
    public static final byte TELJES_KEPERNYO_MOD = 1;
    public static final byte NORMAL_KEPERNYO = 3;
    public static final byte SZELES_KEPERNYO = 2;
    
    
    public static void main(String[] args){
        FeluletKezelo fk = new FeluletKezelo();         
        fk.kepernyoKezelo = new KepernyoKezelo(fk);
        fk.beallitasokAlkalmaz();        
    }
    
    public void pokerFrameBetolt() {
        pokerFrame = new JFrame();
        pokerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pokerFrame.setTitle("Póker");
        pokerFrame.setBackground(Color.black);
        pokerFrame.getContentPane().setPreferredSize(felbontas);     
    }
    
    public void jatekMenuPanelBetolt(FeluletKezelo feluletKezelo){
        if (jatekMenuPanel != null) {
            szalVezerlo.jatekMenuAnimacioMegallit();
        }
        
        szalVezerlo = new SzalVezerlo();
        jatekMenuPanel = new JatekMenuPanel(felbontas, kepFrissites, kepernyoMod, kepernyoKezelo.getKepernyoModok(), elsimitas);
        jatekMenuPanel.setLayout(null);
        jatekMenuPanel.setBackground(Color.black);    
        jatekMenuPanel.setSize(felbontas);     
        jatekMenuPanel.setFeluletKezelo(feluletKezelo);
        jatekMenuPanel.setSzalVezerlo(szalVezerlo);
        szalVezerlo.setJatekMenuPanel(jatekMenuPanel);
        
        if (jatekterPanel != null) {
            pokerFrame.remove(jatekterPanel);
        }
        
        pokerFrame.getContentPane().add(BorderLayout.CENTER, jatekMenuPanel);
        pokerFrame.pack();
    }
    
    public void jatekTerPanelBetolt(){ 
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
    
    public void beallitasokAlkalmaz(){
        beallitasokBetolt(); 

        dm = new DisplayMode(felbontas.width, felbontas.height, 32, kepFrissites);

        if (!kepernyoKezelo.kepernyoModEllenorzes(dm)) {
            felbontas = new Dimension(1024, 768);
            dm = new DisplayMode(felbontas.width, felbontas.height, 32, 60);
        }

        if (kepernyoMod == ABLAKOS_MOD && kepernyoKezelo.isTeljesKepernyoBekapcsolva()) {
            kepernyoKezelo.teljesKepernyoKi();
            pokerFrame.dispose();
            pokerFrameBetolt();
            jatekMenuPanelBetolt(this);
            pokerFrame.setVisible(true);
        } else if (kepernyoMod == TELJES_KEPERNYO_MOD && !kepernyoKezelo.isTeljesKepernyoBekapcsolva()) {//bezárja a póker frame-et és létrehoz egy újat majd betölti a menü panelt.           
            if (pokerFrame != null) {
                pokerFrame.dispose();
            }

            pokerFrameBetolt();
            kepernyoKezelo.teljesKepernyoBe(dm, pokerFrame);
            jatekMenuPanelBetolt(this);
        } else if (kepernyoMod == TELJES_KEPERNYO_MOD && kepernyoKezelo.isTeljesKepernyoBekapcsolva()) {
            if (pokerFrame != null) {
                kepernyoKezelo.teljesKepernyoKi();
                pokerFrame.dispose();
            }

            pokerFrameBetolt();
            kepernyoKezelo.teljesKepernyoBe(dm, pokerFrame);
            jatekMenuPanelBetolt(this);
        } else {
            if (pokerFrame != null) {
                pokerFrame.dispose();
            }

            pokerFrameBetolt();
            jatekMenuPanelBetolt(this);
            pokerFrame.setVisible(true);
        }
    }
    
    private void beallitasokBetolt(){
        List<String> adatok = AdatKezelo.grafikaBeallitasBetolt();
        Iterator itr = adatok.iterator();
        
        felbontas = new Dimension(Integer.parseInt((String)itr.next()), Integer.parseInt((String)itr.next()));
        kepFrissites = (Byte.parseByte((String)itr.next()));
        kepernyoMod = Byte.parseByte((String)itr.next());
        elsimitas = Boolean.parseBoolean((String)itr.next());
    }

    public void setKepFrissites(byte kepFrissites) {
        this.kepFrissites = kepFrissites;
    }

    public byte getKepernyoMod() {
        return kepernyoMod;
    }
}
