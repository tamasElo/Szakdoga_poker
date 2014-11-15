package hu.szakdolgozat.poker.felulet;

import hu.szakdolgozat.poker.alapOsztalyok.Gomb;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import hu.szakdolgozat.poker.vezerloOsztalyok.SzalVezerlo;

public class JatekterPanel extends JPanel{

    private SzalVezerlo szalVezerlo;
    private Image jatekTer;
    private Image elmosottJatekTer;
    private int szelesseg;
    private int magassag;
    private int megadandoOsszeg; 
    private int emelendoOsszeg;
    private int minOsszeg;
    private int maxOsszeg;
    private int lepesKoz;
    private List<Gomb> gombok;
    private Gomb lenyomottGomb;
    private Gomb plusz;
    private Gomb minusz;
    private boolean elmosas;

    public JatekterPanel() {
        inicializal();
    }

    private void inicializal() {     
        addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent ae) {
                jatekTerAncestorAdded(ae);
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
                jatekTerMousePressed(me);               
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                jatekTerMouseReleased(me);
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        });
        
        /*-----tesztelés*/
         migombok();
        /*--*/
    }  
    
    /**
     * Kirajzolja a panelre a grafikát.
     * 
     * @param g 
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image kep;
        kep = elmosas ? elmosottJatekTer : jatekTer;
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        g2D.drawImage(kep, 0, 0, szelesseg, magassag, this);
        
        if (szalVezerlo != null) {
            szalVezerlo.jatekosokRajzol(g2D);    
            
            if (szalVezerlo.isKartyaGrafikaElore()) {
                szalVezerlo.korongokRajzol(g2D);
                szalVezerlo.zsetonokRajzol(g2D);
                szalVezerlo.potRajzol(g2D);
                szalVezerlo.kartyalapokRajzol(g2D);
            } else {
                szalVezerlo.kartyalapokRajzol(g2D);
                szalVezerlo.korongokRajzol(g2D);
                szalVezerlo.zsetonokRajzol(g2D);
                szalVezerlo.potRajzol(g2D);
            }
        }        
        
        szalVezerlo.felhoRajzol(g);
        
        szalVezerlo.nyertesRajzol(g2D);
        
        if (gombok != null) {            
            String osszeg;
            double szovegSzelesseg, szovegMagassag;
            
            g.setColor(Color.yellow);
            g.setFont(new Font("Arial", 1, magassag / 60));
            
            for (byte i = 0; i < gombok.size(); i++) {
                Gomb gomb = gombok.get(i);
                gomb.rajzol(g, this);
                
                if (gomb.getNev().equals("call_check") && gomb.getMegjSorszam() != 3 && megadandoOsszeg > 0) {
                    osszeg = String.valueOf(megadandoOsszeg);
                    szovegSzelesseg = g.getFontMetrics().getStringBounds(osszeg, g).getWidth();
                    szovegMagassag = g.getFontMetrics().getStringBounds(osszeg, g).getHeight();
                    g.drawString(osszeg, (int) (gomb.getX() + gomb.getSzelesseg() / 2 - szovegSzelesseg / 2), (int) (magassag/1.077 - szovegMagassag / 2));
                } else if (gomb.getNev().equals("raise_bet") && gomb.getMegjSorszam() != 3) {
                    osszeg = String.valueOf(emelendoOsszeg);
                    szovegSzelesseg = g.getFontMetrics().getStringBounds(osszeg, g).getWidth();
                    szovegMagassag = g.getFontMetrics().getStringBounds(osszeg, g).getHeight();
                    g.drawString(osszeg, (int) (gomb.getX() + gomb.getSzelesseg() / 2 - szovegSzelesseg / 2), (int) (magassag/1.077 - szovegMagassag / 2));
                } 
            }            
        }
    }

    /**
     * Létrehozza a gombsort.
     */
    private void gombsorBeallit() {
        Image elsoKep, masodikKep, harmadikKep;
        String nev;
        double[][] xyPoziciok = {{szalVezerlo.aranytSzamol(2.68), szalVezerlo.aranytSzamol(2.24), 
                                  szalVezerlo.aranytSzamol(2), szalVezerlo.aranytSzamol(1.84), 
                                  szalVezerlo.aranytSzamol(1.85), szalVezerlo.aranytSzamol(1.735)},
                                 {szalVezerlo.aranytSzamol(1.4),szalVezerlo.aranytSzamol(1.4),
                                  szalVezerlo.aranytSzamol(1.4),szalVezerlo.aranytSzamol(1.376),
                                  szalVezerlo.aranytSzamol(1.412),szalVezerlo.aranytSzamol(1.4)}};
        double[][] meretek = {{szalVezerlo.aranytSzamol(16), szalVezerlo.aranytSzamol(25), 
                               szalVezerlo.aranytSzamol(25), szalVezerlo.aranytSzamol(50), 
                               szalVezerlo.aranytSzamol(40), szalVezerlo.aranytSzamol(32)},
                              {szalVezerlo.aranytSzamol(26.67), szalVezerlo.aranytSzamol(25), 
                               szalVezerlo.aranytSzamol(25), szalVezerlo.aranytSzamol(94.12), 
                               szalVezerlo.aranytSzamol(40), szalVezerlo.aranytSzamol(32)}};     
        
        String[] gombNev = {"allin", "call_check", "raise_bet", "minus", "plus", "fold"};
        gombok = new ArrayList<>();
        Gomb gomb;
        for (byte i = 0; i < gombNev.length; i++) {
            nev = gombNev[i];
            elsoKep = new ImageIcon(this.getClass().getResource("/hu/szakdolgozat/poker/adatFajlok/iranyitas/"
                    + gombNev[i] + "_1.png")).getImage();
            masodikKep = new ImageIcon(this.getClass().getResource("/hu/szakdolgozat/poker/adatFajlok/iranyitas/"
                    + gombNev[i] + "_2.png")).getImage();
            harmadikKep = new ImageIcon(this.getClass().getResource("/hu/szakdolgozat/poker/adatFajlok/iranyitas/"
                    + gombNev[i] + "_3.png")).getImage();            
            gomb = new Gomb(nev, elsoKep, masodikKep, harmadikKep, xyPoziciok[0][i], 
                    xyPoziciok[1][i] - meretek[1][i] / 2, meretek[0][i], meretek[1][i]);
            gomb.setMegjSorszam(3);
            gombok.add(gomb);
            
            switch (gomb.getNev()) {
                case "plus":
                    plusz = gomb;
                    break;
                case "minus":
                    minusz = gomb;
                    break;
            }
        }
    }
    
    /**
     * A paraméterként átadott boolean tömb alapján aktiválja a gombokat.
     * 
     * @param aktivalandoGombok 
     */
    public void gombsorAktival(boolean[] aktivalandoGombok){
        Gomb gomb;
        for (byte i = 0; i < gombok.size(); i++) {
            gomb = gombok.get(i);
            if (aktivalandoGombok[i]) {
                gomb.setMegjSorszam(2);
            }
        }
    }
    
    /**
     * Passziválja a gombsort.
     */
    private void gombsorPasszival() {
        for (Gomb gomb : gombok) {
            gomb.setMegjSorszam(3);
        }
    }
    
    /**
     * Amikor a panel hozzáadódik egy másik komponenshez, akkor beállítja
     * a szélesség, magasság értékeket, meghívja a gombsorBeallit(), 
     * jatekosokBeallit() és jatekVezerloIndit() metódusokat.
     * 
     * @param ae 
     */
    private void jatekTerAncestorAdded(AncestorEvent ae){    
        jatekTer = new ImageIcon(this.getClass().getResource("/hu/szakdolgozat/poker/adatFajlok/jatekTer/jatekTer_normal.png")).getImage();
        elmosottJatekTer = new ImageIcon(this.getClass().getResource("/hu/szakdolgozat/poker/adatFajlok/jatekTer/jatekTer_normal_blur.png")).getImage();   
        szelesseg = this.getWidth();
        magassag = this.getHeight();  
        gombsorBeallit();
        szalVezerlo.jatekosokBeallit();
        szalVezerlo.jatekVezerloIndit();
        repaint();
    }
    
    /**
     * Megvizsgálja hogy ráklikkelt-e a játékos valamelyik gombra.
     * 
     * @param me 
     */
    private void jatekTerMousePressed(MouseEvent me) {
        for (Gomb gomb : gombok) {
            if(gomb.getMegjSorszam() == 2){
                Rectangle2D negyszog = new Rectangle2D.Double(gomb.getX(), gomb.getY(), gomb.getSzelesseg(), gomb.getMagassag());
                if (negyszog.contains(me.getX(), me.getY()) && me.getButton() == 1) {
                    gomb.setMegjSorszam(1);
                    lenyomottGomb = gomb;
                }  
            }
        }
        repaint();
    }

    /**
     * A lenyomott gomb felengedésekor meghívja az osszegValtoztat() és 
     * lehetosegValazt() metódusokat.
     * 
     * @param me 
     */
    private void jatekTerMouseReleased(MouseEvent me) {
            if (lenyomottGomb != null && lenyomottGomb.getMegjSorszam() == 1) {
                lenyomottGomb.setMegjSorszam(2);
                osszegValtoztat();
                lehetosegValaszt();
            }
            repaint();
    }

    /**
     * A tét értékét változtatja.
     */
    private void osszegValtoztat() {
        switch (lenyomottGomb.getNev()) {
            case "plus":
                if (emelendoOsszeg + lepesKoz < maxOsszeg) {
                    if (minusz.getMegjSorszam() == 3) {
                        minusz.setMegjSorszam(2);
                    }
                    emelendoOsszeg += lepesKoz;
                } else {
                    emelendoOsszeg = maxOsszeg;
                    lenyomottGomb.setMegjSorszam(3);
                }
                break;
            case "minus":
                if (emelendoOsszeg - lepesKoz > minOsszeg) {
                    if (plusz.getMegjSorszam() == 3) {
                        plusz.setMegjSorszam(2);
                    }
                    emelendoOsszeg -= lepesKoz;
                } else {
                    emelendoOsszeg = minOsszeg;
                    lenyomottGomb.setMegjSorszam(3);
                }
                break;
        }
    }

    /**
     * A lenyomott gombhoz választja ki a megfelelő lehetőséget.
     */
    private void lehetosegValaszt(){
        switch (lenyomottGomb.getNev()) {
            case "allin":
                szalVezerlo.emberiJatekosAllIn();
                gombsorPasszival();
                break;
            case "call_check":
                if(megadandoOsszeg > 0) szalVezerlo.emberiJatekosMegad();
                else szalVezerlo.emberiJatekosPasszol();
                gombsorPasszival();
                break;
            case "raise_bet":
                if(megadandoOsszeg == 0) szalVezerlo.emberiJatekosNyit(emelendoOsszeg);
                else szalVezerlo.emberiJatekosEmel(emelendoOsszeg);
                gombsorPasszival();
                break;
            case "fold":
                szalVezerlo.emberiJatekosBedob();
                gombsorPasszival();
                break;
        }   
    }    
    
    public void setSzalVezerlo(SzalVezerlo szalVezerlo) {
        this.szalVezerlo = szalVezerlo;
    }

    public void setElmosas(boolean elmosas) {
        this.elmosas = elmosas;
    }

    public void setMegadandoOsszeg(int megadandoOsszeg) {
        this.megadandoOsszeg = megadandoOsszeg;
    }

    public void setEmelendoOsszeg(int emelendoOsszeg) {
        this.emelendoOsszeg = emelendoOsszeg;
    }

    public void setMinOsszeg(int minOsszeg) {
        this.minOsszeg = minOsszeg;
    }

    public void setMaxOsszeg(int maxOsszeg) {
        this.maxOsszeg = maxOsszeg;
    }
    
    public void setLepesKoz(int lepesKoz) {
        this.lepesKoz = lepesKoz;
    }
    
    /*------tesztelés----------------*/
    int jx, jy;
    int osszeg;
    JLabel lblNev = new JLabel();
    JLabel lblOsszeg = new JLabel();
    JButton allin = new JButton("all");
    JButton passzol = new JButton("passzol");
    JButton emel = new JButton("emel");
    JButton megad = new JButton("megad");
    JButton nyit = new JButton("nyit");
    JButton eldob = new JButton("eldob");
    JButton plussz = new JButton("plusz");
    JButton minussz = new JButton("minusz");  
    List<JButton> gombLista = new ArrayList<>();

    private void migombok() {        
        this.add(allin);
        this.add(passzol);
        this.add(megad);
        this.add(nyit);
        this.add(emel);
        this.add(plussz);
        this.add(minussz);
        this.add(eldob);
        this.add(lblOsszeg);
        this.add(lblNev);
        
        gombLista.add(nyit);        
        gombLista.add(emel);      
        gombLista.add(megad);        
        gombLista.add(passzol);           
        
        allin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                szalVezerlo.emberiJatekosAllIn();
            }
        });

        passzol.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                szalVezerlo.emberiJatekosPasszol();
            }
        });
        emel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                szalVezerlo.emberiJatekosEmel(osszeg);
            }
        });       
        megad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                szalVezerlo.emberiJatekosMegad();
            }
        });
        nyit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                szalVezerlo.emberiJatekosNyit(osszeg);
            }
        });
        eldob.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                szalVezerlo.emberiJatekosBedob();
            }
        });
        plussz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                osszeg += 10;
                if(!minussz.isEnabled())minussz.setEnabled(true);
                lblOsszeg.setText(String.valueOf(osszeg));
            }
        });
        minussz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(osszeg > szalVezerlo.getOsszeg())
                osszeg -= 1;
                else minussz.setEnabled(false);
                lblOsszeg.setText(String.valueOf(osszeg));
            }
        });
    }

    public void migombsoraktival(boolean[] tomb) {
        for (int i = 0; i < tomb.length; i++) {
            gombLista.get(i).setEnabled(tomb[i]);
        }
    }

    public void setNevlabel(String nev) {
        lblNev.setText(nev);
    }

    public void setOsszeg(int osszeg) {
        this.osszeg = osszeg;
        lblOsszeg.setText(String.valueOf(osszeg));
    }
}
