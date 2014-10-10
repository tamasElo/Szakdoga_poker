package vezerloOsztalyok;

import alapOsztalyok.Felho;
import alapOsztalyok.Jatekos;
import alapOsztalyok.Kartyalap;
import alapOsztalyok.Korong;
import alapOsztalyok.Zseton;
import felulet.JatekterPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.ImageIcon;
import vezerloOsztalyok.szalak.FelhoMozgato;
import vezerloOsztalyok.szalak.KartyaMozgato;
import vezerloOsztalyok.szalak.KorongMozgato;
import vezerloOsztalyok.szalak.ZsetonMozgato;

public class SzalVezerlo {
    private List<Kartyalap> kartyalapok;  
    private List<Kartyalap> leosztottKartyalapok;
    private List<Jatekos> jatekosok;
    private List<Korong> korongok;
    private Map<Byte, List<Kartyalap>> jatekosokKartyalapjai;
    private Map<Byte, List<Zseton>> jatekosokZsetonjai;    
    private List<Zseton> pot;
    private boolean kartyaGrafikaElore;
    private JatekterPanel jatekterPanel;
    private JatekVezerlo jatekVezerlo;
    private final Image keveresAnimacio;
    private boolean gombsorAktiv;
    private ZsetonMozgato zsetonMozgato;
    private KartyaMozgato kartyaMozgato;
    private Felho felho;

    public SzalVezerlo() {
        keveresAnimacio = new ImageIcon(this.getClass().getResource("/adatFajlok/kartyaPakli/keveresAnimacio.gif")).getImage();
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
            g2D.drawImage(keveresAnimacio, (int)aranytSzamol(2.286), (int)aranytSzamol(2.143, 'y'), (int)aranytSzamol(8), (int)aranytSzamol(16, 'y'), jatekterPanel);
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
     * Kirajzolja a panelre a játékosok neveit és zsetonjaik összegét.
     *
     * @param g2D
     */
    public void jatekosokRajzol(Graphics2D g2D) {
        if (jatekosok != null) {
            double szovegSzelesseg, szovegMagassag;
            String osszeg;
            
            for (Jatekos jatekos : jatekosok) {
                jatekos.rajzol(g2D, jatekterPanel);
                osszeg = String.valueOf(getJatekosZsetonOsszeg(jatekos.getSorszam())+"$");
                szovegSzelesseg = g2D.getFontMetrics().getStringBounds(osszeg, g2D).getWidth();
                szovegMagassag = g2D.getFontMetrics().getStringBounds(osszeg, g2D).getHeight();
                g2D.setColor(Color.yellow);
                g2D.drawString(osszeg, (int) (jatekos.getKx() - szovegSzelesseg / 2), (int) ((jatekos.getKy() + szovegMagassag / 2) + jatekterPanelMagassag()/60));
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

    public void potRajzol(Graphics2D g2D) {
        if (pot != null) {
            for (Zseton zseton : pot) {
                zseton.rajzol(g2D, jatekterPanel);
            }
        }
    }
    
    public void korongokRajzol(Graphics2D g2D){
        if(korongok != null){
            for (Korong korong : korongok) {
                korong.rajzol(g2D, jatekterPanel);
            }
        }
    }
    
    public void felhoRajzol(Graphics g){
        if(felho != null) felho.rajzol(g, jatekterPanel);
    }
    
    /**
     * Létrehozza a játékosokat és beállítja a neveik pozícióját, hogy az asztal szélétől azonos
     * távolságra legyenek.
     */
    public void jatekosokBeallit() {
       /*tesztelés*/
        jatekosok = new ArrayList<>();
        jatekosok.add(new Jatekos("Sanyi"));
        jatekosok.add(new Jatekos("Pityu"));
        jatekosok.add(new Jatekos("Tomi"));
        jatekosok.add(new Jatekos("Géza"));
        jatekosok.add(new Jatekos("András"));
       /*-----------------------------------*/
        
        int i = 0;
        List<Point> vegpontLista = SzogSzamito.vegpontLista(jatekosokSzama(), jatekterPanelSzelesseg(), jatekterPanelMagassag());
        Point vegpont;
        for (Jatekos jatekos : jatekosok) {
            vegpont = vegpontLista.get(i++);
            jatekos.setKx(vegpont.getX() + jatekterPanelSzelesseg()/11.035 * Math.cos(Math.toRadians(SzogSzamito.szogSzamit(jatekterPanelSzelesseg(), jatekterPanelMagassag(), vegpont.getX(), vegpont.getY()))));
            jatekos.setKy(vegpont.getY() + jatekterPanelMagassag()/8.276 * Math.sin(Math.toRadians(SzogSzamito.szogSzamit(jatekterPanelSzelesseg(), jatekterPanelMagassag(), vegpont.getX(), vegpont.getY()))));
            jatekos.setFont(new Font("Arial", 1, jatekterPanelMagassag() / 60));
            jatekos.setAktiv(true);
        }
    }
    
    public void jatekVezerloIndit() {
        jatekVezerlo = new JatekVezerlo(this);
    }
    
    public void zsetonokKioszt(){
        zsetonMozgato = new ZsetonMozgato(this);
        zsetonMozgato.zsetonokBetolt();
        pot = new CopyOnWriteArrayList<>();
    }
    
    public void zsetonokPotba(byte jatekosSorszam, int osszeg){
        zsetonMozgato = new ZsetonMozgato(this);            
        zsetonMozgato.setJatekosSorszam(jatekosSorszam);
        zsetonMozgato.setJatekosTetOsszege(osszeg);
        zsetonMozgato.setJatekosTetMozgatasa(true);
        zsetonMozgato.start();
    }
    
    public void kartyalapokKiosztSzalIndit(byte dealer) {
        kartyaMozgato = new KartyaMozgato(this);
        kartyaMozgato.setKartyalapokKiosztasa(true);
        kartyaMozgato.setDealer(dealer);
        kartyaMozgato.start();
    }
    
    public void kartyalapokLeosztSzalIndit(boolean osszesetLeoszt){
        kartyaMozgato = new KartyaMozgato(this);
        kartyaMozgato.setKartyalapLeosztas(true);
        kartyaMozgato.setOsszesKartyalapLeosztas(osszesetLeoszt);
        kartyaMozgato.start();
    }
    
    public void kartyalapokBedobSzalIndit(byte jatekosSorszam){
        kartyaMozgato = new KartyaMozgato(this);
        kartyaMozgato.setJatekosSorszam(jatekosSorszam);
        kartyaMozgato.setKartyalapokBedobasa(true);
        kartyaMozgato.start();
    }
    
    public void korongokMozgatSzalIndit(byte dealer){
        KorongMozgato korongMozgato = new KorongMozgato(this);
        korongMozgato.setDealer(dealer);
        korongMozgato.start();
    }
    
    public void felhoSzalIndit(String nev, byte jatekosSorszam){
        Image felhoKep = new ImageIcon(this.getClass().getResource("/adatFajlok/felho/felho.png")).getImage();
        Jatekos jatekos = getJatekosok().get(jatekosSorszam);        
        double felhoKepSzelesseg = jatekterPanelSzelesseg() * 0.06875, felhoKepMagassag = jatekterPanelMagassag() * 0.0975;
        double kx = jatekos.getKx(), ky = jatekos.getKy();  
        felho = new Felho(felhoKep, nev);             
        felho.setKx(kx);
        felho.setKy(ky);
        felho.setFelhoKepSzelesseg(felhoKepSzelesseg);
        felho.setFelhoKepMagassag(felhoKepMagassag);
        FelhoMozgato felhoMozgato = new FelhoMozgato(this);
        felhoMozgato.start();
    }
    
    /**
     * Hozzáad egy kártyalapot a leosztottKartyalapok listához. Ha a lista 
     * null-ra hivatkozik akkor létrehoz egy új ArrayList-et.
     * @param kartyalap 
     */
    public void leosztottKartyalapokhozAd(Kartyalap kartyalap){        
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
        if(jatekosokKartyalapjai == null) jatekosokKartyalapjai = new ConcurrentHashMap<>();
        if (jatekosokKartyalapjai.containsKey(sorszam)) {
            jatekosokKartyalapjai.get(sorszam).add(kartyalap);
        } else {
            List<Kartyalap> jatekosKartyalapok = new CopyOnWriteArrayList<>();
            jatekosKartyalapok.add(kartyalap);
            jatekosokKartyalapjai.put(sorszam, jatekosKartyalapok);
        }
    }    

    public void pothozAd(List<Zseton> jatekosTetje) {
        pot.addAll(jatekosTetje);
    }

    public void gombSorAllapotvalt() {        
        if (jatekVezerlo != null) {
            if (!jatekVezerlo.gepiJatekos() && !gombsorAktiv) {
                boolean[] aktivalandoGombok = {true, true, true, false, true, true};

                if (!jatekVezerlo.isMegadhat() && !jatekVezerlo.isPasszolhat()) {
                    aktivalandoGombok[1] = false;
                }
                
                if (!jatekVezerlo.isEmelhet() && !jatekVezerlo.isNyithat()) {
                    for (byte i = 2; i < 5; i++) {
                        aktivalandoGombok[i] = false;
                    }
                }
                
                jatekterPanel.gombsorAktival(aktivalandoGombok);                  
                jatekterPanel.setMegadandoOsszeg(jatekVezerlo.getOsszeg()-jatekVezerlo.getJatekosokTetje()[JatekVezerlo.EMBER_JATEKOS_SORSZAM]);
                jatekterPanel.setLepesKoz(jatekVezerlo.getKisVakOsszeg());
                jatekterPanel.setEmelendoOsszeg(jatekVezerlo.getOsszeg() == 0 ? jatekVezerlo.getNagyVakOsszeg() : jatekVezerlo.getOsszeg());
                jatekterPanel.setMinOsszeg(jatekVezerlo.getOsszeg() == 0 ? jatekVezerlo.getNagyVakOsszeg() : jatekVezerlo.getOsszeg());
                jatekterPanel.setMaxOsszeg(getJatekosZsetonOsszeg(JatekVezerlo.EMBER_JATEKOS_SORSZAM));
                gombsorAktiv = true;
            }

            if (jatekVezerlo.gepiJatekos() && gombsorAktiv) {
                jatekterPanel.gombsorPasszival();
                gombsorAktiv = false;
            }
        }
    }
    
    public double aranytSzamol(double ertek){
        double osztando = 0;
        if((double)Math.round(10 * jatekterPanelSzelesseg() / jatekterPanelMagassag()) / 10 == 1.3) osztando = jatekterPanelSzelesseg();
        return osztando/ertek;
    }
    public double aranytSzamol(double ertek, char tengely){
        double osztando = 0;
        if((double)Math.round(10 * jatekterPanelSzelesseg() / jatekterPanelMagassag()) / 10 == 1.3) if(tengely == 'x')osztando = jatekterPanelSzelesseg();
                else osztando = jatekterPanelMagassag();
        return osztando/ertek;
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
    
    public void jatekosokAktival() {
        for (Jatekos jatekos : jatekosok) {
            if (jatekosokZsetonjai != null && !jatekosokZsetonjai.get(jatekos.getSorszam()).isEmpty()) {
                jatekos.setAktiv(true);
            }
        }
    }

    public void jatekosPasszival(byte jatekosSorszam) {
        jatekosok.get(jatekosSorszam).setAktiv(false);
    }

    public void emberiJatekosPasszol() {
        jatekVezerlo.passzol();
    }

    public void emberiJatekosNyit(int osszeg) {
        jatekVezerlo.nyit(osszeg);
    }

    public void emberiJatekosEmel(int osszeg) {
        jatekVezerlo.emel(osszeg);
    }

    public void emberiJatekosMegad() {
        jatekVezerlo.megad();
    }

    public void emberiJatekosAllIn() {
        jatekVezerlo.allIn();
    }

    public void emberiJatekosEldob() {
        jatekVezerlo.bedob();
    }

    public void setJatekTerPanel(JatekterPanel jatekTerPanel) {
        this.jatekterPanel = jatekTerPanel;
    }

    public void setKartyalapok(List<Kartyalap> kartyalapok) {
        this.kartyalapok = kartyalapok;
    }

    public void setKorongok(List<Korong> korongok) {
        this.korongok = korongok;
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

    public List<Kartyalap> getKartyalapok() {
        return kartyalapok;
    }

    public List<Jatekos> getJatekosok() {
        return jatekosok;
    }
    
    public Map<Byte, List<Zseton>> getJatekosokZsetonjai() {
        return jatekosokZsetonjai;
    }    
    
    public int getJatekosZsetonOsszeg(byte jatekosSorszam) {
        return ZsetonKezelo.zsetonokOsszege(jatekosokZsetonjai.get(jatekosSorszam));
    }

    public List<Zseton> getPot() {
        return pot;
    }    

    public Felho getFelho() {
        return felho;
    }

    public boolean isJatekosAktiv(byte jatekosSorszam) {
        return jatekosok.get(jatekosSorszam).isAktiv();
    }
    
    public boolean isKartyaGrafikaElore() {
        return kartyaGrafikaElore;
    }
    
    /*----------tesztelés---------------------*/    
    public void setMikezeles(boolean nyithat, boolean emelhet, boolean megadhat, boolean passzolhat) {
        boolean[] tomb = {nyithat, emelhet, megadhat, passzolhat};
        jatekterPanel.migombsoraktival(tomb);
    }
    
    public int getOsszeg(){
        return jatekVezerlo.getOsszeg();
    }
    
    public void setjatekosSorszam(byte jatekosSorszam) {       
       jatekterPanel.setNevlabel(jatekosok.get(jatekosSorszam).getNev());
    }

    public void setjatekosMegadandoOsszeg(int osszeg) {
        jatekterPanel.setOsszeg(osszeg);
    }
}
