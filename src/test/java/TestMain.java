import com.motcs.build.enums.DataEnu;
import com.motcs.build.mvc.DataTime;
import com.motcs.build.mvc.MapClassValue;

import java.util.Map;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2024-05-17 星期五
 */
public class TestMain {


    public static void main(String[] args) {
        TestClass testClass = MapClassValue.mapToClass(TestClass.class, Map.of("v1", 1, "v2", "v2"));
        System.out.println("MapClassValue.mapToClass:" + testClass);

        Map<String, String> stringMap = MapClassValue.beanToMapString(testClass);
        System.out.println("MapClassValue.beanToMapString:" + stringMap);

        Map<String, Object> objectMap = MapClassValue.beanToMapObject(testClass);
        System.out.println("MapClassValue.beanToMapObject:" + objectMap);

        String day = DataTime.localDate(DataEnu.DAY);
        System.out.println("当前天：" + day);
    }

}
