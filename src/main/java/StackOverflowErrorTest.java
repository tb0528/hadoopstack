/**
 * 栈溢出测试
 * Created by xiaoxiaomo on 2014/7/6.
 */
public class StackOverflowErrorTest {

    public static void main(String[] args) {
        newObject(0) ;
    }

    public  static void newObject( int i ){
        i++ ;
        System.out.println(i);
        newObject(i) ;
    }
}
