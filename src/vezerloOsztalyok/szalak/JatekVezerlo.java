package vezerloOsztalyok.szalak;

import java.util.logging.Level;
import java.util.logging.Logger;
import vezerloOsztalyok.Mi;
import vezerloOsztalyok.SzalVezerlo;

public class JatekVezerlo extends Thread{
    public static final byte EMBER_JATEKOS_SORSZAM = 0;
    public static final byte LEOSZTHATO_KARTYALAPOK_SZAMA = 5;
    private byte korokSzama;
    private SzalVezerlo szalVezerlo;    
    private byte dealerJatekosSorszam;    
    private byte kisVakJatekosSorszam;
    private byte nagyVakJatekosSorszam;
    private byte jatekosSorszam;
    private byte jatekosokSzama;    
    private byte aktivJatekosokSzama;
    private byte allInSzamlalo;
    private int osszeg;
    private int[] jatekosokTetje;
    private byte licitSzamlalo;
    private Mi mi;
    private boolean nyithat;
    private boolean passzolhat;
    private boolean megadhat;
    private boolean emelhet;
    private boolean ujkorIndit;
    private boolean korVege;
    private boolean szalStop;
    private int kisVakErtek;
    private int nagyVakErtek;
    private int aktJatekosZsetonOsszeg;
    private byte korOszto;
    private long ido;
    
    public JatekVezerlo(SzalVezerlo szalVezerlo){
        this.szalVezerlo = szalVezerlo;
        this.jatekosokSzama = szalVezerlo.jatekosokSzama();
        kisVakErtek = szalVezerlo.kisVakErtekVisszaad();
        nagyVakErtek = szalVezerlo.nagyVakErtekVisszaad();
        jatekosokTetje = new int[jatekosokSzama];
        dealerJatekosSorszam = (byte) (Math.random() * jatekosokSzama);
        mi = new Mi();     
        korOszto = 10;
        ido = 1000;
        ujkorIndit = true;
        szalVezerlo.zsetonokKioszt();   
    }   

    /**
     * Elindít egy új kört.
     */
    private void ujKor() {
        korokSzama++;
        szalVezerlo.jatekosokAktival();
        aktivJatekosokSzama = szalVezerlo.aktivJatekosokKeres();
        
        if (aktivJatekosokSzama > 1) {
            jatekosSorszamokBeallit();
            osszeg = nagyVakErtek;
            allInSzamlalo = 0;
            licitSzamlalo = 0;
            szalVezerlo.korongokMozgatSzalIndit(dealerJatekosSorszam);
            szalVezerlo.kartyalapokKiosztSzalIndit(dealerJatekosSorszam);
            szalVezerlo.ujPot();
            megallit();
            vakokErtekeBeallit();
            lehetosegekBeallit();
            ujkorIndit = false;
        } else {
            szalStop = true;
            folytat();
            szalVezerlo.nyertesSzalIndit();            
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
        jatekosSorszam = szalVezerlo.aktivJatekosSorszamKeres((byte) (nagyVakJatekosSorszam + eltolas));        
    }
    
    /**
     * Beállítja a vakok értékeit.
     */
    private void vakokErtekeBeallit() {
        if (korokSzama % korOszto == 0) {
            kisVakErtek *= 2;
            nagyVakErtek = kisVakErtek * 2;
            szalVezerlo.vakokErtekeBeallit(kisVakErtek, nagyVakErtek);
        }

        /*Ezek az utasítások a kisvak és a nagyvak játékosok zsetonjaiból
          automatikusan berakják a potba a vakoknak megfelelő értéket.*/
        jatekosokTetje[kisVakJatekosSorszam] = kisVakErtek;
        jatekosokTetje[nagyVakJatekosSorszam] = nagyVakErtek;
        szalVezerlo.zsetonokPotba(kisVakJatekosSorszam, kisVakErtek);
        szalVezerlo.zsetonokPotba(nagyVakJatekosSorszam, nagyVakErtek);
    }
    
    /**
     * Beállítja a soron következő játékos lehetőségeit.
     */
    private void lehetosegekBeallit(){ 
        aktJatekosZsetonOsszeg = szalVezerlo.getJatekosZsetonOsszeg(jatekosSorszam);
        
        if (aktJatekosZsetonOsszeg <= osszeg-jatekosokTetje[jatekosSorszam]) { //Ez a lehetőség akkor van, ha az aktuális játékos zsetonjainak értéke kevesebb mint az előzőleg feltett tét
            nyithat = false;
            emelhet = false;
            megadhat = false;
            passzolhat = false;
        } else if (osszeg == 0) { //Ez a lehetőség akkor van, ha a licitkörben még senki nem tett fel tétet.
            nyithat = true;
            emelhet = false;
            megadhat = false;
            passzolhat = true;
        } else if (jatekosokTetje[jatekosSorszam] == osszeg) { //Új kör és első licit kör esetén ha a játékos a nagyvak, akkor van ez a lehetőség.
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

        /*---tesztelés---*/
        szalVezerlo.setMikezeles(nyithat, emelhet, megadhat, passzolhat);
        szalVezerlo.setjatekosSorszam(jatekosSorszam);
        szalVezerlo.setjatekosMegadandoOsszeg(osszeg);
        /*----------*/

        if (gepiJatekos()) {
            mi.kovetkezoJatekos(jatekosSorszam);
            mi.setLehetosegek(nyithat, emelhet, megadhat, passzolhat);
        } else szalVezerlo.gombSorAllapotvalt();
    }
        
    /**
     * A következő játékosra lép.
     */
    public void kovetkezoJatekos() {
        jatekosAllapotEllenorzes();
        lehetosegekBeallit();
    }
    
    /**
     * Leellenőrzi, hogy gépi játékos-e az aktuális játékos.
     * 
     * @return 
     */
    public boolean gepiJatekos(){        
        return (jatekosSorszam != EMBER_JATEKOS_SORSZAM);
    }
    
    /**
     * Kiválasztja az aktuális játékos sorszámát az aktív játékosok közül.
     */
    private void jatekosAllapotEllenorzes(){
        jatekosSorszam++;         
        licitSzamlaloLeptet();   

        if (jatekosSorszam == jatekosokSzama) {
            jatekosSorszam = 0;
        }

        if (!szalVezerlo.isJatekosAktiv(jatekosSorszam)) { //Ha az adott sorszámú játékos nem aktív akkor a metódus meghívja önmagát.
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
        szalVezerlo.felhoSzalIndit("Passzol", jatekosSorszam);
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
        jatekosokTetje[jatekosSorszam] = osszeg;
        szalVezerlo.zsetonokPotba(jatekosSorszam, osszeg);     
        licitSzamlalo = 0;        
        szalVezerlo.felhoSzalIndit("Nyit", jatekosSorszam);
        folytat();
    }
    
    /**
     * Ha a játékos emel akkor ez a metódus hívódik meg és ha már van a 
     * játékosnak tétje, akkor az összegből levonja, hozzá adja az emelt tétet
     * és ezt az összeget adja hozzá a pothoz.
     * @param emeltOsszeg
     */
    public void emel(int emeltOsszeg){        
        osszeg -= jatekosokTetje[jatekosSorszam];
        osszeg += emeltOsszeg;
        jatekosokTetje[jatekosSorszam] += osszeg;
        szalVezerlo.zsetonokPotba(jatekosSorszam, osszeg);     
        osszeg = jatekosokTetje[jatekosSorszam];
        licitSzamlalo = 0;
        szalVezerlo.felhoSzalIndit("Emel", jatekosSorszam);
        folytat();
    }
    
    /**
     * Ha a játékos megadja a tétet akkor ez a metódus hívódik meg és ha már
     * van a játékosnak tétje akkor az levónódik az összegból majd az új összeg
     * hozzá adódik a pothoz.
     */
    public void megad(){
        osszeg -= jatekosokTetje[jatekosSorszam];
        jatekosokTetje[jatekosSorszam] += osszeg;
        szalVezerlo.zsetonokPotba(jatekosSorszam, osszeg);
        osszeg = jatekosokTetje[jatekosSorszam];
        szalVezerlo.felhoSzalIndit("Megad", jatekosSorszam);
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
        osszeg = szalVezerlo.getJatekosZsetonOsszeg(jatekosSorszam);        
        szalVezerlo.zsetonokPotba(jatekosSorszam, osszeg);
                
        if(osszeg > elozoOsszeg) licitSzamlalo = 0;
        else osszeg = elozoOsszeg;
        
        szalVezerlo.jatekosPasszival(jatekosSorszam);
        aktivJatekosokSzama--;
        allInSzamlalo++;
        szalVezerlo.felhoSzalIndit("All in", jatekosSorszam);
        
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
        szalVezerlo.jatekosPasszival(jatekosSorszam);
        aktivJatekosokSzama--;
        szalVezerlo.felhoSzalIndit("Bedob", jatekosSorszam);
        szalVezerlo.jatekosKiszall(jatekosSorszam);
        szalVezerlo.kartyalapokBedobSzalIndit(jatekosSorszam);
        
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
    private void licitSzamlaloLeptet(){
        if (++licitSzamlalo == jatekosokSzama) ujLicitkor();
    }
    
    /**
     * Elindít egy új licit kört. Ha a leosztott kártyalapok száma 5 akkor
     * meghívja a korVege() metódust.
     */
    private void ujLicitkor(){              
        jatekosSorszam = kisVakJatekosSorszam;
        
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
    public void run() {
        while (true) {
            if (ujkorIndit) {
                ujKor();
            } else if (korVege) {
                korVege();
            } else {
                kovetkezoJatekos();
            }
            
            if(szalStop) break;
                
            megallit();
            
            {
                try {
                    sleep(ido);
                } catch (InterruptedException ex) {
                    Logger.getLogger(JatekVezerlo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
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

    public byte getJatekosSorszam() {
        return jatekosSorszam;
    }

    public int[] getJatekosokTetje() {
        return jatekosokTetje;
    }
}
