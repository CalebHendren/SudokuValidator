public class SudokuValidator {
    private static final int GRID_SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    private int[][] grid;
    private boolean isValid;

    public SudokuValidator(int[][] grid) {
        this.grid = grid;
        this.isValid = true;
    }

    public void validate() {
        // Create threads for validating rows
        Thread rowThread = new Thread(new RowValidator());

        // Create threads for validating columns
        Thread columnThread = new Thread(new ColumnValidator());

        // Create threads for validating 3x3 subgrids
        Thread[] subgridThreads = new Thread[GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            subgridThreads[i] = new Thread(new SubgridValidator(i));
        }

        // Start all threads
        rowThread.start();
        columnThread.start();
        for (Thread thread : subgridThreads) {
            thread.start();
        }

        // Wait for all threads to complete
        try {
            rowThread.join();
            columnThread.join();
            for (Thread thread : subgridThreads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check the validation result
        if (isValid) {
            System.out.println("The Sudoku puzzle is valid.");
        } else {
            System.out.println("The Sudoku puzzle is not valid.");
        }
    }

    private class RowValidator implements Runnable {
        @Override
        public void run() {
            for (int row = 0; row < GRID_SIZE; row++) {
                if (!isValid) {
                    break;
                }
                boolean[] digits = new boolean[GRID_SIZE + 1];
                for (int col = 0; col < GRID_SIZE; col++) {
                    int digit = grid[row][col];
                    if (digit < 1 || digit > 9 || digits[digit]) {
                        isValid = false;
                        break;
                    }
                    digits[digit] = true;
                }
            }
        }
    }

    private class ColumnValidator implements Runnable {
        @Override
        public void run() {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (!isValid) {
                    break;
                }
                boolean[] digits = new boolean[GRID_SIZE + 1];
                for (int row = 0; row < GRID_SIZE; row++) {
                    int digit = grid[row][col];
                    if (digit < 1 || digit > 9 || digits[digit]) {
                        isValid = false;
                        break;
                    }
                    digits[digit] = true;
                }
            }
        }
    }

    private class SubgridValidator implements Runnable {
        private int subgridIndex;

        public SubgridValidator(int subgridIndex) {
            this.subgridIndex = subgridIndex;
        }

        @Override
        public void run() {
            int rowStart = (subgridIndex / SUBGRID_SIZE) * SUBGRID_SIZE;
            int colStart = (subgridIndex % SUBGRID_SIZE) * SUBGRID_SIZE;
            boolean[] digits = new boolean[GRID_SIZE + 1];

            for (int row = rowStart; row < rowStart + SUBGRID_SIZE; row++) {
                for (int col = colStart; col < colStart + SUBGRID_SIZE; col++) {
                    int digit = grid[row][col];
                    if (digit < 1 || digit > 9 || digits[digit]) {
                        isValid = false;
                        return;
                    }
                    digits[digit] = true;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[][] grid = {
            {6, 2, 4, 5, 3, 9, 1, 8, 7},
            {5, 1, 9, 7, 2, 8, 6, 3, 4},
            {8, 3, 7, 6, 1, 4, 2, 9, 5},
            {1, 4, 3, 8, 6, 5, 7, 2, 9},
            {9, 5, 8, 2, 4, 7, 3, 6, 1},
            {7, 6, 2, 3, 9, 1, 4, 5, 8},
            {3, 7, 1, 9, 5, 6, 8, 4, 2},
            {4, 9, 6, 1, 8, 2, 5, 7, 3},
            {2, 8, 5, 4, 7, 3, 9, 1, 6}
        };

        SudokuValidator validator = new SudokuValidator(grid);
        validator.validate();
    }
}