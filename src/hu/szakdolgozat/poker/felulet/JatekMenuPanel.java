package hu.szakdolgozat.poker.felulet;

import hu.szakdolgozat.poker.vezerloOsztalyok.KepernyoKezelo;
import hu.szakdolgozat.poker.alapOsztalyok.Menupont;
import hu.szakdolgozat.poker.vezerloOsztalyok.AdatKezelo;
import hu.szakdolgozat.poker.vezerloOsztalyok.FeluletKezelo;
import hu.szakdolgozat.poker.vezerloOsztalyok.SzalVezerlo;
import hu.szakdolgozat.poker.vezerloOsztalyok.AudioLejatszo;
import java.awt.Dimension;
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
import java.util.Map;
import javax.swing.JOptionPane;

public class JatekMenuPanel extends JPanel{
    private FeluletKezelo feluletKezelo;
    private SzalVezerlo szalVezerlo; 
    private Map<String, List<Double>> xmlAdatok;
    private Iterator<Double> itr;
    private JScrollPane scpPokerSzabaly;
    private List<Menupont> menupontok;  
    private Menupont aktMenupont;
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
    private int nagyBetuMeret;
    private int kisBetuMeret;
    private static final byte[] NAGY_VAK_ERTEKEK = {2, 10, 20, 30, 40, 50};
    private static final String JATEK_FOLYTAT = "Játék folytat";
    private static final String UJ_JATEK = "Új játék";
    private static final String BEALLITASOK = "Beállítások";
    private static final String SEGITSEG = "Segítség";
    private static final String KILEPES = "Kilépés";
    private static final String ALKALMAZ = "Alkalmaz";
    private static final String VISSZA = "Vissza";

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
        String[] elemNevek = {"JatekFolytat", "UjJatek", "Beallitasok", "Segitseg", "Kilepes"};
        String[] mpontNevek = {JATEK_FOLYTAT, UJ_JATEK, BEALLITASOK, SEGITSEG, KILEPES};
        
        if(isAncestorOf(scpPokerSzabaly)) remove(scpPokerSzabaly);
        
        for (JComponent beallitasKomponens : beallitasKomponensek) {
            remove(beallitasKomponens);
        }
        
        beallitasKomponensek.clear();
        menupontok.clear();
        
        for (byte i = 0; i < elemNevek.length; i++) {
            menupontHozzaad(elemNevek[i], mpontNevek[i]);
        }

        repaint();
    }

    /**
     * Létrehozza a beállítások menü opciókat.
     */
    private void beallitasokMenuBetolt() {
        kepernyoMod = feluletKezelo.getKepernyoMod();
        
        JLabel lblGrafikaAudio = new JLabel("Grafika és audió");
        lblGrafikaAudio.setName("lblGrafikaAudio");

        JLabel lblFelbontas = new JLabel("Felbontás");
        lblFelbontas.setName("lblFelbontas");
        
        JLabel lblJatekmenet = new JLabel("Játékmenet");
        lblJatekmenet.setName("lblJatekmenet");
        
        JLabel lblNev = new JLabel("Név");
        lblNev.setName("lblNev");
        
        JLabel lblJatekosokSzama = new JLabel("Játékosok száma");
        lblJatekosokSzama.setName("lblJatekosokSzama");
        
        JLabel lblOsszeg = new JLabel("Összeg");
        lblOsszeg.setName("lblOsszeg");
        
        JLabel lblNagyVakErtek = new JLabel("Nagy vak értéke");
        lblNagyVakErtek.setName("lblNagyVakErtek");
        
        JLabel lblVakEmeles = new JLabel("Vakok növelése(x körönként)");
        lblVakEmeles.setName("lblVakEmeles");
        
        jatekmenetBeallitasokBetolt();
        
        sliFelbontas = new JSlider();
        sliFelbontas.setName("sliFelbontas");
        sliFelbontas.setMaximum(kepernyoModok.size() - 1);
        DisplayMode dmode;
        
        for (byte i = 0; i < kepernyoModok.size(); i++) {
            dmode = kepernyoModok.get(i);

            if (dmode.equals(kepernyoMod)) {
                sliFelbontas.setValue(i);
            }
        }
        
        lblFelbontasE = new JLabel(kepernyoMod.getWidth() + " x " + kepernyoMod.getHeight() + " " + kepernyoMod.getRefreshRate() + "Hz");
        lblFelbontasE.setName("lblFelbontasE");
        
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
        rbtnAblakos.setName("rbtnAblakos");
        rbtnTeljesKepernyo = new JRadioButton("Teljes");   
        rbtnTeljesKepernyo.setName("rbtnTeljesKepernyo");
        bgrKepernyoMod.add(rbtnAblakos);
        bgrKepernyoMod.add(rbtnTeljesKepernyo);     
        
        if (kepernyoAllapot == KepernyoKezelo.TELJES_KEPERNYO_MOD) {
            rbtnTeljesKepernyo.setSelected(true);
        } else {
            rbtnAblakos.setSelected(true);
        }

        cbElsimitas = new JCheckBox("Élsimítás");
        cbElsimitas.setName("cbElsimitas");
        cbElsimitas.setSelected(elsimitas);

        menuZene = AudioLejatszo.isMenuZene();
        cbMenuZene = new JCheckBox("Menü Zene");
        cbMenuZene.setName("cbMenuZene");
        cbMenuZene.setSelected(menuZene);
        
        hangok = AudioLejatszo.isHangok();
        cbHang = new JCheckBox("Hangok");
        cbHang.setName("cbHang");
        cbHang.setSelected(hangok);
        
        txtNev = new JTextField(jatekosNev);
        txtNev.setName("txtNev");
        
        sliJatekosokSzama = new JSlider(2, 5);
        sliJatekosokSzama.setName("sliJatekosokSzama");
        sliJatekosokSzama.setValue(jatekosokSzama);
        lblJatekosokSzamaE = new JLabel(String.valueOf(sliJatekosokSzama.getValue())); 
        lblJatekosokSzamaE.setName("lblJatekosokSzamaE");
        sliJatekosokSzama.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent evt) {
                jatekosokSzama = (byte) sliJatekosokSzama.getValue();
                lblJatekosokSzamaE.setText(String.valueOf(jatekosokSzama));
            }
        });
        
        osszegNovekmeny = 500;
        sliOsszeg = new JSlider(1, 7);
        sliOsszeg.setName("sliOsszeg");
        sliOsszeg.setValue(osszeg / 500);
        lblOsszegE = new JLabel(String.valueOf(sliOsszeg.getValue() * osszegNovekmeny));
        lblOsszegE.setName("lblOsszegE");

        sliOsszeg.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent evt) {
                osszeg = sliOsszeg.getValue() * osszegNovekmeny;
                lblOsszegE.setText(String.valueOf(osszeg));
            }
        });

        sliNagyVakErtek = new JSlider();
        sliNagyVakErtek.setName("sliNagyVakErtek");
        sliNagyVakErtek.setMaximum(5);
        
        for (byte i = 0; i < NAGY_VAK_ERTEKEK.length; i++) {
            if (nagyVakErtek == NAGY_VAK_ERTEKEK[i]) {
                sliNagyVakErtek.setValue(i);
            }
        }
        
        lblNagyVakErtekE = new JLabel(String.valueOf(NAGY_VAK_ERTEKEK[sliNagyVakErtek.getValue()]));
        lblNagyVakErtekE.setName("lblNagyVakErtekE");
        
        sliNagyVakErtek.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent evt) {
                nagyVakErtek = NAGY_VAK_ERTEKEK[sliNagyVakErtek.getValue()];
                lblNagyVakErtekE.setText(String.valueOf(nagyVakErtek));
            }
        });
        
        sliVakEmeles = new JSlider();
        sliVakEmeles.setName("sliVakEmeles");
        sliVakEmeles.setMaximum(10);
        sliVakEmeles.setValue(vakEmeles);
        lblVakEmelesE = new JLabel(String.valueOf(sliVakEmeles.getValue()));
        lblVakEmelesE.setName("lblVakEmelesE"); 
                
        sliVakEmeles.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent evt) {
                vakEmeles = (byte) sliVakEmeles.getValue();
                lblVakEmelesE.setText(String.valueOf(vakEmeles));
            }
        });                
        
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
        xmlAdatok = AdatKezelo.aranyErtekekBetolt("Komponensek", new Dimension(szelesseg, magassag));        
        
        for (JComponent beallitasKomponens : beallitasKomponensek) {
            itr = xmlAdatok.get(beallitasKomponens.getName()).iterator();
            beallitasKomponens.setOpaque(false);
            beallitasKomponens.setBounds((int) (double) itr.next(), (int) (double) itr.next(),
                    (int) (double) itr.next(), (int) (double) itr.next());
            beallitasKomponens.setFont(new Font("Copperplate Gothic Bold", 0, (int) (double) itr.next()));

            if (beallitasKomponens instanceof AbstractButton) {
                aButton = (AbstractButton) beallitasKomponens;
                aButton.setHorizontalTextPosition(SwingConstants.LEFT);
                aButton.setFocusPainted(false);
            }

            add(beallitasKomponens);
        }

        menupontok.clear();
        menupontHozzaad(VISSZA, "Vissza");        
        menupontHozzaad(ALKALMAZ, "Alkalmaz");
        repaint();  
    }
    
    /**
     * Betölti a Segítség menü elemeit.
     */
    private void segitsegMenuBetolt() {
        xmlAdatok = AdatKezelo.aranyErtekekBetolt("Komponensek", new Dimension(szelesseg, magassag));
        itr = xmlAdatok.get("scpPokerSzabaly").iterator();
        JEditorPane epPokerSzabaly = new JEditorPane();
        URL urlPokerSzabaly = this.getClass().getResource("/hu/szakdolgozat/poker/adatFajlok/jatekMenu/A_Texas_holdem_menete.html");
        scpPokerSzabaly = new JScrollPane(epPokerSzabaly);
        scpPokerSzabaly.setBounds((int) (double) itr.next(), (int) (double) itr.next(), (int) (double) itr.next(), (int) (double) itr.next());
        scpPokerSzabaly.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scpPokerSzabaly.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        try {
            epPokerSzabaly.setPage(urlPokerSzabaly);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

        add(scpPokerSzabaly);        
        menupontok.clear();
        menupontHozzaad(VISSZA, "Vissza");
        repaint();
    }

    /**
     * Hozzáad egy menüpontot a panelhez.
     * 
     * @param elemNev
     * @param menupontNev 
     */
    private void menupontHozzaad(String elemNev, String menupontNev) {
        xmlAdatok = AdatKezelo.aranyErtekekBetolt("Menupontok", new Dimension(szelesseg, magassag));
        itr = xmlAdatok.get(elemNev).iterator();
        Menupont menupont;
        menupont = new Menupont(menupontNev);
        menupont.setKx(itr.next());
        menupont.setKy(itr.next());
        
        if (menupontNev.equals(JATEK_FOLYTAT) && !AdatKezelo.jatekAllasEllenorzes()) {
            menupont.setBetuMeret(kisBetuMeret);
            menupont.setBetuTipus("Copperplate Gothic Bold");
            menupont.setPassziv(true);
        } else {
            menupont.setBetuMeret(kisBetuMeret);
            menupont.setBetuTipus("Copperplate Gothic Bold");
        }
        
        menupontok.add(menupont);
    }
    
    /**
     * Betölti az játékmenet beállításokat.
     */
    private void jatekmenetBeallitasokBetolt() {        
        List<String> adatok = AdatKezelo.beallitasBetolt(AdatKezelo.JATEKMENET);
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
        xmlAdatok = AdatKezelo.aranyErtekekBetolt("Menupontok", new Dimension(szelesseg, magassag));
        menupontok = new ArrayList<>();
        kisBetuMeret = (int) (double) xmlAdatok.get("BetuMeret").get(0);
        nagyBetuMeret = (int) (double) xmlAdatok.get("BetuMeret").get(1);
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

            if (negyszog.contains(me.getX(), me.getY()) && !menupont.isPassziv()) {
                menupontNev = menupont.getNev();
                aktMenupont = menupont;
                menupont.setBetuMeret(nagyBetuMeret);
            } else {
                menupont.setBetuMeret(kisBetuMeret);
            }
        }

        repaint();
    }
    
    private void jatekMenuMouseReleased(MouseEvent me) {
        switch (menupontNev) {
            case JATEK_FOLYTAT:
                if (!aktMenupont.isPassziv()) {
                    feluletKezelo.jatekFolytat();
                }
                
                break;
            case UJ_JATEK:
                szalVezerlo.jatekMenuAnimacioLegallit();
                feluletKezelo.jatekTerPanelBetolt(false);
                break;
            case BEALLITASOK:
                beallitasokMenuBetolt();
                break;
            case SEGITSEG:
                segitsegMenuBetolt();
                break;
            case VISSZA:
                foMenuBetolt();
                break;
            case ALKALMAZ:
                elsimitas = cbElsimitas.isSelected();                
                feluletKezelo.setFelbontasValtozott(!feluletKezelo.getKepernyoMod().equals(kepernyoMod));                
                kepernyoAllapot = rbtnTeljesKepernyo.isSelected() ? KepernyoKezelo.TELJES_KEPERNYO_MOD : KepernyoKezelo.ABLAKOS_MOD;   
                menuZene = cbMenuZene.isSelected();
                hangok = cbHang.isSelected();
                jatekosNev = txtNev.getText();
                
                AdatKezelo.grafikaBeallitasMent(kepernyoMod, kepernyoAllapot, elsimitas);
                AdatKezelo.grafikaBeallitasMent(kepernyoMod, kepernyoAllapot, elsimitas);
                AdatKezelo.audioBeallitasMent(menuZene, hangok);
                AdatKezelo.audioBeallitasMent(menuZene, hangok);
                AdatKezelo.jatekmenetBeallitasMent(jatekosNev, jatekosokSzama, osszeg, nagyVakErtek, vakEmeles);
                AdatKezelo.jatekmenetBeallitasMent(jatekosNev, jatekosokSzama, osszeg, nagyVakErtek, vakEmeles);
                feluletKezelo.beallitasokAlkalmaz();
                foMenuBetolt();
                break;
            case KILEPES:
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
