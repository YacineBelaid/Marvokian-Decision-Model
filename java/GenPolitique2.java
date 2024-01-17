import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

public class GenPolitique2 {

    public static void main(String args[]) throws Exception {
        Reader reader = args.length==0 ? new InputStreamReader(System.in) : new FileReader(args[0]);
        plateau = Plateau.load(reader);

        //pour adversaire
        GenPolitique1.genererPolique(plateau);
        genererPolitqueAvance();

        OutputStreamWriter out = new OutputStreamWriter(System.out);
        PolitiqueAvancee.save(politique, out);
        out.close();
    }

    //plateau
    static int positionCible;
    static Plateau plateau;

    //adversaire
    static GenPolitique1 adversaire;

    //joueur
    static float[][] grilleEsperance;
    static PolitiqueAvancee politique;

    public static float[][] genererGrilleEsperance(int taille) {
        float[][] grille = new float[taille][taille];
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j<taille;j++) {
                grille[i][j] = 0;
            }
        }

        //la ou l'aderversaire gagne
        for(int i=0; i<taille; i++){
            grille[i][taille-1] = 0;
        }

        //la ou on gagne
        for(int i=0; i<taille; i++){
            grille[taille-1][i] = 1;
        }

        return grille;
    }
    
    static public ActionType[] genererCoupGenerique(int taille){
        ActionType[] coupFinal = new ActionType[taille];
        for(int i = 0; i < taille; i++){
            coupFinal[i]= ActionType.DeuxDes;
        }
        return coupFinal;
    }

    static public boolean siGrilleEsperanceEgale(float[][] comparateur){
        for (int i =0; i<comparateur.length; i++){
            for (int j = 0; j<comparateur[i].length; j++){
                if (comparateur[i][j]!=grilleEsperance[i][j]){
                    return false;
                }
            }
        }
        return true;
    }

    static public float[][] copierGrille(float[][] original){
        float[][] grille = new float[original.length][original.length];
        for(int i = 0; i<original.length;i++){
            for (int j = 0; j<original.length;j++) {
                grille[i][j] = original[i][j];
            }
        }
        return grille;
    }

    static public void initialisationPolitiqueAvance(){
        for (int i=0; i<politique.actions.length; i++){
            for (int j = 0; j<politique.actions[i].length; j++){
                politique.actions[i][j]= ActionType.UneSeuleCase;
            }
        }
        initialiserPositionsInjouablesAdversaire();
        initialiserPositionsFinales();
    }

    static public void initialiserPositionsFinales(){
        politique.actions[positionCible] = genererCoupGenerique(positionCible+1);
        for (int i =0; i<politique.actions.length; i++) {
            politique.actions[i][positionCible] = ActionType.DeuxDes;
        }
    }

    static public void initialiserPositionsInjouablesAdversaire(){
        //on cherche les positions injouables
        ArrayList<Integer> positionsInjouables = new ArrayList<Integer>();
        for (int i = 0; i<plateau.nbCases-1;i++){
            if (appliquerDeplacement(i, 0) != i){
                positionsInjouables.add(i);
            }
        }
        for (int i = 0; i<politique.actions.length; i++){
            for (int positionInjouable : positionsInjouables){
                politique.actions[i][positionInjouable] = ActionType.DeuxDes;
            }
        }
    }

    static public void genererPolitqueAvance(){
        //initialisation
        positionCible = plateau.nbCases - 1;
        politique = new PolitiqueAvancee(positionCible + 1);
        initialisationPolitiqueAvance();
        grilleEsperance = genererGrilleEsperance(positionCible + 1);

        while (true){

            float[][] comparateur = copierGrille(grilleEsperance); 
           
            for (int position = positionCible-1; position >= 0; position--){
                if (appliquerDeplacement(position, 0) == position){
                    markovienDP(position);
                } else {
                    politique.actions[position] = genererCoupGenerique(positionCible+1);
                }
            }

            if (siGrilleEsperanceEgale(comparateur)) break;
        }
    }

    public static int appliquerDeplacement(int position, int avancement) {
        return position + avancement > positionCible ? position : plateau.getNextCase(position, avancement);
    }

    static public void markovienDP(int position){
        ActionType[] coupAJouer = new ActionType[positionCible+1];
        coupAJouer[positionCible] = ActionType.DeuxDes;//////////////////////

        for (int positionAdversaire = positionCible-1; positionAdversaire >= 0; positionAdversaire--){

            //generer consequence possible de l'adversaire
            ArrayList<Ensemble<Integer,Float>> consequencesAdversaire = ActionType.copierArray(ActionType.getValeurProb(GenPolitique1.politique.actions[positionAdversaire]));
            for (Ensemble<Integer,Float> consequenceAdversaire : consequencesAdversaire) {
                consequenceAdversaire.t = appliquerDeplacement(positionAdversaire, consequenceAdversaire.t);
            }

            //fold sur Integer pour ne retenir que les etats differents
            ArrayList<Ensemble<Integer,Float>> consequencesAdversaireFold = new ArrayList<>();
            for(Ensemble<Integer,Float> consequence : consequencesAdversaire){
                boolean consequenceTrouve = false;
                for (Ensemble<Integer,Float> c : consequencesAdversaireFold){
                    if (c.t == consequence.t){ 
                        c.e += consequence.e;
                        consequenceTrouve = true;
                    }
                }
                if (!consequenceTrouve){
                    consequencesAdversaireFold.add(new Ensemble<Integer,Float>(consequence.t, consequence.e));
                }
            }

            //generer consequence a nous
            ArrayList<Ensemble<ActionType,Ensemble<Integer,Float>>> consequences = new ArrayList<>();
            for (ActionType coup : ActionType.ACTION_TYPES) {
                for (Ensemble<Integer,Float> consequence: ActionType.copierArray(ActionType.getValeurProb(coup))) {
                     consequence.t = appliquerDeplacement(position, consequence.t);
                    consequences.add(new Ensemble<ActionType,Ensemble<Integer,Float>>(coup, new Ensemble<Integer,Float>(consequence.t, consequence.e) ));
                }
            }

            //fold sur ActionType,Integer pour ne retenir que les etats differents pour chacune des actions
            ArrayList<Ensemble<ActionType,Ensemble<Integer,Float>>> consequencesFold = new ArrayList<>();
            for(Ensemble<ActionType,Ensemble<Integer,Float>> consequence: consequences){
                boolean consequenceTrouve = false;
                for(Ensemble<ActionType,Ensemble<Integer,Float>> c : consequencesFold){
                    if (c.t == consequence.t && c.e.t == consequence.e.t) {
                        c.e.e += consequence.e.e;
                        consequenceTrouve = true;
                    }
                }
                if (!consequenceTrouve) {
                    consequencesFold.add(new Ensemble<ActionType,Ensemble<Integer,Float>>(consequence.t, consequence.e));
                }
            }

            // combinaison consequence de nous et adversaire
            ArrayList<Ensemble<ActionType,Float>> combinaision = new ArrayList<>(); 
            for (Ensemble<ActionType,Ensemble<Integer,Float>> consequence : consequencesFold){
                float esperance = 0f;
                for (Ensemble<Integer,Float> consequenceAdversaire : consequencesAdversaireFold){
                    esperance += grilleEsperance[consequence.e.t][consequenceAdversaire.t] * consequence.e.e * consequenceAdversaire.e;
                }
                combinaision.add(new Ensemble<ActionType,Float>(consequence.t, esperance));
            }

            // fold sur ActionType
            ArrayList<Ensemble<ActionType,Float>> consequenceActions = new ArrayList<>(
                Arrays.asList(new Ensemble<ActionType,Float>(ActionType.UneSeuleCase, 0f),
                new Ensemble<ActionType,Float>(ActionType.UnDe, 0f),
                new Ensemble<ActionType,Float>(ActionType.DeuxDes, 0f)));
            for(Ensemble<ActionType,Float> combi : combinaision){
                for(Ensemble<ActionType,Float> consequenceAction : consequenceActions){
                    if (consequenceAction.t == combi.t){
                        consequenceAction.e += combi.e;
                    }
                }
            }

            // retient max Float
            Ensemble<ActionType,Float> resultat = null;
            for(Ensemble<ActionType,Float> consequenceAction : consequenceActions ){
                if (resultat == null){
                    resultat = consequenceAction;
                } else if (resultat.e < consequenceAction.e){
                    resultat = consequenceAction;
                }
            }

            //mise a jour des tableau
            coupAJouer[positionAdversaire] = resultat.t;
            grilleEsperance[position][positionAdversaire] = resultat.e;
        }

        politique.actions[position] = coupAJouer;
    }
}