package alapOsztalyok;

import java.util.Comparator;

public class KartyalapRendezSzinSzerint implements Comparator<Kartyalap>{

    @Override
    public int compare(Kartyalap egyik, Kartyalap masik) {
        return egyik.getKartyaSzin().compareTo(masik.getKartyaSzin());//A String osztály compareTo metódusát használja fel a két objektum összehasonlítására.
    }
}
