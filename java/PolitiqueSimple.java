import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

public class PolitiqueSimple {
    
    protected ActionType[] actions;
    
    public PolitiqueSimple(int nbcases){
        actions = new ActionType[nbcases];
    }
    
    public ActionType getAction(int position){
        return actions[position];
    }
    
    public static PolitiqueSimple load(Reader reader) throws IOException {
        BufferedReader br = new BufferedReader(reader);
        int nb = Integer.parseInt(br.readLine());
        PolitiqueSimple politique = new PolitiqueSimple(nb);
        for(int i=0;i<nb;i++){
            String a = br.readLine();
            politique.actions[i] = ActionType.get(a);
        }
        return politique;
    }
    
    public static void save(PolitiqueSimple p, Writer writer) throws IOException {
        PrintWriter out = new PrintWriter(writer);
        out.println(p.actions.length);
        for(int i=0;i<p.actions.length;i++)
            out.println("" + p.actions[i].getLabel());
        out.close();
    }   
}
