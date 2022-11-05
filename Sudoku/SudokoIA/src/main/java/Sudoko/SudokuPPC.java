package Sudoko;

import org.apache.commons.cli.*;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.chocosolver.solver.search.strategy.Search.minDomLBSearch;
import static org.chocosolver.util.tools.ArrayUtils.append;

public class SudokuPPC {
    static int n, s;
     static long timeout = 3600000; // one hour
    IntVar[][] rows, cols, shapes; //Create variables
    Model model; //Create a Model
    static int number, choix9;
    ExtraireMatrice f = new ExtraireMatrice();
    int[][] matricePredefini9 = f.loadMatrice9(); //chargement de la matrice pre-remplis de 9*9
    String[][] matricePredefini16 = f.loadMatrice16(); //chargement de la matrice pre-remplis de 16*16
    String[][] matriceSigne = f.loadMatriceSigneLigne();
    String[][] matriceColonne = f.loadMatriceSigneColonne();
    static HashMap<String, Integer> codeAscii = new HashMap<>();

    public static void main(String[] args) throws ParseException, IOException {
        System.out.println(" -------------------------------- Jeu de sudoko --------------------------------");
        System.out.println("Bienvenue dans le jeu de sudoko, Merci de rentré la taille de la grille, qui doit etre de taille  4, 9 ou 16");
        Scanner inputReader = new Scanner(System.in);
        System.out.println("Enter un nombre: ");
        number = inputReader.nextInt();
        if (number == 9) {
            System.out.println("- Tapez 1 si vous voulez voire le résultat de la matrice prédifini dans le tp ");
            System.out.println("- Tapez 2 si vous voulez voire le résultat d'une matrice aléatoire ");
            System.out.println("- Tapez 3 si vous voulez voire le résultat de sudoko  Greater Than Sudoku");
            choix9 = inputReader.nextInt();
            System.out.println("Solution pour sudoko 9*9:  ");

        }
        if (number == 16) {
            System.out.println("Solution pour sudoko 16*16:  ");

        }

        final Options options = configParameters();
        final CommandLineParser parser = new DefaultParser();
        final CommandLine line = parser.parse(options, args);
        boolean helpMode = line.hasOption("help");
        if (helpMode) {
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("sudoku", options, true);
            System.exit(0);
        }
        // Check arguments and options
        for (Option opt : line.getOptions()) {
            checkOption(line, opt.getLongOpt());
        }
        n = number;
        s = (int) Math.sqrt(n);
        new SudokuPPC().solve();

        //pour voire tous les solution possible
        // new SudokuPPC().solveAll();
    }

    public void solve() {
        buildModel();
        model.getSolver().solve();
        printGrid();
        model.getSolver().printStatistics();
    }

    public void solveAll() {
        buildModel();
        while (model.getSolver().solve()) {
            model.getSolver().solve();
            printGrid();
        }
        model.getSolver().printStatistics();
    }

    public void printGrid() {

        String a = "┌───";
        String b = "├───";
        String c = "─┬────┐";
        String d = "─┼────┤";
        String e = "─┬───";
        String f = "─┼───";
        String g = "└────┴─";
        String h = "───┘";
        String k = "───┴─";
        String esp = " ";

        for (int i = 0; i < n; i++) {
            for (int line = 0; line < n; line++) {
                if (line == 0) {
                    System.out.print(i == 0 ? a : b);
                } else if (line == n - 1) {
                    System.out.print(i == 0 ? c : d);
                } else {
                    System.out.print(i == 0 ? e : f);
                }
            }
            System.out.println("");
            System.out.print("│ ");
            for (int j = 0; j < n; j++) {
                if (rows[i][j].getValue() > number)
                    System.out.print(rows[i][j].getValue() + " │ ");
                else {
                    if (number == 16) System.out.print(esp + getKey(codeAscii, rows[i][j].getValue()) + " │ ");
                    else System.out.print(esp + rows[i][j].getValue() + " │ ");
                }
            }
            if (i == n - 1) {
                System.out.println("");
                for (int line = 0; line < n; line++) {
                    System.out.print(line == 0 ? g : (line == n - 1 ? h : k));
                }
            }
            System.out.println("");

        }
    }
    public void buildModel() {
        model = new Model("Sudoko problem");
        rows = new IntVar[n][n];
        cols = new IntVar[n][n];
        shapes = new IntVar[n][n];
        if (choix9 == 3) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    rows[i][j] = model.intVar(1, 9);
                    cols[j][i] = rows[i][j];
                }
            }
            for (int i = 0; i < n; i++) {
                int limiteBoucle = 0;
                for (int j = 0; j < n; j++) {
                    if (j % s != 0) {
                        String oppLigne = matriceSigne[i][limiteBoucle];
                        String oppCol = matriceColonne[i][limiteBoucle];
                        model.arithm(rows[i][j - 1], oppLigne, rows[i][j]).post();
                        model.arithm(cols[i][j - 1], oppCol, cols[i][j]).post();
                        limiteBoucle++;
                    }
                }
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                switch (number) {
                    case 4: {
                        rows[i][j] = model.intVar("c_" + i + "_" + j, 1, n, false);
                        cols[j][i] = rows[i][j];
                        break;
                    }
                    case 9: {
                        int valeur = matricePredefini9[i][j];
                        if (choix9 == 1) {
                            if (valeur < 1) {
                                rows[i][j] = model.intVar("c_" + i + "_" + j, 1, 9, false);
                                cols[j][i] = rows[i][j];
                            } else {
                                rows[i][j] = model.intVar(matricePredefini9[i][j]);
                                cols[j][i] = rows[i][j];
                            }
                        } else if (choix9 == 2) {
                            rows[i][j] = model.intVar("c_" + i + "_" + j, 1, 9, false);
                            cols[j][i] = rows[i][j];
                        }
                        break;
                    }
                    case 16: {
                        String valeur = matricePredefini16[i][j];
                        if (valeur.equals("0")) {
                            rows[i][j] = model.intVar("c_" + i + "_" + j, 1, 16, false);
                            cols[j][i] = rows[i][j];
                        } else {
                            rows[i][j] = model.intVar(this.getCodeAscii().get(valeur));
                            cols[j][i] = rows[i][j];

                        }
                        break;
                    }

                    default: {
                        System.err.println("Vous devez choisir comme nombre : 4, 9, 16");
                        System.exit(2);
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s; j++) {
                for (int k = 0; k < s; k++) {
                    for (int l = 0; l < s; l++) {
                        shapes[j + k * s][i + (l * s)] = rows[l + k * s][i + j * s];
                    }
                }
            }
        }
        for (int i = 0; i < n; i++) {
            model.allDifferent(rows[i]).post();
            model.allDifferent(cols[i]).post();
            model.allDifferent(shapes[i]).post();
        }
    }

    public static void checkOption(CommandLine line, String option) {
        switch (option) {
            case "inst":
                number = Integer.parseInt(line.getOptionValue(option));
                break;
            case "timeout":
                timeout = Long.parseLong(line.getOptionValue(option));
                break;
            default: {
                System.err.println("Bad parameter: " + option);
                System.exit(2);
            }
        }
    }

    private static Options configParameters() {
        final Option helpFileOption = Option.builder("h").longOpt("help").desc("Display help message").build();
        final Option instOption = Option.builder("i").longOpt("instance").hasArg(true).argName("sudoku instance")
                .desc("sudoku size").required(false).build();
        final Option limitOption = Option.builder("t").longOpt("timeout").hasArg(true).argName("timeout in ms")
                .desc("Set the timeout limit to the specified time").required(false).build();
        final Options options = new Options();
        options.addOption(instOption);
        options.addOption(limitOption);
        options.addOption(helpFileOption);
        return options;
    }

    public void configureSearch() {
        model.getSolver().setSearch(minDomLBSearch(append(rows)));
    }

    public static <String, Integer> String getKey(Map<String, Integer> map, Integer value) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public HashMap<String, Integer> getCodeAscii() {
        codeAscii.put("1", 1);
        codeAscii.put("2", 2);
        codeAscii.put("3", 3);
        codeAscii.put("4", 4);
        codeAscii.put("5", 5);
        codeAscii.put("6", 6);
        codeAscii.put("7", 7);
        codeAscii.put("8", 8);
        codeAscii.put("9", 9);
        codeAscii.put("A", 10);
        codeAscii.put("B", 11);
        codeAscii.put("C", 12);
        codeAscii.put("D", 13);
        codeAscii.put("E", 14);
        codeAscii.put("F", 15);
        codeAscii.put("G", 16);
        return codeAscii;
    }
}