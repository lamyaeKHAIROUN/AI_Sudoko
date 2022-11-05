package Sudoko;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
public class ExtraireMatrice {
    public int[][] loadMatrice9() {
        try {
            int[][] matrix = Files.lines(Paths.get("src/main/java/Fichier/matrice9.csv"))
                    .map(line -> Arrays.stream(line.split(";"))
                            .mapToInt(Integer::parseInt)
                            .toArray())
                    .toArray(int[][]::new);
            return matrix;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String[][] loadMatrice16() {
        String dataRow;
        String[][] csvMatrix;

        try {
            BufferedReader CSVFile =
                    new BufferedReader(new FileReader("src/main/java/Fichier/matrice16.csv"));
            LinkedList<String[]> rows = new LinkedList<>();
            int i = 0;
            while ((dataRow = CSVFile.readLine()) != null) {
                i++;
                rows.addLast(dataRow.split(";"));
            }
            csvMatrix = rows.toArray(new String[rows.size()][]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return csvMatrix;
    }

    public String[][] loadMatriceSigneLigne() {
        String dataRow;
        String[][] csvMatrix;

        try {
            BufferedReader CSVFile =
                    new BufferedReader(new FileReader("src/main/java/Fichier/matriceSigne.csv"));
            LinkedList<String[]> rows = new LinkedList<>();
            int i = 0;
            while ((dataRow = CSVFile.readLine()) != null) {
                i++;
                rows.addLast(dataRow.split(";"));
            }
            csvMatrix = rows.toArray(new String[rows.size()][]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return csvMatrix;
    }

    public String[][] loadMatriceSigneColonne() {
        String dataRow;
        String[][] csvMatrix;

        try {
            BufferedReader CSVFile =
                    new BufferedReader(new FileReader("src/main/java/Fichier/matriceSigneColonne.csv"));
            LinkedList<String[]> rows = new LinkedList<>();
            int i = 0;
            while ((dataRow = CSVFile.readLine()) != null) {
                i++;
                rows.addLast(dataRow.split(";"));
            }
            csvMatrix = rows.toArray(new String[rows.size()][]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return csvMatrix;
    }
}