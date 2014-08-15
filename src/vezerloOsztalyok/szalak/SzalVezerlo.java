package vezerloOsztalyok.szalak;

import alapOsztalyok.Dealer;
import alapOsztalyok.Jatekos;
import alapOsztalyok.Kartyalap;
import alapOsztalyok.Vak;
import alapOsztalyok.Zseton;
import felulet.JatekterPanel;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.ImageIcon;
import vezerloOsztalyok.SzogSzamito;

public class SzalVezerlo {
    private List<Kartyalap> kartyalapok;  
    private List<Kartyalap> leosztottKartyalapok;
    private List<Jatekos> jatekosok;
    private Map<Byte, List<Kartyalap>> jatekosokKartyalapjai;
    private Map<Byte, List<Zseton>> jatekosokZsetonjai;
    private boolean kartyaGrafikaElore;
    private Dealer dealer;
    private Vak kisVak;
    private Vak nagyVak;
    private JatekterPanel jatekterPanel;
    private final Image keveresAnimacio;

    public SzalVezerlo() {
        keveresAnimacio = new ImageIcon(this.getClass().getResource("/adatFajlok/kartyaPakli/keveresAnimacio.gif")).getImage();
         
        /*tesztelés*/
        jatekosok = new ArrayList<>();
        jatekosok.add(new Jatekos("Sanyai"));
        jatekosok.add(new Jatekos("Pityua"));
        jatekosok.add(new Jatekos("Tomias"));
        jatekosok.add(new Jatekos("Gézaka"));
        jatekosok.add(new Jatekos("Kndras"));
        /*-----------------------------------*/
        
    } 

    /**
     * Kirajzolja a kártyapaklit, a játékosok lapjait és a leosztott
     * kártyalapokat a panelre.
     *
     * @param g2D
     */
    public void kartyalapokRajzol(Graphics2D g2D) {
        if (kartyalapok != null) {
            for (Kartyalap kartyalap : kartyalapok) {
                kartyalap.rajzol(g2D, jatekterPanel);
            }
        } else {
            g2D.drawImage(keveresAnimacio, (int)(jatekterPanelSzelesseg() / 2.286), (int)(jatekterPanelMagassag() / 2.143), jatekterPanelSzelesseg()/8, jatekterPanelMagassag()/16, jatekterPanel);
        }
        
        if(jatekosokKartyalapjai != null){
            List<Kartyalap> jatekosKartyalapok;
            for (Map.Entry<Byte, List<Kartyalap>> entry : jatekosokKartyalapjai.entrySet()) {
                jatekosKartyalapok = entry.getValue();
                for (Kartyalap kartyalap : jatekosKartyalapok) {
                    kartyalap.rajzol(g2D, jatekterPanel);
                }
            }
        }
        
        if(leosztottKartyalapok != null){
            for (Kartyalap kartyalap : leosztottKartyalapok) {
                kartyalap.rajzol(g2D, jatekterPanel);
                }
            }
    }

    /**
     * Kirajzolja a panelre a játékosok neveit.
     *
     * @param g2D
     */
    public void jatekosokRajzol(Graphics2D g2D) {
        if (jatekosok != null) {
            for (Jatekos jatekos : jatekosok) {
                jatekos.rajzol(g2D, jatekterPanel);
            }
        }
    }

    /**
     * Kirajzolja a zsetonokat a panelre.
     *
     * @param g2D
     */
    public void zsetonokRajzol(Graphics2D g2D) {
        if (jatekosokZsetonjai != null) {
            List<Zseton> jatekosZsetonok;
            for (Map.Entry<Byte, List<Zseton>> entry : jatekosokZsetonjai.entrySet()) {
                jatekosZsetonok = entry.getValue();
                for (Zseton zseton : jatekosZsetonok) {
                    zseton.rajzol(g2D, jatekterPanel);
                }
            }
        }
    }

    /**
     * Beállítja a játékosok neveinek pozícióját, hogy az asztal szélétől azonos
     * távolságra legyenek.
     */
    public void jatekosokBeallit() {
        int i = 0;
        List<Point> vegpontLista = SzogSzamito.vegpontLista(jatekosokSzama(), jatekterPanelSzelesseg(), jatekterPanelMagassag());
        Point vegpont;
        for (Jatekos jatekos : jatekosok) {
            vegpont = vegpontLista.get(i++);
            jatekos.setX(vegpont.getX() + jatekterPanelSzelesseg()/11.035 * Math.cos(Math.toRadians(SzogSzamito.szogSzamit(jatekterPanelSzelesseg(), jatekterPanelMagassag(), vegpont.getX(), vegpont.getY()))));
            jatekos.setY(vegpont.getY() + jatekterPanelMagassag()/8.276 * Math.sin(Math.toRadians(SzogSzamito.szogSzamit(jatekterPanelSzelesseg(), jatekterPanelMagassag(), vegpont.getX(), vegpont.getY()))));
            jatekos.setFont(new Font("Arial", 1, jatekterPanelMagassag() / 60));
        }
    }
    
    public void jatekvezerloSzalIndit() {
        JatekVezerlo jatekVezerlo = new JatekVezerlo(this);
        jatekVezerlo.start();
    }
    
    public void zsetonokKiosztSzalIndit(){
        ZsetonMozgato zsetonMozgato = new ZsetonMozgato(this);
        zsetonMozgato.setZsetonokBetolt(true);
        zsetonMozgato.start();
    }
    
    public void kartyalapokKiosztSzalIndit(byte dealer) {
        KartyaMozgato kartyaMozgato = new KartyaMozgato(this);
        kartyaMozgato.setKartyalapokKiosztasa(true);
        kartyaMozgato.setKartyalapLeosztas(true);
        kartyaMozgato.setDealer(dealer);
        kartyaMozgato.start();
    }
 
    public void korongokMozgatSzalIndit(byte dealer){
        
    }
    
    /**
     * Hozzáad egy kártyalapot a leosztottKartyalapok listához. Ha a lista 
     * null-ra hivatkozik akkor létrehoz egy új ArrayList-et.
     * @param kartyalap 
     */
    public void leosztottKartyalapHozzaad(Kartyalap kartyalap){        
        if(leosztottKartyalapok == null) leosztottKartyalapok = new ArrayList<>();
        leosztottKartyalapok.add(kartyalap);
    }    
   
    public int leosztottKartyalapokSzama() {
        return (leosztottKartyalapok == null) ? 0 : leosztottKartyalapok.size();
    }

    /**
     * Egy feltétel vizsgálattal eldönti, hogy a HashMap tartalmazza e a játékos azonosító kulcsot.
     * Ha igen, akkor a kulcshoz tartozó értéket ami egy lista, lekéri és hozzá adja a metódus paramétereként
     * megadott kártyalap objektumot. Ha nem, akkor beilleszti az új sorszámot és a hozzá tartozó listát.
     * Ha a jatekosokKartyalapjai Map null-ra hivatkozik akkor létrehoz egy új HashMap-et.
     * @param sorszam
     * @param kartyalap 
     */
    public void jatekosKartyalapokhozAd(byte sorszam, Kartyalap kartyalap) {        
        if(jatekosokKartyalapjai == null) jatekosokKartyalapjai = new HashMap<>();
        if (jatekosokKartyalapjai.containsKey(sorszam)) {
            jatekosokKartyalapjai.get(sorszam).add(kartyalap);
        } else {
            List<Kartyalap> jatekosKartyalapok = new CopyOnWriteArrayList<>();
            jatekosKartyalapok.add(kartyalap);
            jatekosokKartyalapjai.put(sorszam, jatekosKartyalapok);
        }
    }
    
    public void gombsorAktival() {
        jatekterPanel.gombsorAktival();
    }
    
    public void gombsorPasszival(){
        jatekterPanel.gomsorPasszival();
    }
    
    /**
     * Frissíti a játéktér panelt.
     */
    public void frissit() {
        jatekterPanel.repaint();
    }
      
    /**
     * Visszaadja a játéktér panel szélességét.
     * @return 
     */
    public int jatekterPanelSzelesseg(){
        return jatekterPanel.getWidth();
    }
    
    /**
     * Visszaadja a játéktér panel magasságát.
     * @return 
     */
    public int jatekterPanelMagassag(){
        return jatekterPanel.getHeight();
    }
    
    /**
     * Visszaadja a játékosok számát.
     * @return 
     */
    public byte jatekosokSzama(){
        return (byte)jatekosok.size();
    }
    
    public void setJatekTerPanel(JatekterPanel jatekTerPanel) {
        this.jatekterPanel = jatekTerPanel;
    }

    public void setKartyalapok(List<Kartyalap> kartyalapok) {
        this.kartyalapok = kartyalapok;
    }

    public void setKartyaGrafikaElore(boolean kartyaGrafikaElore) {
        this.kartyaGrafikaElore = kartyaGrafikaElore;
    }

    public void setJatekosokZsetonjai(Map<Byte, List<Zseton>> jatekosokZsetonjai) {
        this.jatekosokZsetonjai = jatekosokZsetonjai;
    }

    public Map<Byte, List<Kartyalap>> getJatekosokKartyalapjai() {
        return jatekosokKartyalapjai;
    }

    public List<Jatekos> getJatekosok() {
        return jatekosok;
    }

    public boolean isKartyaGrafikaElore() {
        return kartyaGrafikaElore;
    }
}