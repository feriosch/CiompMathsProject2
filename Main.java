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

public class Main {

    static HashMap<String, Integer> capitalSymbolsMap;
    static ArrayList<ArrayList<String>> grammarRules;
    static HashMap<String, Integer> lowcaseSymbolsMap;
    static Scanner sc;
    static String input;
    static BufferedReader reader;
    static File file = null;
    static String[] capitalSymbols;
    static String[] lowCaseSymbols;
    static String initialCapitalSymbol;

    public static void main(String[] args) {

        grammarRules = new ArrayList<ArrayList<String>>();
        capitalSymbolsMap = new HashMap<String, Integer>();
        lowcaseSymbolsMap = new HashMap<String, Integer>();
        
        /**
         * The file is chosen here. Only .txt files are accepted.
         * The program will not continue until a .txt file is accepted. 
         */
        readFile(file);

        System.out.println();
		System.out.println("To exit, press Q");
        sc = new Scanner(System.in);
        
        do{
            System.out.print("Insert string: ");
            input = sc.nextLine();

            if(!input.equals("Q")){
                if(topDown(input)){
                    System.out.println("Accepted.");
                }
                else{
                    System.out.println("Unaccepted");
                }
            } 
        }while(!input.equals("Q"));
    }    

    public static void readFile(File file){

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

                grammarRules.get(capitalSymbolsMap.get(currentCapitalSymbol)).add(currentRule);

				currentLine = reader.readLine();
			}            
            reader.close();  
        } catch(IOException e){
            e.printStackTrace();
        }

    }

    public static boolean topDown(String p){
        Queue<String> queue = new LinkedList<String>();
        String q, u, A, v, w, uwv;
        boolean done;
        int i, j;
        
        uwv = null;
        queue.add(initialCapitalSymbol);

        do{
            q = queue.remove();
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
                    && (uwv.length() <= p.length()) ){
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

    public static String leftmostCapital(String expression){
        char[] characters = expression.toCharArray();
        for (char c : characters) {
            if ( capitalSymbolsMap.containsKey(Character.toString(c)) ){
                return Character.toString(c);
            }
        }
        return "none";
    }
}