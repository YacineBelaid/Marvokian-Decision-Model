import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.StringTokenizer;

public class PolitiqueAvancee {

    protected ActionType[][]  actions;
    
    public PolitiqueAvancee(int nbcase){
        actions = new ActionType[nbcase][nbcase];
        for(int i=0;i<nbcase;i++)
            for(int j=0;j<nbcase;j++)
                actions[i][j] = ActionType.DeuxDes;
    }
    
    public ActionType getAction(int positionJoueur, int positionAdversaire){
        return actions[positionJoueur][positionAdversaire];
    }
    
    public static PolitiqueAvancee load(Reader reader) throws IOException {
        BufferedReader br = new BufferedReader(reader);
        int nb = Integer.parseInt(br.readLine());
        PolitiqueAvancee politique = new PolitiqueAvancee(nb);
        for(int i=0;i<nb;i++){
            String ligne = br.readLine();
            StringTokenizer tokens = new StringTokenizer(ligne);
            int n = tokens.countTokens();
            if(n!=1 && n!=nb)
                throw new RuntimeException("Nombre incorrect de colonnes.");
            String a="DD";
            for(int j=0;j<nb;j++){
                if(tokens.hasMoreTokens())
                    a = tokens.nextToken();
                politique.actions[i][j] = ActionType.get(a);
            }
        }
        return politique;
    }
    
    public static void save(PolitiqueAvancee p, Writer writer) throws IOException {
        PrintWriter out = new PrintWriter(writer);
        out.println(p.actions.length);
        for(int i=0;i<p.actions.length;i++){
            for(int j=0;j<p.actions.length;j++)
                out.print(p.actions[i][j].getLabel() + " ");
            out.println();
        }
        out.close();
    }
}
