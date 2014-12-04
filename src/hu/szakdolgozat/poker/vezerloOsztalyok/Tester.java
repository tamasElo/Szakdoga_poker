package hu.szakdolgozat.poker.vezerloOsztalyok;

import hu.szakdolgozat.poker.alapOsztalyok.Jatekos;
import hu.szakdolgozat.poker.alapOsztalyok.Kartyalap;
import hu.szakdolgozat.poker.burkoloOsztalyok.PokerKez;
import hu.szakdolgozat.poker.alapOsztalyok.Zseton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tester {
    
    public static void main(String[] args){ 
        Jatekos jatekos = new Jatekos("Tomi");
//        jatekos.setJatekosZsetonok(ZsetonKezelo.zsetonKioszt(250));
//        System.out.println(jatekos.getJatekosZsetonok());
//        ZsetonKezelo.pot(jatekos.getJatekosZsetonok(), 233);
//        System.out.println(jatekos.getJatekosZsetonok()); 
        PokerKez pokerKez;
        Map<Byte, PokerKez> nyertesek = new HashMap<>();
        ArrayList<Kartyalap> lapok = new ArrayList<>();
        ArrayList<Kartyalap> lapok2 = new ArrayList<>();
        ArrayList<Kartyalap> lapok3 = new ArrayList<>();
        ArrayList<Kartyalap> lapok4 = new ArrayList<>();
        ArrayList<Kartyalap> lapok5 = new ArrayList<>();
        ArrayList<Kartyalap> leosztas = new ArrayList<>();
        
        lapok.add(new Kartyalap(null, "14", "clubs", (byte) 4));
        lapok.add(new Kartyalap(null, "5", "clubs", (byte) 9));
        lapok2.add(new Kartyalap(null, "3", "hearts", (byte) 7));
        lapok2.add(new Kartyalap(null, "3", "diamond", (byte) 11));
//        lapok3.add(new Kartyalap(null, "3", "diamond", (byte) 10));
//        lapok3.add(new Kartyalap(null, "3", "diamond", (byte) 12));
//        lapok4.add(new Kartyalap(null, "3", "diamond", (byte) 14));
//        lapok4.add(new Kartyalap(null, "3", "club", (byte) 4));
//        lapok5.add(new Kartyalap(null, "3", "diamond", (byte) 5));
//        lapok5.add(new Kartyalap(null, "3", "club", (byte) 12));

        leosztas.add(new Kartyalap(null, "2", "clubs", (byte) 11));
        leosztas.add(new Kartyalap(null, "3", "diamonds", (byte) 12));
        leosztas.add(new Kartyalap(null, "4", "diamonds", (byte) 14));
        leosztas.add(new Kartyalap(null, "11", "diamond", (byte) 5));
        leosztas.add(new Kartyalap(null, "8", "diamond", (byte) 2));        
        
        Map<Byte, List<Kartyalap>> jatekosokKartyalapjai = new HashMap<>();

        jatekosokKartyalapjai.put((byte) 0, lapok);
//        jatekosokKartyalapjai.put((byte) 1, lapok2);
//        jatekosokKartyalapjai.put((byte) 2, lapok3);
//        jatekosokKartyalapjai.put((byte) 3, lapok4);
//        jatekosokKartyalapjai.put((byte) 4, lapok5);
        
        
//        Lapkombinaciok.maxPokerkezMeghataroz(lapok, leosztas);
//        pokerKez = PokerKezKiertekelo.pokerKezKeres(lapok, leosztas);
//        System.out.println(pokerKez.getPokerKezErtek());
        nyertesek = PokerKezKiertekelo.nyertesPokerKezKeres(jatekosokKartyalapjai, leosztas);
        for (Map.Entry<Byte, PokerKez> entrySet : nyertesek.entrySet()) {
            Byte key = entrySet.getKey();
            PokerKez value = entrySet.getValue();
            System.out.print(key + " " + value.getPokerKezNev() + " ");
            for (Kartyalap kartyalap : value.getPokerKezKartyalapok()) {
                System.out.print(kartyalap.getKartyaErtek() + " " + kartyalap.getKartyaSzin() + ", ");
            }
            System.out.println("");
            for (Kartyalap kartyalap : value.getKiseroKartyalapok()) {
                System.out.print(kartyalap.getKartyaErtek() + " " + kartyalap.getKartyaSzin() + ", ");
            }
                      
        }
//        List<Zseton> pot = ZsetonKezelo.zsetonKioszt(2656);
//        Map<Byte, List<Zseton>> nyertesekZsetonjai = ZsetonKezelo.potSzetvalogat((byte) nyertesek.size(), pot);
//        for (Map.Entry<Byte, List<Zseton>> entrySet : nyertesekZsetonjai.entrySet()) {
//            Byte key = entrySet.getKey();
//            List<Zseton> value = entrySet.getValue();
//            System.out.println(key +" " + ZsetonKezelo.zsetonokOsszege(value));
//        }
    }
}
