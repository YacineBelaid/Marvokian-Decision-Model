
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;

public class GenPolitique3 {

    static PolitiqueAvancee generate(Plateau plateau) {
        PolitiqueAvancee pol = new PolitiqueAvancee(plateau.nbCases);
        
        for(int i=0;i<plateau.nbCases;i++)
            for(int j=0;j<plateau.nbCases;j++)
                pol.actions[i][j] = i<j ? ActionType.DeuxDes : ActionType.UnDe;
        
        return pol;
    }
     
    public static void main(String args[]) throws Exception {
        Reader reader = args.length==0 ? new InputStreamReader(System.in) : new FileReader(args[0]);
        Plateau plateau = Plateau.load(reader);
        PolitiqueAvancee pol = generate(plateau);
        OutputStreamWriter out = new OutputStreamWriter(System.out);
        PolitiqueAvancee.save(pol, out);
        out.close();
    }

}
