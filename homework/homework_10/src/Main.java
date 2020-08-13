import com.oocourse.specs2.AppRunner;

public class Main {
    public static void main(String[] args) throws Exception {
        //long start = System.currentTimeMillis();
        AppRunner runner = AppRunner.newInstance(
                MyPath.class, MyGraph.class);
        runner.run(args);
        //long end = System.currentTimeMillis();
        //System.out.println(end - start);
    }
}
