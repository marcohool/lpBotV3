package Config;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {

    private static final Dotenv dotenv = Dotenv.load();

    public static String getToken(String token) {
        return dotenv.get(token);
    }

    public static String getAPIKey(String apikey) {
        return dotenv.get(apikey);
    }

}
