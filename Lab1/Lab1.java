package Lab1;

import java.util.List;

import shared.Matrix;
import shared.InitData;
import shared.MultiplierThread;
import shared.AddingThread;
import shared.SortingThread;

class Lab1 {

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
    // Matrix.printResultMatrix("Results_Lab1.txt", resultMatrix, resultMatrixMT);
  }

  /**
   * Багатопоточне МНОЖЕННЯ матриць
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
    final MultiplierThread[] multiplierThreads = new MultiplierThread[threadCount]; // Масив потоків

    // Створення та запуск потоків
    for (int threadIndex = threadCount - 1; threadIndex >= 0; --threadIndex) {
      int lastIndex = firstIndex + cellsForThread; // Індекс останньої комірки
      if (threadIndex == 0) {
        /* Один з потоків повинен обчислювати остачу,
         * якщо кількість комірок не кратне кількості потоків
         */
        lastIndex = rowCount * colCount;
      }
      multiplierThreads[threadIndex] = new MultiplierThread(result, firstMatrix, secondMatrix, firstIndex, lastIndex);

      // ЗАВДАННЯ! Запускаємо потоки
      multiplierThreads[threadIndex].start();
      firstIndex = lastIndex;
    }

    // ЗАВДАННЯ! Чекаємо на завершення потоків
    try {
      for (final MultiplierThread multiplierThread : multiplierThreads) {
        multiplierThread.join();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
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
    final AddingThread[] addingThreads = new AddingThread[threadCount]; // Масив потоків

    // Створення та запуск потоків
    for (int threadIndex = threadCount - 1; threadIndex >= 0; --threadIndex) {
      int lastIndex = firstIndex + cellsForThread; // Індекс останньої комірки
      if (threadIndex == 0) {
        /* Один з потоків повинен обчислювати остачу,
         * якщо кількість комірок не кратне кількості потоків
         */
        lastIndex = rowCount * colCount;
      }
      addingThreads[threadIndex] = new AddingThread(result, firstMatrix, secondMatrix, firstIndex, lastIndex);

      // ЗАВДАННЯ! Запускаємо потоки
      addingThreads[threadIndex].start();
      firstIndex = lastIndex;
    }

    // ЗАВДАННЯ! Чекаємо на завершення потоків
    try {
      for (final AddingThread addingThread : addingThreads) {
        addingThread.join();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
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
    final SortingThread[] sortingThreads = new SortingThread[threadCount]; // Масив потоків

    // Створення та запуск потоків
    for (int threadIndex = 0; threadIndex <= threadCount - 1; ++threadIndex) {
      int lastRowIndex = firstRowIndex + rowsForThread; // Індекс останнього рядка
      if (threadIndex == threadCount - 1 || lastRowIndex == 0) {
        /* Один з потоків повинен обчислювати остачу,
         * якщо кількість рядків не кратне кількості потоків
         */
        lastRowIndex = rowCount;
      }
      sortingThreads[threadIndex] = new SortingThread(matrix, firstRowIndex, lastRowIndex, true);

      // ЗАВДАННЯ! Запускаємо потоки
      sortingThreads[threadIndex].start();
      firstRowIndex = lastRowIndex;

      /* Якщо кількість рядків менша за кількість можливих потоків,
       * то зупиняємо створення потоків
       */
      if (firstRowIndex == rowCount) {
        break;
      }
    }

    // ЗАВДАННЯ! Чекаємо на завершення потоків
    try {
      for (final SortingThread sortingThread : sortingThreads) {
        sortingThread.join();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
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
    final SortingThread[] sortingThreads = new SortingThread[threadCount]; // Масив потоків

    // Створення та запуск потоків
    for (int threadIndex = 0; threadIndex <= threadCount - 1; ++threadIndex) {
      int lastColIndex = firstColIndex + colsForThread; // Індекс останнього стовпця
      if (threadIndex == threadCount - 1 || lastColIndex == 0) {
        /* Один з потоків повинен обчислювати остачу,
         * якщо кількість стовпців не кратне кількості потоків
         */
        lastColIndex = colCount;
      }
      sortingThreads[threadIndex] = new SortingThread(matrix, firstColIndex, lastColIndex, false);

      // ЗАВДАННЯ! Запускаємо потоки
      sortingThreads[threadIndex].start();
      firstColIndex = lastColIndex;

      /* Якщо кількість стовпців менша за кількість можливих потоків,
       * то зупиняємо створення потоків
       */
      if (firstColIndex == colCount) {
        break;
      }
    }

    // ЗАВДАННЯ! Чекаємо на завершення потоків
    try {
      for (final SortingThread sortingThread : sortingThreads) {
        if (sortingThread != null) {
          sortingThread.join();
        }
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    return matrix;
  }

}
