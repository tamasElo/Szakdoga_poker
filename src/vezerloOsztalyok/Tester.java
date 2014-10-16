package vezerloOsztalyok;

import alapOsztalyok.Jatekos;
import alapOsztalyok.Kartyalap;
import alapOsztalyok.PokerKez;
import vezerloOsztalyok.ZsetonKezelo;
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
        Map<Byte, PokerKez> nyertesek = new HashMap<>();
        ArrayList<Kartyalap> lapok = new ArrayList<>();
        ArrayList<Kartyalap> lapok2 = new ArrayList<>();
        ArrayList<Kartyalap> lapok3 = new ArrayList<>();
        ArrayList<Kartyalap> lapok4 = new ArrayList<>();
        ArrayList<Kartyalap> lapok5 = new ArrayList<>();
        ArrayList<Kartyalap> leosztas = new ArrayList<>();
        
        lapok.add(new Kartyalap(null, "3", "diamond", (byte) 9));
        lapok.add(new Kartyalap(null, "3", "club", (byte) 3));
        lapok2.add(new Kartyalap(null, "3", "diamond", (byte) 4));
        lapok2.add(new Kartyalap(null, "3", "hearts", (byte) 2));
        lapok3.add(new Kartyalap(null, "3", "diamond", (byte) 10));
        lapok3.add(new Kartyalap(null, "3", "diamond", (byte) 12));
        lapok4.add(new Kartyalap(null, "3", "diamond", (byte) 14));
        lapok4.add(new Kartyalap(null, "3", "club", (byte) 4));
        lapok5.add(new Kartyalap(null, "3", "diamond", (byte) 5));
        lapok5.add(new Kartyalap(null, "3", "club", (byte) 12));

        leosztas.add(new Kartyalap(null, "2", "diamond", (byte) 13));
        leosztas.add(new Kartyalap(null, "3", "spades", (byte) 4));
        leosztas.add(new Kartyalap(null, "4", "club", (byte) 2));
        leosztas.add(new Kartyalap(null, "5", "hearts", (byte) 5));
        leosztas.add(new Kartyalap(null, "6", "spades", (byte) 10));        
        
        Map<Byte, List<Kartyalap>> jatekosokKartyalapjai = new HashMap<>();

        jatekosokKartyalapjai.put((byte) 0, lapok);
        jatekosokKartyalapjai.put((byte) 1, lapok2);
        jatekosokKartyalapjai.put((byte) 2, lapok3);
        jatekosokKartyalapjai.put((byte) 3, lapok4);
        jatekosokKartyalapjai.put((byte) 4, lapok5);
        
        nyertesek = PokerKezKiertekelo.nyertesPokerKezKeres(jatekosokKartyalapjai, leosztas);
        for (Map.Entry<Byte, PokerKez> entrySet : nyertesek.entrySet()) {
            Byte key = entrySet.getKey();
            PokerKez value = entrySet.getValue();
            System.out.println(key + " " + value.getPokerKezNev());
        }
    }
}
