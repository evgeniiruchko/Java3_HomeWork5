package ru.geekbrains.Java3;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Main {
    public static final int CARS_COUNT = 4;
    /*
    Не смог объяснить, поэтому оставил в комментарии
    Почему с CyclicBarrier всё работает когда я передаю количество участников + 1?
    а если просто количество участников, то он последнего не дожидается и продолжает выполнение.
    При этом main не завершаясь висит после того как все потоки выполнились.
    Может из-за того что await() вызываю в двух местах (в майне и в Cаr)?

    С CountDownLatch всё отрабатывает как надо.
     */
    //public static final CyclicBarrier READY_CARS = new CyclicBarrier(CARS_COUNT + 1);
    public static final CountDownLatch READY_CARS = new CountDownLatch(CARS_COUNT);
    public static final CountDownLatch FINISHED_CARS = new CountDownLatch(CARS_COUNT);
    public static Semaphore tunnelSemaphore = new Semaphore(CARS_COUNT / 2);


    public static void main(String[] args) {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(tunnelSemaphore), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10));
        }
        for (int i = 0; i < cars.length; i++) {
            new Thread(cars[i]).start();
        }
        try {
            READY_CARS.await();
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");

            FINISHED_CARS.await();
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } /*catch (BrokenBarrierException e) {
            e.printStackTrace();
        }*/
    }
}




