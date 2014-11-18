package hu.szakdolgozat.poker.felulet;

import hu.szakdolgozat.poker.alapOsztalyok.Menupont;
import hu.szakdolgozat.poker.vezerloOsztalyok.AdatKezelo;
import hu.szakdolgozat.poker.vezerloOsztalyok.FeluletKezelo;
import hu.szakdolgozat.poker.vezerloOsztalyok.SzalVezerlo;
import hu.szakdolgozat.poker.vezerloOsztalyok.AudioLejatszo;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.DisplayMode;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.File;
import java.io.IOException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.net.URL;
import javax.swing.JOptionPane;

public class JatekMenuPanel extends JPanel{
    private FeluletKezelo feluletKezelo;
    private SzalVezerlo szalVezerlo; 
    private JScrollPane scpPokerSzabaly;
    private List<Menupont> menupontok;  
    private List<JComponent> beallitasKomponensek;
    private JTextField txtNev;
    private JLabel lblFelbontasE;
    private JLabel lblJatekosokSzamaE;
    private JLabel lblOsszegE;
    private JLabel lblNagyVakErtekE;
    private JLabel lblVakEmelesE;
    private JSlider sliFelbontas;
    private JSlider sliJatekosokSzama;
    private JSlider sliOsszeg ;
    private JSlider sliNagyVakErtek;
    private JSlider sliVakEmeles;
    private JRadioButton rbtnAblakos;
    private JRadioButton rbtnTeljesKepernyo;
    private JCheckBox cbElsimitas;
    private JCheckBox cbMenuZene;
    private JCheckBox cbHang;
    private DisplayMode kepernyoMod;
    private byte kepernyoAllapot;
    private List<DisplayMode> kepernyoModok;
    private boolean elsimitas;
    private byte[] nagyVakErtekek = {2, 10, 20, 30, 40, 50}; 
    private String jatekosNev;
    private byte jatekosokSzama;
    private int osszeg;
    private int osszegNovekmeny;
    private byte nagyVakErtek;
    private byte vakEmeles;
    private boolean menuZene;
    private boolean hangok;
    private Image eloter;
    private Image hatter;
    private int szelesseg;
    private int magassag;
    private String menupontNev;

    public JatekMenuPanel(byte kepernyoAllapot, List<DisplayMode> kepernyoModok, boolean elsimitas) {
        this.kepernyoAllapot = kepernyoAllapot;
        this.kepernyoModok = kepernyoModok;
        this.elsimitas = elsimitas;
        inicializal();
    }   
    
    private void inicializal() {  
        addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent ae) {
                jatekMenuAncestorAdded(ae);
            }

            @Override
            public void ancestorRemoved(AncestorEvent ae) {
            }

            @Override
            public void ancestorMoved(AncestorEvent ae) {
            }
        });
        
        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent me) {
            }

            @Override
            public void mousePressed(MouseEvent me) {                
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                jatekMenuMouseReleased(me);
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        });
        
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                if(menupontok != null) jatekMenuMouseMoved(evt);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        
        if (elsimitas) {
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        }
        
        g2D.drawImage(hatter, 0, 0, szelesseg, magassag, this);
        szalVezerlo.kartyalapokRajzol(g2D);
        g2D.drawImage(eloter, 0, 0, szelesseg, magassag, this);

        if (menupontok != null) {
            for (Menupont menupont : menupontok) {
                menupont.rajzol(g2D);
            }
        }
    }

    /**
     * Létrehozza a menüpontokat és beállítja helyzetüket.
     */
    private void foMenuBetolt() {
        String[] menupontNevek = {"Játék folytatása", "Új játék", "Beállítások", "Segítség", "Kilépés"};
        double novekmeny = szelesseg / 30;
        double kx = szelesseg / 2, ky = magassag / 2 - (menupontNevek.length / 2) * novekmeny;

        
        if(isAncestorOf(scpPokerSzabaly)) remove(scpPokerSzabaly);
        
        for (JComponent beallitasKomponens : beallitasKomponensek) {
            remove(beallitasKomponens);
        }
        
        beallitasKomponensek.clear();
        menupontok.clear();
        
        for (String mpontNev : menupontNevek) {
            menupontHozzaad(kx, ky, mpontNev);
            ky += novekmeny;
        }

        repaint();
    }

    /**
     * Létrehozza a beállítások menü opciókat.
     */
    private void beallitasokMenuBetolt() {
        kepernyoMod = feluletKezelo.getKepernyoMod();
        JLabel lblFelbontas = new JLabel("Felbontás");
        JLabel lblGrafikaAudio = new JLabel("Grafika és audió");
        JLabel lblJatekmenet = new JLabel("Játékmenet");
        JLabel lblNev = new JLabel("Név");
        JLabel lblJatekosokSzama = new JLabel("Játékosok száma");
        JLabel lblOsszeg = new JLabel("Összeg");
        JLabel lblNagyVakErtek = new JLabel("Nagy vak értéke");
        JLabel lblVakEmeles = new JLabel("Vakok növelése(x körönként)"); 
        
        jatekmenetBeallitasokBetolt();
        
        sliFelbontas = new JSlider();
        sliFelbontas.setMaximum(kepernyoModok.size() - 1);
        DisplayMode dmode;
        
        for (byte i = 0; i < kepernyoModok.size(); i++) {
            dmode = kepernyoModok.get(i);

            if (dmode.equals(kepernyoMod)) {
                sliFelbontas.setValue(i);
            }
        }
        
        lblFelbontasE = new JLabel(kepernyoMod.getWidth() + " x " + kepernyoMod.getHeight() + " " + kepernyoMod.getRefreshRate() + "Hz");
        
        sliFelbontas.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent evt) {
                int horizontalisFelbontas = kepernyoModok.get(sliFelbontas.getValue()).getWidth(), 
                        vertikalisFelbontas = kepernyoModok.get(sliFelbontas.getValue()).getHeight();
                byte kepFrissites = (byte) kepernyoModok.get(sliFelbontas.getValue()).getRefreshRate();
                kepernyoMod = new DisplayMode(horizontalisFelbontas, vertikalisFelbontas, kepernyoMod.getBitDepth(), kepFrissites);
                lblFelbontasE.setText(horizontalisFelbontas + " x " + vertikalisFelbontas + " " + kepFrissites + "Hz");
            }
        });        
                
        ButtonGroup bgrKepernyoMod = new ButtonGroup();
        rbtnAblakos = new JRadioButton("Ablakos");
        rbtnTeljesKepernyo = new JRadioButton("Teljes");        
        bgrKepernyoMod.add(rbtnAblakos);
        bgrKepernyoMod.add(rbtnTeljesKepernyo);     
        
        if (kepernyoAllapot == FeluletKezelo.TELJES_KEPERNYO_MOD) {
            rbtnTeljesKepernyo.setSelected(true);
        } else {
            rbtnAblakos.setSelected(true);
        }
        
        cbElsimitas = new JCheckBox("Élsimítás");
        cbElsimitas.setSelected(elsimitas);
        
        menuZene = AudioLejatszo.isMenuZene();
        cbMenuZene = new JCheckBox("Menü Zene");
        cbMenuZene.setSelected(menuZene);
        
        hangok = AudioLejatszo.isHangok();
        cbHang = new JCheckBox("Hangok");
        cbHang.setSelected(hangok);
        
        txtNev = new JTextField(jatekosNev);
        
        sliJatekosokSzama = new JSlider(2, 5);
        sliJatekosokSzama.setValue(jatekosokSzama);
        lblJatekosokSzamaE = new JLabel(String.valueOf(sliJatekosokSzama.getValue())); 
        sliJatekosokSzama.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent evt) {
                jatekosokSzama = (byte) sliJatekosokSzama.getValue();
                lblJatekosokSzamaE.setText(String.valueOf(jatekosokSzama));
            }
        });
        
        osszegNovekmeny = 500;
        sliOsszeg = new JSlider(1, 7);
        sliOsszeg.setValue(osszeg / 500);
        lblOsszegE = new JLabel(String.valueOf(sliOsszeg.getValue() * osszegNovekmeny));

        sliOsszeg.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent evt) {
                osszeg = sliOsszeg.getValue() * osszegNovekmeny;
                lblOsszegE.setText(String.valueOf(osszeg));
            }
        });

        sliNagyVakErtek = new JSlider();
        sliNagyVakErtek.setMaximum(5);
        
        for (byte i = 0; i < nagyVakErtekek.length; i++) {
            if (nagyVakErtek == nagyVakErtekek[i]) {
                sliNagyVakErtek.setValue(i);
            }
        }
        
        lblNagyVakErtekE = new JLabel(String.valueOf(nagyVakErtekek[sliNagyVakErtek.getValue()]));
        
        sliNagyVakErtek.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent evt) {
                nagyVakErtek = nagyVakErtekek[sliNagyVakErtek.getValue()];
                lblNagyVakErtekE.setText(String.valueOf(nagyVakErtek));
            }
        });
        
        sliVakEmeles = new JSlider();
        sliVakEmeles.setMaximum(10);
        sliVakEmeles.setValue(vakEmeles);
        lblVakEmelesE = new JLabel(String.valueOf(sliVakEmeles.getValue()));
                
        sliVakEmeles.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent evt) {
                vakEmeles = (byte) sliVakEmeles.getValue();
                lblVakEmelesE.setText(String.valueOf(vakEmeles));
            }
        });        
        
        // ezeket majd egy xml fájlból kell beolvasni és így be lehet tenni ciklusba.
        lblGrafikaAudio.setBounds(405, 150, 300, 30);
        lblFelbontas.setBounds(405, 200, 100, 30);
        lblFelbontasE.setBounds(595, 200, 150, 30);
        sliFelbontas.setBounds(490, 202, 100, 30);
        rbtnAblakos.setBounds(400, 250, 100, 30);
        rbtnTeljesKepernyo.setBounds(500, 250, 100, 30);
        cbElsimitas.setBounds(400, 300, 100, 30);
        cbMenuZene.setBounds(400, 350, 150, 30);
        cbHang.setBounds(400, 400, 100, 30);
        lblJatekmenet.setBounds(405, 500, 300, 30);
        lblNev.setBounds(405, 550, 50, 30);
        txtNev.setBounds(450, 550, 100, 30);
        lblJatekosokSzama.setBounds(405, 600, 150, 30);
        lblOsszeg.setBounds(405, 650, 100, 30);
        lblNagyVakErtek.setBounds(405, 700, 150, 30);
        lblVakEmeles.setBounds(405, 750, 250, 30);
        sliJatekosokSzama.setBounds(545, 602, 100, 30);
        sliOsszeg.setBounds(465, 652, 100, 30);
        sliNagyVakErtek.setBounds(540, 702, 100, 30);
        sliVakEmeles.setBounds(645, 752, 100, 30);
        lblJatekosokSzamaE.setBounds(650, 600, 30, 30);
        lblOsszegE.setBounds(570, 650, 70, 30);
        lblNagyVakErtekE.setBounds(645, 700, 30, 30);
        lblVakEmelesE.setBounds(750, 750, 30, 30); 
        
        beallitasKomponensek.add(lblGrafikaAudio);
        beallitasKomponensek.add(lblFelbontas);
        beallitasKomponensek.add(lblFelbontasE);
        beallitasKomponensek.add(sliFelbontas);
        beallitasKomponensek.add(rbtnAblakos);
        beallitasKomponensek.add(rbtnTeljesKepernyo);
        beallitasKomponensek.add(cbElsimitas);
        beallitasKomponensek.add(cbMenuZene);
        beallitasKomponensek.add(cbHang);
        beallitasKomponensek.add(lblJatekmenet);
        beallitasKomponensek.add(lblNev);
        beallitasKomponensek.add(lblJatekosokSzama);
        beallitasKomponensek.add(lblOsszeg);
        beallitasKomponensek.add(lblNagyVakErtek);
        beallitasKomponensek.add(lblVakEmeles);
        beallitasKomponensek.add(txtNev);
        beallitasKomponensek.add(sliJatekosokSzama);
        beallitasKomponensek.add(sliOsszeg);
        beallitasKomponensek.add(sliNagyVakErtek);
        beallitasKomponensek.add(sliVakEmeles);
        beallitasKomponensek.add(lblJatekosokSzamaE);
        beallitasKomponensek.add(lblOsszegE);
        beallitasKomponensek.add(lblNagyVakErtekE);
        beallitasKomponensek.add(lblVakEmelesE);

        AbstractButton aButton;

        for (JComponent beallitasKomponens : beallitasKomponensek) {
            beallitasKomponens.setOpaque(false);

            if (beallitasKomponens.equals(lblGrafikaAudio) || beallitasKomponens.equals(lblJatekmenet)) {
                beallitasKomponens.setFont(new Font("Copperplate Gothic Bold", 0, 20));
            } else {
                beallitasKomponens.setFont(new Font("Copperplate Gothic Bold", 0, 14));
            }

            if (beallitasKomponens instanceof AbstractButton) {
                aButton = (AbstractButton) beallitasKomponens;
                aButton.setHorizontalTextPosition(SwingConstants.LEFT);
                aButton.setFocusPainted(false);
            }
            
            add(beallitasKomponens);
        }
        
        menupontok.clear();
        menupontHozzaad(szelesseg / 1.5, magassag / 1.4, "Vissza");        
        menupontHozzaad(szelesseg / 3, magassag / 1.4, "Alkalmaz");
        repaint();  
    }
    
    /**
     * Betölti a Segítség menü elemeit.
     */
    private void segitsegMenuBetolt() {
        JEditorPane epPokerSzabaly = new JEditorPane();
        URL urlPokerSzabaly = this.getClass().getResource("/hu/szakdolgozat/poker/adatFajlok/jatekMenu/A_Texas_holdem_menete.html");
        scpPokerSzabaly = new JScrollPane(epPokerSzabaly);
        scpPokerSzabaly.setBounds(szelesseg / 4, szelesseg / 8, szelesseg / 2, (int) (szelesseg * 0.375));
        scpPokerSzabaly.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scpPokerSzabaly.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        try {
            epPokerSzabaly.setPage(urlPokerSzabaly);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

        add(scpPokerSzabaly);        
        menupontok.clear();
        menupontHozzaad(szelesseg / 2, magassag / 1.4, "Vissza");
        repaint();
    }
    
    /**
     * Hozzáad egy menüpontot a panelhez.
     * 
     * @param kx
     * @param ky
     * @param nev 
     */
    private void menupontHozzaad(double kx, double ky, String nev){
        Menupont menupont;
        menupont = new Menupont(nev);
        menupont.setBetuMeret((int) (szelesseg / 64));
        menupont.setBetuTipus("Copperplate Gothic Bold");
        menupont.setKx(kx);
        menupont.setKy(ky);
        menupontok.add(menupont);
    }
    
    /**
     * Betölti az játékmenet beállításokat.
     */
    private void jatekmenetBeallitasokBetolt() {        
        List<String> adatok = AdatKezelo.beallitasBetolt(AdatKezelo.JATEKMENET);;
        Iterator<String> itr = adatok.iterator();       
        jatekosNev = itr.next();
        jatekosokSzama = Byte.parseByte(itr.next());
        osszeg = Integer.parseInt(itr.next());
        nagyVakErtek = Byte.parseByte(itr.next());
        vakEmeles = Byte.parseByte(itr.next());          
    }
    
    /**
     * Fájlból tölti be a betű típust.
     */
    private void fontBetoltes(){
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        URL urlFont = this.getClass().getResource("/hu/szakdolgozat/poker/adatFajlok/jatekMenu/COPRGTB.ttf");

        try {
            env.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(urlFont.getFile())));
        } catch (FontFormatException | IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
        
    private void jatekMenuAncestorAdded(AncestorEvent ae) {    
        eloter = new ImageIcon(this.getClass().getResource("/hu/szakdolgozat/poker/adatFajlok/jatekMenu/eloter.png")).getImage();
        hatter = new ImageIcon(this.getClass().getResource("/hu/szakdolgozat/poker/adatFajlok/jatekMenu/hatter.png")).getImage(); 
        kepernyoMod = feluletKezelo.getKepernyoMod();       
        szelesseg = kepernyoMod.getWidth();
        magassag = kepernyoMod.getHeight();
        menupontok = new ArrayList<>();
        beallitasKomponensek = new ArrayList<>();
        szalVezerlo.jatekMenuAnimacioIndit();
        fontBetoltes();
        foMenuBetolt();
    }

    private void jatekMenuMouseMoved(MouseEvent me) {
        Rectangle2D negyszog;
        double kx, ky;
        double szovegSzelesseg, szovegMagassag;
        menupontNev = "";

        for (Menupont menupont : menupontok) {
            kx = menupont.getKx();
            ky = menupont.getKy();
            szovegSzelesseg = menupont.getSzovegSzelesseg();
            szovegMagassag = menupont.getSzovegMagassag();
            negyszog = new Rectangle2D.Double(kx - szovegSzelesseg / 2, ky - szovegMagassag / 2, szovegSzelesseg, szovegMagassag);

            if (negyszog.contains(me.getX(), me.getY())) {
                menupontNev = menupont.getNev();
                menupont.setBetuMeret((int) (szelesseg * 0.021875));
            } else {
                menupont.setBetuMeret((int) (szelesseg / 64));
            }
        }

        repaint();
    }
    
    private void jatekMenuMouseReleased(MouseEvent me) {
        switch (menupontNev) {
            case "Új játék":
                szalVezerlo.jatekMenuAnimacioMegallit();
                feluletKezelo.jatekTerPanelBetolt();
                break;
            case "Beállítások":
                beallitasokMenuBetolt();
                break;
            case "Segítség":
                segitsegMenuBetolt();
                break;
            case "Vissza":
                foMenuBetolt();
                break;
            case "Alkalmaz":
                elsimitas = cbElsimitas.isSelected();                
                feluletKezelo.setFelbontasValtozott(!feluletKezelo.getKepernyoMod().equals(kepernyoMod));                
                kepernyoAllapot = rbtnTeljesKepernyo.isSelected() ? FeluletKezelo.TELJES_KEPERNYO_MOD : FeluletKezelo.ABLAKOS_MOD;   
                menuZene = cbMenuZene.isSelected();
                hangok = cbHang.isSelected();
                jatekosNev = txtNev.getText();
                
                AdatKezelo.grafikaBeallitasMent(kepernyoMod, kepernyoAllapot, elsimitas);
                AdatKezelo.audioBeallitasMent(menuZene, hangok);
                AdatKezelo.jatekmenetBeallitasMent(jatekosNev, jatekosokSzama, osszeg, nagyVakErtek, vakEmeles);
                feluletKezelo.beallitasokAlkalmaz();
                foMenuBetolt();
                break;
            case "Kilépés":
                System.exit(0);
        }
    }

    public void setFeluletKezelo(FeluletKezelo feluletKezelo) {
        this.feluletKezelo = feluletKezelo;
    }
    
    public void setSzalVezerlo(SzalVezerlo szalVezerlo) {
        this.szalVezerlo = szalVezerlo;
    }
}
