package io.dico.dicore.saving;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import io.dico.dicore.util.JsonUtil;
import io.dico.dicore.util.exceptions.ExceptionHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonConfiguration extends FileConfiguration {

    @Override
    public String saveToString() {
        try {
            return save();
        } catch (Exception ex) {
            ExceptionHandler.log(Bukkit.getLogger()::severe, "saving config to string", ex);
            return "";
        }
    }

    @Override
    public void loadFromString(String input) throws InvalidConfigurationException {
        try (StringReader stringReader = new StringReader(input);
             JsonReader reader = new JsonReader(stringReader)) {
            JsonToken token = reader.peek();
            if (token != JsonToken.BEGIN_OBJECT) {
                throw new InvalidConfigurationException("Json configurations must always start with a key-value mapping");
            }
            readSection(reader, this);
        } catch (InvalidConfigurationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InvalidConfigurationException(ex);
        }
    }

    @Override
    protected String buildHeader() {
        return null;
    }

    private String save() throws IOException {
        try (StringWriter stringWriter = new StringWriter();
             JsonWriter writer = new JsonWriter(stringWriter)) {
            writer.setIndent("  ");
            JsonUtil.insert(writer, this);
            return stringWriter.toString();
        }
    }

    // READING //

    private void read(JsonReader reader, ConfigurationSection into, String key) throws IOException, InvalidConfigurationException {
        switch (reader.peek()) {
            case BEGIN_OBJECT:
                readSection(reader, into.createSection(key));
                break;
            case BEGIN_ARRAY:
                into.set(key, readArray(reader));
                break;
            case NUMBER:
                into.set(key, reader.nextDouble());
                break;
            case STRING:
                into.set(key, reader.nextString());
                break;
            case BOOLEAN:
                into.set(key, reader.nextBoolean());
                break;
            default:
                throw new InvalidConfigurationException();
        }
    }

    private void readSection(JsonReader reader, ConfigurationSection into) throws IOException, InvalidConfigurationException {
        reader.beginObject();
        while (reader.hasNext()) {
            read(reader, into, reader.nextName());
        }
        reader.endObject();
    }

    private static Object read(JsonReader reader) throws IOException, InvalidConfigurationException {
        switch (reader.peek()) {
            case BEGIN_OBJECT:
                return readMap(reader);
            case BEGIN_ARRAY:
                return readArray(reader);
            case BOOLEAN:
                return reader.nextBoolean();
            case NUMBER:
                return reader.nextDouble();
            case NULL:
                return null;
            case STRING:
                return reader.nextString();
            default:
                throw new InvalidConfigurationException();
        }
    }

    private static Map readMap(JsonReader reader) throws IOException, InvalidConfigurationException {
        Map result = new HashMap();
        reader.beginObject();
        while (reader.hasNext()) {
            result.put(reader.nextName(), read(reader));
        }
        reader.endObject();
        return result;
    }

    private static List readArray(JsonReader reader) throws IOException, InvalidConfigurationException {
        List result = new ArrayList();
        reader.beginArray();
        while (reader.hasNext()) {
            result.add(read(reader));
        }
        reader.endArray();
        return result;
    }


}
