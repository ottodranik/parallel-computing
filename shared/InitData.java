package shared;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import shared.Matrix;

public class InitData {

  final static int NUMBER_OF_ROWS = 1500;
  final static int NUMBER_OF_COLS = 1500;

  /**
   * Генерація нових даних
   *
   * @param filePath  Шлях до файла
   */
  public static void initInputData(String filePath) {
    final double[][] MO_Matrix = new double[NUMBER_OF_ROWS][NUMBER_OF_COLS]; // MO матриця
    final double[][] MP_Matrix = new double[NUMBER_OF_ROWS][NUMBER_OF_COLS]; // MP матриця
    final double[][] R_Matrix = new double[NUMBER_OF_ROWS][NUMBER_OF_COLS];  // R матриця
    final double[][] S_Matrix = new double[NUMBER_OF_ROWS][NUMBER_OF_COLS];  // S матриця

    Matrix.randomMatrix(MO_Matrix);
    Matrix.randomMatrix(MP_Matrix);
    Matrix.randomMatrix(R_Matrix);
    Matrix.randomMatrix(S_Matrix);

    try (final FileWriter fileWriter = new FileWriter(filePath, false)) {
      fileWriter.write("MO_Matrix:\n");
      Matrix.printMatrix(fileWriter, MO_Matrix, 0);

      fileWriter.write("MP_Matrix:\n");
      Matrix.printMatrix(fileWriter, MP_Matrix, 0);

      fileWriter.write("R_Matrix:\n");
      Matrix.printMatrix(fileWriter, R_Matrix, 0);

      fileWriter.write("S_Matrix:\n");
      Matrix.printMatrix(fileWriter, S_Matrix, 0);

    } catch (IOException e) {
      e.printStackTrace();
    }    
  }

  /**
   * Зчитування з файлу
   *
   * @param filePath  Шлях до файла
   * @return  Лист з матрицями
   */
  public static List<double[][]> readInputData(String filePath) {
    List<double[][]> list = new ArrayList<double[][]>();

    try {
      File file = new File(filePath);
      // Створюємо об'єкт FileReader
      FileReader fr = new FileReader(file);
      // Створюємо BufferedReader з FileReader для читання з файлу
      BufferedReader reader = new BufferedReader(fr);

      String[] ids = { "MO_Matrix", "MP_Matrix", "R_Matrix", "S_Matrix" };

      int currentListIndex = -1;
      int currentArrIndex = -1;
      double[][] currentArr = new double[NUMBER_OF_ROWS][NUMBER_OF_COLS];

      // Читаємо перший рядок з файлу
      String line = reader.readLine();
      while (line != null) {
        // Якщо хоча б один з заголовків присутній в рядку,
        // то створюємо нову матрицю даних в листі
        // та готуємо цю матрицю до запису даних (робимо її поточною)
        if (Arrays.stream(ids).anyMatch(line::contains)) {
          ++currentListIndex;
          currentArrIndex = 0;
          list.add(new double[NUMBER_OF_ROWS][NUMBER_OF_COLS]);
          currentArr = list.get(currentListIndex);
        } else {
          // Якщо не має заголовків, то просто зчитуємо рядок,
          // перетворюємо його на масив строк, потів на масив double
          // та записуємо у поточну матрицю
          line = line.trim().replaceAll(" +", " ");
          String[] stringArr = line.split(" ", -1);
          double[] doubleValues = Arrays.stream(stringArr).mapToDouble(Double::parseDouble).toArray();
          currentArr[currentArrIndex] = doubleValues;
          ++currentArrIndex;
        }
        // зчитуємо наступний рядок
        line = reader.readLine();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return list;
  }
}