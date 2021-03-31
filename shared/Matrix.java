package shared;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Arrays;

public class Matrix {

  /**
   * Однопоточне сортування матриць
   *
   * @param matrix Вхідна матриця
   * @return Результуюча матриця
   */
  public static double[][] sortMatrix(
    final double[][] matrix
  ) {
    final int rowCount = matrix.length; // Кількість рядків результуючої матриці
    final int colCount = matrix[0].length; // Кількість стовпців результуючої матриці
    final double[][] result = new double[rowCount][colCount]; // Результуюча матриця
    
    for (int row = 0; row < rowCount; ++row) {
      result[row] = sortRowVector(matrix[row]); // За допомогою вбудованих JAVA-методів
    }

    for (int col = 0; col < colCount; ++col) {
      sortColVector(result, col);
    }

    return result;
  }

  /**
   * Сортування вектора-рядка
   * Сортування елементів рядка матриці
   * За допомогою вбудованих JAVA-методів
   *
   * @param rowVector Вхідний вектор-рядок
   * @return Результуючий вектор
   */
  public static double[] sortRowVector(
    final double[] rowVector
  ) {
    double[] destRowVector = new double[rowVector.length];
    System.arraycopy(rowVector, 0, destRowVector, 0, rowVector.length);
    Arrays.sort(destRowVector);
    return destRowVector;
  }

  /**
   * Сортування вектора-стовбця
   * Сортування елементів стовпця матриці
   * Сортування методом "бульбашки"
   *
   * @param matrix Вхідна матриця
   * @param col Індекс стовпця
   * @return Результуючий вектор
   */
  public static void sortColVector(
    final double[][] matrix,
    final int col
  ) {
    final int rowCount = matrix.length;

    for (int row_i = 0; row_i < rowCount - 1; ++row_i) {
      for (int row_j = rowCount - 1; row_j > row_i; --row_j) {
        if(matrix[row_j - 1][col] > matrix[row_j][col]) {
          double buf = matrix[row_j - 1][col];
          matrix[row_j - 1][col] = matrix[row_j][col];
          matrix[row_j][col] = buf;
        }
      }
    }
  }

  /**
   * Сортування матриці за елементами рядка
   * Сортуються елемента у кожному рядку
   *
   * @param matrix Вхідний вектор
   * @param firstRowIndex Початковий індекс рядка (включно)
   * @param lastRowIndex Кінцевий індекс рядка (виключно)
   * @return Результуючий вектор
   */
  public static void sortMatrixByRowElements(
    final double[][] matrix,
    final int firstRowIndex,
    final int lastRowIndex
  ) {
    for (int row = firstRowIndex; row < lastRowIndex; ++row) {
      matrix[row] = sortRowVector(matrix[row]); // Сортуємо кожен вектор
    }
  }

  /**
   * Сортування матриці за елементами стовпця
   * Сортуються елементи у кожного стовпці
   * Використовується простий метод "бульбашки"
   *
   * @param matrix Вхідна матриця
   * @param firstColIndex Початковий індекс стовпця (включно)
   * @param lastColIndex Кінцевий індекс стовпця (виключно)
   * @return Результуючий вектор
   */
  public static void sortMatrixByColumnElements(
    final double[][] matrix,
    final int firstColIndex,
    final int lastColIndex
  ) {
    for (int col = firstColIndex; col < lastColIndex; ++col) {
      sortColVector(matrix, col);
    }
  }

  /**
   * Однопоточне додавання матриць
   *
   * @param firstMatrix  Перша матриця
   * @param secondMatrix Друга матриця
   * @return Результуюча матриця
   */
  public static double[][] addMatrix(
    final double[][] firstMatrix,
    final double[][] secondMatrix
  ) {
    final int rowCount = firstMatrix.length; // Кількість рядків результуючої матриці
    final int colCount = secondMatrix[0].length; // Кількість стовпців результуючої матриці
    final double[][] result = new double[rowCount][colCount]; // Результуюча матриця
    
    for (int row = 0; row < rowCount; ++row) {
      for (int col = 0; col < colCount; ++col) {
        result[row][col] = calcAddingValue(firstMatrix, secondMatrix, row, col);
      }
    }

    return result;
  }

  /**
   * Однопоточне множення матриць
   *
   * @param firstMatrix  Перша матриця
   * @param secondMatrix Друга матриця
   * @return Результуюча матриця
   */
  public static double[][] multiplyMatrix(
    final double[][] firstMatrix,
    final double[][] secondMatrix
  ) {
    final int rowCount = firstMatrix.length; // Кількість рядків результуючої матриці
    final int colCount = secondMatrix[0].length; // Кількість стовпців результуючої матриці
    final int sumLength = secondMatrix.length; // Кількість членів суми при обчисленні значення комірки
    final double[][] result = new double[rowCount][colCount]; // Результуюча матриця

    for (int row = 0; row < rowCount; ++row) { // Цикл за рядками
      for (int col = 0; col < colCount; ++col) { // Цикл за стовпцями
        result[row][col] = calcMultiplyValue(firstMatrix, secondMatrix, row, col, sumLength);
      }
    }

    return result;
  }

  /**
   * Обчислення ДОДАВАННЯ однієї комірки результуючої матриці
   *
   * @param firstMatrix Перша матриця
   * @param secondMatrix Друга матриця
   * @param row Номер рядку
   * @param col Номер стовпця
   */
  public static double calcAddingValue(
    final double[][] firstMatrix,
    final double[][] secondMatrix,
    final int row,
    final int col
  ) {
    return firstMatrix[row][col] + secondMatrix[row][col];
  }

  /**
   * Обчислення ДОБУТКУ однієї комірки результуючої матриці
   *
   * @param firstMatrix Перша матриця
   * @param secondMatrix Друга матриця
   * @param row Номер рядку
   * @param col Номер стовпця
   * @param sumLength Кількість членів суми при обчисленні значення комірки
   */
  public static double calcMultiplyValue(
    final double[][] firstMatrix,
    final double[][] secondMatrix,
    final int row,
    final int col,
    final int sumLength
  ) {
    double res = 0;
    for (int i = 0; i < sumLength; ++i) {
      res += firstMatrix[row][i] * secondMatrix[i][col];
    }
    return res;
  }

  /**
   * Заповнення матриці випадковими числами
   *
   * @param matrix Матриця для заповнення
   */
  public static void randomMatrix(final double[][] matrix) {
    final Random random = new Random(); // Генератор випадкових чисел
    for (int row = 0; row < matrix.length; ++row) {
      for (int col = 0; col < matrix[row].length; ++col) {
        Integer multiplier = random.nextInt(1000); // Випадкове число від 0 до 1000 (множник)
        matrix[row][col] = random.nextDouble() * multiplier;// Випадкове число від 0.0 до 1.0
      }
    }
  }

  /**
   * Порівняння двох результуючих матриць
   *
   * @param firstMatrix
   * @param secondMatrix
   */
  public static boolean compareMatrix(final double[][] firstMatrix, final double[][] secondMatrix) {
    final double epsilon = 0.0001;
    final int rowCount = firstMatrix.length;
    final int colCount = firstMatrix[0].length;
    
    for (int row = 0; row < rowCount; ++row) {
      for (int col = 0; col < colCount; ++col) {
        if (Math.abs(secondMatrix[row][col] - firstMatrix[row][col]) > epsilon) {
          System.out.println("Error in multithread calculation!");
          System.out.println(secondMatrix[row][col]);
          System.out.println(firstMatrix[row][col]);
          return false;
        }
      }
    }

    return true;
  }

  /**
   * Вивід матриці в файл. Відбувається вирівнювання значень
   *
   * @param fileWriter Об'єкт-файл для запису
   * @param matrix     Матриця, що виводиться в файл
   * @throws IOException
   */
  public static void printMatrix(
    final FileWriter fileWriter,
    final double[][] matrix,
    final int numberOfRealPart
  ) throws IOException {
    boolean hasNegative = false; // Ознака того, що в матриці є від'ємні числа
    double maxValue = 0; // Максимальне за модулем число

    // Обчислюємо максимальне за модулем число в матриці
    // і перевіряємо на наявність від'ємних чисел
    for (final double[] row : matrix) { // Цикл за рядками
      for (final double element : row) { // Цикл за стовпцями
        double temp = element;
        if (element < 0) {
          hasNegative = true;
          temp = -temp;
        }
        if (temp > maxValue) {
          maxValue = temp;
        }
      }
    }

    // Обчислення довжини позиції під число (міряємо тільки основну частину)
    int len = Integer.toString((int) maxValue).length() + 1 + 5; // 1 - місце під роздільник (пробіл)
                                                                 // 5 - місце під мантису числа
    if (hasNegative) {
      ++len; // Додаємо місце під мінус, якщо є від'ємні числа
    }

    // Форматуємо рядок матриці
    final String formatString = "%" + (numberOfRealPart > 0 ? numberOfRealPart : len) + ".4f";

    // Вивід элементів матриці у файл
    for (final double[] row : matrix) {
      for (final double element : row) {
        fileWriter.write(String.format(formatString, element));
      }
      fileWriter.write("\n"); // Перенос рядка матриці
    }
  }

  /**
   * Вивід результуючих матриць у файл.
   * Файл буде перезаписаний
   *
   * @param fileName Им'я файла для виводу
   * @param resultSingleThread  Перша матриця
   * @param resultMultiThread Друга матриця
   */
  public static void printResultMatrix(
    final String fileName,
    final double[][] resultSingleThread,
    final double[][] resultMultiThread
  ) {
    System.out.println("Printing in file started...");
    try (final FileWriter fileWriter = new FileWriter(fileName, false)) {
      fileWriter.write("Result single thread matrix:\n");
      printMatrix(fileWriter, resultSingleThread, 20);

      fileWriter.write("\nResult multi thread matrix:\n");
      printMatrix(fileWriter, resultMultiThread, 20);

    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("Printing in file completed.");
  }
}