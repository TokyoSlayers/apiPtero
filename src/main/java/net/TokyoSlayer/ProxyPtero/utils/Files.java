package net.TokyoSlayer.ProxyPtero.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.TokyoSlayer.ProxyPtero.Main;

import java.io.IOException;
import java.util.*;

public class Files {
    private final FileUtils fileUtils = new FileUtils();

    public void object(JsonObject jsonobj) throws IOException {
        Iterator<String> keys = jsonobj.keySet().iterator();
        while(keys.hasNext()) {
            String key = keys.next();
            Object value = jsonobj.get(key);
            extraTexts.put(key,value);
            if(value instanceof JsonObject){
                object((JsonObject) value);
            }
        }
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

    public String translateRedis(String key) {
        final String translation;
        if (!this.getExtraTexts().containsKey(key.toLowerCase())) {
            return String.format("Error : Message error (%s) ", key.toLowerCase());
        } else {
            translation = this.getExtraTexts().get(key.toLowerCase()).toString().replace("&","§");
        }
        return translation;
    }

    public String translate(String key, Object... args) {
        final String translation;
        if (!this.getExtraTexts().containsKey(key.toLowerCase())) {
            return String.format("Error : Message error (%s) ", key.toLowerCase());
        } else {
            translation = this.getExtraTexts().get(key.toLowerCase()).toString().replace("&","§");
        }
        if(args == null){
            return translation;
        }else {
            try {
                return String.format(translation, args);
            } catch (IllegalFormatException e) {
                System.out.println(String.format("Error while formatting translation (%s)", key.toLowerCase()));
                return translation + " (Format error)";
            }
        }
    }
}
