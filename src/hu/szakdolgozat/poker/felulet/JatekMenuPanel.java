package hu.szakdolgozat.poker.felulet;

import hu.szakdolgozat.poker.alapOsztalyok.Menupont;
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
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import hu.szakdolgozat.poker.vezerloOsztalyok.FeluletKezelo;
import hu.szakdolgozat.poker.vezerloOsztalyok.SzalVezerlo;

public class JatekMenuPanel extends JPanel{
    private SzalVezerlo szalVezerlo; 
    private JScrollPane scpPokerSzabaly;
    private List<Menupont> menupontok;  
    private List<JComponent> beallitasKomponensek;    
    private JLabel lblFelbontasErtek;
    private JTextField txtNev;
    private JSlider sliFelbontas;
    private JSlider sliJatekosokSzama;
    private JSlider sliOsszeg ;
    private JSlider sliNagyVakErtek;
    private JSlider sliVakEmeles;
    private JRadioButton rbtnSzeles;
    private JRadioButton rbtnAblakos;
    private JRadioButton rbtnTeljesKepernyo;
    private JCheckBox cbElsimitas;
    private JCheckBox cbMenuZene;
    private JCheckBox cbHang;
    private JRadioButton rbtnNormal;
    private Image eloter;
    private Image hatter;
    private int szelesseg;
    private int magassag;
    private String menupontNev;

    public JatekMenuPanel() {
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
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
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
        ButtonGroup bgrKeparany = new ButtonGroup();
        ButtonGroup bgrKepernyoMod = new ButtonGroup();
        JLabel lblFelbontas = new JLabel("Felbontás");
        JLabel lblGrafikaAudio = new JLabel("Grafika és audió");
        JLabel lblJatekmenet = new JLabel("Játékmenet");
        JLabel lblNev = new JLabel("Név");
        JLabel lblJatekosokSzama = new JLabel("Játékosok száma");
        JLabel lblOsszeg = new JLabel("Összeg");
        JLabel lblNagyVakErtek = new JLabel("Nagy vak értéke");
        JLabel lblVakEmeles = new JLabel("Vakok növelése(x körönként)");
        lblFelbontasErtek = new JLabel("1280 x 960");
        sliFelbontas = new JSlider();
        sliJatekosokSzama = new JSlider();
        sliOsszeg = new JSlider();
        sliNagyVakErtek = new JSlider();
        sliVakEmeles = new JSlider();
        rbtnSzeles = new JRadioButton("Széles");
        rbtnNormal = new JRadioButton("Normál");
        rbtnAblakos = new JRadioButton("Ablakos");
        rbtnTeljesKepernyo = new JRadioButton("Teljes");
        cbElsimitas = new JCheckBox("Élsimítás");
        cbMenuZene = new JCheckBox("Menü Zene");
        cbHang = new JCheckBox("Hangok");
        txtNev = new JTextField("Tomi");

        sliFelbontas.setMaximum(30);
        sliFelbontas.setValue(2);

        // ezeket majd egy xml fájlból kell beolvasni és így be lehet tenni ciklusba.
        lblGrafikaAudio.setBounds(405, 150, 300, 30);
        lblFelbontas.setBounds(405, 200, 100, 30);
        lblFelbontasErtek.setBounds(640, 200, 150, 30);
        sliFelbontas.setBounds(490, 202, 150, 30);
        rbtnSzeles.setBounds(400, 250, 100, 30);
        rbtnNormal.setBounds(500, 250, 100, 30);
        rbtnAblakos.setBounds(400, 300, 100, 30);
        rbtnTeljesKepernyo.setBounds(500, 300, 100, 30);
        cbElsimitas.setBounds(400, 350, 100, 30);
        cbMenuZene.setBounds(400, 400, 150, 30);
        cbHang.setBounds(400, 450, 100, 30);
        lblJatekmenet.setBounds(405, 550, 300, 30);
        lblNev.setBounds(405, 600, 50, 30);
        lblJatekosokSzama.setBounds(405, 650, 150, 30);
        lblOsszeg.setBounds(405, 700, 100, 30);
        lblNagyVakErtek.setBounds(405, 750, 150, 30);
        lblVakEmeles.setBounds(405, 800, 250, 30);
        txtNev.setBounds(450, 600, 100, 30);
        sliJatekosokSzama.setBounds(545, 652, 150, 30);
        sliOsszeg.setBounds(465, 702, 100, 30);
        sliNagyVakErtek.setBounds(540, 752, 150, 30);
        sliVakEmeles.setBounds(645, 802, 250, 30);
        
        bgrKeparany.add(rbtnSzeles);
        bgrKeparany.add(rbtnNormal);
        bgrKepernyoMod.add(rbtnAblakos);
        bgrKepernyoMod.add(rbtnTeljesKepernyo);
        
        beallitasKomponensek.add(lblGrafikaAudio);
        beallitasKomponensek.add(lblFelbontas);
        beallitasKomponensek.add(lblFelbontasErtek);
        beallitasKomponensek.add(sliFelbontas);
        beallitasKomponensek.add(rbtnSzeles);
        beallitasKomponensek.add(rbtnNormal);
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
            Logger.getLogger(JatekMenuPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        setLayout(null);
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
    
    private void jatekMenuAncestorAdded(AncestorEvent ae) {        
        eloter = new ImageIcon(this.getClass().getResource("/hu/szakdolgozat/poker/adatFajlok/jatekMenu/eloter.png")).getImage();
        hatter = new ImageIcon(this.getClass().getResource("/hu/szakdolgozat/poker/adatFajlok/jatekMenu/hatter.png")).getImage();        
        szelesseg = this.getWidth();
        magassag = this.getHeight();        
        menupontok = new ArrayList<>();
        beallitasKomponensek = new ArrayList<>();
        szalVezerlo.jatekMenuAnimacioIndit();
        fontBetoltes();
        foMenuBetolt();
    }

    private void jatekMenuMouseReleased(MouseEvent me) {
        switch (menupontNev) {
            case "Új játék":
                szalVezerlo.jatekMenuAnimacioMegallit();
                FeluletKezelo.jatekTerPanelBetolt();
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
            case "Kilépés":
                System.exit(0);
        }
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
    
    /**
     * Fájlból tölti be a betű típust.
     */
    private void fontBetoltes(){
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        URL urlFont = this.getClass().getResource("/hu/szakdolgozat/poker/adatFajlok/jatekMenu/COPRGTB.ttf");

        try {
            env.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(urlFont.getFile())));
        } catch (FontFormatException | IOException ex) {
            Logger.getLogger(JatekMenuPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setSzalVezerlo(SzalVezerlo szalVezerlo) {
        this.szalVezerlo = szalVezerlo;
    }
}
