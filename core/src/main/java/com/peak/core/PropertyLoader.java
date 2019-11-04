package com.peak.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.peak.core.Constant.DEFAULT_CHARSET;

@Slf4j
public class PropertyLoader {

    private static final Map<String, Properties> propMap = new HashMap<String, Properties>();

    private static final String DEFAULT_ENV = "system";

    static {
        String path = PropertyLoader.class.getClassLoader().getResource("").getPath();
        if(path.startsWith("/")) path = path.substring(1);
        File file = new File(path);
        if(file.exists()) {
            for(File f : file.listFiles()) {
                if(f.isFile() && f.getName().lastIndexOf(".properties") > -1) {
                    String propName = f.getName().replaceAll("\\.properties", "");
                    Properties prop = new Properties();
                    try (InputStream is = new FileInputStream(f);
                         InputStreamReader ir = new InputStreamReader(is, DEFAULT_CHARSET);){
                        prop.load(ir);
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                    propMap.put(propName, prop);
                }
            }
        }
    }

    public static String get(String key) {
        return get(DEFAULT_ENV, key);
    }

    public static String get(String env, String key) {
        String propEnv = DEFAULT_ENV;
        if(StringUtils.isNotBlank(env)) {
            for(String k : propMap.keySet()) {
                if(env.equals(k)) {
                    propEnv = env;
                    break;
                }
            }
        }
        if(propEnv.equals(DEFAULT_ENV)) {
            log.info("正在使用{}", DEFAULT_CHARSET);
        }
        return (String)propMap.get(env).get(key);
    }

    public static void main(String[] args) {
        System.out.println(get("a"));
    }
}
