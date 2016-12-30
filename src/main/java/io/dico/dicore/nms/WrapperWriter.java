package io.dico.dicore.nms;

import gnu.trove.map.hash.THashMap;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class WrapperWriter {

    private static Method[] objectMethods = Object.class.getMethods();

    private static boolean similar(Method m1, Method m2) {
        return m1.getName().equals(m2.getName()) && Arrays.equals(m1.getParameterTypes(), m2.getParameterTypes());
    }

    private static boolean containsSimilar(Collection<Method> coll, Method m) {
        for (Method m2 : coll) {
            if (similar(m2, m)) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsSimilar(Method[] coll, Method m) {
        for (Method m2 : coll) {
            if (similar(m2, m)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws InterruptedException {
        WrapperWriter writer = new WrapperWriter("io.dico.dicore.nms.nbt.CompoundTag", NBTTagCompound.class, true);
        writer.rename("a", "setAll", NBTTagCompound.class);
        writer.rename("c", "keys");
        writer.rename("b", "getTypeIdOf", String.class);
        /*
        WrapperWriter writer = new WrapperWriter("io.dico.dicore.nms.nbt.TagList", NBTTagList.class, true);
        writer.rename("a", "set", int.class, NBTBase.class);
        writer.rename("a", "remove", int.class);
        writer.rename("c", "getIntArray", int.class);
        writer.rename("d", "getDouble", int.class);
        writer.rename("e", "getFloat", float.class);
        writer.rename("get", "getCompound", int.class);
        writer.rename("g", "getTag", int.class);
        writer.rename("f", "getElementTypeId");
        // * /
        writer.exclude(NBTBase.class);
        writer.exclude(Tag.class);
        writer.replace(NBTBase.class, Tag.class);
        writer.replace(NBTTagCompound.class, CompoundTag.class);
        writer.replace(NBTTagList.class, TagList.class);
        */

        writer.write();
        writer.print();
        Thread.sleep(100);
    }

    private String packageName, className;
    private Class<?> wrapped;
    private Method[] methods;
    private LinkedList<String> lines = new LinkedList<>();
    private Map<String, Class<?>> imports = new HashMap<>();
    private Map<Method, LinkedHashMap<String, String>> parameters = new HashMap<>();
    private Map<Method, String> nameMap = MethodErasureMap.normalInstance();
    private Set<Class> excludedMethods = new HashSet<>();
    private Map<Class, Class> replacements = new HashMap<>();
    private boolean asInterface;

    public WrapperWriter(String className, Class<?> wrapped, boolean asInterface) {
        String[] split = className.split("\\.");
        this.className = split[split.length - 1];
        this.packageName = String.join(".", Arrays.copyOfRange(split, 0, split.length - 1));
        this.wrapped = wrapped;
        this.asInterface = asInterface;
    }

    private String getName(Method m) {
        String result = nameMap.get(m);
        if (result == null) {
            result = m.getName();
        }
        return result;
    }

    public void print() {
        for (String s : lines) {
            System.out.println(s);
        }
    }

    public void exclude(Class<?> clazz) {
        excludedMethods.add(clazz);
    }

    public void rename(String name, String newName, Class<?>... parameters) {
        try {
            Method m = wrapped.getMethod(name, parameters);
            nameMap.computeIfPresent(m, (x, y) -> newName);
        } catch (NoSuchMethodException ignored) {
        }
    }

    public void replace(Class<?> clazz, Class<?> otherClass) {
        replacements.put(clazz, otherClass);
    }

    private Type replace(Type type) {
        return type instanceof Class ? replace((Class) type) : type;
    }

    private Class<?> replace(Class<?> clazz) {
        return replacements.getOrDefault(clazz, clazz);
    }

    private void writeParameters() {
        methods = getMethods(asInterface);
        for (Method m : methods) {
            nameMap.put(m, m.getName());
        }

        for (Method m : methods) {
            LinkedHashMap<String, String> mparams = new LinkedHashMap<>();
            parameters.put(m, mparams);
            Set<String> keySet = new HashSet<>(mparams.keySet());
            keySet.add("wrapped");

            Type[] generic = m.getGenericParameterTypes();
            Class<?>[] classes = m.getParameterTypes();
            for (int index = 0; index < classes.length; index++) {
                String name = getVariableName(replace(classes[index]), keySet);
                mparams.put(name, getUsedName(replace(generic[index])));
            }
        }
    }

    public void write() {
        if (!lines.isEmpty()) {
            throw new IllegalStateException();
        }
        writeParameters();

        StringBuilder firstLine = new StringBuilder("public ");
        firstLine.append(asInterface ? "interface " : "class ");
        firstLine.append(className).append(" ");
        Class[] extensions = excludedMethods.stream().filter(Class::isInterface).toArray(Class[]::new);
        if (extensions.length > 0) {
            firstLine.append(asInterface ? "extends " : "implements ");
            appendIfPresent(extensions, firstLine, "", " ");
        }
        firstLine.append("{");

        lines.addFirst(firstLine.toString());

        if (!asInterface) {
            StringBuilder line = new StringBuilder("private final ");
            line.append(getUsedName(wrapped));
            line.append(';');
            lines.addFirst(line.toString());
            lines.addFirst("public " + className + "(" + getUsedName(wrapped) + " instance) {");
            lines.addFirst("wrapped = instance;");
            lines.addFirst("}");
        }

        for (Method m : methods) {
            StringBuilder line = new StringBuilder();
            if (!asInterface) {
                line.append("public ");
            }

            appendIfPresent(m.getTypeParameters(), line, "<", "> ");

            line.append(getUsedName(replace(m.getGenericReturnType()))).append(' ');
            line.append(nameMap.get(m)).append('(');
            boolean first = true;
            for (Map.Entry<String, String> entry : parameters.get(m).entrySet()) {
                if (first) {
                    first = false;
                } else {
                    line.append(", ");
                }
                line.append(entry.getValue()).append(' ').append(entry.getKey());
            }
            line.append(")");

            appendIfPresent(m.getExceptionTypes(), line, " throws ", "");

            if (asInterface) {
                line.append(';');
            } else {
                line.append(" {\n");
                if (m.getReturnType() != Void.TYPE) {
                    line.append("return ");
                }
                line.append("wrapped.").append(nameMap.get(m)).append('(');
                first = true;
                for (String parameterName : parameters.get(m).keySet()) {
                    if (first) {
                        first = false;
                    } else {
                        line.append(", ");
                    }
                    line.append(parameterName);
                }
                line.append(");\n}");
            }

            lines.addLast(line.toString());
        }

        lines.addLast("}");

        for (Map.Entry<Method, String> entry : nameMap.entrySet()) {
            if (!entry.getValue().equals(entry.getKey().getName())) {
                Method m = entry.getKey();
                String erasure = m.getName() + "(";
                boolean first = true;
                for (String parameterType : parameters.get(m).values()) {
                    if (first) {
                        first = false;
                    } else {
                        erasure += ", ";
                    }
                    erasure += parameterType;
                }
                erasure += ")";

                String line = "//renamed " + erasure + " to " + entry.getValue();
                lines.addFirst(line);
            }
        }

        for (Map.Entry<String, Class<?>> entry : imports.entrySet()) {
            Class<?> clazz = entry.getValue();
            if (clazz.isPrimitive() || clazz.isArray()) {
                continue;
            }
            String name = entry.getKey();
            if (name.startsWith("java.lang.")) {
                continue;
            }
            lines.addFirst("import " + name + ";");
        }

        if (!packageName.isEmpty()) {
            lines.addFirst("package " + packageName + ";");
        }
    }

    public String getResult() {
        return String.join("\n", lines);
    }

    private String getUsedName(Type type) {
        return type instanceof Class ? getUsedName((Class) type) : type.getTypeName();
    }

    private String getUsedName(Class<?> clazz) {
        String name = clazz.getName();
        Class associated = imports.get(name);
        if (associated == clazz) {
            return getFinalClassName(name);
        }
        if (associated == null) {
            imports.put(name, clazz);
            return getFinalClassName(name);
        }
        return name;
    }

    private static String getFinalClassName(String qualifier) {
        String[] split = qualifier.split("\\.");
        String result = split.length == 0 ? "NO_NAME" : split[split.length - 1];
        switch (result) {
            case "[B":
                return "byte[]";
            case "[I":
                return "int[]";
        }
        return result;
    }

    private Method[] getMethods(boolean useInterface) {
        List<Method> result = new ArrayList<>();
        Class<?> of = wrapped;
        while (of != null && of != Object.class) {
            if (useInterface && !excludedMethods.contains(of)) {
                outer:
                for (Method m : of.getDeclaredMethods()) {
                    if (java.lang.reflect.Modifier.isPublic(m.getModifiers())) {
                        if (useInterface && containsSimilar(objectMethods, m)) {
                            continue;
                        }

                        for (Method present : result) {
                            if (nameMap.get(present.getName()).equals(nameMap.get(m.getName())) && Arrays.equals(present.getTypeParameters(), m.getTypeParameters())) {
                                continue outer;
                            }
                        }

                        result.add(m);
                    }
                }
            }
            of = of.getSuperclass();
        }

        return result.toArray(new Method[result.size()]);
    }

    private String getVariableName(Class<?> clazz, Set<String> presentVariables) {
        String result = variableName(getFinalClassName(clazz.getName()));
        try {
            boolean lower = true;
            for (char c : result.toCharArray()) {
                if (!Character.isLowerCase(c)) {
                    lower = false;
                }
            }
            if (lower &&
                    Arrays.stream(Modifier.class.getDeclaredFields()).filter(field -> Modifier.isPublic(field.getModifiers()))
                            .map(field -> field.getName().toLowerCase()).collect(Collectors.toSet()).contains(result)) {
                result = "value";
            } else if (clazz.isPrimitive()) {
                result = "value";
            } else if (clazz.isArray()) {
                result = "array";
            }

        } catch (Exception ignored) {
        }

        result = result.replace("[", "");
        result = result.replace("]", "");

        int length = result.length();
        while (presentVariables.contains(result)) {
            int number = Integer.parseInt("0" + result.substring(length), 10);
            number++;
            result += number;
        }
        return result;
    }

    private CharSequence[] names(Type[] types) {
        return Arrays.stream(types).map(type -> getUsedName(replace(type))).toArray(CharSequence[]::new);
    }

    private String join(Type[] types) {
        return String.join(", ", names(types));
    }

    private void appendIfPresent(Type[] types, StringBuilder to, String start, String end) {
        if (types.length > 0) {
            to.append(start);
            to.append(join(types));
            to.append(end);
        }
    }

    private static String variableName(String typeName) {
        char[] chars = typeName.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return String.valueOf(chars);
    }

    private static abstract class MethodErasureMap<V> extends THashMap<Method, V> {
        @Override
        protected boolean equals(Object notnull, Object two) {
            if (notnull instanceof Method && two instanceof Method) {
                Method m1 = (Method) notnull;
                Method m2 = (Method) two;
                return nameOf(m1).equals(nameOf(m2)) && Arrays.equals(m1.getParameterTypes(), m2.getParameterTypes());
            }
            return super.equals(notnull, two);
        }

        @Override
        protected int hash(Object notnull) {
            if (notnull instanceof Method) {
                Method m = (Method) notnull;
                int hash = nameOf(m).hashCode();
                for (Class<?> type : m.getParameterTypes()) {
                    hash = hash * 31 + type.hashCode();
                }
                return hash;
            }
            return super.hash(notnull);
        }

        protected abstract String nameOf(Method m);

        static <V> MethodErasureMap<V> normalInstance() {
            return new MethodErasureMap<V>() {
                @Override
                protected String nameOf(Method m) {
                    return m.getName();
                }
            };
        }
    }

    private class AlteredMethodErasureMap<V> extends MethodErasureMap<V> {
        @Override
        protected String nameOf(Method m) {
            return getName(m);
        }
    }

}
