package hu.szakdolgozat.poker.vezerloOsztalyok.szalak;

import hu.szakdolgozat.poker.vezerloOsztalyok.AdatKezelo;
import java.util.logging.Level;
import java.util.logging.Logger;
import hu.szakdolgozat.poker.vezerloOsztalyok.SzalVezerlo;
import java.io.Serializable;

public class JatekVezerlo extends Thread implements Serializable{

    public static final byte EMBER_JATEKOS_SORSZAM = 0;
    public static final byte LEOSZTHATO_KARTYALAPOK_SZAMA = 5;
    private byte korokSzama;
    private SzalVezerlo szalVezerlo;
    private byte dealerJatekosSorszam;
    private byte kisVakJatekosSorszam;
    private byte nagyVakJatekosSorszam;
    private byte aktJatekosSorszam;
    private byte jatekosokSzama;    
    private byte aktivJatekosokSzama;
    private byte allInSzamlalo;
    private int osszeg;
    private int[] jatekosokTetje;
    private byte licitSzamlalo;
    private boolean nyithat;
    private boolean passzolhat;
    private boolean megadhat;
    private boolean emelhet;
    private boolean ujkorIndit;
    private boolean korVege;
    private boolean jatekosBlokkol;
    private boolean szalStop;
    private int kisVakErtek;
    private int nagyVakErtek;
    private int aktJatekosZsetonOsszeg;
    private byte korOszto;
    private long ido;
    
    public JatekVezerlo(SzalVezerlo szalVezerlo, int nagyVakErtek, byte korOszto){
        this.szalVezerlo = szalVezerlo;
        this.nagyVakErtek = nagyVakErtek;
        this.korOszto = korOszto;
        kisVakErtek = nagyVakErtek / 2;
        jatekosokSzama = szalVezerlo.getJatekosokSzama();
        jatekosokTetje = new int[jatekosokSzama];
        dealerJatekosSorszam = (byte) (Math.random() * jatekosokSzama);
        ido = 1500;
        ujkorIndit = true;
        szalVezerlo.zsetonokKioszt();   
    }   

    /**
     * Elindít egy új kört.
     */
    private void ujKor() {   
        szalVezerlo.jatekosokAktival();
        aktivJatekosokSzama = szalVezerlo.aktivJatekosokKeres();
        
        if (aktivJatekosokSzama > 1 && szalVezerlo.isJatekosAktiv(EMBER_JATEKOS_SORSZAM)) {
            jatekosSorszamokBeallit();
            allInSzamlalo = 0;
            licitSzamlalo = 0;
            szalVezerlo.korongokMozgatSzalIndit(dealerJatekosSorszam);
            szalVezerlo.kartyalapokKiosztSzalIndit(dealerJatekosSorszam);
            megallit();
            vakokErtekeBeallit();
            ujkorIndit = false;
            lehetosegekBeallit();
        } else if (aktivJatekosokSzama == 1) {
            szalVezerlo.jatekVezerloSzalLeallit();
            folytat();
            AdatKezelo.jatekAllasTorol();
            szalVezerlo.nyertesSzalIndit();
        } else {
            szalVezerlo.miSzalLeallit();
            szalVezerlo.jatekVezerloSzalLeallit();
            folytat();
            AdatKezelo.jatekAllasTorol();
            szalVezerlo.kilepes();
        }
    }

    /**
     * Beállítja a kisvak, nagyvak, dealer és az aktuális aktív játékos sorszámát
     * rekurzió segítségével az új kör indításakor.
     */
    private void jatekosSorszamokBeallit(){
        byte eltolas = 1;
        dealerJatekosSorszam += eltolas;
        dealerJatekosSorszam = szalVezerlo.aktivJatekosSorszamKeres(dealerJatekosSorszam);
        kisVakJatekosSorszam = szalVezerlo.aktivJatekosSorszamKeres((byte) (dealerJatekosSorszam + eltolas));
        nagyVakJatekosSorszam = szalVezerlo.aktivJatekosSorszamKeres((byte) (kisVakJatekosSorszam + eltolas));
        aktJatekosSorszam = szalVezerlo.aktivJatekosSorszamKeres((byte) (nagyVakJatekosSorszam + eltolas));        
    }
    
    /**
     * Beállítja a vakok értékeit.
     */
    private void vakokErtekeBeallit() {        
        if (korOszto > 0 && korokSzama > 0 && korokSzama % korOszto == 0) {
            kisVakErtek *= 2;
            nagyVakErtek = kisVakErtek * 2;
            szalVezerlo.vakokErtekeBeallit(kisVakErtek, nagyVakErtek);
        }
   
        osszeg = nagyVakErtek;
        korokSzama++;

        /*Ezek az utasítások a kisvak és a nagyvak játékosok zsetonjaiból
          automatikusan berakják a potba a vakoknak megfelelő értéket. A a kisvak játékos vagy a nagyvak játékosnak nincs elég zsetonja akkor allint hív*/
        if (kisVakErtek >= (aktJatekosZsetonOsszeg = szalVezerlo.getJatekosZsetonOsszeg(kisVakJatekosSorszam))) {
            allInSzamlalo++;
            aktivJatekosokSzama--;
            jatekosokTetje[kisVakJatekosSorszam] = aktJatekosZsetonOsszeg;
            szalVezerlo.zsetonokPotba(kisVakJatekosSorszam, aktJatekosZsetonOsszeg);
            szalVezerlo.jatekosPasszival(kisVakJatekosSorszam);
        } else {
            jatekosokTetje[kisVakJatekosSorszam] = kisVakErtek;
            szalVezerlo.zsetonokPotba(kisVakJatekosSorszam, kisVakErtek);
        }

        if (nagyVakErtek >= (aktJatekosZsetonOsszeg = szalVezerlo.getJatekosZsetonOsszeg(nagyVakJatekosSorszam))) {
            allInSzamlalo++;
            aktivJatekosokSzama--;
            jatekosokTetje[nagyVakJatekosSorszam] = aktJatekosZsetonOsszeg;
            szalVezerlo.zsetonokPotba(nagyVakJatekosSorszam, aktJatekosZsetonOsszeg);
            szalVezerlo.jatekosPasszival(nagyVakJatekosSorszam);
        } else {
            jatekosokTetje[nagyVakJatekosSorszam] = nagyVakErtek;
            szalVezerlo.zsetonokPotba(nagyVakJatekosSorszam, nagyVakErtek);
        }
    }
    
    /**
     * Beállítja a soron következő játékos lehetőségeit.
     */
    private void lehetosegekBeallit() {
        aktJatekosZsetonOsszeg = szalVezerlo.getJatekosZsetonOsszeg(aktJatekosSorszam);

        if (aktJatekosZsetonOsszeg <= osszeg - jatekosokTetje[aktJatekosSorszam]) { //Ez a lehetőség akkor van, ha az aktuális játékos zsetonjainak értéke kevesebb mint az előzőleg feltett tét
            nyithat = false;
            emelhet = false;
            megadhat = false;
            passzolhat = false;
        } else if (aktJatekosZsetonOsszeg <= osszeg * 2 - jatekosokTetje[aktJatekosSorszam]) {
            nyithat = false;
            emelhet = false;
            megadhat = true;
            passzolhat = false;
        } else if (osszeg == 0) { //Ez a lehetőség akkor van, ha a licitkörben még senki nem tett fel tétet.
            nyithat = true;
            emelhet = false;
            megadhat = false;
            passzolhat = true;
        } else if (jatekosokTetje[aktJatekosSorszam] == osszeg) { //Új kör és első licit kör esetén ha a játékos a nagyvak, akkor van ez a lehetőség.
            nyithat = false;
            emelhet = true;
            megadhat = false;
            passzolhat = true;
        } else {
            nyithat = false;
            emelhet = true;
            megadhat = true;
            passzolhat = false;
        }
        
        if (gepiJatekos()) {
            if (szalVezerlo.isMiSzalFut()) {
                szalVezerlo.miFolytat();
            } else {
                szalVezerlo.miSzalIndit();
            }
        } else {
            szalVezerlo.gombSorAllapotvalt();
        }
    }
        
    /**
     * A következő játékosra lép.
     */
    public void kovetkezoJatekos() {
        jatekosAllapotEllenorzes();
        if (!ujkorIndit) {
            lehetosegekBeallit();
            szalVezerlo.setMenthet(true);
        }
    }
    
    /**
     * Leellenőrzi, hogy gépi játékos-e az aktuális játékos.
     * 
     * @return 
     */
    public boolean gepiJatekos(){        
        return (aktJatekosSorszam != EMBER_JATEKOS_SORSZAM);
    }
    
    /**
     * Kiválasztja az aktuális játékos sorszámát az aktív játékosok közül.
     */
    private void jatekosAllapotEllenorzes(){
        aktJatekosSorszam++;         
        licitSzamlaloLeptet();   

        if (aktJatekosSorszam == jatekosokSzama) {
            aktJatekosSorszam = 0;
        }
        
        if (!szalVezerlo.isJatekosAktiv(aktJatekosSorszam)) { //Ha az adott sorszámú játékos nem aktív akkor a metódus meghívja önmagát.
            jatekosAllapotEllenorzes();
        }
    }
    
    /**
     * A kör végén a nyertes játékosok keresését indítja el.
     */
    private void korVege(){ 
        szalVezerlo.nyertesJatekosKeres();
        korVege = false;
        ujkorIndit = true;
    }
    
    /**
     * Ha a játékos paszsol akkor ez a metódus hívódik meg.
     */
    public void passzol(){        
        szalVezerlo.felhoSzalIndit("Passzol", aktJatekosSorszam);
        folytat();
    }
    
    /**
     * Ha a játékos nyit akkor ez a metódus hívódik meg és hozzá adja a pothoz
     * a játékos tétjét.
     * 
     * @param nyitoOsszeg
     */
    public void nyit(int nyitoOsszeg){
        osszeg = nyitoOsszeg;
        jatekosokTetje[aktJatekosSorszam] = osszeg;
        szalVezerlo.zsetonokPotba(aktJatekosSorszam, osszeg);     
        licitSzamlalo = 0;        
        szalVezerlo.felhoSzalIndit("Nyit", aktJatekosSorszam);
        folytat();
    }
    
    /**
     * Ha a játékos emel akkor ez a metódus hívódik meg és ha már van a 
     * játékosnak tétje, akkor az összegből levonja, hozzá adja az emelt tétet
     * és ezt az összeget adja hozzá a pothoz.
     * @param emeltOsszeg
     */
    public void emel(int emeltOsszeg){        
        osszeg -= jatekosokTetje[aktJatekosSorszam];
        osszeg += emeltOsszeg;
        jatekosokTetje[aktJatekosSorszam] += osszeg;
        szalVezerlo.zsetonokPotba(aktJatekosSorszam, osszeg);     
        osszeg = jatekosokTetje[aktJatekosSorszam];
        licitSzamlalo = 0;
        szalVezerlo.felhoSzalIndit("Emel", aktJatekosSorszam);
        folytat();
    }
    
    /**
     * Ha a játékos megadja a tétet akkor ez a metódus hívódik meg és ha már
     * van a játékosnak tétje akkor az levónódik az összegból majd az új összeg
     * hozzá adódik a pothoz.
     */
    public void megad(){
        osszeg -= jatekosokTetje[aktJatekosSorszam];
        jatekosokTetje[aktJatekosSorszam] += osszeg;
        szalVezerlo.zsetonokPotba(aktJatekosSorszam, osszeg);
        osszeg = jatekosokTetje[aktJatekosSorszam];
        szalVezerlo.felhoSzalIndit("Megad", aktJatekosSorszam);
        folytat();
    }
    
    /**
     * Ha a játékos az összes zsetonját felteszi tétnek akkor ez a metódus 
     * hívódik meg. Ha a megett összeg nagyobb mint az előző játékos tétje,
     * akkor a licitSzamlalo-t kinullázza mivel ez emelésnek számít.
     * Ez a metódus azt is figyeli, hogy van-e még aktív játékos.
     * Ha nincs akkor meghívja a korVege() metódust.
     */
    public void allIn(){
        int elozoOsszeg = osszeg;
        osszeg = szalVezerlo.getJatekosZsetonOsszeg(aktJatekosSorszam);        
        szalVezerlo.zsetonokPotba(aktJatekosSorszam, osszeg);
                
        if(osszeg > elozoOsszeg) licitSzamlalo = 0;
        else osszeg = elozoOsszeg;
        
        szalVezerlo.jatekosPasszival(aktJatekosSorszam);
        aktivJatekosokSzama--;
        allInSzamlalo++;
        szalVezerlo.felhoSzalIndit("All in", aktJatekosSorszam);
        
        if (aktivJatekosokSzama == 0) {               
            korVege = true;
        }
        
        folytat();
    }
        
    /**
     * Ha a játékos bedobja a lapjait akkor ez a metódus hívódik meg, ami 
     * csökkenti az aktív játékosok számát egyel. Ha az aktív játékosok száma 1,
     * akkor a korVege metódust hívja meg.
     */
    public void bedob(){
        szalVezerlo.jatekosPasszival(aktJatekosSorszam);
        aktivJatekosokSzama--;
        szalVezerlo.felhoSzalIndit("Bedob", aktJatekosSorszam);
        szalVezerlo.jatekosKiszall(aktJatekosSorszam);
        szalVezerlo.kartyalapokBedobSzalIndit(aktJatekosSorszam);
        
        if (aktivJatekosokSzama == 1 && allInSzamlalo == 0) {
            korVege = true;
        } else if (aktivJatekosokSzama == 0) {
            korVege = true;
        }
        
        folytat();
    }
    
    /**
     * A licitSzamlalo értékét inkrementálja. Ha az érték egyenlő a játékosok
     * számával akkor az ujLicitkor() metódust hívja meg.
     */
    private void licitSzamlaloLeptet() {
        if (++licitSzamlalo == jatekosokSzama) {
            ujLicitkor();
        } 
    }

    /**
     * Elindít egy új licit kört. Ha a leosztott kártyalapok száma 5 akkor
     * meghívja a korVege() metódust.
     */
    private void ujLicitkor(){             
        aktJatekosSorszam = kisVakJatekosSorszam;
        
        if (szalVezerlo.leosztottKartyalapokSzama() < LEOSZTHATO_KARTYALAPOK_SZAMA && aktivJatekosokSzama > 1) {            
            szalVezerlo.kartyalapokLeosztSzalIndit();
            megallit();
        } else {
            korVege();
        }
        
        jatekosokTetje = new int[jatekosokSzama];
        osszeg = 0;
        licitSzamlalo = 0;
    }
    
    /**
     * Várakozó állapotba állítja a szálat.
     */
    private synchronized void megallit(){
        try {
            wait();
        } catch (InterruptedException ex) {
            Logger.getLogger(JatekVezerlo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Felébreszti a szálat.
     */
    public synchronized void folytat(){
        notify();     
    }
    
    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {
        while (true) {
            if (ujkorIndit) {
                ujKor();
            } else if (korVege) {
                korVege();
            } else if(!jatekosBlokkol){            
                kovetkezoJatekos();
            } else{
                if (!gepiJatekos()) {
                    szalVezerlo.gombSorAllapotvalt();
                }
                
                jatekosBlokkol = false;
            }

            if (szalStop) {
                break;
            }

            megallit();
            szalVezerlo.setMenthet(false);
            
            try {
                sleep(ido);
            } catch (InterruptedException ex) {
                Logger.getLogger(JatekVezerlo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setSzalStop(boolean szalStop) {
        this.szalStop = szalStop;
    }

    public void setJatekosBlokkol(boolean jatekosBlokkol) {
        this.jatekosBlokkol = jatekosBlokkol;
    }
    
    public boolean isNyithat() {
        return nyithat;
    }

    public boolean isPasszolhat() {
        return passzolhat;
    }

    public boolean isMegadhat() {
        return megadhat;
    }

    public boolean isEmelhet() {
        return emelhet;
    }

    public int getOsszeg() {
        return osszeg;
    }

    public byte getAktJatekosSorszam() {
        return aktJatekosSorszam;
    }

    public byte getDealerJatekosSorszam() {
        return dealerJatekosSorszam;
    }

    public int[] getJatekosokTetje() {
        return jatekosokTetje;
    }

    public int getKisVakErtek() {
        return kisVakErtek;
    }

    public int getNagyVakErtek() {
        return nagyVakErtek;
    }
}
