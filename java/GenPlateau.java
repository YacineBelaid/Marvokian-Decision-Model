import java.io.OutputStreamWriter;

public class GenPlateau {
    public static void main(String args[]) throws Exception 
    {
        Plateau p = Plateau.generate(Integer.parseInt(args[0]),
                                     Integer.parseInt(args[1]),
                                     Integer.parseInt(args[2]),
                                     Integer.parseInt(args[3]),
                                     Integer.parseInt(args[4]));
        OutputStreamWriter out = new OutputStreamWriter(System.out);
        Plateau.save(p, out);
        out.close();
    }
}
