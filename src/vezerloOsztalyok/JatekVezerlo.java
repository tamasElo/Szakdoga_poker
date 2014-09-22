package vezerloOsztalyok;

import alapOsztalyok.Zseton;
import java.util.List;
import vezerloOsztalyok.szalak.SzalVezerlo;

public class JatekVezerlo{
    private byte korokSzama;
    private SzalVezerlo szalVezerlo;
    private byte jatekosSorszam;
    private byte jatekosokSzama;
    private boolean ujKorInditva;
    private int osszeg;
    private byte dealerJatekosSorszam;
    private byte licitkorSzamlalo;
    private Mi mi;
    private boolean nyithat;
    private boolean passzolhat;
    private boolean megadhat;
    private boolean emelhet;
    private boolean osszegPotba;
    private List<Zseton> pot;
    private List<Zseton> aktJatekosZsetonjai;
    
    public JatekVezerlo(SzalVezerlo szalVezerlo){
        this.szalVezerlo = szalVezerlo;
        this.jatekosokSzama = szalVezerlo.jatekosokSzama(); 
        dealerJatekosSorszam = (byte) (Math.random()*jatekosokSzama);
        mi = new Mi();
        ujKor();
    }   

    private void ujKor() { 
            ujKorInditva = false;
            szalVezerlo.jatekosokAktival();
            dealerJatekosSorszamBeallit();
            lehetosegekBeallit();
            szalVezerlo.korongokMozgatSzalIndit(dealerJatekosSorszam);
            szalVezerlo.zsetonokKiosztSzalIndit();
            szalVezerlo.kartyalapokKiosztSzalIndit(dealerJatekosSorszam); 
            korokSzama++;
            ujKorInditva = true;
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
        aktJatekosZsetonjai = szalVezerlo.getJatekosZsetonjai(jatekosSorszam);
        lehetosegekBeallit();
    }
    
    private void lehetosegekBeallit(){ 
        if(ZsetonKezelo.zsetonokOsszege(aktJatekosZsetonjai) <= osszeg){
            nyithat = false;
            emelhet = false;
            megadhat = false;
            passzolhat = true;
        }
        else if(!ujKorInditva){
            nyithat = false;
            emelhet = true;
            megadhat = false;
            passzolhat = true;
        } else if (osszegPotba) {
            nyithat = false;
            emelhet = true;
            megadhat = true;
            passzolhat = false;
        } else{
            nyithat = true;
            emelhet = true;
            megadhat = false;
            passzolhat = true;
        }        
        
        if (gepiJatekos()) {
            mi.kovetkezoJatekos(jatekosSorszam);
            mi.setLehetosegek(nyithat, emelhet, megadhat, passzolhat);
        }
        else szalVezerlo.gombSorAllapotvalt();
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
        pot.addAll(ZsetonKezelo.pot(aktJatekosZsetonjai, osszeg));
        kovetkezoJatekos();
    }
    
    public void emeles(int emeltOsszeg){        
        this.osszeg = emeltOsszeg;
        osszegPotba = true;
        licitkorSzamlalo = 0;
        pot.addAll(ZsetonKezelo.pot(aktJatekosZsetonjai, osszeg));
        kovetkezoJatekos();
    }
    
    public void megadas(){
        osszegPotba = true;
        pot.addAll(ZsetonKezelo.pot(aktJatekosZsetonjai, osszeg));
        kovetkezoJatekos();
    }
    
    public void allIn(){
        osszeg = ZsetonKezelo.zsetonokOsszege(aktJatekosZsetonjai);
        pot.addAll(ZsetonKezelo.pot(aktJatekosZsetonjai, osszeg));
        osszegPotba = true;
    }
        
    public void bedobas(){
        szalVezerlo.jatekosDeaktival(jatekosSorszam);
        licitkorSzamlaloLeptet();
        kovetkezoJatekos();
    }
    
    private void licitkorSzamlaloLeptet(){
        if(licitkorSzamlalo < jatekosokSzama)
            licitkorSzamlalo++;
        else ujLicitkor();
    }
    
    private void ujLicitkor(){
        smallBlindjatekosraBeallit();
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
