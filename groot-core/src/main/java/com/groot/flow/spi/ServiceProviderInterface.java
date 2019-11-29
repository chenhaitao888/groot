package com.groot.flow.spi;

import com.groot.flow.classloader.GrootClassLoader;
import com.groot.flow.constant.GrootConfig;
import com.groot.flow.utils.IOUtil;
import com.groot.flow.utils.StringUtils;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * @author chenhaitao
 * @date 2019-11-28
 */
public class ServiceProviderInterface {
    private static GrootClassLoader grootClassLoader = new GrootClassLoader();;
    private static final ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
    private static final String GROOT_DIRECTORY = "META-INF/groot/";

    public static <T> T load(final Class<T> clazz, GrootConfig config) throws Exception {
        SPI spi = clazz.getAnnotation(SPI.class);
        String dynamicConfigKey = spi.key();
        String defaultValue = spi.value();
        if(StringUtils.isNotEmpty(config.getParameter(dynamicConfigKey))){
            defaultValue = config.getParameter(dynamicConfigKey);
        }
        Map<String, Object> clazzName = getClazzName(clazz);
        String className = (String)clazzName.get(defaultValue);
        return loadClass(className);
    }
    @SuppressWarnings("unchecked")
    public static <T> T loadClass(String clazzName) throws Exception{
        Class<T> loadClass = (Class<T>) grootClassLoader.loadClass(clazzName);
        Constructor<T> constructor = loadClass.getConstructor();
        return (T) constructor.newInstance();
    }
    public static Map<String, Object> getClazzName(final Class<?> clazz) throws Exception {
        GrootClassLoader.setSystemClassLoader(systemClassLoader);
        Enumeration<URL> configs = grootClassLoader.getResources(GROOT_DIRECTORY + clazz.getName());
        Map<String, Object> clazzNameMap = null;
        while (configs.hasMoreElements()) {
            URL url = configs.nextElement();
            clazzNameMap = IOUtil.readUrlStream(url);
        }
        return clazzNameMap;
    }
    public static void checkParams(final Class<?> clazz){
        if (clazz == null)
            throw new IllegalArgumentException("type == null");
        if (!clazz.isInterface()) {
            throw new IllegalArgumentException(" type(" + clazz + ") is not interface!");
        }
        if (!clazz.isAnnotationPresent(SPI.class)) {
            throw new IllegalArgumentException("type(" + clazz + ") is not extension, because WITHOUT @"
                    + SPI.class.getSimpleName() + " Annotation!");
        }
    }
}
