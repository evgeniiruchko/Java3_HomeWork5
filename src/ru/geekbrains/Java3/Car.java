package ru.geekbrains.Java3;

public class Car implements Runnable {
    private static int CARS_COUNT;
    public static volatile boolean isFinished = false;

    static {
        CARS_COUNT = 0;
    }
    private Race race;
    private int speed;
    private String name;
    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }
    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }
    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов");
            /* Не знаю, насколько правильно объявлять CountDownLatch в Майне. и так к нему тут обращаться.
            Просьба прокомментировать этот момент.
            Пробовал его сделать в этом классе, но при этом не получилось сдрежать выполнение Майна.
             */
            Main.READY_CARS.countDown(); // в случае с CyclicBarrier эту строку убирал
            Main.READY_CARS.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }

        Main.FINISHED_CARS.countDown();
        if (!isFinished){
            isFinished = true;
            System.out.println(this.name + " - WIN");
        }
    }
}
