package demo1;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 生产者
 */
public class Producer implements Runnable {
    //生产容器
    private final Container<Customer> container;
    // 监听生产者线程
    private final Object producerMonitor;
    //监听消费者线程
    private final Object consumerMonitor;
    static final int produceSpeed = 300;
    
    public Producer(Object producerMonitor,Object consumerMonitor,Container<Customer> container){
        this.producerMonitor = producerMonitor;
        this.consumerMonitor = consumerMonitor;
        this.container = container;
    }
    
    @Override
    public void run() {
        while (true){
            produce();
        }
    }
    //取号机生产等待的客户，注意模拟前后两个客户之间的时间间隔
    public void produce(){
        try {
            if (container.isFull()) {
                synchronized (producerMonitor) {
                    System.out.println("大厅容量已满暂停发号");
                    producerMonitor.wait();
                }
            }
            synchronized (container) {
                if (!container.isFull()) {
                    container.add(new Customer());
                    System.out.println(container.getList().get(container.getSize()-1).toString() +
                            "号顾客正在等待服务");
                    if (container.getSize() == 1) {
                        synchronized (consumerMonitor) {
                            consumerMonitor.notify();
                        }
                    }
                }
            }
            Random rand = new Random();
            TimeUnit.MILLISECONDS.sleep(rand.nextInt(produceSpeed));
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}