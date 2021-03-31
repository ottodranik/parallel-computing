package shared;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import shared.Matrix;

/** Потік-обчислювач ДОДАВАННЯ елементів матриці (з використанням CyclicBarrier) */
public class AddingThreadCyclicBarrier extends Thread {
  /** Результуюча матриця */
  private final double[][] resultMatrix;
  /** Перша (ліва) матриця */
  private final double[][] firstMatrix;
  /** Друга (права) матриця */
  private final double[][] secondMatrix;
  /** Початковий індекс */
  private final int firstIndex;
  /** Кінцевий індекс */
  private final int lastIndex;
  /** Барьєр для роботи з потоками */
  private final CyclicBarrier cyclicBarrier;

  /**
   * @param resultMatrix Результуюча матриця
   * @param firstMatrix  Перша матриця
   * @param secondMatrix Друга матриця
   * @param firstIndex   Початковий індекс (включно)
   * @param lastIndex    Кінцевий індекс (виключно)
   * @param cyclicBarrier    Барьєр для роботи з потоками
   */
  public AddingThreadCyclicBarrier(
    final double[][] resultMatrix,
    final double[][] firstMatrix,
    final double[][] secondMatrix,
    final int firstIndex,
    final int lastIndex,
    final CyclicBarrier cyclicBarrier
  ) {
    this.resultMatrix = resultMatrix;
    this.firstMatrix = firstMatrix;
    this.secondMatrix = secondMatrix;
    this.firstIndex = firstIndex;
    this.lastIndex = lastIndex;
    this.cyclicBarrier = cyclicBarrier;
  }

  /** Робоча функція потоку */
  @Override
  public void run() {
    System.out.println("Thread " + getName() + " started. Adding cells from " + firstIndex + " to " + lastIndex + "...");

    final int colCount = secondMatrix[0].length; // Число стовпців результуючої матриці
    for (int index = firstIndex; index < lastIndex; ++index) {
      int row = index / colCount;
      int col = index % colCount;
      resultMatrix[row][col] = Matrix.calcAddingValue(firstMatrix, secondMatrix, row, col);
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

