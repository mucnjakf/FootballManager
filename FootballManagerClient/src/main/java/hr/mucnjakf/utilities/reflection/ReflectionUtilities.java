package hr.mucnjakf.utilities.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

public class ReflectionUtilities {

    public static void readClassInfo(Class<?> inputClass, StringBuilder sbClass) {
        appendPackage(inputClass, sbClass);
        appendModifiers(inputClass, sbClass);
        appendParent(inputClass, sbClass, true);
        appendInterfaces(inputClass, sbClass);
    }

    private static void appendPackage(Class<?> inputClass, StringBuilder sbClass) {
        sbClass
                .append("<h3>Package: ")
                .append(inputClass.getPackage())
                .append("</h3></br>");
    }

    private static void appendModifiers(Class<?> inputClass, StringBuilder sbClass) {
        sbClass
                .append("<h3>Modifiers:</h3>")
                .append("<p>")
                .append(Modifier.toString(inputClass.getModifiers()))
                .append("</p></br>");
    }

    private static void appendParent(Class<?> inputClass, StringBuilder sbClass, boolean first) {
        Class<?> parent = inputClass.getSuperclass();

        if (parent == null) {
            return;
        }

        if (first) {
            sbClass
                    .append("<h3>")
                    .append("Extends:")
                    .append("</h3>");
        }

        sbClass
                .append("<p>")
                .append(" ")
                .append(parent.getName())
                .append("</p>");

        appendParent(parent, sbClass, false);
    }

    private static void appendInterfaces(Class<?> inputClass, StringBuilder sbClass) {
        if (inputClass.getInterfaces().length > 0) {
            sbClass
                    .append("</br><h3>")
                    .append("Implements:")
                    .append("</h3>");
        }

        for (Class<?> in : inputClass.getInterfaces()) {
            sbClass
                    .append("<p>")
                    .append(" ")
                    .append(in.getName())
                    .append("</p>");
        }
    }

    public static void readClassAndMembersInfo(Class<?> inputClass, StringBuilder sbClass) {
        readClassInfo(inputClass, sbClass);
        appendFields(inputClass, sbClass);
        appendMethods(inputClass, sbClass);
        appendConstructors(inputClass, sbClass);
    }

    private static void appendFields(Class<?> inputClass, StringBuilder sbClass) {
        Field[] fields = inputClass.getDeclaredFields();

        sbClass.append("</br><h3>Fields:</h3>");

        if (fields.length > 0) {
            for (Field field : fields) {
                sbClass
                        .append("<p>")
                        .append(field)
                        .append("</p>");
            }
        } else {
            sbClass.append("<p>No fields</p>");
        }
    }

    private static void appendMethods(Class<?> inputClass, StringBuilder sbClass) {
        Method[] methods = inputClass.getDeclaredMethods();

        sbClass.append("</br><h3>Methods:</h3>");

        if (methods.length > 0) {
            for (Method method : methods) {
                sbClass.append("<p>");

                appendMethodAnnotations(method, sbClass);

                sbClass
                        .append(" ")
                        .append(Modifier.toString(method.getModifiers()))
                        .append(" ")
                        .append(method.getReturnType())
                        .append(" ")
                        .append(method.getName());

                appendParameters(method, sbClass);
                appendExceptions(method, sbClass);
            }
        } else {
            sbClass.append("<p>No methods</p>");
        }
    }

    private static void appendMethodAnnotations(Executable executable, StringBuilder sbClass) {
        for (Annotation annotation : executable.getAnnotations()) {
            sbClass
                    .append(annotation)
                    .append(" - ");
        }
    }

    private static void appendParameters(Executable executable, StringBuilder sbClass) {
        sbClass
                .append("(");

        for (Parameter parameter : executable.getParameters()) {
            sbClass
                    .append(parameter)
                    .append(", ");
        }
        if (sbClass.toString().endsWith(", ")) {
            sbClass.delete(sbClass.length() - 2, sbClass.length());
        }
        sbClass
                .append(")")
                .append(" ");
    }

    private static void appendExceptions(Executable executable, StringBuilder sbClass) {
        Class<?>[] exceptionTypes = executable.getExceptionTypes();

        if (exceptionTypes.length > 0) {
            sbClass
                    .append("- throws");

            for (Class<?> exceptionType : exceptionTypes) {
                sbClass
                        .append(" ")
                        .append(exceptionType)
                        .append(", ");
            }
            if (sbClass.toString().endsWith(", ")) {
                sbClass.delete(sbClass.length() - 2, sbClass.length());
            }
        }

        sbClass.append("</p>");
    }

    private static void appendConstructors(Class<?> inputClass, StringBuilder sbClass) {
        Constructor[] constructors = inputClass.getDeclaredConstructors();

        sbClass.append("</br><h3>Constructors:</h3>");

        for (Constructor constructor : constructors) {
            appendMethodAnnotations(constructor, sbClass);

            sbClass
                    .append("<p>")
                    .append(Modifier.toString(constructor.getModifiers()))
                    .append(" ")
                    .append(constructor.getName());

            appendParameters(constructor, sbClass);
            appendExceptions(constructor, sbClass);
        }

        sbClass.append("</br>").append("<hr>").append("</br>");
    }
}
