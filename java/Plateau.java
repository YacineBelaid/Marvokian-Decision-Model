import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Random;
import java.util.StringTokenizer;

public class Plateau {
    
    protected int    nbCases;
    protected int[]  liens; // serpents et échelles
    
    public Plateau(int nbcases){
        nbCases = nbcases;
        liens = new int[nbCases];
        for(int i=0;i<nbCases;i++)
            liens[i] = -1;
    }

    public int getNbCases(){
        return nbCases;
    }
    
    public void addLink(int d, int f){
        if(f!=d && getNextCase(f, 0)!=d)
            liens[d] = f;
    }
    
    public int getNextCase(int case1, int avancement){
        if(case1+avancement < nbCases)
            case1 += avancement;
        while(liens[case1]!=-1)
            case1 = liens[case1];
        return case1;
    }
    
    public static Plateau generate(int nbcases, int nbechelles, int eLongMax,
            int nbserpents, int sLongMax){
        
        Plateau p = new Plateau(nbcases);
        Random r = new Random();
        int free[] = new int[nbcases];
        int n = nbcases-2;
        for(int i=0;i<n;i++)
            free[i] = i+1;
        
        for(int i=0;i<nbechelles && n>0;i++){
            int c1 = free[r.nextInt(n--)];
            int c2 = c1 + r.nextInt(eLongMax-1) + 1;
            if(c2>=nbcases)
                c2 = nbcases - 1;
            p.addLink(c1, c2);
            free[c1] = free[n];
        }
        
        for(int i=0;i<nbserpents && n>0;i++){
            int c1 = free[r.nextInt(n--)];
            int c2 = c1 - r.nextInt(sLongMax-1) - 1;
            if(c2<=0)
                c2 = 0;
            p.addLink(c1, c2);
            free[c1] = free[n];
        }

        return p;
    }
    
    public static Plateau load(Reader reader) throws IOException{
        BufferedReader br = new BufferedReader(reader);
        Plateau p = new Plateau(Integer.parseInt(br.readLine()));
        
        int nb = Integer.parseInt(br.readLine());
        for(int i=0;i<nb;i++){
            StringTokenizer tokens = new StringTokenizer(br.readLine());
            int c1 = Integer.parseInt(tokens.nextToken());
            int c2 = Integer.parseInt(tokens.nextToken());
            p.addLink(c1, c2);
        }
        return p;
    }
    
    public static void save(Plateau p, Writer writer) throws IOException {
        PrintWriter out = new PrintWriter(writer);
        out.println("" + p.nbCases);
        int nb=0;
        for(int i=0;i<p.nbCases;i++)
            if(p.liens[i] != -1)
                nb++;
        out.println("" + nb);
        for(int i=0;i<p.nbCases;i++)
            if(p.liens[i] != -1)
                out.println("" + i + " " + p.liens[i]);
        
    }
}
