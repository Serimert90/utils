package serimert;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.UUID;

public class DefinitionClassCoverer {

    private DefinitionClassCoverer() { /* Empty constructor */ }

    public static void coverClasses(String... classNamesWithPackagePath) {
        coverClasses(new CoverOptions.Builder().all(), classNamesWithPackagePath);
    }

    public static void coverClasses(CoverOptions coverOptions, String... classNamesWithPackagePath) {
        for (String className : classNamesWithPackagePath) {
            try {
                Class<?> classToCover = Class.forName(className);
                Object objectInstance;
                if (classToCover.isEnum())
                    objectInstance = coverEnum(classToCover);
                else
                    objectInstance = coverConstructors(classToCover);

                if (coverOptions.isCoverGetterAndSetters())
                    coverGetterAndSetters(objectInstance, classToCover, coverOptions);
                coverOtherMethods(objectInstance, classToCover, coverOptions);
            } catch (Exception ex) {
                System.out.println("Reflection error for class: "+ className + ": " + ex);
            }
        }
    }

    private static Object coverEnum(Class<?> classToCover) {
        Object sampleObject = null;
        for (Object object : EnumSet.allOf((Class) classToCover)) {
            Object enumObject = Enum.valueOf((Class) classToCover, object.toString());
            if (sampleObject == null)
                sampleObject = enumObject;
        }
        return sampleObject;
    }

    private static Object coverConstructors(Class<?> classToCover)
            throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Object object = null;
        for (Constructor<?> constructor : classToCover.getDeclaredConstructors()) {
            Object[] constructorParamArr = getParameterArr(constructor.getParameterTypes());
            constructor.setAccessible(true);
            object = constructor.newInstance(constructorParamArr);
        }
        return object;
    }

    private static void coverGetterAndSetters(Object objectInstance, Class<?> classToCover,
                                              CoverOptions coverOptions)
            throws InvocationTargetException, IllegalAccessException {
        for (Method method : classToCover.getDeclaredMethods()) {
            if (classToCover.isEnum() && method.getName().contains("valueOf"))
                continue;

            method.setAccessible(true);
            method.invoke(objectInstance, getParameterArr(method.getParameterTypes()));
        }

        if (!coverOptions.isCoverSuperClasses())
            return;

        Class<?> parentClass = classToCover.getSuperclass();
        while (!Object.class.equals(parentClass) && !Enum.class.equals(parentClass)) {
            coverGetterAndSetters(objectInstance, parentClass, coverOptions);
            parentClass = parentClass.getSuperclass();
        }
    }

    /**
     * Cover to string, hashcode, equals, and interface default methods
     * @param objectInstance .
     * @param classToCover .
     */
    private static void coverOtherMethods(Object objectInstance, Class<?> classToCover,
                                          CoverOptions coverOptions)
            throws InvocationTargetException, IllegalAccessException {
        if (coverOptions.isCoverToStringEqualsHashcode()) {
            assert objectInstance.toString() != null;
            assert objectInstance.hashCode() > Integer.MIN_VALUE;
            assert !objectInstance.equals(UUID.randomUUID().toString());
        }

        if (!coverOptions.isCoverInterfaceDefaultMethods())
            return;

        Class<?>[] interfaces = classToCover.getInterfaces();
        int a = 0;
        while (interfaces.length > 0) {
            Class<?> interfaceClass = interfaces[a];
            for (Method method : interfaceClass.getDeclaredMethods()) {
                if (!method.isDefault())
                    continue;

                method.setAccessible(true);
                method.invoke(objectInstance, getParameterArr(method.getParameterTypes()));
            }
            a++;
            interfaces = interfaceClass.getInterfaces();
        }
    }

    private static Object[] getParameterArr(Class<?>[] parameterClasses) {
        if (parameterClasses.length == 0)
            return new Object[0];

        Object[] paramArr = new Object[parameterClasses.length];

        int a = 0;
        for (Class<?> paramClass : parameterClasses) {
            paramArr[a] = paramClass.isPrimitive() ? getPrimitiveSample(paramClass) : null;
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
