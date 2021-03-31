# Завдання

ВАРІАНТ №7

### Lab1
Лабораторна робота №1 – Java + join().
Результат заноситься до файлу: Results_Lab1.txt

### Lab2
Лабораторна робота №2 – Java + CyclicBarrier.
Результат заноситься до файлу: Results_Lab2.txt

### Lab3
Лабораторна робота №3 – Java + Executor.
Результат заноситься до файлу: Results_Lab3.txt

### Shared

Щоб зібрати: `./makefile_[назва-лр].sh`

Щоб запустити: `./execfile_[назва-лр].sh`

`shared/Matrix.java` - API для роботи с матрицями:
- наповнення матриці рандомними даними
- дії над матрицями та її частинами
- порівняння матриць
- вивід матриці у файл

`shared/InitData.java` - робота з даними
- генерування первісних данних та збереження їх у файл
- зчитування даних з файлу

`shared/MultiplierThread.java` та `shared/MultiplierThreadCyclicBarrier.java`
- клас типу Thread для багатопотокового множення матриць
- та аналогічний файл з CyclicBarrier реалізацією

`shared/AddingThread.java` та `shared/AddingThreadСyclicBarrier.java`
- клас типу Thread для багатопотокового додавання матриць
- та аналогічний файл з CyclicBarrier реалізацією

`shared/SortingThread.java` та `shared/SortingThreadCyclicBarrier.java`
- клас типу Thread для багатопотокового сортування матриць
- та аналогічний файл з CyclicBarrier реалізацією
- завдяки прапору `byRows` клас використовується як для сортування елементів рядків матриці, так і для сортування елементів стовпців

### Додатково

Щоб зібрати: `./makefile_[назва-лр].sh`

Щоб запустити: `./execfile_[назва-лр].sh`