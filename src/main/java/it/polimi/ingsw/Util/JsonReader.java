package it.polimi.ingsw.Util;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

public class JsonReader {
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
