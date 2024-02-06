package by.dmitryskachkov.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class RandomUserAgents {
    private static List<String> USER_AGENTS = new ArrayList<>();

    public RandomUserAgents() {
    }

    static {
        USER_AGENTS.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:97.0) Gecko/20100101 Firefox/97.0");
        USER_AGENTS.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36");
        USER_AGENTS.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Safari/605.1.15");
        USER_AGENTS.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36 Edg/98.0.1108.55");
    }

    public static String getRandomUserAgent() {
        Random random = new Random();
        int index = random.nextInt(USER_AGENTS.size());
        return USER_AGENTS.get(index);
    }

}
