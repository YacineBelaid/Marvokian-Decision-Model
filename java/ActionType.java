import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public enum ActionType {
    
    UneSeuleCase, UnDe, DeuxDes;

    /**
     *
     * @return Simule et retourne le nombre de case d'avancement de l'action.
     */
    public int getAvancement(){
        switch(this){
            case UneSeuleCase:
                return 1;
            case UnDe:
                return random.nextInt(6) + 1;
            case DeuxDes:
                return random.nextInt(6) + random.nextInt(6) + 2;
            default:
                return 0;
        }
    }

    public String getLabel(){
        switch(this){
            case UneSeuleCase:
                return labels[0];
            case UnDe:
                return labels[1];
            case DeuxDes:
                return labels[2];
            default:
                return "-";
        }
    }

    public static ActionType get(String s){
        if(s.equalsIgnoreCase(labels[0]))
            return UneSeuleCase;
        if(s.equalsIgnoreCase(labels[2]))
            return DeuxDes;
        if(s.equalsIgnoreCase(labels[1]))
            return UnDe;
        return null;
    }

    private static final String[] labels = new String[]{"1", "D", "DD"};
    private static final Random   random = new Random();
    
    public static ArrayList<Ensemble<Integer, Float>> getValeurProb(ActionType actionType) {
        switch (actionType.getLabel()) {
            case "1":
                return ActionType.VALEUR_PROB_UNE_CASE;
            case "D":
                return ActionType.VALEUR_PROB_UN_DE;
            case "DD":
                return ActionType.VALEUR_PROB_DEUX_DES;
            default:
                throw new Error("erreur dans getValeurProd()");
        }
    }

    final static ArrayList<Ensemble<Integer,Float>> copierArray(ArrayList<Ensemble<Integer,Float>> original){
        ArrayList<Ensemble<Integer,Float>> liste = new ArrayList<>();
        for(Ensemble<Integer,Float> element : original){
            liste.add(new Ensemble<Integer,Float>(element.t, element.e));
        }
        return liste;
    }

    final static ArrayList<ActionType> ACTION_TYPES = new ArrayList<ActionType>(Arrays.asList(ActionType.UneSeuleCase,
            ActionType.UnDe, ActionType.DeuxDes));
    final static ArrayList<Ensemble<Integer, Float>> VALEUR_PROB_UNE_CASE = new ArrayList<>(
            Arrays.asList(new Ensemble<Integer, Float>(1, 1f)));
    final static ArrayList<Ensemble<Integer, Float>> VALEUR_PROB_UN_DE = new ArrayList<>(
            Arrays.asList(new Ensemble<Integer, Float>(1, 1 / 6f),
                    new Ensemble<Integer, Float>(2, 1 / 6f), new Ensemble<Integer, Float>(3, 1 / 6f),
                    new Ensemble<Integer, Float>(4, 1 / 6f), new Ensemble<Integer, Float>(5, 1 / 6f), 
                    new Ensemble<Integer, Float>(6, 1 / 6f)));
    final static ArrayList<Ensemble<Integer, Float>> VALEUR_PROB_DEUX_DES = new ArrayList<>(
            Arrays.asList(new Ensemble<Integer, Float>(2, 1 / 36f),
                    new Ensemble<Integer, Float>(3, 2 / 36f), new Ensemble<Integer, Float>(4, 3 / 36f),
                    new Ensemble<Integer, Float>(5, 4 / 36f),
                    new Ensemble<Integer, Float>(6, 5 / 36f), new Ensemble<Integer, Float>(7, 6 / 36f),
                    new Ensemble<Integer, Float>(8, 5 / 36f),
                    new Ensemble<Integer, Float>(9, 4 / 36f), new Ensemble<Integer, Float>(10, 3 / 36f),
                    new Ensemble<Integer, Float>(11, 2 / 36f),
                    new Ensemble<Integer, Float>(12, 1 / 36f)));
}
