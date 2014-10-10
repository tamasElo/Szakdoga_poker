package vezerloOsztalyok;

public class JatekVezerlo{
    public static final byte EMBER_JATEKOS_SORSZAM = 0;
    private byte korokSzama;
    private SzalVezerlo szalVezerlo;    
    private byte dealerJatekosSorszam;    
    private byte kisVakJatekosSorszam;
    private byte nagyVakJatekosSorszam;
    private byte jatekosSorszam;
    private byte jatekosokSzama;
    private int osszeg;
    private int[] jatekosokTetje;
    private byte licitSzamlalo;
    private Mi mi;
    private boolean nyithat;
    private boolean passzolhat;
    private boolean megadhat;
    private boolean emelhet;
    private int kisVakOsszeg;
    private int nagyVakOsszeg;
    private int aktivJatekosokSzama;
    private int aktJatekosZsetonOsszeg;
    private byte korOszto;
    
    public JatekVezerlo(SzalVezerlo szalVezerlo){
        this.szalVezerlo = szalVezerlo;
        this.jatekosokSzama = szalVezerlo.jatekosokSzama(); 
        jatekosokTetje = new int[jatekosokSzama];
        dealerJatekosSorszam = (byte) (Math.random()*jatekosokSzama);
        mi = new Mi();     
        aktivJatekosokSzama = 5;
        kisVakOsszeg = 5;
        nagyVakOsszeg = kisVakOsszeg * 2;
        korOszto = 10;
        szalVezerlo.zsetonokKioszt();
        ujKor();        
    }   

    private void ujKor() {      
            korokSzama++;
            szalVezerlo.jatekosokAktival();
            jatekosSorszamokBeallit();
            vakokOsszegeBeallit();
            osszeg = nagyVakOsszeg;
            lehetosegekBeallit();
            szalVezerlo.korongokMozgatSzalIndit(dealerJatekosSorszam);
            szalVezerlo.kartyalapokKiosztSzalIndit(dealerJatekosSorszam); 
    }
    
    private void jatekosSorszamokBeallit(){
        byte eltolas = 1;
        
        if(++dealerJatekosSorszam == jatekosokSzama) dealerJatekosSorszam = 0;
        
        if(dealerJatekosSorszam + eltolas == jatekosokSzama)kisVakJatekosSorszam = 0;
        else kisVakJatekosSorszam = (byte) (dealerJatekosSorszam + eltolas);          
               
        if(kisVakJatekosSorszam + eltolas == jatekosokSzama)nagyVakJatekosSorszam = 0;
        else if(kisVakJatekosSorszam + eltolas > jatekosokSzama)nagyVakJatekosSorszam = (byte) ((kisVakJatekosSorszam + eltolas)-jatekosokSzama);
        else nagyVakJatekosSorszam = (byte) (kisVakJatekosSorszam + eltolas);
        
        if(nagyVakJatekosSorszam + eltolas == jatekosokSzama)jatekosSorszam = 0;
        else if(nagyVakJatekosSorszam + eltolas > jatekosokSzama)jatekosSorszam = (byte) ((nagyVakJatekosSorszam + eltolas)-jatekosokSzama);
        else jatekosSorszam = (byte) (nagyVakJatekosSorszam + eltolas);              
    }
    
    private void vakokOsszegeBeallit(){
        if(korokSzama % korOszto == 0) {
                kisVakOsszeg *= 2;
                nagyVakOsszeg = kisVakOsszeg * 2; 
            }
        
        jatekosokTetje[kisVakJatekosSorszam] = kisVakOsszeg;
        jatekosokTetje[nagyVakJatekosSorszam] = nagyVakOsszeg;
        szalVezerlo.zsetonokPotba(kisVakJatekosSorszam, kisVakOsszeg);
        szalVezerlo.zsetonokPotba(nagyVakJatekosSorszam, nagyVakOsszeg);
    }
    
    private void lehetosegekBeallit(){ 
        aktJatekosZsetonOsszeg = szalVezerlo.getJatekosZsetonOsszeg(jatekosSorszam);
        
        if (aktJatekosZsetonOsszeg <= osszeg-jatekosokTetje[jatekosSorszam]) {
            nyithat = false;
            emelhet = false;
            megadhat = false;
            passzolhat = false;
        } else if (osszeg == 0) {
            nyithat = true;
            emelhet = false;
            megadhat = false;
            passzolhat = true;
        } else if (jatekosokTetje[jatekosSorszam] == osszeg) {
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
        }
        
        szalVezerlo.gombSorAllapotvalt();
    }
        
    public void kovetkezoJatekos() {
            jatekosAllapotEllenorzes();
            lehetosegekBeallit();
    }
    
    public boolean gepiJatekos(){        
        return (jatekosSorszam != EMBER_JATEKOS_SORSZAM);
    }
    
    private void jatekosAllapotEllenorzes(){
        jatekosSorszam++;         
        licitSzamlaloLeptet();   
        
        if(jatekosSorszam == jatekosokSzama) jatekosSorszam = 0;
        
        if (!szalVezerlo.isJatekosAktiv(jatekosSorszam)) {    
            jatekosAllapotEllenorzes();
        }
    }
    
    private void korVege(){
        szalVezerlo.kartyalapokLeosztSzalIndit(true);        
    }
    
    public void passzol(){        
        szalVezerlo.felhoSzalIndit("Passzol", jatekosSorszam);
        kovetkezoJatekos();
    }
    
    public void nyit(int nyitoOsszeg){
        osszeg = nyitoOsszeg;
        jatekosokTetje[jatekosSorszam] = osszeg;
        szalVezerlo.zsetonokPotba(jatekosSorszam, osszeg);     
        licitSzamlalo = 0;        
        szalVezerlo.felhoSzalIndit("Nyit", jatekosSorszam);
        kovetkezoJatekos();
    }
    
    public void emel(int emeltOsszeg){        
        osszeg -= jatekosokTetje[jatekosSorszam];
        osszeg += emeltOsszeg;
        jatekosokTetje[jatekosSorszam] += osszeg;
        szalVezerlo.zsetonokPotba(jatekosSorszam, osszeg);     
        osszeg = jatekosokTetje[jatekosSorszam];
        licitSzamlalo = 0;
        szalVezerlo.felhoSzalIndit("Emel", jatekosSorszam);
        kovetkezoJatekos();
    }
    
    public void megad(){
        osszeg -= jatekosokTetje[jatekosSorszam];
        jatekosokTetje[jatekosSorszam] += osszeg;
        szalVezerlo.zsetonokPotba(jatekosSorszam, osszeg);
        szalVezerlo.felhoSzalIndit("Megad", jatekosSorszam);
        kovetkezoJatekos();
    }
    
    public void allIn(){
        int elozoOsszeg = osszeg;
        osszeg = szalVezerlo.getJatekosZsetonOsszeg(jatekosSorszam);        
        szalVezerlo.zsetonokPotba(jatekosSorszam, osszeg);
                
        if(osszeg > elozoOsszeg) licitSzamlalo = 0;
        else osszeg = elozoOsszeg;
        
        szalVezerlo.jatekosPasszival(jatekosSorszam);
        aktivJatekosokSzama--;
        szalVezerlo.felhoSzalIndit("All in", jatekosSorszam);
        
        if (aktivJatekosokSzama > 0) {
            kovetkezoJatekos();
        } else {
            korVege();
        }
    }
        
    public void bedob(){
        szalVezerlo.jatekosPasszival(jatekosSorszam);
        aktivJatekosokSzama--;
        szalVezerlo.felhoSzalIndit("Bedob", jatekosSorszam);
        szalVezerlo.kartyalapokBedobSzalIndit(jatekosSorszam);
        if (aktivJatekosokSzama > 1) {
            kovetkezoJatekos();
        } else {
            korVege();
        }
    }
    
    private void licitSzamlaloLeptet(){
        if(++licitSzamlalo == jatekosokSzama) ujLicitkor();
    }
    
    private void ujLicitkor(){
        jatekosSorszam = kisVakJatekosSorszam;
        
        if (szalVezerlo.leosztottKartyalapokSzama() < 5) {
            szalVezerlo.kartyalapokLeosztSzalIndit(false);
        } else {
            korVege();
        }
        
        jatekosokTetje = new int[jatekosokSzama];
        osszeg = 0;
        licitSzamlalo = 0;
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

    public int getKisVakOsszeg() {
        return kisVakOsszeg;
    }

    public int getNagyVakOsszeg() {
        return nagyVakOsszeg;
    }

    public int getOsszeg() {
        return osszeg;
    }

    public byte getJatekosSorszam() {
        return jatekosSorszam;
    }

    public byte getKisVakJatekosSorszam() {
        return kisVakJatekosSorszam;
    }

    public byte getNagyVakJatekosSorszam() {
        return nagyVakJatekosSorszam;
    }
}
