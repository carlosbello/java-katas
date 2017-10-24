package org.carlosbello.javakatas;

import java.lang.reflect.Method;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args == null || args.length < 1) {
            printHelp();
            return;
        }

        String kataName = args[0];
        String[] arguments = tail(args);

        try {
            Class<?> clazz = Class.forName(Main.class.getPackage().getName() + "." + kataName);
            Method exec = clazz.getMethod("main", arguments.getClass());
            exec.invoke(null, (Object) arguments);
        } catch (ClassNotFoundException e) {
            System.out.println("Kata not found. Check spelling or try a different name");
        } catch (NoSuchMethodException e) {
            System.out.println("The kata specified is not executable.");
        }
    }

    private static void printHelp() {
        System.out.println("java -jar java-katas.jar <kata-name> [arguments...]");
    }

    private static String[] tail(String[] args) {
        return Arrays.copyOfRange(args, 1, args.length);
    }
}
