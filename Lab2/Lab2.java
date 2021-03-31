package Lab2;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import shared.Matrix;
import shared.InitData;
import shared.MultiplierThreadCyclicBarrier;
import shared.AddingThreadCyclicBarrier;
import shared.SortingThreadCyclicBarrier;

class Lab2 {

  public static void main(String[] args) {
    // Зчитуємо інформацію з файла, або генеруємо наново
    List<double[][]> matrixList = InitData.readInputData("../shared/InputData.txt");
    if (matrixList.size() == 0) {
      InitData.initInputData("../shared/InputData.txt");
      matrixList = InitData.readInputData("../shared/InputData.txt");
    }
    final double[][] MO_Matrix = matrixList.get(0); // MO матриця
    final double[][] MP_Matrix = matrixList.get(1); // MP матриця
    final double[][] R_Matrix = matrixList.get(2); // R матриця
    final double[][] S_Matrix = matrixList.get(3); // S матриця

    long m = System.currentTimeMillis();

    double[][] resultMatrixMT = multiplyMatrixMT(
      MO_Matrix,
      MP_Matrix,
      Runtime.getRuntime().availableProcessors()
    );

    resultMatrixMT = multiplyMatrixMT(
      resultMatrixMT,
      R_Matrix,
      Runtime.getRuntime().availableProcessors()
    );

    resultMatrixMT = addMatrixMT(
      resultMatrixMT,
      S_Matrix,
      Runtime.getRuntime().availableProcessors()
    );

    resultMatrixMT = sortMatrixRowsMT(
      resultMatrixMT,
      Runtime.getRuntime().availableProcessors()
    );

    resultMatrixMT = sortMatrixColumnsMT(
      resultMatrixMT,
      Runtime.getRuntime().availableProcessors()
    );

    System.out.println("Total calculation time:" + (double) (System.currentTimeMillis() - m));

    // РОЗКОМЕНТУВАТИ ДЛЯ ПОРІВНЯННЯ ТА ВИВОДА У ФАЙЛ
    // // Перевірка багатопоточних обчислень однопоточними
    // System.out.println("Comparing started...");
    // double[][] resultMatrix = Matrix.multiplyMatrix(MO_Matrix, MP_Matrix);
    // resultMatrix = Matrix.multiplyMatrix(resultMatrix, R_Matrix);
    // resultMatrix = Matrix.addMatrix(resultMatrix, S_Matrix);
    // resultMatrix = Matrix.sortMatrix(resultMatrix);
    // final boolean isEqual = Matrix.compareMatrix(resultMatrix, resultMatrixMT);
    // System.out.println("Comparing finished.");
    // if (!isEqual) { return; }

    // // Вивід матриць у файл
    // Matrix.printResultMatrix("Results_Lab2.txt", resultMatrix, resultMatrixMT);
  }

  /**
   * Багатопоточне множення матриць
   *
   * @param firstMatrix  Перша матриця
   * @param secondMatrix Друга матриця
   * @param threadCount  Кількість потоків
   * @return Результуюча матриця
   */
  private static double[][] multiplyMatrixMT(
    final double[][] firstMatrix,
    final double[][] secondMatrix,
    int threadCount
  ) {
    assert threadCount > 0;

    final int rowCount = firstMatrix.length; // Кількість рядків результуючої матриці
    final int colCount = secondMatrix[0].length; // Кількість стовпців результуючої матриці
    final double[][] result = new double[rowCount][colCount];

    final int cellsForThread = (rowCount * colCount) / threadCount; // Кількість обчислюваних комірок на потік
    int firstIndex = 0; // Індекс першої комірки
    final MultiplierThreadCyclicBarrier[] multiplierThreads = new MultiplierThreadCyclicBarrier[threadCount]; // Масив потоків

    // ЗАВДАННЯ! Створення CyclicBarrier
    final CyclicBarrier cyclicBarrier = new CyclicBarrier(
      threadCount + 1, // +1 тому що основний потік (main thread) теж враховується
      new Runnable() {
        @Override
        public void run() {
          System.out.println("All multiply calculations have finished");
        }
      }
    );

    // Створення та запуск потоків
    for (int threadIndex = threadCount - 1; threadIndex >= 0; --threadIndex) {
      int lastIndex = firstIndex + cellsForThread; // Індекс останньої комірки
      if (threadIndex == 0) {
        lastIndex = rowCount * colCount;
      }
      multiplierThreads[threadIndex] = new MultiplierThreadCyclicBarrier(result, firstMatrix, secondMatrix, firstIndex, lastIndex, cyclicBarrier);
      multiplierThreads[threadIndex].start();
      firstIndex = lastIndex;
    }

    try {
      cyclicBarrier.await(); // await в основному потоці
    } catch (InterruptedException iex) {
      System.out.println(iex.getMessage());
    } catch (BrokenBarrierException bbex) {
      System.out.println(bbex.getMessage());
    }
    
    return result;
  }

  /**
   * Багатопоточне ДОДАВАННЯ матриць
   *
   * @param firstMatrix  Перша матриця
   * @param secondMatrix Друга матриця
   * @param threadCount  Кількість потоків
   * @return Результуюча матриця
   */
  private static double[][] addMatrixMT(
    final double[][] firstMatrix,
    final double[][] secondMatrix,
    int threadCount
  ) {
    assert threadCount > 0;

    final int rowCount = firstMatrix.length; // Кількість рядків результуючої матриці
    final int colCount = secondMatrix[0].length; // Кількість стовпців результуючої матриці
    final double[][] result = new double[rowCount][colCount];

    final int cellsForThread = (rowCount * colCount) / threadCount; // Кількість обчислюваних комірок на потік
    int firstIndex = 0; // Індекс першої комірки
    final AddingThreadCyclicBarrier[] addingThreads = new AddingThreadCyclicBarrier[threadCount]; // Масив потоків

    // ЗАВДАННЯ! Створення CyclicBarrier
    final CyclicBarrier cyclicBarrier = new CyclicBarrier(
      threadCount + 1, // +1 тому що основний потік (main thread) теж враховується
      new Runnable() {
        @Override
        public void run() {
          System.out.println("All adding calculations have finished");
        }
      }
    );

    // Створення та запуск потоків
    for (int threadIndex = threadCount - 1; threadIndex >= 0; --threadIndex) {
      int lastIndex = firstIndex + cellsForThread; // Індекс останньої комірки
      if (threadIndex == 0) {
        lastIndex = rowCount * colCount;
      }
      addingThreads[threadIndex] = new AddingThreadCyclicBarrier(result, firstMatrix, secondMatrix, firstIndex, lastIndex, cyclicBarrier);
      addingThreads[threadIndex].start();
      firstIndex = lastIndex;
    }

    try {
      cyclicBarrier.await(); // await в основному потоці
    } catch (InterruptedException iex) {
      System.out.println(iex.getMessage());
    } catch (BrokenBarrierException bbex) {
      System.out.println(bbex.getMessage());
    }

    return result;
  }

  /**
   * Багатопоточне СОРТУВАННЯ рядків матриці
   *
   * @param matrix  Матриця
   * @param threadCount  Кількість потоків
   * @return Результуюча матриця
   */
  private static double[][] sortMatrixRowsMT(
    final double[][] matrix,
    int threadCount
  ) {
    assert threadCount > 0;

    final int rowCount = matrix.length; // Кількість рядків результуючої матриці
    final int colCount = matrix[0].length; // Кількість стовпців результуючої матриці

    final int rowsForThread = rowCount / threadCount; // Кількість обчислюваних рядків на потік
    int firstRowIndex = 0; // Індекс першого рядка
    final SortingThreadCyclicBarrier[] sortingThreads = new SortingThreadCyclicBarrier[threadCount]; // Масив потоків

    // ЗАВДАННЯ! Створення CyclicBarrier
    final CyclicBarrier cyclicBarrier = new CyclicBarrier(
      threadCount + 1, // +1 тому що основний потік (main thread) теж враховується
      new Runnable() {
        @Override
        public void run() {
          System.out.println("All matrix sorting by rows have finished");
        }
      }
    );

    // Створення та запуск потоків
    for (int threadIndex = 0; threadIndex <= threadCount - 1; ++threadIndex) {
      int lastRowIndex = firstRowIndex + rowsForThread; // Індекс останнього рядка
      if (threadIndex == threadCount - 1 || lastRowIndex == 0) {
        lastRowIndex = rowCount;
      }
      sortingThreads[threadIndex] = new SortingThreadCyclicBarrier(matrix, firstRowIndex, lastRowIndex, true, cyclicBarrier);
      sortingThreads[threadIndex].start();
      firstRowIndex = lastRowIndex;

      if (firstRowIndex == rowCount) {
        break;
      }
    }

    try {
      cyclicBarrier.await(); // await в основному потоці
    } catch (InterruptedException iex) {
      System.out.println(iex.getMessage());
    } catch (BrokenBarrierException bbex) {
      System.out.println(bbex.getMessage());
    }

    return matrix;
  }

  /**
   * Багатопоточне СОРТУВАННЯ стовпців матриці
   *
   * @param matrix  Матриця
   * @param threadCount  Кількість потоків
   * @return Результуюча матриця
   */
  private static double[][] sortMatrixColumnsMT(
    final double[][] matrix,
    int threadCount
  ) {
    assert threadCount > 0;

    final int rowCount = matrix.length; // Кількість рядків результуючої матриці
    final int colCount = matrix[0].length; // Кількість стовпців результуючої матриці

    final int colsForThread = colCount / threadCount; // Кількість обчислюваних стовпців на потік
    int firstColIndex = 0; // Індекс першого стовпця
    final SortingThreadCyclicBarrier[] sortingThreads = new SortingThreadCyclicBarrier[threadCount]; // Масив потоків

    // ЗАВДАННЯ! Створення CyclicBarrier
    final CyclicBarrier cyclicBarrier = new CyclicBarrier(
      threadCount + 1, // +1 тому що основний потік (main thread) теж враховується
      new Runnable() {
        @Override
        public void run() {
          System.out.println("All matrix sorting by columns have finished");
        }
      }
    );

    // Створення та запуск потоків
    for (int threadIndex = 0; threadIndex <= threadCount - 1; ++threadIndex) {
      int lastColIndex = firstColIndex + colsForThread; // Індекс останнього стовпця
      if (threadIndex == threadCount - 1 || lastColIndex == 0) {
        lastColIndex = colCount;
      }
      sortingThreads[threadIndex] = new SortingThreadCyclicBarrier(matrix, firstColIndex, lastColIndex, false, cyclicBarrier);
      sortingThreads[threadIndex].start();
      firstColIndex = lastColIndex;
      if (firstColIndex == colCount) {
        break;
      }
    }

    try {
      cyclicBarrier.await(); // await в основному потоці
    } catch (InterruptedException iex) {
      System.out.println(iex.getMessage());
    } catch (BrokenBarrierException bbex) {
      System.out.println(bbex.getMessage());
    }

    return matrix;
  }
}
