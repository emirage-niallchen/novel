package cn.com.emirage.novel.core.util.crawl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author ChenBoyun
 * @date 2023-09-02 23:07
 */
public class PatternFactory {
    private static final Map<String, Pattern> PATTERN_CACHED_MAP = new HashMap<>();

    /**
     * 根据正则表达式获取一个预编译的Pattern对象
     */
    public static Pattern getPattern(String regex) {
        Pattern pattern = PATTERN_CACHED_MAP.get(regex);
        if (Objects.isNull(pattern)) {
            pattern = Pattern.compile(regex);
            PATTERN_CACHED_MAP.put(regex, pattern);
        }
        return pattern;
    }

}
