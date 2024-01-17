import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;

public class GenPolitique1 {

    public static void main(String args[]) throws Exception {
        Reader reader = args.length == 0 ? new InputStreamReader(System.in) : new FileReader(args[0]);
        Plateau plateauP = Plateau.load(reader);
        plateau = plateauP;
        genererPolitiqueSimple();
        OutputStreamWriter out = new OutputStreamWriter(System.out);
        PolitiqueSimple.save(politique, out);
        out.close();
    }

    // dynamique
    static PolitiqueSimple politique;
    static Plateau plateau;
    static int positionCible;
    static float[] grilleEsperance;

    public static void genererPolique(Plateau p) {
        plateau = p;
        genererPolitiqueSimple();
    }

    public static float[] genererGrilleEsperance(int taille) {
        float[] grille = new float[taille];
        for (int i = 0; i < taille; i++) {
            grille[i] = 0;
        }
        return grille;
    }

    public static void genererPolitiqueSimple() {
        positionCible = plateau.nbCases - 1;
        politique = new PolitiqueSimple(positionCible + 1);
        grilleEsperance = genererGrilleEsperance(positionCible + 1);

        // initialiser derniere case
        politique.actions[positionCible] = ActionType.DeuxDes;
        grilleEsperance[positionCible] = 0;

        // s'assurer qu'on ne repete pas la position
        while (true) {

            // PolitiqueSimple comparateur = politique;
            float[] grilleComparaison = copierGrille(grilleEsperance);

            for (int position = positionCible - 1; position >= 0; position--) {
                if (plateau.getNextCase(position, 0) == position) {
                    markovienDP(position);
                } else {
                    // action par defaut pour case injouable
                    politique.actions[position] = ActionType.DeuxDes;
                }
            }

            if (siGrilleEsperanceEgale(grilleComparaison))
                break;
            // if (siPolitiqueEgale(comparateur)) break;
        }
    }

    public static float[] copierGrille(float[] original) {
        float[] grille = new float[original.length];
        for (int i = 0; i < original.length; i++) {
            grille[i] = original[i];
        }
        return grille;
    }

    public static Boolean siGrilleEsperanceEgale(float[] grilleComparaison) {
        assert (grilleEsperance.length == grilleComparaison.length);
        for (int i = 0; i < grilleEsperance.length; i++) {
            if (grilleEsperance[i] != grilleComparaison[i]) {
                return false;
            }
        }
        return true;
    }

    public static Boolean siPolitiqueEgale(PolitiqueSimple comparateur) {
        assert (politique.actions.length == comparateur.actions.length);
        for (int position = 0; position <= positionCible; position++) {
            if (politique.actions[position] != comparateur.actions[position]) {
                return false;
            }
        }
        return true;
    }

    public static int appliquerDeplacement(int position, int avancement) {
        return position + avancement > positionCible ? position : plateau.getNextCase(position, avancement);
    }

    // verifier esperance des serpent et des echelles!!!
    public static void markovienDP(int position) {
        // <ActionType,Ensemble<Integer,Float>>
        ArrayList<Ensemble<ActionType, Float>> resultats = new ArrayList<>();

        // on genere les consequences pour toutes les actions possibles
        for (ActionType action : ActionType.ACTION_TYPES) {
            // 1, 6, 12
            ArrayList<Ensemble<Integer, Float>> consequences = ActionType.copierArray(ActionType.getValeurProb(action));

            // determine la position final d'un deplacement
            for (int i = 0; i < consequences.size(); i++) {

                consequences.get(i).t = appliquerDeplacement(position, consequences.get(i).t);
                
            }

            ///fold pour supprimer les recurrences detats subsequents
            ArrayList<Ensemble<Integer,Float>> consequencesFold = new ArrayList<Ensemble<Integer,Float>>();
            for (Ensemble<Integer, Float> consequence : consequences) {
                boolean consequenceTrouve= false;
                for (Ensemble<Integer,Float> c : consequencesFold){
                    if (c.t == consequence.t){
                        c.e += consequence.e;
                        consequenceTrouve = true;
                    }
                }
                if(!consequenceTrouve){
                    consequencesFold.add(consequence);
                }
            }

            // fait la somme des esperances de chacune des consequences
            float esperance = 0;
            for (Ensemble<Integer, Float> consequence : consequencesFold) {
                esperance += (1 + grilleEsperance[consequence.t]) * consequence.e;
            }

            resultats.add(new Ensemble<ActionType, Float>(action, esperance));
        }

        // choisir la meilleur action
        Ensemble<ActionType, Float> choix = null;
        for (Ensemble<ActionType, Float> resultat : resultats) {
            if (choix == null) {
                choix = resultat;
            } else if (resultat.e < choix.e) {
                choix = resultat;
            }
        }

        // mettre a jour grille de cout
        // mettre a jour politique
        politique.actions[position] = choix.t;
        grilleEsperance[position] = choix.e;
    }
}