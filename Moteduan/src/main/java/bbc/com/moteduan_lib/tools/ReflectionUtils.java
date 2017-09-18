package bbc.com.moteduan_lib.tools;

/**
 * Created by Administrator on 2016/7/13 0013.
 */
public class ReflectionUtils {

    private static final String TYPE_NAME_PREFIX = "class ";

    //通过Type获取对象class
    public static String getClassName(java.lang.reflect.Type type) {
        if (type==null) {
            return "";
        }
        String className = type.toString();
        if (className.startsWith(TYPE_NAME_PREFIX)) {
            className = className.substring(TYPE_NAME_PREFIX.length());
        }
        return className;
    }

    public static Class<?> getClass(java.lang.reflect.Type type)
            throws ClassNotFoundException {
        String className = getClassName(type);
        if (className==null || className.isEmpty()) {
            return null;
        }
        return Class.forName(className);
    }

    //通过Type创建对象
    public static Object newInstance(java.lang.reflect.Type type)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> clazz = getClass(type);
        if (clazz==null) {
            return null;
        }
        return clazz.newInstance();
    }



}
