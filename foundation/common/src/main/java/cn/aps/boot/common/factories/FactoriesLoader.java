package cn.aps.boot.common.factories;


import cn.aps.boot.common.spi.SPIMeta;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class FactoriesLoader {
    private static final Logger LOGGER = Logger.getLogger(FactoriesLoader.class.getName());

    private static final Map<Class<?>, Map<String, Object>> cache = new ConcurrentHashMap<>();

    private static final Map<String, Object> NULL_MAP = Collections.emptyMap();

    private FactoriesLoader() {
    }

    @SuppressWarnings("unchecked")
    private static <T> Map<String, T> getFactories(Class<T> factoryClass) {
        // 检查参数
        if (factoryClass == null) {
            throw new IllegalArgumentException("factoryClass cannot be null");
        }

        Map<String, T> factoryInstances = (Map<String, T>) cache.get(factoryClass);

        if (factoryInstances == null) {
            synchronized (FactoriesLoader.class) {
                factoryInstances = (Map<String, T>) cache.get(factoryClass);
                if (factoryInstances == null) {
                    loadFactories(factoryClass);
                    factoryInstances = (Map<String, T>) cache.get(factoryClass);
                }
            }
        }
        return factoryInstances;
    }

    private static <T> void loadFactories(Class<T> factoryClass) {
        // 使用ServiceLoader加载实现类
        ServiceLoader<T> serviceLoader = ServiceLoader.load(factoryClass);
        Iterator<T> iterator = serviceLoader.iterator();

        if (!iterator.hasNext()) {
            cache.put(factoryClass, NULL_MAP);
            return;
        }

        Map<String, Object> factoryInstances = new LinkedHashMap<>();

        while (iterator.hasNext()) {
            T factory;
            try {
                factory = iterator.next();
            } catch (ServiceConfigurationError e) {
//                LOGGER.severe("Error loading SPI implementation for " + factoryClass, e);
                continue;
            }

            if (factory == null) {
                continue;
            }

            String spiName = getSpiName(factory.getClass());
            if (spiName == null || spiName.trim().isEmpty()) {
//                LOGGER.warn("Invalid SPI name for class: " + factory.getClass().getName());
                continue;
            }

            Object old = factoryInstances.get(spiName);
            if (old != null) {
//                LOGGER.warn("SPI id has already exist, new class's order >= old class's order, override.[new class = [%s], old class = [%s]]",
//                        factory.getClass().getName(),
//                        old.getClass().getName());
            }

            factoryInstances.put(spiName, factory);
        }
        cache.put(factoryClass, factoryInstances);
    }

    /**
     * 获取扩展点的spi名称，
     *
     * @param clazz
     * @return 若使用SPIMeta显示指定spi名称，则直接返回该spi名称。若未配置，则使用类类路径作为spi名称
     */
    public static String getSpiName(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        SPIMeta spiMeta = clazz.getAnnotation(SPIMeta.class);
        //若未配置注解，则默认使用类名作为key值
        if (spiMeta == null) {
            return clazz.getSimpleName();
        }

        return (!"".equals(spiMeta.id())) ? spiMeta.id() : clazz.getSimpleName();
    }

    /**
     * 根据接口上定义的默认实现获取对应的实例
     *
     * @return 若没有定义默认实现则返回NULL
     */
    public static <T> T getDefaultFactory(Class<T> factoryClass) {
        if (factoryClass == null) {
            return null;
        }
        Map<String, T> factoryInstances = getFactories(factoryClass);
        if (factoryInstances == null || factoryInstances.isEmpty()) {
            return null;
        }
        return factoryInstances.values().iterator().next();
    }

    /**
     * 获取ORDER最大的实现对应的实例
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getNewestFactory(Class<T> factoryClass) {
        if (factoryClass == null) {
            return null;
        }
        Map<String, T> factoryInstances = getFactories(factoryClass);

        if (factoryInstances == null || factoryInstances.isEmpty()) {
            return null;
        } else {
            return (T) factoryInstances.values().toArray()[factoryInstances.size() - 1];
        }
    }

    /**
     * 获取ORDER最小的实现对应的实例
     *
     * @return
     */
    public static <T> T getOldestFactory(Class<T> factoryClass) {
        if (factoryClass == null) {
            return null;
        }
        Map<String, T> factoryInstances = getFactories(factoryClass);

        if (factoryInstances == null || factoryInstances.isEmpty()) {
            return null;
        } else {
            return factoryInstances.values().iterator().next();
        }
    }

    /**
     * 根据指定ID获取对应的实例
     *
     * @param id SPI实现上定义的ID
     * @return 若没有对应实现则返回NULL
     */
    public static <T> T getFactoryById(Class<T> factoryClass, String id) {
        if (factoryClass == null || id == null) {
            return null;
        }
        Map<String, T> factoryInstances = getFactories(factoryClass);
        if (factoryInstances == null || factoryInstances.isEmpty()) {
            return null;
        } else {
            return factoryInstances.get(id);
        }
    }

    /**
     * 获取全部实现实例列表
     *
     * @return 若没有任何实现则返回空列表
     */
    public static <T> List<T> getAllFactories(Class<T> factoryClass) {
        if (factoryClass == null) {
            return Collections.emptyList();
        }
        Map<String, T> factoryInstances = getFactories(factoryClass);
        if (factoryInstances == null || factoryInstances.isEmpty()) {
            return Collections.emptyList();
        }

        return new ArrayList<>(factoryInstances.values());
    }

    /**
     * 按GROUP获取实现实例列表
     *
     * @return 若没有符合条件的实现则返回空列表
     */
    public static <T> List<T> getFactoriesByGroup(Class<T> factoryClass) {
        // 由于JDK SPI不支持分组,直接返回所有实现
        return getAllFactories(factoryClass);
    }
}
