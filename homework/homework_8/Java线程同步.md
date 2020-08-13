## Java线程和并行编程

**多线程API是Java的一个精华。它对操作系统的多线程机制进行了良好的封装，提供了许多有用的接口。在使用多线程时经常出现多个线程访问共享对象产生的线程安全问题，今天就来详细说一下**。

**包括：线程、同步方法、同步语句和监听器、锁的概念和使用、原子操作、线程安全容器、并行编程七个主题**。（线程调度机制中，还有一个自动化工具，叫线程池，本文不涉及）

好多初学者认为多线程是为了提高时间效率。我给大家澄清一下，多数情况多线程确实能加快速度，但是，**有些情况反而会更慢**。比如我们创建100个线程并行排序，但是CPU是四核八线程的，也就是说**最多只有8个线程是真正并行的**，其他92个线程和这8个线程的关系只能叫**并发**而不是并行，并且需要占用CPU资源反复进行线程调度，反而损失了效率。另外，**多线程不一定是为了提高效率**，有时是为了实现一些抽象模型，比如中断响应式监听器，如果用单线程，几乎是不可实现的。GUI编程、以及Android开发中的事件驱动机制，多数是基于多线程机制的。

### 一、任务和线程的创建

任务是指实现`Runnable`接口的类。`Runnable`接口有一个抽象方法`public void run()`，实现这个方法的时候，把你需要在这个任务中执行的代码写进去即可。

`Thread`类是`Runnable`接口的一个实现类，它提供了`start`方法，用于启动一个线程。还提供了一个构造方法`public Thread(Runnable task)`，用于把任务task装填到线程中。

创建一个线程来执行一个任务，可以实现`Runnable`接口，然后调用`Thread`的构造方法创建线程；也可以直接`extends Thread`，重写`run()`方法来创建线程。如下：

```java
/*
 * 创建一个任务，实现Runnable接口
 */
class MyTask implements Runnable {
    @Override
    public void run() {
        System.out.println("Hi, I'm a task!");
    }
}

/*
 * 扩展Thread，自定义线程
 */
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Hi, I'm a thread!");
    }
}

public class Main {
    public static void main(String[] args) {
        MyTask myTask = new MyTask();
        Thread thread1 = new Thread(myTask); // Thread类的构造函数: public Thread(Runnable)
        Thread thread2 = new MyThread();
        thread1.start();
        thread2.start();
    }
}
```

### 二、线程竞争和同步（synchronized）方法

如果两个线程使用同一个对象（这里同一个对象的理解：**如果是基本类型**，则理解为对同一个变量的操作；**如果是类对象**，则应该理解为两个线程的对象引用指向同一个对象实例），会有什么情况？

如果同时读，或者一读一写，还不会引发什么错误。但如果**两个线程同时写**，则会出现不可预知的错误。这就是线程安全性问题。具体说，两个线程同时对一个对象的修改，会被操作系统的线程调度机制打断，从而在某些时刻的状态不正确。

比如一个银行账户类，初始有余额20万美元（~~真希望这是我的账户，滑稽~~~）：

```java
public class Account {
    private int amount = 200000;
    private String accountID;
    
    public Account(String str) {
        accountID = str;
    }
    
    public int getAmount() {
        return amount;
    }
    
    // 取款方法
    public boolean withdraw(int money) {
        if (amount < money) {
            System.err.printf("%s: Attempt to withdraw $%d, but left only $%d!\n",
                              accountID, money, amount);
            return false;
        } else {
            amount -= money;
            try {
                Thread.sleep(300L); // 睡眠300毫秒，是为了增加发生错误的可能
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("%s: Withdrew $%d successfully! Now, left $%d.\n",
                              accountID, money, amount);
            return true;
        }
    }
    
    // 存款方法
    public void charge(int money) {
        amount += money;
        System.out.printf("%s: Charged $%d successfully! Now, left $%d.\n",
                          accountID, money, amount);
    }
}
```

我们定义一个线程，用来取款，取款额每次用随机数产生，最大为20000美元。

```java
public class Withdraw extends Thread {
    private Account account;
    
    public Withdraw(Account a) {
        account = a;
    }
    
    @Override
    public void run() {
        Random random = new Random();
        while (account.withdraw(random.nextInt()));
    }
}
```

main方法中，创建两个取款线程，分别对同一个账户提款。

```java
public class Main {
    public static void main(String[] args) {
        Account account = new Account("Account1");
        Withdraw task1 = new Withdraw(account);
        Withdraw task2 = new Withdraw(account);
        task1.start();
        task2.start();
    }
}
```

不出所料，发生严重错误。task1报告的余额，和task2同时反馈出来的余额不相等。并且，余额并不是按时间递减的，而是杂乱无章的减到最小。

我们在withdraw方法增加了线程睡眠，在一个线程中，这方法没有结束的时候，另一个线程就又调用了相应的取款方法。等到睡眠结束的时候，就输出了错误的余额。这就是线程竞争，安全性出了问题。

为了解决这个问题，我们把withdraw方法加上一个修饰关键字：`synchronized`。这个关键字修饰的方法，叫做同步方法。它的含义就是，**当两个线程调用同一个对象的这个方法时，只有一个线程能够调用成功，其他线程必须停下来等待，直到当前线程完成这一函数**。

withdraw的方法头改成`public synchronized boolean withdraw(int money)`。这次，运行就正确了。

### 三、同步语句、wait()和notify()

上面讲了同步方法，解决了两个线程同时取款的竞争。现在，新需求就是，当余额不足的时候，取款线程停下来等待存款线程。存款线程每次存款10万美元（~~感觉是在洗钱，哈哈哈~~），并且只有当取款线程余额不足停下来的时候，才可以存款。

我们先说同步语句的使用，然后逐步完成这一需求。

刚才说了synchronized关键字，它可以用来修饰函数。其实，它还可以修饰代码块，这样的代码块叫做同步语句。格式如下：

```java
synchronized (sharedObject) {
    // your code
}
```

这个sharedObject叫做共享对象。为什么这样叫呢？是因为，它经常是多个线程共同使用的某个对象。这不是废话吗？那好，既然说是废话，我们来看一看，同步方法可以如何替换成等价的同步语句。

上面这个同步语句的执行，需要用到锁的概念。当执行到同步语句时，首先获取共享对象sharedObject的锁，也就是告诉操作系统：我在运行，其它的线程统统蹲下抱头！

比如一个简单的

```java
class Ccc {
    private static final Object lock = new Object();
    private final String str;
    
    public Ccc(String str) {
        this.str = str;
    }
    
    public void output() {
        synchronized (lock) {
            System.out.println(str);
        }
    }
}
```

首先定义了一个公用锁lock，它作共享对象。为什么是共享对象？就在于那个static，它被这个类的所有对象共享。输出的话，当然一次只能有一个线程输出，否则就输出穿插了，乱套了。

现在我提示大家，存取款线程的共享对象就是这个账户。大家思考，如何改造取款线程和构造存款线程。

再来看`wait()`和`notify()`。这两个函数是Object类中的，也就是说一切类对象都可以调用它。但是，使用不当会导致一个异常。具体来说，必须是在同步语句或同步方法中，调用相应共享对象的这两个方法，才可以保证不报异常。

实现某些条件下的互斥，可以让需要等待的线程不断询问条件是否满足。比如刚才的取款线程，可以不断询问余额是否大于等于需要的取款额（也就是取款是否成功）。这叫做轮询。

```java
while (true) {
    boolean flag;
    do {
        synchronized (account) {
            flag = account.withdraw(money);
        }
    } while (!flag); // 轮询
    try {
        Thread.sleep(1000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
```

这浪费了宝贵的CPU时间。现在，存取款线程有共享对象account，它们共享一个账户。当余额不足的时候，取款线程释放掉CPU资源，暂时停下来（称为线程等待）。每当存款线程存进一笔，就通知取款线程resume并重新尝试。这种互斥等待的方法，释放出CPU资源，节省了CPU时间，提高了效率。

所以，可以改成

```java
// 取款线程中
while (true) {
    synchronized (account) {
        while (account.getAmount < money) {
            account.wait(); // 可以全部写在同步方法中，因为wait方法会释放锁
        }
        account.withdraw(money);
    }
    try {
        Thread.sleep(1000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}

// 存款线程中
while (true) {
    synchronized (account) {
        account.charge(100000);
        account.notiyAll();
    }
    try {
        Thread.sleep(3000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
```

为什么不用`if`而用`while`呢？是因为，如果notify方法通知到取款线程，而余额仍然不足，则继续等待。

### 4、lock和condition

synchronized提供了一种物理锁。刚才说到同步语句，把共享对象作为同步语句的参数。现在，我们看一下，一个类中，同步方法和同步语句的关系。

```java
class Ijk {
    private int aaa = 5;
    
    // 下面这两个方法是等价的
    public synchronized void setAaa(int x) {
        aaa = x;
    }
    
    public void setAaa2(int x) {
        synchronized (this) {
            aaa = x;
        }
    }
}
```

就这么简单，对不对？

我们还说了，synchronized的实现原理是把用这关键字修饰的代码块加上锁，保证同一时刻只有一个线程执行这段代码。锁是个关键。

现在，我们来介绍两个强大工具：`Reentrantlock`和`Condition`。

Reentrantlock称为“可重入锁”，是Lock接口的实现类。也就是，它可以多次上锁，用来实现信号量机制。Condition翻译为条件，但它不是真正的“条件”，而是锁的辅助工具，用来进行等待和唤醒的。我更习惯叫它监听器。本来它应该叫Listener的，但是这个标识符被别的东西占有（事件机制，大家可以了解一下），为了避免冲突，就叫了个不伦不类的condition。

所以，我们刚才使用同步语句来实现的互斥访问，现在可以用lock和condition来实现了。

lock的用法，就是构造函数创建一把锁。然后condition的用法，是在lock上注册一个监听器。

```java
ReentrantLock lock = new ReentrantLock();
Condition condition = lock.newCondition(); // 注意，必须在锁上注册，不能直接调用构造方法
```

Lock有一个lock和一个unlock方法，用来加锁和释放锁。在这两条语句之间的代码，就等价于在synchronized中。
