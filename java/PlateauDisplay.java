import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class PlateauDisplay extends JPanel {
    
    protected Plateau plateau;
    protected PolitiqueSimple politique;
    protected int     nbCols, nbLignes;

    public PlateauDisplay(){
        nbCols = 20;
        nbLignes = 20;
        setBackground(Color.DARK_GRAY);
    }
    
    public void setPlateau(Plateau p){
        plateau = p;
        int d = p.nbCases / 4 * 3;
        nbLignes = (int) Math.sqrt((int) d);
        nbCols = (p.nbCases+nbLignes-1) / nbLignes;
        repaint();
    }
    
    public void setPolitique(PolitiqueSimple ps){
        politique = ps;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        
        for(int l=0;l<=nbLignes;l++){
            int y = l * getHeight() / nbLignes;
            g.drawLine(0, y, getWidth(), y);
        }
        for(int c=0;c<=nbCols;c++){
            int x = c * getWidth() / nbCols;
            g.drawLine(x, 0, x, getHeight());
        }

        int n=0;
        for(int l=0;l<nbLignes;l++)
            for(int c=0;c<nbCols;c++){
                int y = l * getHeight() / nbLignes;
                int x = c * getWidth() / nbCols;
                int y2 = (l+1) * getHeight() / nbLignes;
                int x2 = (c+1) * getWidth() / nbCols;
                if(n<plateau.nbCases){
                    g.setColor(Color.white);
                    g.drawString("" + n, x, y+14);
                
                    if(plateau.liens[n] != -1){
                        int t = plateau.liens[n];
                        g.setColor(t>n ? Color.green : Color.red);
                        g.drawString("" + t, x, y+28);
                    }
                    
                    if(politique!=null && n<politique.actions.length){
                        int a = (int) politique.actions[n].ordinal();
                        if(a>=0){
                            g.setColor(colorsPolitiques[a]);
                            g.fillRect(x2-24, y2-24, 24, 24);
                            g.setColor(Color.black);
                            g.drawString(actionsNoms[a], x2-22, y2-4);
                        }
                    }
                }
                n++;
            }
    }
    
    private static Color[] colorsPolitiques = new Color[] {Color.ORANGE, Color.CYAN, Color.PINK};
    private static String[] actionsNoms = {"1", "D", "DD"};

}
