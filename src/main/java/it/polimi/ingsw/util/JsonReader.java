package it.polimi.ingsw.util;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

/**
 * JsonReader class is used to read objects from a JSON file.
 */
public class JsonReader {
    /**
     * Method getObjFromJson returns an object read from a JSON file.
     *
     * @param jsonFileName of type {@code String} - name of the JSON file.
     * @param objClass of type {@code Class<?>} - expected class of the object that will be read.
     * @return {@code Object} - instance of the object read.
     */
    public static Object getObjFromJson(String jsonFileName, Class<?> objClass) {
        InputStream stream = JsonReader.class.getClassLoader().getResourceAsStream("JSON/" + jsonFileName + ".json");
        try (Reader reader = new InputStreamReader(Objects.requireNonNull(stream))) {
            return new Gson().fromJson(reader, objClass);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert stream != null;
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
