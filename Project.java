import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * <p>
 * This class contains every method to perform a string validation thorugh a
 * top-down analysis with an specific free-context grammar.
 * <p>
 * It recieves the grammar rules through a file text, indicating the terminal and
 * non-terminal symbols, as well as the rule derivations.
 * <p>
 * It uses iterative algorithms in roder to derivate the initial non-terminal
 * symbol unitl the string is reached or until it is no longer possible to 
 * obtain it.
 *
 * @author Fernando Rios Chavez
 * @see BufferedReader
 * @see FileReader
 * @see HashMap
 * @see ArrayList
 * @see Scanner
 * @see LinkedList
 * @see Node
 */
public class Project {

    /**
	 * <p>A hashmap that turns non-termianl symbols into integers, with the 
     * purpose of simplifying data manipulation. 
	 * E.g. {S => 0, A => 1, B => 2, ...}
	 */	
    HashMap<String, Integer> capitalSymbolsMap;

    /**
	 * <p>A bidimensional ArrayList of Strings that represent the grammar rules. 
	 * It is an ArrayList of size = number of nom-terminal symbols, where every 
     * cell contains an ArrayList of Strings made of the following derivations 
     * using that non-terminal symbols.  
	 * E.g. grammarRules.get(0).get(1) = AaaS (first rule using symbol 0 (S)).
	 */
    ArrayList<ArrayList<String>> grammarRules;

    /**
	 * <p>A hashmap that turns terminal symbols into integers, with the 
     * purpose of simplifying data manipulation. 
	 * E.g. {a => 0, b => 1, c => 2, ...}
	 */	
    HashMap<String, Integer> lowcaseSymbolsMap;

    /**
	 * The scanner will be used for the string inputs. 
	 */
    Scanner sc;
    String input;
    BufferedReader reader;
    File file = null;
    String[] capitalSymbols;
    String[] lowCaseSymbols;
    String initialCapitalSymbol;

    /**
	 *<p> Project Constructor
	 */
    public Project() {
        grammarRules = new ArrayList<ArrayList<String>>();
        capitalSymbolsMap = new HashMap<String, Integer>();
        lowcaseSymbolsMap = new HashMap<String, Integer>();
    }    

    /**
     * <p> This function opens and reads a file obtaining grammar data and 
     * storing it in the correspondent data structures. 
	 * It opens a search dialog window to select .txt files exclusively.  
	 * Once selected, it reads line by line with a BufferedReader retrieving data
     * with the specified format.  
	 * @see JFileChooser
	 * @see BufferedReader
	 */
    public void readFile(){

        do {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("."));
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.addChoosableFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
            int returnVal = chooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = chooser.getSelectedFile();
            }
        } while (file == null);

        try{
            reader = new BufferedReader(new FileReader(file));
            String currentLine;
            System.out.println("File being used: " + file.getName());

            currentLine = reader.readLine();
            capitalSymbols = currentLine.split(",");
            for (int i = 0; i < capitalSymbols.length; i++) {
				capitalSymbolsMap.put(capitalSymbols[i], i);
				ArrayList<String> rulesList = new ArrayList<String>();
				grammarRules.add(rulesList);
            }
            
            currentLine = reader.readLine();
            lowCaseSymbols = currentLine.split(",");
            for (int i = 0; i < lowCaseSymbols.length; i++) {
				lowcaseSymbolsMap.put(lowCaseSymbols[i], i);
            }
            
            currentLine = reader.readLine();
            initialCapitalSymbol = currentLine;
            
            currentLine = reader.readLine();
			while (currentLine != null) {
				String currentCapitalSymbol = currentLine.split("->")[0];
                String currentRule = currentLine.split("->")[1];

                if(currentRule.equals("lmd")){
                    grammarRules.get(capitalSymbolsMap.get(currentCapitalSymbol)).add("");
                } else {
                    grammarRules.get(capitalSymbolsMap.get(currentCapitalSymbol)).add(currentRule);
                }
                
				currentLine = reader.readLine();
			}            
            reader.close();  
        } catch(IOException e){
            e.printStackTrace();
        }

    }

    /**
	 *<p> Performs the top-down algorithm seen in class.  
	 * The initial non-terminal symbol is added to the queue, and added as a new node. 
     * Then, the string is splited in three where it finds its first non-terminal symbol.
     * 'u' is the left-most part. 'A' is the non-terminal symbol. 'v' is the rest.  
     * Derivation rules of that non-terminal symbol are applied in order, transforming
     * 'A' into a new substring 'w'.  'u', 'w' and 'v' are concatenated into 'uwv'.
     * If 'uwv' is not the final string and it does contain non-terminal symbols and
     * its length is equal or shorter than the final string, 'uwv' is added to the
     * queue and the process is repeated. 
     * Otherwise, 'uwv' is no longer analized.
     * If the queue becomes empty before finding the string, the string is rejected. 
     * Otherwise it is accpted. 
	 * @param p a String in order to be analyzed.
     * @return accepted a boolean indicating if the input string is accpted or not. 
	 * @see #leftmostCapital
     * @see Node
	 */
    public boolean topDown(String p){
        Queue<String> queue = new LinkedList<String>();
        String q, u, A, v, w, uwv;
        boolean done;
        int i, j;
        
        uwv = null;
        queue.add(initialCapitalSymbol);

        do{
            q = queue.remove();

            /**
             * Add a node of the element remove from the queue, using
             * the Tree structure from www.javagists.com
             */
            Node current_parent = new Node(q);
            
            i = 0;
            done = false;

            A = leftmostCapital(q);
            u = q.split(A, 2)[0];
            v = q.split(A, 2)[1];
            
            do{
                j = i + 1;
                if (j > grammarRules.get(capitalSymbolsMap.get(A)).size()){
                    done = true;
                } else {
                    w = grammarRules.get(capitalSymbolsMap.get(A)).get(i);
                    uwv = u + w + v;
                    if (!(leftmostCapital(uwv).equals("none")) && p.startsWith(u) 
                    && (uwv.length() <= p.length() + 5) ){
                        queue.add(uwv);
                        current_parent.addChild(new Node(uwv));
                    }
                }
                i = j;
            }while(!done && !uwv.equals(p) );

        }while(!queue.isEmpty() && !uwv.equals(p) );

        if(uwv.equals(p)){
            return true;
        } else{
            return false;
        }
        
    }

    /**
     *<p> A function that checks if a string contains a non-terminal symbol. 
	 * @param expression a String in order to be analyzed.
     * @return the leftmost non-terminal symbol found. If not found, will return "none". 
	 */
    public String leftmostCapital(String expression){
        char[] characters = expression.toCharArray();
        for (char c : characters) {
            if ( capitalSymbolsMap.containsKey(Character.toString(c)) ){
                return Character.toString(c);
            }
        }
        return "none";
    }
}