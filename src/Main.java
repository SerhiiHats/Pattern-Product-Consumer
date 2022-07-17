import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Block block = new Block(2);
        Thread productThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    block.product();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread consumerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    block.consumer();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        productThread.start();
        consumerThread.start();
        productThread.join();
        consumerThread.join();

    }

    static class Block{
        Queue<Integer> queue;
        int size;

        public Block(int size) {
            this.queue = new LinkedList<>();
            this.size = size;
        }

        public void product() throws InterruptedException {
            int value = 1;
            while (true){
                synchronized (this){
                    while (queue.size() >= size){
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    queue.add(value);
                    System.out.println(Thread.currentThread().getName() + " Произвели продукт с value: " + value + " ".repeat(Math.max(0,(40 - ("Произвели продукт с value: " + value).length()))) + "+1 на склад: остаток: " + queue.size());
                    notify();
                    value++;
                    Thread.sleep(850L);
                }
            }
        }

        public void consumer() throws InterruptedException {
            while (true){
                synchronized (this){
                    while (queue.size() == 0){
                        wait();
                    }
                    int value = queue.poll();
                    System.out.println(Thread.currentThread().getName() + " Потребили продукт с ID: " + value + " ".repeat(Math.max(0, (40 - ("Потребили продукт с ID: " + value).length()))) + "-1 c склада: остаток: " + queue.size());
                    notify();
                    Thread.sleep(850L);
                }
            }
        }

    }
}
