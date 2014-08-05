/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vezerloOsztalyok;

import alapOsztalyok.Jatekos;
import vezerloOsztalyok.ZsetonKezelo;
import java.util.ArrayList;

/**
 *
 * @author Tomi
 */
public class Tester {
    public static void main(String[] args){
        Jatekos jatekos = new Jatekos("Tomi");
        jatekos.setJatekosZsetonok(ZsetonKezelo.zsetonKioszt(250));
        System.out.println(jatekos.getJatekosZsetonok());
        ZsetonKezelo.pot(jatekos.getJatekosZsetonok(), 233);
        System.out.println(jatekos.getJatekosZsetonok());
        // próba
//            ArrayList<Kartyalap> lapok = new ArrayList<>();
//            ArrayList<Kartyalap> leosztas = new ArrayList<>();
//        
//            lapok.add(new Kartyalap(null, "3", "diamond", (byte)5));
//            lapok.add(new Kartyalap(null, "3", "club", (byte)5));
//            
//            leosztas.add(new Kartyalap(null, "2", "club", (byte)7));
//            leosztas.add(new Kartyalap(null, "3", "club", (byte)7));
//            leosztas.add(new Kartyalap(null, "4", "diamond", (byte)5));
//            leosztas.add(new Kartyalap(null, "5", "diamond", (byte)7));
//            leosztas.add(new Kartyalap(null, "6", "diamond", (byte)8));
//            Jatekos jatekos = new Jatekos();
//            jatekos.setJatekosKartyalapok(lapok);
//        //
//    
//        Lapkombinaciok.lapKombinacioKeres(jatekos, leosztas);
    }

}
