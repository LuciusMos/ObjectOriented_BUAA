package demo1;

import java.util.Random;
import java.util.concurrent.TimeUnit;
// 消费者
public class Consumer implements Runnable {
    private int windowId;
    //模拟生产容器
    private final Container<Customer> container;
    //监听生产者线程
    private final Object producerMonitor;
    //监听消费者线程
    private final Object consumerMonitor;
    static final int consumeSpeed = 10000;
    
    public Consumer( Object producerMonitor,Object consumerMonitor,Container<Customer> container,int id ) {
        this.producerMonitor = producerMonitor;
        this.consumerMonitor = consumerMonitor;
        this.container = container;
        this.windowId = id;
    }
    
    @Override
    public void run(){
        while (true){
            consume();
        }
    }
    
    //消费
    public void consume(){
        try {
            if (container.isEmpty()) {
                synchronized (consumerMonitor) {
                    consumerMonitor.wait();
                }
            }
            synchronized (container) {
                if (!container.isEmpty()) {
                    String window;
                    if (windowId > 9) {
                        window = String.valueOf(windowId);
                    } else {
                        window = "0" + windowId;
                    }
                    System.out.println(container.get().toString() +
                            "号顾客请到" + window + "号窗口");
                    if (container.getSize() == container.getCapacity() - 1) {
                        synchronized (producerMonitor) {
                            producerMonitor.notify();
                        }
                    }
                }
            }
            Random rand = new Random();
            TimeUnit.MILLISECONDS.sleep(rand.nextInt(consumeSpeed));
        } catch(InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}