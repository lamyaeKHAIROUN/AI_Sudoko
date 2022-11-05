package Sudoko;


import org.junit.jupiter.api.Test;

import static Sudoko.SudokuBT.compteur;
import static Sudoko.SudokuBT.grid;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SudokuBTTest {
    SudokuBT testSudoko = new SudokuBT(4);
    int[][] matriceTest = {{1, 2, 3, 4},
            {3, 4, 1, 2},
            {2, 1, 4, 3}, {4, 3, 2, 1}};


    @Test
    void findSolutionTest() {

        assertEquals(true, testSudoko.findSolution(0, 0));
    }

    @Test
    void findSolutionTestMatrice() {
        long debut = System.currentTimeMillis();

        testSudoko.findSolution(0, 0);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals(matriceTest[i][j], grid[i][j]);

            }
        }
        System.out.println(System.currentTimeMillis() - debut);
    }

    @Test
    void findSolutionAllTest() {
        //donner le nombre et verifier que c'est le bon
        testSudoko.findSolutionAll(0, 0);
        int compteurTest = compteur;
        assertEquals(288, compteurTest);

    }

    @Test
    void testToString() {
    }

    @Test
    void main() {
    }
}