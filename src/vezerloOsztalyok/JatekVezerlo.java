package vezerloOsztalyok;

public class JatekVezerlo{
    private byte korokSzama;
    private SzalVezerlo szalVezerlo;    
    private byte dealerJatekosSorszam;    
    private byte kisVakJatekosSorszam;
    private byte nagyVakJatekosSorszam;
    private byte jatekosSorszam;
    private byte jatekosokSzama;
    private int osszeg;
    private int[] jatekosokTetje;
    private byte licitkorSzamlalo;
    private Mi mi;
    private boolean nyithat;
    private boolean passzolhat;
    private boolean megadhat;
    private boolean emelhet;
    private int aktJatekosZsetonOsszeg;
    private int kisVakOsszeg;
    private int nagyVakOsszeg;
    private int aktivJatekosokSzama;
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
        if (aktJatekosZsetonOsszeg <= osszeg) {
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
        aktJatekosZsetonOsszeg = szalVezerlo.getJatekosZsetonOsszeg(jatekosSorszam);
        lehetosegekBeallit();
    }
    
    public boolean gepiJatekos(){        
        return (jatekosSorszam != 0);
    }
    
    public void jatekosAllapotEllenorzes(){
        jatekosSorszam++;         
        licitkorSzamlaloLeptet();   
        
        if(jatekosSorszam == jatekosokSzama) jatekosSorszam = 0;
        
        if (!szalVezerlo.isJatekosAktiv(jatekosSorszam)) {    
            if(aktivJatekosokSzama > 1)jatekosAllapotEllenorzes();
        }
    }
    
    public void passz(){
        kovetkezoJatekos();
    }
    
    public void nyitas(int nyitoOsszeg){
        osszeg = nyitoOsszeg;
        jatekosokTetje[jatekosSorszam] = osszeg;
        szalVezerlo.zsetonokPotba(jatekosSorszam, osszeg);     
        licitkorSzamlalo = 0;
        kovetkezoJatekos();
    }
    
    public void emeles(int emeltOsszeg){        
        osszeg -= jatekosokTetje[jatekosSorszam];
        osszeg += emeltOsszeg;
        jatekosokTetje[jatekosSorszam] += osszeg;
        szalVezerlo.zsetonokPotba(jatekosSorszam, osszeg);     
        osszeg = jatekosokTetje[jatekosSorszam];
        licitkorSzamlalo = 0;
        kovetkezoJatekos();
    }
    
    public void megadas(){
        osszeg -= jatekosokTetje[jatekosSorszam];
        jatekosokTetje[jatekosSorszam] += osszeg;
        szalVezerlo.zsetonokPotba(jatekosSorszam, osszeg); 
        osszeg = jatekosokTetje[jatekosSorszam];
        kovetkezoJatekos();
    }
    
    public void allIn(){
        int elozoOsszeg = osszeg;
        osszeg = szalVezerlo.getJatekosZsetonOsszeg(jatekosSorszam);        
        szalVezerlo.zsetonokPotba(jatekosSorszam, osszeg);
                
        if(osszeg > elozoOsszeg) licitkorSzamlalo = 0;
        else osszeg = elozoOsszeg;
        
        szalVezerlo.jatekosPasszival(jatekosSorszam);
        aktivJatekosokSzama--;
        kovetkezoJatekos();
    }
        
    public void bedobas(){
        szalVezerlo.jatekosPasszival(jatekosSorszam);
        aktivJatekosokSzama--;
        kovetkezoJatekos();
    }
    
    private void licitkorSzamlaloLeptet(){
        if(++licitkorSzamlalo == jatekosokSzama) ujLicitkor();
    }
    
    private void ujLicitkor(){
        jatekosSorszam = kisVakJatekosSorszam;
        szalVezerlo.kartyalapokLeosztSzalIndit();
        jatekosokTetje = new int[jatekosokSzama];
        osszeg = 0;
        licitkorSzamlalo = 0;
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
}
