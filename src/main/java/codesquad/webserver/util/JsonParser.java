package codesquad.webserver.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonParser {
    private static final Pattern OBJECT_PATTERN = Pattern.compile("\\{(.*?)\\}");
    private static final Pattern KEY_VALUE_PATTERN = Pattern.compile("\"(.*?)\"\\s*:\\s*(\".*?\"|\\{.*?\\}|-?\\d+(\\.\\d+)?|true|false|null)");

    public static Map<String, Object> parse(String jsonString) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonString = jsonString.trim();
        Matcher objectMatcher = OBJECT_PATTERN.matcher(jsonString);
        if(objectMatcher.find()){
            String objectContent = objectMatcher.group(1);
            Matcher keyValueMatcher = KEY_VALUE_PATTERN.matcher(objectContent);
            while(keyValueMatcher.find()){
                String key = keyValueMatcher.group(1);
                String value = keyValueMatcher.group(2);
                jsonMap.put(key, parseValue(value));
            }
        }
        return jsonMap;
    }

    private static Object parseValue(String value){
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        } else if (value.startsWith("{") && value.endsWith("}")) {
            return parseValue(value);
        } else if (value.equals("true") || value.equals("false")) {
            return Boolean.parseBoolean(value);
        } else if (value.equals("null")) {
            return null;
        } else{
            try {
                if (value.contains(".")) {
                    return Double.parseDouble(value);
                } else {
                    return Long.parseLong(value);
                }
            } catch (NumberFormatException e) {
                return value;
            }
        }
    }
}
