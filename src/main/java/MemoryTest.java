import sun.org.mozilla.javascript.internal.ast.WhileLoop;

import java.util.ArrayList;
import java.util.List;

/**
 * 内存溢出测试
 * Created by xiaoxiaomo on 2014/7/6.
 */
public class MemoryTest {

    public static void main(String[] args) throws InterruptedException {
        List<MemoryTest> list = new ArrayList<MemoryTest>() ;
        for (int i = 0; 1==1 ; i++) {
            System.out.println( i );
            list.add( new MemoryTest() );
            Thread.sleep(1);
        }
    }




}
