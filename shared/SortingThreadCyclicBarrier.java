package shared;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import shared.Matrix;

/** Потік-обчислювач СОРТУВАННЯ елементів матриці (з використанням CyclicBarrier) */
public class SortingThreadCyclicBarrier extends Thread {
  /** Вхідна та результуюча матриця */
  private final double[][] inOutMatrix;
  /** Початковий індекс рядка/стовпця */
  private final int firstIndex;
  /** Кінцевий індекс рядка/стовпця */
  private final int lastIndex;
  /** Ознака вказує на сортування за рядками або за стовпцями */
  private final boolean byRows;
  /** Барьєр для роботи з потоками */
  private final CyclicBarrier cyclicBarrier;

  /**
   * @param inOutMatrix  Вхідна та результуюча матриця
   * @param firstIndex   Початковий індекс рядка (включно)
   * @param lastIndex    Кінцевий індекс рядка (виключно)
   * @param byRows       Ознака вказує на сортування за рядками або за стовпцями
   * @param cyclicBarrier    Барьєр для роботи з потоками
   */
  public SortingThreadCyclicBarrier(
    final double[][] inOutMatrix,
    final int firstIndex,
    final int lastIndex,
    final boolean byRows,
    final CyclicBarrier cyclicBarrier
  ) {
    this.inOutMatrix = inOutMatrix;
    this.firstIndex = firstIndex;
    this.lastIndex = lastIndex;
    this.byRows = byRows;
    this.cyclicBarrier = cyclicBarrier;
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

    // ЗАВДАННЯ! Кожен потік викликає await, чим зменшує лічильник потоків
    try {
      cyclicBarrier.await();
    } catch (InterruptedException iex) {
      System.out.println(iex.getMessage());
    } catch (BrokenBarrierException bbex) {
      System.out.println(bbex.getMessage());
    }
  }
}

