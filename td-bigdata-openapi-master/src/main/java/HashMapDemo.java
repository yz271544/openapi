
import bean.Student;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by John on 2016/3/30.
 */
public class HashMapDemo {
    /*
    *将学生对象和学生的归属地通过键与值存储到map集合中。
     */
    public static void main(String[] args) {
        Map<Student,String> hm = new HashMap<Student,String>();


        hm.put(new Student("lisi",38),"北京");
        hm.put(new Student("zhaoliu",24),"上海");
        hm.put(new Student("xiaoqiang",31),"沈阳");
        hm.put(new Student("wangcai",28),"大连");
        hm.put(new Student("zhaoliu",24),"铁岭");

        Set<Student> keySet = hm.keySet();
        Iterator<Student> it = keySet.iterator();

        while(it.hasNext()){
            Student key = it.next();
            String value = hm.get(key);
            System.out.println(key.getName()+":"+ key.getAge()+"---"+value);
        }
    }
}
