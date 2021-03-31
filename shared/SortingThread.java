package shared;

import shared.Matrix;

/** Потік-обчислювач СОРТУВАННЯ елементів матриці */
public class SortingThread extends Thread {
  /** Вхідна та результуюча матриця */
  private final double[][] inOutMatrix;
  /** Початковий індекс рядка/стовпця */
  private final int firstIndex;
  /** Кінцевий індекс рядка/стовпця */
  private final int lastIndex;
  /** Ознака вказує на сортування за рядками або за стовпцями */
  private final boolean byRows;

  /**
   * @param inOutMatrix  Вхідна та результуюча матриця
   * @param firstIndex   Початковий індекс рядка (включно)
   * @param lastIndex    Кінцевий індекс рядка (виключно)
   * @param byRows       Ознака вказує на сортування за рядками або за стовпцями
   */
  public SortingThread(
    final double[][] inOutMatrix,
    final int firstIndex,
    final int lastIndex,
    final boolean byRows
  ) {
    this.inOutMatrix = inOutMatrix;
    this.firstIndex = firstIndex;
    this.lastIndex = lastIndex;
    this.byRows = byRows;
  }

  /** Робоча функція потоку */
  @Override
  public void run() {
    if (byRows) {
      System.out.println("Thread " + getName() + " started. Sorting rows from " + firstIndex + " to " + lastIndex + "...");
      Matrix.sortMatrixByRowElements(inOutMatrix, firstIndex, lastIndex);
    } else {
      System.out.println("Thread " + getName() + " started. Sorting cols from " + firstIndex + " to " + lastIndex + "...");
      Matrix.sortMatrixByColumnElements(inOutMatrix, firstIndex, lastIndex); // За допомогою простого сортування "бульбашкою"
    }

    System.out.println("Thread " + getName() + " finished.");
  }
}

