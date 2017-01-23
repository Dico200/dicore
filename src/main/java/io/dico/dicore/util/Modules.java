package io.dico.dicore.util;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import io.dico.dicore.Module;
import io.dico.dicore.ModuleManager;
import io.dico.dicore.saving.JsonLoadable;
import io.dico.dicore.saving.fileadapter.FileAdapter;
import io.dico.dicore.saving.fileadapter.GsonFileAdapter;
import io.dico.dicore.saving.fileadapter.JsonFileAdapter;
import io.dico.dicore.util.exceptions.ExceptionHandler;
import io.dico.dicore.util.exceptions.Exceptions;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Modules {
    
    private Modules() {
        throw new UnsupportedOperationException();
    }
    
    private static String getModuleName(Class<? extends Module> moduleClass) {
        String[] split = moduleClass.getName().split("\\.");
        String name = split[split.length - 1];
        if (name.toLowerCase().endsWith("module")) {
            name = name.substring(0, name.length() - "module".length());
        } else if (name.toLowerCase().startsWith("module")) {
            name = name.substring("module".length());
        }
        return name;
    }
    
    public static Module newInstanceOf(Class<? extends Module> moduleClass, ModuleManager inst) {
        if (!inst.isEnabled()) {
            inst.error("Attempted to register module " + moduleClass.getCanonicalName() + " but the plugin is not enabled");
        }
        
        for (Constructor<?> ctor : moduleClass.getConstructors()) {
            Class<?>[] params = ctor.getParameterTypes();
            if (params.length == 0) {
                return (Module) Exceptions.supplySafe(ctor::newInstance);
            }
            if (params.length == 1) {
                Class<?> param = params[0];
                if (param == String.class) {
                    return (Module) Exceptions.supplySafe(() -> ctor.newInstance(getModuleName(moduleClass)));
                } else if (param.isAssignableFrom(inst.getClass())) {
                    return (Module) Exceptions.supplySafe(() -> ctor.newInstance(inst));
                }
                continue;
            }
            if (params.length == 2) {
                if (params[0] == String.class && params[1].isAssignableFrom(inst.getClass())) {
                    return (Module) Exceptions.supplySafe(() -> ctor.newInstance(getModuleName(moduleClass), inst));
                } else if (params[0].isAssignableFrom(inst.getClass()) && params[1] == String.class) {
                    return (Module) Exceptions.supplySafe(() -> ctor.newInstance(inst, getModuleName(moduleClass)));
                }
                continue;
            }
            if (params.length == 4) {
                if (params[0] == String.class && params[1].isAssignableFrom(inst.getClass()) && params[2] == Boolean.TYPE && params[3] == Boolean.TYPE) {
                    return (Module) Exceptions.supplySafe(() -> ctor.newInstance(getModuleName(moduleClass), inst, true, false));
                } else if (params[0].isAssignableFrom(inst.getClass()) && params[1] == String.class && params[2] == Boolean.TYPE && params[3] == Boolean.TYPE) {
                    return (Module) Exceptions.supplySafe(() -> ctor.newInstance(inst, getModuleName(moduleClass), true, false));
                }
            }
        }
        
        inst.error("module class " + moduleClass.getCanonicalName() + " does not declare any recognized public constructors. Any of the following work:");
        inst.error("(no parameters), (" + inst.getClass().getCanonicalName() + ") or (String, " + inst.getClass().getCanonicalName() + ", boolean, boolean");
        return null;
    }
    
    public static <T extends JsonLoadable> FileAdapter<T> newJsonFileAdapter(Logging logging, String usage, Supplier<T> supplier) {
        Consumer<Exception> onError = ex -> ExceptionHandler.log(logging::error, "loading or saving " + usage, ex);
        return JsonFileAdapter.create(supplier, onError, onError);
    }
    
    public static <T> FileAdapter<T> newGsonFileAdapter(Logging logging, Type typeOfT, String usage, TypeAdapter<? super T> typeAdapter) {
        Consumer<Exception> onError = ex -> ExceptionHandler.log(logging::error, "loading or saving " + usage, ex);
        return GsonFileAdapter.create(typeOfT, new GsonBuilder().setPrettyPrinting().registerTypeAdapter(typeOfT, typeAdapter).create(), onError, onError);
    }
    
}
