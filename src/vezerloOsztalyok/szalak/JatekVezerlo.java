package vezerloOsztalyok.szalak;

public class JatekVezerlo extends Thread{
    private byte korokSzama;
    private SzalVezerlo szalVezerlo;
    private byte jatekosSorszam;
    private byte jatekosokSzama;
    private boolean ujKorInditva;
    private boolean gombsorAktiv;
    private byte dealer;
    
    public JatekVezerlo(SzalVezerlo szalVezerlo){
        this.szalVezerlo = szalVezerlo;
        this.jatekosokSzama = szalVezerlo.jatekosokSzama();       
        dealer = (byte) (Math.random()*jatekosokSzama);
    }   

  //  public void
    
    private void ujKor() {
        if (!ujKorInditva) {
            szalVezerlo.zsetonokKiosztSzalIndit();
            szalVezerlo.kartyalapokKiosztSzalIndit(dealer);
            ujKorInditva = true;
        }
    }

    private void gombSorAllapotvalt() {
        if (jatekosSorszam == 0 && !gombsorAktiv) {
            szalVezerlo.gombsorAktival();
            gombsorAktiv = true;
        }
        
        if (jatekosSorszam != 0 && gombsorAktiv) {
            szalVezerlo.gombsorPasszival();
            gombsorAktiv = false;
        }
    }

    @Override
    public void run() {
        while (true) {
            ujKor();
            gombSorAllapotvalt();
        }
    }
}
