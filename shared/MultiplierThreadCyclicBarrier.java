package shared;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import shared.Matrix;

/** Поток-обчислювач ДОБУТКУ елементів матриці (з використанням CyclicBarrier) */
public class MultiplierThreadCyclicBarrier extends Thread {
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
  /** Кількість члені суми при обчисленні значення комірки */
  private final int sumLength;
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
  public MultiplierThreadCyclicBarrier(
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

    sumLength = secondMatrix.length;
  }

  /** Робоча функція потоку */
  @Override
  public void run() {
    System.out.println("Thread " + getName() + " started. Multiply cells from " + firstIndex + " to " + lastIndex + "...");

    final int colCount = secondMatrix[0].length; // Число стовпців результуючої матриці
    for (int index = firstIndex; index < lastIndex; ++index) {
      int row = index / colCount;
      int col = index % colCount;
      resultMatrix[row][col] = Matrix.calcMultiplyValue(firstMatrix, secondMatrix, row, col, sumLength);
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