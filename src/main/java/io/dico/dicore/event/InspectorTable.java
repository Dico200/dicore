package io.dico.dicore.event;

import io.dico.dicore.util.box.BooleanBox;
import org.bukkit.event.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

class InspectorTable {

    private final Map<Class<? extends Event>, Map<Class, Function>> inspectors;

    public <T extends Event, R> Function<T, R> get(Class<T> input, Class<R> output) {

        Class<?> inputClass = input;
        while (inputClass != null) {
            if (inspectors.containsKey(inputClass)) {
                Map<Class<R>, Function<T, R>> map = inspectors.get(inputClass);

            }


            inputClass = inputClass.getSuperclass();
        }
    }


    public InspectorTable(Map<Class<? extends Event>, Map<Class, Function>> inspectors) {
        this.inspectors = inspectors;
    }


    public static InspectorTableBuilder builder() {
        return new InspectorTableBuilder();
    }

    private static class InspectorTableBuilder {

        private final Map<Class, Map<Class, Function>> pipes = new HashMap<>();

        public <T, R> void addPipe(Class<T> input, Class<R> output, Function<T, R> function) {
            getOrCreate(pipes, input, HashMap::new).put(output, function);
        }

        public InspectorTable build() {

            Map<Class<? extends Event>, Map<Class, Function>> inspectors = new HashMap<>();
            pipes.forEach((input, map) -> {
                if (Event.class.isAssignableFrom(input)) {
                    inspectors.put(input, map);
                }
            });

            BooleanBox changed = new BooleanBox(true);
            while (changed.value) {
                changed.value = false;
                new HashMap<>(inspectors).forEach((event, map) -> {
                    new HashMap<>(map).forEach((output, function) -> {
                        pipes.forEach((pipeInput, map2) -> {
                            if (!pipeInput.isAssignableFrom(output)) {
                                return;
                            }

                            map2.forEach((pipeOutput, function2) -> {
                                if (map.containsKey(pipeOutput)) {
                                    return;
                                }

                                map.put(pipeOutput, function.andThen(function2));
                                changed.value = true;
                            });
                        });
                    });
                });

            }

            return new InspectorTable(inspectors);
        }
    }

    private static <K, V> V getOrCreate(Map<K, V> map, K key, Supplier<V> constructor) {
        V result = map.get(key);
        if (result == null) {
            result = constructor.get();
            map.put(key, result);
        }
        return result;
    }

    private static <K, V> V getOrDefault(Map<K, V> map, K key, Supplier<V> constructor) {
        V result = map.get(key);
        if (result == null) {
            result = constructor.get();
        }
        return result;
    }
}
