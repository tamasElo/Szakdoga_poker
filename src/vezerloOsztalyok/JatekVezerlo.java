package vezerloOsztalyok;

import vezerloOsztalyok.szalak.SzalVezerlo;

public class JatekVezerlo{
    private byte korokSzama;
    private SzalVezerlo szalVezerlo;
    private byte jatekosSorszam;
    private byte jatekosokSzama;
    private boolean ujLeosztasInditva;
    private int osszeg;
    private byte dealerJatekosSorszam;
    private byte licitkorSzamlalo;
    private Mi mi;
    private boolean nyithat;
    private boolean passzolhat;
    private boolean megadhat;
    private boolean emelhet;
    private boolean osszegPotba;
    private int aktJatekosZsetonOsszeg;
    
    public JatekVezerlo(SzalVezerlo szalVezerlo){
        this.szalVezerlo = szalVezerlo;
        this.jatekosokSzama = szalVezerlo.jatekosokSzama(); 
        dealerJatekosSorszam = (byte) (Math.random()*jatekosokSzama);
        mi = new Mi();      
        szalVezerlo.zsetonokKioszt();
        ujLeosztas();        
    }   

    private void ujLeosztas() { 
            ujLeosztasInditva = false;
            szalVezerlo.jatekosokAktival();
            dealerJatekosSorszamBeallit();
            lehetosegekBeallit();
            szalVezerlo.korongokMozgatSzalIndit(dealerJatekosSorszam);
            szalVezerlo.kartyalapokKiosztSzalIndit(dealerJatekosSorszam); 
            korokSzama++;
            ujLeosztasInditva = true;
    }
    
    private void dealerJatekosSorszamBeallit(){
        byte eltolas = 3;
        if(++dealerJatekosSorszam == jatekosokSzama) dealerJatekosSorszam = 0;
        if(dealerJatekosSorszam + eltolas == jatekosokSzama)jatekosSorszam = 0;
        if(dealerJatekosSorszam + eltolas > jatekosokSzama)jatekosSorszam = (byte) ((dealerJatekosSorszam + eltolas)-jatekosokSzama);
        if(dealerJatekosSorszam + eltolas < jatekosokSzama)jatekosSorszam = (byte) (dealerJatekosSorszam + eltolas);
    }
    
    private void smallBlindjatekosraBeallit(){
        jatekosSorszam = (byte) (dealerJatekosSorszam+1);        
    }
    
    public void kovetkezoJatekos() {
        jatekosSorszam++;
        licitkorSzamlaloLeptet();
        if(jatekosSorszam == jatekosokSzama) jatekosSorszam = 0;
        aktJatekosZsetonOsszeg = szalVezerlo.getJatekosZsetonOsszeg(jatekosSorszam);
        lehetosegekBeallit();
    }
    
    private void lehetosegekBeallit(){ 
        aktJatekosZsetonOsszeg = szalVezerlo.getJatekosZsetonOsszeg(jatekosSorszam);
        if (aktJatekosZsetonOsszeg <= osszeg) {
            nyithat = false;
            emelhet = false;
            megadhat = false;
            passzolhat = false;
        } else if (osszegPotba || !ujLeosztasInditva) {
            nyithat = false;
            emelhet = true;
            megadhat = true;
            passzolhat = false;
        } else {
            nyithat = true;
            emelhet = false;
            megadhat = false;
            passzolhat = true;
        }
        
        /*---tesztelés---*/
        szalVezerlo.setMikezeles(nyithat, emelhet, megadhat, passzolhat);
        szalVezerlo.setjatekosSorszam(jatekosSorszam);
        /*----------*/

        if (gepiJatekos()) {
            mi.kovetkezoJatekos(jatekosSorszam);
            mi.setLehetosegek(nyithat, emelhet, megadhat, passzolhat);
        }
        szalVezerlo.gombSorAllapotvalt();
    }
    
    public boolean gepiJatekos(){        
        return (jatekosSorszam != 0);
    }
    
    public void passz(){
        osszegPotba = false;
        kovetkezoJatekos();
    }
    
    public void nyitas(int nyitoOsszeg){
        this.osszeg = nyitoOsszeg;
        osszegPotba = true;
        licitkorSzamlalo = 0;
        szalVezerlo.zsetonokPotba(jatekosSorszam, osszeg);
        kovetkezoJatekos();
    }
    
    public void emeles(int emeltOsszeg){        
        this.osszeg = emeltOsszeg;
        osszegPotba = true;
        licitkorSzamlalo = 0;
        szalVezerlo.zsetonokPotba(jatekosSorszam, osszeg);
        kovetkezoJatekos();
    }
    
    public void megadas(){
        osszegPotba = true;
        szalVezerlo.zsetonokPotba(jatekosSorszam, osszeg);
        kovetkezoJatekos();
    }
    
    public void allIn(){
        osszegPotba = true;
        osszeg = szalVezerlo.getJatekosZsetonOsszeg(jatekosSorszam);
        szalVezerlo.zsetonokPotba(jatekosSorszam, osszeg);
        kovetkezoJatekos();
    }
        
    public void bedobas(){
        szalVezerlo.jatekosDeaktival(jatekosSorszam);
        licitkorSzamlaloLeptet();
        kovetkezoJatekos();
    }
    
    private void licitkorSzamlaloLeptet(){
        if(++licitkorSzamlalo == jatekosokSzama) ujLicitkor();
    }
    
    private void ujLicitkor(){
        smallBlindjatekosraBeallit();
        osszegPotba = false;
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
}