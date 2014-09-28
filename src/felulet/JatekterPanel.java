package felulet;

import alapOsztalyok.Gomb;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import vezerloOsztalyok.SzalVezerlo;

public class JatekterPanel extends JPanel{

    private SzalVezerlo szalVezerlo;
    private Image jatekTer;
    private int szelesseg;
    private int magassag;
    private List<Gomb> gombok;
    private Gomb lenyomottGomb;

    public JatekterPanel() {
        inicializal();
    }

    private void inicializal() {
        jatekTer = new ImageIcon(this.getClass().getResource("/adatFajlok/jatekTer/jatekTer_normal.png")).getImage();
        
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
                jatekTermouseReleased(me);
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
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        g2D.drawImage(jatekTer, 0, 0, this.getWidth(), this.getHeight(), this);
        
        if (szalVezerlo != null) {
            szalVezerlo.jatekosokRajzol(g2D);            
            szalVezerlo.korongokRajzol(g2D);
            szalVezerlo.potRajzol(g2D);
            
            if (szalVezerlo.isKartyaGrafikaElore()) {
                szalVezerlo.zsetonokRajzol(g2D);
                szalVezerlo.kartyalapokRajzol(g2D);
            } else {
                szalVezerlo.kartyalapokRajzol(g2D);
                szalVezerlo.zsetonokRajzol(g2D);
            }
        }        
        
        if (gombok != null) {
            for (Gomb gomb : gombok) {
                gomb.rajzol(g);
            }
        }
    }

    private void gombsorBeallit() {
        Image elsoKep, masodikKep, harmadikKep;
        String nev;
        double[][] xyPoziciok = {{szelesseg / 2.68, szelesseg/2.24, szelesseg/2, szelesseg/1.84, szelesseg/1.85, szelesseg / 1.735},
                              {magassag * 0.95,magassag * 0.95,magassag * 0.95,magassag * 0.968,magassag * 0.943,magassag * 0.95}};
        int[][] meretek = {{szelesseg / 16, szelesseg / 25, szelesseg / 25, szelesseg / 50, szelesseg / 40, szelesseg / 32},
                           {magassag / 20, (int)(magassag / 18.75), (int)(magassag / 18.75), magassag / 70, magassag / 30, magassag / 24}};
        String[] gombNev = {"allin", "call_check", "raise_bet", "minus", "plus", "fold"};
        gombok = new ArrayList<>();
        Gomb gomb;
        for (int i = 0; i < gombNev.length; i++) {
            nev = gombNev[i];
            elsoKep = new ImageIcon(this.getClass().getResource("/adatFajlok/iranyitas/"
                    + gombNev[i] + "_1.png")).getImage();
            masodikKep = new ImageIcon(this.getClass().getResource("/adatFajlok/iranyitas/"
                    + gombNev[i] + "_2.png")).getImage();
            harmadikKep = new ImageIcon(this.getClass().getResource("/adatFajlok/iranyitas/"
                    + gombNev[i] + "_3.png")).getImage();            
            gomb = new Gomb(nev, elsoKep, masodikKep, harmadikKep, (int)xyPoziciok[0][i], 
                    (int)xyPoziciok[1][i] - meretek[1][i] / 2, meretek[0][i], meretek[1][i]);
            gomb.setMegjSorszam(3);
            gombok.add(gomb);
        }
    }
    
    public void gombsorAktival(boolean[] aktivalandoGombok){
        for (byte i = 0; i < gombok.size(); i++) {
            if(aktivalandoGombok[i])gombok.get(i).setMegjSorszam(2);
        }
    }
    
    public void gombsorPasszival() {
        for (Gomb gomb : gombok) {
            gomb.setMegjSorszam(3);
        }
    }
    
    private void jatekTerAncestorAdded(AncestorEvent ae){       
        szelesseg = this.getWidth();
        magassag = this.getHeight();       
        
        gombsorBeallit();
        szalVezerlo.jatekosokBeallit();
        szalVezerlo.jatekvezerloIndit();
        repaint();
    }
    
    private void jatekTerMousePressed(MouseEvent me) {
        for (Gomb gomb : gombok) {
            if(gomb.getMegjSorszam() == 2){
                Rectangle2D negyszog = new Rectangle2D.Double(gomb.getX(), gomb.getY(), gomb.getSzelesseg(), gomb.getMagassag());
                if (negyszog.contains(me.getX(), me.getY()) && me.getButton() == 1) {
                    gomb.setMegjSorszam(1);
                    gomb.setY((int) (gomb.getY() + magassag / 300));
                    lenyomottGomb = gomb;
                }  
            }
        }
        repaint();
    }

    private void jatekTermouseReleased(MouseEvent me) {
        if (lenyomottGomb != null && lenyomottGomb.getMegjSorszam() == 1) {
            lenyomottGomb.setMegjSorszam(2);
            lenyomottGomb.setY((int) (lenyomottGomb.getY() - magassag / 300));
        }
        repaint();
    }
    
    public void setSzalVezerlo(SzalVezerlo szalVezerlo) {
        this.szalVezerlo = szalVezerlo;
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
    JButton plusz = new JButton("plusz");
    JButton minusz = new JButton("minusz");  
    List<JButton> gombLista = new ArrayList<>();

    private void migombok() {        
        this.add(allin);
        this.add(passzol);
        this.add(megad);
        this.add(nyit);
        this.add(emel);
        this.add(plusz);
        this.add(minusz);
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
                szalVezerlo.allin();
            }
        });

        passzol.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                szalVezerlo.passzol();
            }
        });
        emel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                szalVezerlo.emel(osszeg);
            }
        });       
        megad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                szalVezerlo.megad(osszeg);
            }
        });
        nyit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                szalVezerlo.nyit(osszeg);
            }
        });
        eldob.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                szalVezerlo.eldob();
            }
        });
        plusz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                osszeg += 10;
                if(!minusz.isEnabled())minusz.setEnabled(true);
                lblOsszeg.setText(String.valueOf(osszeg));
            }
        });
        minusz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(osszeg > szalVezerlo.getOsszeg())
                osszeg -= 1;
                else minusz.setEnabled(false);
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
