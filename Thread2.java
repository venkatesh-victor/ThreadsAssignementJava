import java.util.ArrayList;
import java.util.List;

class Producer extends Thread {
    private List<Integer> buffer;
    private int size;

    public Producer(List<Integer> buffer, int size) {
        this.buffer = buffer;
        this.size = size;
    }

    @Override
    public void run() {
        for(int i = 1; i < 10; i++) {
            produce(i);
        }
    }

    private void produce(int item) {
        synchronized(buffer) {
            while(buffer.size() == size) {
                try {
                    System.out.println("Buffer is full. Producer is waiting.");
                    buffer.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } 

            buffer.add(item);
            System.out.println("Produced: " + item);
            buffer.notifyAll();
        }
    }
}

class Consumer extends Thread {
    private List<Integer> buffer;

    public Consumer(List<Integer> buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while(true) {
            consume();
        }
    }

    private void consume() {
        synchronized (buffer) {
            while(buffer.isEmpty()) {
                try {
                    System.out.println("Buffer is empty. Consumer is waiting.");
                    buffer.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            int item = buffer.remove(0);
            System.out.println("Consumed: " + item);
            buffer.notifyAll();
        }
    }
}


public class Thread2 {
    public static void main(String[] args) {
        List<Integer> buffer = new ArrayList<>();
        int size = 5;

        Producer producer = new Producer(buffer, size);
        Consumer consumer = new Consumer(buffer);
        producer.start();
        consumer.start();
    }
}
