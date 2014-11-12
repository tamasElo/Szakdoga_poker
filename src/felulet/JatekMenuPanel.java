package felulet;

import alapOsztalyok.Menupont;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import vezerloOsztalyok.FeluletKezelo;
import vezerloOsztalyok.SzalVezerlo;

public class JatekMenuPanel extends JPanel{
    private SzalVezerlo szalVezerlo;
    private List<Menupont> menupontok;  
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
    private void menupontokBeallit() {
        String[] menupontNevek = {"Játék folytatása", "Új játék", "Beállítások", "Segítség", "Kilépés"};
        Menupont menupont;
        double novekmeny = szelesseg / 30;
        double kx = szelesseg / 2, ky = magassag / 2 - (menupontNevek.length / 2) * novekmeny;
        menupontok = new ArrayList<>();

        for (String mpontNev : menupontNevek) {
            menupont = new Menupont(mpontNev);
            menupont.setBetuMeret((int)(szelesseg / 64));
            menupont.setBetuTipus("Copperplate Gothic Bold");
            menupont.setKx(kx);
            menupont.setKy(ky);
            menupontok.add(menupont);
            ky += novekmeny;
        }

        repaint();
    }

    private void jatekMenuAncestorAdded(AncestorEvent ae) {
        eloter = new ImageIcon(this.getClass().getResource("/adatFajlok/jatekMenu/eloter.png")).getImage();
        hatter = new ImageIcon(this.getClass().getResource("/adatFajlok/jatekMenu/hatter.png")).getImage();
        szelesseg = this.getWidth();
        magassag = this.getHeight();
        szalVezerlo.jatekMenuAnimacioIndit();
        menupontokBeallit();
    }

    private void jatekMenuMouseReleased(MouseEvent me) {
        switch (menupontNev) {
            case "Új játék":
                szalVezerlo.jatekMenuAnimacioMegallit();
                FeluletKezelo.jatekTerPanelBetolt();                
                break;
            case "Kilépés":
                System.exit(0);
        }
    }

    private void jatekMenuMouseMoved(MouseEvent me) {
        Rectangle2D rect;
        double kx, ky;
        double szovegSzelesseg, szovegMagassag;
        menupontNev = "";

        for (Menupont menupont : menupontok) {
            kx = menupont.getKx();
            ky = menupont.getKy();
            szovegSzelesseg = menupont.getSzovegSzelesseg();
            szovegMagassag = menupont.getSzovegMagassag();
            rect = new Rectangle2D.Double(kx - szovegSzelesseg / 2, ky - szovegMagassag / 2, szovegSzelesseg, szovegMagassag);

            if (rect.contains(me.getX(), me.getY())) {
                menupontNev = menupont.getNev();
                menupont.setBetuMeret((int) (szelesseg * 0.021875));
            } else {
                menupont.setBetuMeret((int) (szelesseg / 64));
            }
        }

        repaint();
    }
    
    public void setSzalVezerlo(SzalVezerlo szalVezerlo) {
        this.szalVezerlo = szalVezerlo;
    }
}
