package net.TokyoSlayer.ProxyPtero.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.TokyoSlayer.ProxyPtero.Main;

import java.io.IOException;
import java.util.*;

public class Files {

    private final FileUtils fileUtils = new FileUtils();

    public void toMap(String path,JsonObject jsonobj)  throws IOException {
        Iterator<String> keys = jsonobj.keySet().iterator();
        while(keys.hasNext()) {
            String key = keys.next();
            Object value = jsonobj.get(key);
            if (value instanceof JsonObject) {
                String newPath = String.format("%s%s%s", path, path.equals("") ? "" : ".", key);
                toList(newPath, (JsonObject) value);
                this.toMap(newPath, (JsonObject) value);
            }
        }
    }

    public void toList(String path,JsonObject obj) throws IOException {
        if(!path.endsWith("lobby")) {
            ArrayList<String> value = new ArrayList<>(obj.keySet());

            value.forEach(s -> extraTexts.put(path +"."+s, obj.get(s)));
        }
    }


    public void object(JsonObject jsonobj) throws IOException {
        toMap("",jsonobj);

    }

    public void load(final Main pl, final String file) {
        try {
            Object objet = JsonParser.parseString(FileUtils.loadContent(fileUtils.buildFile(pl, file)));
            JsonObject jsonObject = (JsonObject) objet;
            object(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final Map<String, Object> extraTexts = new HashMap<>();

    public Map<String, Object> getExtraTexts() {
        return this.extraTexts;
    }

    public int translateInt(String key) {
        final int translation;
        if (!this.getExtraTexts().containsKey(key.toLowerCase())) {
            return 404;
        } else {
            translation = Integer.parseInt(this.getExtraTexts().get(key.toLowerCase()).toString());
        }
        return translation;
    }

    public String translate(String key) {
        final String translation;
        if (!this.getExtraTexts().containsKey(key.toLowerCase())) {
            return String.format("Error : Message error (%s) ", key.toLowerCase());
        } else {
            translation = this.getExtraTexts().get(key.toLowerCase()).toString().replace("&","§");
        }
        return translation.replace("\"","");
    }
}
