package serimert;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DefinitionClassCoverer {

    private static final Map<Class<?>, Object> ALREADY_INSTANTIATED_CLASS_INSTANCES = new HashMap<>();

    static {
        ALREADY_INSTANTIATED_CLASS_INSTANCES.put(String.class, "a");
        ALREADY_INSTANTIATED_CLASS_INSTANCES.put(Integer.class, Integer.valueOf("1"));
        ALREADY_INSTANTIATED_CLASS_INSTANCES.put(Long.class, Long.valueOf("1"));
        ALREADY_INSTANTIATED_CLASS_INSTANCES.put(Double.class, Double.valueOf("1"));
    }

    private DefinitionClassCoverer() { /* Empty constructor */ }

    public static void coverClasses(String... classNamesWithPackagePath) {
        coverClasses(new CoverOptions.Builder().all(), classNamesWithPackagePath);
    }

    public static void coverClasses(CoverOptions coverOptions, String... classNamesWithPackagePath) {
        for (String className : classNamesWithPackagePath) {
            try {
                Class<?> classToCover = Class.forName(className);
                Object objectInstance = classToCover.isEnum() ? coverEnum(classToCover)
                        : coverConstructors(classToCover);
                coverGetterAndSetters(objectInstance, classToCover, coverOptions);
                coverToStringEqualsHashcode(objectInstance, coverOptions);
                coverInterfaceDefaultMethods(objectInstance, classToCover, coverOptions);
            } catch (Exception ex) {
                System.out.println("Reflection error for class: "+ className + ": " + ex);
            }
        }
    }

    private static Object coverEnum(Class<?> classToCover) {
        Object sampleObject = null;
        for (Object object : EnumSet.allOf((Class) classToCover)) {
            Object enumObject = Enum.valueOf((Class) classToCover, object.toString());
            if (sampleObject == null) {
                sampleObject = enumObject;
                assert !object.equals(UUID.randomUUID().toString());
            }
        }
        return sampleObject;
    }

    private static Object coverConstructors(Class<?> classToCover)  {
        Object object = null;
        for (Constructor<?> constructor : classToCover.getDeclaredConstructors()) {
            Object[] constructorParamArr = getParameterArr(constructor.getParameterTypes());
            constructor.setAccessible(true);
            try {
                object = constructor.newInstance(constructorParamArr);
                ALREADY_INSTANTIATED_CLASS_INSTANCES.put(classToCover, object);
                assert !object.equals(UUID.randomUUID().toString());
            } catch (Exception ignored) {}
        }
        return object;
    }

    private static void coverGetterAndSetters(Object objectInstance, Class<?> classToCover,
                                              CoverOptions coverOptions) {
        if (!coverOptions.isCoverGetterAndSetters())
            return;

        for (Method method : classToCover.getDeclaredMethods()) {
            if (shouldSkipForGetterAndSetters(classToCover, method))
                continue;

            method.setAccessible(true);
            try {
                Object res = method.invoke(objectInstance, getParameterArr(method.getParameterTypes()));
                assert res == null || !res.equals(UUID.randomUUID().toString());
            } catch (Exception ignored) {}
        }

        if (!coverOptions.isCoverSuperClasses())
            return;

        Class<?> parentClass = classToCover.getSuperclass();
        while (!Object.class.equals(parentClass) && !Enum.class.equals(parentClass)) {
            coverGetterAndSetters(objectInstance, parentClass, coverOptions);
            parentClass = parentClass.getSuperclass();
        }
    }

    private static void coverToStringEqualsHashcode(Object objectInstance,CoverOptions coverOptions) {
        if (!coverOptions.isCoverToStringEqualsHashcode())
            return;

        try {
            assert objectInstance.toString() != null;
            assert objectInstance.hashCode() > Integer.MIN_VALUE;
            assert !objectInstance.equals(UUID.randomUUID().toString());
            boolean result = objectInstance.equals(coverConstructors(objectInstance.getClass()));
        } catch (Exception ignored) {}
    }

    /**
     * Cover interface default methods
     * @param objectInstance .
     * @param classToCover .
     */
    private static void coverInterfaceDefaultMethods(Object objectInstance, Class<?> classToCover,
                                                     CoverOptions coverOptions) {
        if (!coverOptions.isCoverInterfaceDefaultMethods())
            return;

        Class<?>[] interfaces = classToCover.getInterfaces();
        for (Class<?> interfaceClass : interfaces) {
            if (interfaceClass.getSimpleName().equals("Serializable"))
                break;

            for (Method method : interfaceClass.getDeclaredMethods()) {
                if (!method.isDefault())
                    continue;

                method.setAccessible(true);
                try {
                    method.invoke(objectInstance, getParameterArr(method.getParameterTypes()));
                } catch (Exception ignored) {}
            }
        }
    }

    private static boolean shouldSkipForGetterAndSetters(Class<?> classToCover, Method method) {
        return method.getName().equals("toString")
                || method.getName().equals("hashCode")
                || method.getName().equals("equals")
                || (classToCover.isEnum() && method.getName().contains("valueOf"));
    }

    private static Object[] getParameterArr(Class<?>[] parameterClasses) {
        if (parameterClasses.length == 0)
            return new Object[0];

        Object[] paramArr = new Object[parameterClasses.length];

        int a = 0;
        for (Class<?> paramClass : parameterClasses) {
            if (paramClass.isPrimitive())
                paramArr[a] = getPrimitiveSample(paramClass);
            else
                paramArr[a] = ALREADY_INSTANTIATED_CLASS_INSTANCES.getOrDefault(paramClass, null);
            a++;
        }
        return paramArr;
    }

    private static Object getPrimitiveSample(Class<?> paramClass) {
        if (int.class.equals(paramClass)) {
            return 1;
        } else if (short.class.equals(paramClass)) {
            return 1;
        } else if (long.class.equals(paramClass)) {
            return 1L;
        } else if (float.class.equals(paramClass)) {
            return 1F;
        } else if (double.class.equals(paramClass)) {
            return 1D;
        } else if (char.class.equals(paramClass)) {
            return 'a';
        } else if (boolean.class.equals(paramClass)) {
            return true;
        } else { // byte
            return 12;
        }
    }
}
