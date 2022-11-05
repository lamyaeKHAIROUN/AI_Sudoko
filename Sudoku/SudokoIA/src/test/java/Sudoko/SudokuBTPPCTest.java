package Sudoko;

import org.junit.jupiter.api.Test;

class SudokuBTPPCTest {


    @Test
    public void testTempsUneSolution() {
        long startTime = System.currentTimeMillis();
        SudokuBT bt = new SudokuBT(4);
        bt.findSolution(0, 0);
        long endTime = System.currentTimeMillis();
        System.out.println("Le temps total écoulé dans l'exécution de la méthode findSolution est :" + (endTime - startTime) + "ms");
    }

    @Test
    public void testTempsAllSolution() {
        long startTime = System.currentTimeMillis();
        SudokuBT bt = new SudokuBT(4);
        bt.findSolutionAll(0, 0);
        long endTime = System.currentTimeMillis();
        System.out.println("Le temps total écoulé dans l'exécution de la méthode find all solution  :" + (endTime - startTime) + "ms");
    }


}