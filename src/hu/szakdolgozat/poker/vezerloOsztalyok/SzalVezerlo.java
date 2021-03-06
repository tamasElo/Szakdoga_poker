package hu.szakdolgozat.poker.vezerloOsztalyok;

import hu.szakdolgozat.poker.vezerloOsztalyok.szalak.Mi;
import hu.szakdolgozat.poker.alapOsztalyok.Dealer;
import hu.szakdolgozat.poker.vezerloOsztalyok.szalak.JatekVezerlo;
import hu.szakdolgozat.poker.alapOsztalyok.Felho;
import hu.szakdolgozat.poker.alapOsztalyok.Jatekos;
import hu.szakdolgozat.poker.alapOsztalyok.Kartyalap;
import hu.szakdolgozat.poker.alapOsztalyok.Korong;
import hu.szakdolgozat.poker.alapOsztalyok.Nyertes;
import hu.szakdolgozat.poker.alapOsztalyok.Toltes;
import hu.szakdolgozat.poker.burkoloOsztalyok.PokerKez;
import hu.szakdolgozat.poker.alapOsztalyok.Vak;
import hu.szakdolgozat.poker.alapOsztalyok.Zseton;
import hu.szakdolgozat.poker.felulet.JatekMenuPanel;
import hu.szakdolgozat.poker.felulet.JatekterPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.ImageIcon;
import hu.szakdolgozat.poker.vezerloOsztalyok.szalak.FelhoMozgato;
import hu.szakdolgozat.poker.vezerloOsztalyok.szalak.KartyaMozgato;
import hu.szakdolgozat.poker.vezerloOsztalyok.szalak.KorongMozgato;
import hu.szakdolgozat.poker.vezerloOsztalyok.szalak.NyertesMozgato;
import hu.szakdolgozat.poker.vezerloOsztalyok.szalak.VarakozasMozgato;
import hu.szakdolgozat.poker.vezerloOsztalyok.szalak.ZsetonMozgato;
import java.awt.Dimension;
import java.io.Serializable;
import java.util.Iterator;

public class SzalVezerlo implements Serializable {

    private transient FeluletKezelo feluletKezelo;
    private transient JatekterPanel jatekterPanel;
    private transient JatekMenuPanel jatekMenuPanel;
    private transient Map<String, List<Double>> xmlAdatok;
    private transient Iterator<Double> itr;
    private boolean menthet;
    private List<Kartyalap> kartyalapok;
    private List<Kartyalap> leosztottKartyalapok;
    private List<Jatekos> jatekosok;
    private List<Korong> korongok;
    private Map<Byte, List<Kartyalap>> jatekosokKartyalapjai;
    private Map<Byte, List<Kartyalap>> kiszalltJatekosokKartyalapjai;
    private Map<Byte, List<Zseton>> jatekosokZsetonjai;
    private transient Map<Byte, PokerKez> nyertesPokerKezek;
    private List<Zseton> pot;
    private boolean kartyaGrafikaElore;
    private JatekVezerlo jatekVezerlo;
    private transient Mi mi;
    private Vak kisVak;
    private Vak nagyVak;
    private Dealer dealer;
    private transient Image keveresAnimacio; //Ezt valamiért nem lehet menteni, mert utánna nem frissít rendesen a jatekterpanel paint component-je.
    private transient ZsetonMozgato zsetonMozgato;
    private transient KartyaMozgato kartyaMozgato;
    private transient FelhoMozgato felhoMozgato;
    private transient NyertesMozgato nyertesMozgato;
    private transient VarakozasMozgato varakozasMozgato;
    private transient Felho felho;
    private transient Nyertes nyertes;
    private transient Toltes toltes;
    private String emberJatekosNev;
    private byte jatekosokSzama;
    private int zsetonOsszeg;
    private int nagyVakErtek;
    private byte vakErtekEmeles;
    
    public SzalVezerlo() {
        KorongMozgato.setKorongokBetoltve(false);
        Jatekos.sorszamIndexNullaz();
        beallitasokBetolt();
        keveresAnimacio = new ImageIcon(this.getClass().getResource("/hu/szakdolgozat/poker/adatFajlok/kartyaPakli/keveresAnimacio.gif")).getImage();
        kisVak = new Vak(new ImageIcon(this.getClass().getResource("/hu/szakdolgozat/poker/adatFajlok/korongok/small_blind.png")), 
                         new ImageIcon(this.getClass().getResource("/hu/szakdolgozat/poker/adatFajlok/korongok/small_blind_blur.png")));
        kisVak.setErtek(nagyVakErtek / 2);
        nagyVak = new Vak(new ImageIcon(this.getClass().getResource("/hu/szakdolgozat/poker/adatFajlok/korongok/big_blind.png")), 
                          new ImageIcon(this.getClass().getResource("/hu/szakdolgozat/poker/adatFajlok/korongok/big_blind_blur.png")));
        nagyVak.setErtek(nagyVakErtek);
        dealer = new Dealer(new ImageIcon(this.getClass().getResource("/hu/szakdolgozat/poker/adatFajlok/korongok/dealer.png")), 
                            new ImageIcon(this.getClass().getResource("/hu/szakdolgozat/poker/adatFajlok/korongok/dealer_blur.png")));
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
        } else if (isJatekterPanelBetoltve() && xmlAdatok != null) {
            itr = xmlAdatok.get("KeveresAnimacio").iterator();
            g2D.drawImage(keveresAnimacio, (int) (double) itr.next(), (int) (double) itr.next(), (int) (double) itr.next(), (int) (double) itr.next(), jatekterPanel);
        }

        if (jatekosokKartyalapjai != null) {
            List<Kartyalap> jatekosKartyalapok;
            
            for (Map.Entry<Byte, List<Kartyalap>> entry : jatekosokKartyalapjai.entrySet()) {
                jatekosKartyalapok = entry.getValue();
                for (Kartyalap kartyalap : jatekosKartyalapok) {
                    kartyalap.rajzol(g2D, jatekterPanel);
                }
            }
        }

        if (kiszalltJatekosokKartyalapjai != null) {
            List<Kartyalap> jatekosKartyalapok;
            
            for (Map.Entry<Byte, List<Kartyalap>> entry : kiszalltJatekosokKartyalapjai.entrySet()) {
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

    /**
     * Kirajzolja a pot-ot a panelre.
     * 
     * @param g2D 
     */
    public void potRajzol(Graphics2D g2D) {
        if (pot != null) {
            for (Zseton zseton : pot) {
                zseton.rajzol(g2D, jatekterPanel);
            }
        }
    }
    
    /**
     * Kirajzolja a korongokat a panelre.
     * 
     * @param g2D 
     */
    public void korongokRajzol(Graphics2D g2D){
        if(korongok != null){
            for (Korong korong : korongok) {
                korong.rajzol(g2D, jatekterPanel);
            }
        }
    }
    
    /**
     * Kirajzolja a döntési felhőt a panelre.
     * 
     * @param g 
     */
    public void felhoRajzol(Graphics g){
        if (felhoMozgato != null && felhoMozgato.isAlive()) {
            felho.rajzol(g, jatekterPanel);
        }
    }
    
    /**
     * Kirajzolja a nyertest a panelre.
     *
     * @param g2D
     */
    public void nyertesRajzol(Graphics2D g2D) {
        if (nyertesMozgato != null && nyertesMozgato.isAlive()) {
            nyertes.rajzol(g2D, jatekterPanel);
        }
    }
    
    public void toltesRajzol(Graphics2D g2D){
        if(varakozasMozgato != null && varakozasMozgato.isAlive()){
            toltes.rajzol(g2D, jatekterPanel);
        }
    }

    /**
     * Be vagy kikapcsolja az átadott paraméter szerint a grafikai elemek
     * elmosását.
     * 
     * @param elmosas 
     */
    public void grafikaElmosas(boolean elmosas){
        jatekterPanel.setElmosas(elmosas);
        
        for (Kartyalap kartyalap : kartyalapok) {
            kartyalap.setElmosas(elmosas);
        }
        
        for (Map.Entry<Byte, List<Kartyalap>> entrySet : jatekosokKartyalapjai.entrySet()) {
            List<Kartyalap> jatekosKartyalapjai = entrySet.getValue();

            for (Kartyalap kartyalap : jatekosKartyalapjai) {
                kartyalap.setElmosas(elmosas);
            }
        }
        
        if (kiszalltJatekosokKartyalapjai != null) {
            for (Map.Entry<Byte, List<Kartyalap>> entrySet : kiszalltJatekosokKartyalapjai.entrySet()) {
                List<Kartyalap> jatekosKartyalapjai = entrySet.getValue();

                for (Kartyalap kartyalap : jatekosKartyalapjai) {
                    kartyalap.setElmosas(elmosas);
                }
            }
        }

        for (Map.Entry<Byte, List<Zseton>> entrySet : jatekosokZsetonjai.entrySet()) {
            List<Zseton> zsetonok = entrySet.getValue();

            for (Zseton zseton : zsetonok) {
                zseton.setElmosas(elmosas);
            }
        }
        
        for (Zseton zseton : pot) {
            zseton.setElmosas(elmosas);
        }
        
        for (Korong korong : korongok) {
            korong.setElmosas(elmosas);
        }
    }
    
    /**
     * Létrehozza a játékosokat és beállítja a neveik pozícióját, hogy az asztal szélétől azonos
     * távolságra legyenek.
     */
    public void jatekosokBeallit() {
        String[] jatekosNevek = {"Sanyi", "Isti", "Gergő", "András", "Eszti",
            "Szabi", "Reni", "Józsi", "Kriszti", "Ati", "Livi", "Bazsi", "Roli", "Krisztián"};
        String jatekosNev;
        boolean talalat = false;

        if (jatekosok == null) {//A játékállás betöltésekor nem null.
            jatekosok = new ArrayList<>();
            jatekosok.add(new Jatekos(emberJatekosNev));

            while (jatekosok.size() != jatekosokSzama) {
                jatekosNev = jatekosNevek[(int) (Math.random() * (jatekosNevek.length - 1))];
                for (Jatekos jatekos : jatekosok) {
                    if (jatekos.getNev().equals(jatekosNev)) {
                        talalat = true;
                    }
                }

                if (!talalat) {
                    jatekosok.add(new Jatekos(jatekosNev));
                }

                talalat = false;
            }
        }

        int i = 0;
        double elteres = jatekterPanelMagassag() / 8.275862;
        List<Point> vegpontLista = SzogSzamito.vegpontLista(jatekosokSzama, jatekterPanelSzelesseg(), jatekterPanelMagassag());
        Point vegpont;

        for (Jatekos jatekos : jatekosok) {
            vegpont = vegpontLista.get(i++);
            jatekos.setKx(vegpont.getX() + elteres * Math.cos(Math.toRadians(SzogSzamito.szogSzamit(jatekterPanelSzelesseg(), jatekterPanelMagassag(), vegpont.getX(), vegpont.getY()))));
            jatekos.setKy(vegpont.getY() + elteres * Math.sin(Math.toRadians(SzogSzamito.szogSzamit(jatekterPanelSzelesseg(), jatekterPanelMagassag(), vegpont.getX(), vegpont.getY()))));
            jatekos.setFont(new Font("Arial", 1, jatekterPanelMagassag() / 60));
        }
    }

    /**
     * Kiosztja a játékosoknak a zsetonokat és létre hoz egy pot-ot.
     */
    public void zsetonokKioszt() {
        if (pot == null) {
            pot = new CopyOnWriteArrayList<>();
        }
        
        zsetonMozgato = new ZsetonMozgato(this);
        zsetonMozgato.zsetonokBetolt();     
    }
    
    /**
     * A játékosok tétjeit berakja a potba és elindítja az ehhez tartozó
     * animációt.
     * 
     * @param jatekosSorszam
     * @param osszeg 
     */
    public void zsetonokPotba(byte jatekosSorszam, int osszeg){
        zsetonMozgato = new ZsetonMozgato(this);            
        zsetonMozgato.setJatekosSorszam(jatekosSorszam);
        zsetonMozgato.setJatekosTetOsszege(osszeg);
        zsetonMozgato.setJatekosTetMozgatasa(true);
        zsetonMozgato.start();
    }
    
    /**
     * Elindítja a játék panel szálat.
     */
    public void jatekterSzalIndit(){
        Thread jatekterSzal = new Thread(jatekterPanel);
        jatekterSzal.start();
    }
    
    /**
     * Leállítja a játék panel szálat.
     */
    public void jatekterSzalLeallit(){
        jatekterPanel.setSzalStop(true);
    }
    
    /**
     * Létrehoz egy jatekVezerlo objektumot.
     */
    public void jatekVezerloIndit() {
        if (jatekVezerlo == null) { //A játékállás betöltésekor nem null.
            jatekVezerlo = new JatekVezerlo(this, nagyVakErtek, vakErtekEmeles);
        } else {
            betoltottAdatokFrissit();
        }
        
        jatekVezerlo.start();
    }
    
    /**
     * Folytatja a jatekVezerlo szal futását.
     */
    public void jatekVezerlesFolytat(){
        jatekVezerlo.folytat();
    }
    
    public void jatekVezerloSzalLeallit(){
        jatekVezerlo.setSzalStop(true);
    }
    
    /**
     * Elindítja az mi szálat.
     */
    public void miSzalIndit() {
        if (!jatekterPanel.isMent()) {
            mi = new Mi(jatekVezerlo, this);
            mi.start();
        }
    }

    /**
     * Folytatja az mi szál futását.
     */
    public void miFolytat(){
        mi.folytat();
    }    
    
    /**
     * Leállítja az mi szál futását.
     */
    public void miSzalLeallit(){
        mi.setSzalStop(true);
    }
    
    /**
     * Megadja, hogy fut-e az mi szál.
     * 
     * @return 
     */
    public boolean isMiSzalFut() {
        return mi != null && mi.isAlive();
    }
    
    /**
     * Betölti a kártya paklit.
     * 
     * @param dealer
     */
    public void kartyaPakliBetolt(byte dealer){    
        kartyaMozgato = new KartyaMozgato(this);
        kartyaMozgato.setDealer(dealer);
        kartyaMozgato.pakliBetolt();
    }
    
    /**
     * Kiosztja a kártyalapokat a játékosoknak.
     * 
     * @param dealer 
     */
    public void kartyalapokKiosztSzalIndit(byte dealer) {        
        xmlAdatok = AdatKezelo.aranyErtekekBetolt("GrafikaElemek", 
                new Dimension(jatekterPanelSzelesseg(), jatekterPanelMagassag()));
        kartyalapok = null;
        leosztottKartyalapok = new CopyOnWriteArrayList<>();
        jatekosokKartyalapjai = new ConcurrentHashMap<>();
        kiszalltJatekosokKartyalapjai = new ConcurrentHashMap<>();
        
        kartyaPakliBetolt(dealer);
        kartyaMozgato.setKartyalapokKiosztasa(true);
        kartyaMozgato.start();
    }
    
    /**
     * Leosztja a kártyalapokat.
     */
    public void kartyalapokLeosztSzalIndit(){
        kartyaMozgato = new KartyaMozgato(this);
        kartyaMozgato.setKartyalapLeosztasa(true);
        kartyaMozgato.start();
    }
    
    /**
     * Elindítja a kártyalap bedobás animációt.
     * 
     * @param jatekosSorszam 
     */
    public void kartyalapokBedobSzalIndit(byte jatekosSorszam){
        kartyaMozgato = new KartyaMozgato(this);
        kartyaMozgato.setJatekosSorszam(jatekosSorszam);
        kartyaMozgato.setKartyalapokBedobasa(true);
        kartyaMozgato.start();
    }
    
    /**
     * Elindítja a játék menü háttér animációt.
     */
    public void jatekMenuAnimacioIndit(){        
        kartyalapok = null;
        kartyaMozgato = new KartyaMozgato(this);
        kartyaMozgato.setMenuAnimacio(true);
        kartyaMozgato.start();
    }

    /**
     * Megállítja a játék menü háttér animációt
     */
    public void jatekMenuAnimacioLegallit() {
        if(kartyaMozgato != null) kartyaMozgato.setSzalStop(true);//akkor null, ha a játékmenüböl betöltünk egy játékállást és utánna egyből kilépünk
    }
    
    /**
     * Beállítja a korongokat a megfelelő játékoshoz.
     * 
     * @param dealer 
     */
    public void korongokMozgatSzalIndit(byte dealer){
        KorongMozgato korongMozgato = new KorongMozgato(this);
        korongMozgato.setDealer(dealer);
        korongMozgato.start();
    }
    
    /**
     * Elindítja a döntési felhő animációt a megfelelő paraméterekkel.
     * 
     * @param nev
     * @param jatekosSorszam 
     */
    public void felhoSzalIndit(String nev, byte jatekosSorszam){
        Image felhoKep = new ImageIcon(this.getClass().getResource("/hu/szakdolgozat/poker/adatFajlok/felho/felho.png")).getImage();
        Jatekos jatekos = getJatekosok().get(jatekosSorszam);        
        double kx = jatekos.getKx(), ky = jatekos.getKy();  
        felho = new Felho(felhoKep, nev);             
        felho.setKx(kx);
        felho.setKy(ky);
        felho.setFont(new Font("", 0, 0));
        felhoMozgato = new FelhoMozgato(felho, this);
        felhoMozgato.start();
    }
    
    public void nyertesSzalIndit(){
        byte jatekosSorszam = aktivJatekosSorszamKeres((byte)0);
        Image nyertesKep = new ImageIcon(this.getClass().getResource("/hu/szakdolgozat/poker/adatFajlok/nyertes/nyertes.png")).getImage();
        Jatekos jatekos = getJatekosok().get(jatekosSorszam);
        nyertes = new Nyertes(jatekos.getNev(), nyertesKep);
        nyertesMozgato = new NyertesMozgato(nyertes, this);
        nyertesMozgato.start();
    }
    
    /**
     * Megkeresi a nyertes játékosokat és elindítja az ehez tartozó animációkat.
     */
    public void nyertesJatekosKeres(){ 
        kartyaMozgato = new KartyaMozgato(this);
        kartyaMozgato.setOsszesKartyalapLeosztasa(true);
        kartyaMozgato.setKartyalapLeosztasa(true);
        kartyaMozgato.setKartyalapokKiertekelese(true);
        
        zsetonMozgato = new ZsetonMozgato(this);
        zsetonMozgato.setPotNyertesekhezMozgatasa(true); 
        
        ExecutorService executor = Executors.newFixedThreadPool(1);  
        executor.submit(kartyaMozgato);
        executor.submit(zsetonMozgato);
        executor.shutdown();
    }
    
    /**
     * Elindítja a várakozás animációt. 
     */
    public void varakozasSzalIndit(){
        varakozasMozgato = new VarakozasMozgato(this);
        xmlAdatok = AdatKezelo.aranyErtekekBetolt("GrafikaElemek", new Dimension(jatekterPanelSzelesseg(), jatekterPanelMagassag()));
        itr = xmlAdatok.get("Toltes").iterator();
        toltes = new Toltes(new ImageIcon("src/hu/szakdolgozat/poker/adatFajlok/varakozas/tolt.png").getImage());
        toltes.setKx(itr.next());
        toltes.setKy(itr.next());
        toltes.setToltoKepMagassag(itr.next());
        toltes.setToltoKepSzelesseg(itr.next());
        varakozasMozgato.start();
    }
    
    /**
     * Leállítja a várakozás animáció szálat.
     */
    private void varakozasSzalLeallit() {
        if (varakozasMozgato != null) {
            varakozasMozgato.setSzalStop(true);
        }
    }

    /**
     * Kikeresi hogy mennyi aktív játékos van.
     * 
     * @return 
     */
    public byte aktivJatekosokKeres() {
        byte aktivJatekosokSzama = 0;
        
        for (byte i = 0; i < jatekosokSzama; i++) {
            if (isJatekosAktiv(i)) {
                aktivJatekosokSzama++;
            }
        }

        return aktivJatekosokSzama;
    }

    /**
     * Megkeresi hogy melyi játékos aktív.
     *
     * @param sorszam
     * @return
     */
    public byte aktivJatekosSorszamKeres(byte sorszam) {
        boolean aktivJatekosTalalat = false;
        
        while (!aktivJatekosTalalat) {
            if (sorszam == jatekosokSzama) {
                sorszam = 0;
            }

            if (!isJatekosAktiv(sorszam)) {
                sorszam++;
            } else {
                aktivJatekosTalalat = true;
            }
        }

        return sorszam;
    }
    
    /**
     * Hozzáad egy kártyalapot a leosztottKartyalapok listához. Ha a lista 
     * null-ra hivatkozik akkor létrehoz egy új ArrayList-et.
     * @param kartyalap 
     */
    public void leosztottKartyalapokhozAd(Kartyalap kartyalap){        
        leosztottKartyalapok.add(kartyalap);
    }    
   
    /**
     * Visszaadja a leosztott kártyalapok számát.
     * 
     * @return 
     */
    public int leosztottKartyalapokSzama() {
        return (leosztottKartyalapok == null) ? 0 : leosztottKartyalapok.size();
    }

    /**
     * Egy feltétel vizsgálattal eldönti, hogy a HashMap tartalmazza e a játékos azonosító kulcsot.
     * Ha igen, akkor a kulcshoz tartozó értéket ami egy lista, lekéri és hozzá adja a metódus paramétereként
     * megadott kártyalap objektumot. Ha nem, akkor beilleszti az új sorszámot és a hozzá tartozó listát.
     * Ha a jatekosokKartyalapjai Map null-ra hivatkozik akkor létrehoz egy új HashMap-et.
     * 
     * @param sorszam
     * @param kartyalap 
     */
    public void jatekosKartyalapokhozAd(byte sorszam, Kartyalap kartyalap) {   
        if (jatekosokKartyalapjai.containsKey(sorszam)) {
            jatekosokKartyalapjai.get(sorszam).add(kartyalap);
        } else {
            List<Kartyalap> jatekosKartyalapok = new CopyOnWriteArrayList<>();
            jatekosKartyalapok.add(kartyalap);
            jatekosokKartyalapjai.put(sorszam, jatekosKartyalapok);
        }
    }    

    /**
     * Hozzáadja a játékos tétjét a pothoz.
     * 
     * @param jatekosTetje 
     */
    public void pothozAd(List<Zseton> jatekosTetje) {
        pot.addAll(jatekosTetje);
        Collections.sort(pot);
    }
    
    public void jatekosKiszall(byte jatekosSorszam){
        kiszalltJatekosokKartyalapjai.put(jatekosSorszam, jatekosokKartyalapjai.remove(jatekosSorszam));
    }
    
    public void vakokErtekeBeallit(int kisVakErtek, int nagyVakErtek){
        kisVak.setErtek(kisVakErtek);
        nagyVak.setErtek(nagyVakErtek);
    }
    
    public int kisVakErtekVisszaad(){
        return kisVak.getErtek();
    }
    
    public int nagyVakErtekVisszaad(){
        return nagyVak.getErtek();
    }
    
    /**
     * Aktiválja a megfelelő gombokat a gombsorból.
     */
    public void gombSorAllapotvalt() {
        if (!jatekterPanel.isMent()) {
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
            jatekterPanel.setMegadandoOsszeg(jatekVezerlo.getOsszeg() - jatekVezerlo.getJatekosokTetje()[JatekVezerlo.EMBER_JATEKOS_SORSZAM]);
            jatekterPanel.setLepesKoz(kisVakErtekVisszaad());
            jatekterPanel.setEmelendoOsszeg(jatekVezerlo.getOsszeg() == 0 ? nagyVakErtekVisszaad() : jatekVezerlo.getOsszeg());
            jatekterPanel.setMinOsszeg(jatekVezerlo.getOsszeg() == 0 ? nagyVakErtekVisszaad() : jatekVezerlo.getOsszeg());
            jatekterPanel.setMaxOsszeg(getJatekosZsetonOsszeg(JatekVezerlo.EMBER_JATEKOS_SORSZAM));
        }
    }

    /**
     * Aktiválja azokat a játékosokat, akiknek vannak még zsetonjaik.
     */
    public void jatekosokAktival() {
        for (Jatekos jatekos : jatekosok) {
            if (jatekosokZsetonjai != null && !jatekosokZsetonjai.get(jatekos.getSorszam()).isEmpty()) {
                jatekos.setAktiv(true);
            }
        }
    }

    /**
     * Passziválja a paraméterként átadott sorszámhoz tartozó játékost.
     * 
     * @param jatekosSorszam 
     */
    public void jatekosPasszival(byte jatekosSorszam) {
        jatekosok.get(jatekosSorszam).setAktiv(false);
    }

    /**
     * Ha az emberi játékos passzol, akkor ez a metódus hívódik meg.
     */
    public void emberiJatekosPasszol() {
        jatekVezerlo.passzol();
    }

    /**
     * Ha az emberi játékos nyit, akkor ez a metódus hívódik meg.
     * 
     * @param osszeg 
     */
    public void emberiJatekosNyit(int osszeg) {
        jatekVezerlo.nyit(osszeg);
    }

    /**
     * Ha az emberi játékos emel, akkor ez a metódus hívódik meg.
     * 
     * @param osszeg 
     */
    public void emberiJatekosEmel(int osszeg) {
        jatekVezerlo.emel(osszeg);
    }

    /**
     * Ha az emberi játékos megadja a tétet, akkor ez a metódus hívódik meg.
     */
    public void emberiJatekosMegad() {
        jatekVezerlo.megad();
    }

    /**
     * Ha az emberi játékos felteszi az összes zsetonját, akkor ez a metódus 
     * hívódik meg.
     */
    public void emberiJatekosAllIn() {
        jatekVezerlo.allIn();
    }

    /**
     * Ha az emberi játékos bedobja a lapjait, akkor ez a metódus hívódik meg.
     */
    public void emberiJatekosBedob() {
        jatekVezerlo.bedob();
    }
    
    /**
     * Frissíti a menü panelt.
     */
    public void menuPanelFrissit() {
        jatekMenuPanel.repaint();
    }

    /**
     * Az beállítja a grafika elemek méreteit és pozícióit a felbontáshoz igazítva és korrigál néhány eltérést.
     */
    public void betoltottAdatokFrissit() {
        byte dealerJatekosSorszam = jatekVezerlo.getDealerJatekosSorszam();
        keveresAnimacio = new ImageIcon(this.getClass().getResource("/hu/szakdolgozat/poker/adatFajlok/kartyaPakli/keveresAnimacio.gif")).getImage();
        jatekVezerlo.setJatekosBlokkol(true);
        
        if (jatekVezerlo.getAktJatekosSorszam() != JatekVezerlo.EMBER_JATEKOS_SORSZAM) {
            miSzalIndit();
        }
        
        kartyaPakliBetolt(dealerJatekosSorszam);
        korongokMozgatSzalIndit(dealerJatekosSorszam);
        zsetonokKioszt();
    }

    /**
     * Betölti az audió és játékmenet beállításokat.
     */
    private void beallitasokBetolt() {
        List<String> adatok;
        Iterator<String> itr;

        adatok = AdatKezelo.beallitasBetolt(AdatKezelo.JATEKMENET);
        itr = adatok.iterator();
        emberJatekosNev = itr.next();
        jatekosokSzama = Byte.parseByte(itr.next());
        zsetonOsszeg = Integer.parseInt(itr.next());
        nagyVakErtek = Integer.parseInt(itr.next());
        vakErtekEmeles = Byte.parseByte(itr.next());
    }
    
    /**
     * Kilép a főmenübe.
     */
    public void kilepes(){     
        jatekterSzalLeallit();
        varakozasSzalLeallit();
        feluletKezelo.jatekMenuPanelBetolt(feluletKezelo);
    }
    
    /**
     * Visszaadja a játéktér panel szélességét.
     * 
     * @return 
     */
    public int jatekterPanelSzelesseg(){
        return jatekterPanel.getWidth();
    }
    
    /**
     * Visszaadja a játéktér panel magasságát.
     * 
     * @return 
     */
    public int jatekterPanelMagassag(){
        return jatekterPanel.getHeight();
    }
 
    /**
     * Visszaadja a játék menü panel szélességét.
     *
     * @return
     */
    public int jatekMenuPanelSzelesseg() {
        return jatekMenuPanel.getWidth();
    }

    /**
     * Visszaadja a játék menü panel magasságát.
     *
     * @return
     */
    public int jatekMenuPanelMagassag() {
        return jatekMenuPanel.getHeight();
    }
    
    public void setKartyalapok(List<Kartyalap> kartyalapok) {
        this.kartyalapok = kartyalapok;
    }

    public void setNyertesPokerKezek(Map<Byte, PokerKez> nyertesPokerKezek) {
        this.nyertesPokerKezek = nyertesPokerKezek;
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
    
    public void setJatekTerPanel(JatekterPanel jatekTerPanel) {
        this.jatekterPanel = jatekTerPanel;
    }

    public void setJatekMenuPanel(JatekMenuPanel jatekMenuPanel) {
        this.jatekMenuPanel = jatekMenuPanel;
    }

    public void setFeluletKezelo(FeluletKezelo feluletKezelo) {
        this.feluletKezelo = feluletKezelo;
    }

    public void setMenthet(boolean menthet) {
        this.menthet = menthet;
    }

    public Map<Byte, List<Kartyalap>> getJatekosokKartyalapjai() {
        return jatekosokKartyalapjai;
    }

    public Map<Byte, List<Kartyalap>> getKiszalltJatekosokKartyalapjai() {
        return kiszalltJatekosokKartyalapjai;
    }

    public List<Kartyalap> getLeosztottKartyalapok() {
        return leosztottKartyalapok;
    }

    public List<Kartyalap> getKartyalapok() {
        return kartyalapok;
    }

    public Map<Byte, PokerKez> getNyertesPokerKezek() {
        return nyertesPokerKezek;
    }

    public List<Jatekos> getJatekosok() {
        return jatekosok;
    }    
    
    public byte getJatekosokSzama(){
        return jatekosokSzama;
    }
    
    public Map<Byte, List<Zseton>> getJatekosokZsetonjai() {
        return jatekosokZsetonjai;
    }    
    
    public int getZsetonOsszeg() {
        return zsetonOsszeg;
    }
    
    /**
     * A paraméterként átadot sorszámhoz tartozó játékos zseonjainak összegét
     * adja vissza.
     * 
     * @param jatekosSorszam
     * @return 
     */
    public int getJatekosZsetonOsszeg(byte jatekosSorszam) {
        return ZsetonKezelo.zsetonokOsszege(jatekosokZsetonjai.get(jatekosSorszam));
    }

    public List<Zseton> getPot() {
        return pot;
    }    

    public Vak getKisVak() {
        return kisVak;
    }

    public Vak getNagyVak() {
        return nagyVak;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public List<Korong> getKorongok() {
        return korongok;
    }

    public Felho getFelho() {
        return felho;
    }

    public Nyertes getNyertes() {
        return nyertes;
    }

    public Toltes getToltes() {
        return toltes;
    }
    
    public boolean isJatekterPanelBetoltve(){
            return jatekterPanel != null;
    }    

    public boolean isMenthet() {
        return menthet;
    }
    
    public boolean isJatekosAktiv(byte jatekosSorszam) {
        return jatekosok.get(jatekosSorszam).isAktiv();
    }
    
    public boolean isKartyaGrafikaElore() {
        return kartyaGrafikaElore;
    }
}
