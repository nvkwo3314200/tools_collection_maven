package com.peak.core;

public class ApplicationContext extends PropertyLoader{

    private static final String rootClassPath;

    static {
        String path = ApplicationContext.class.getClassLoader().getResource("").getPath();
        if(path.startsWith("/")) {
            rootClassPath = path.substring(1);
        } else {
            rootClassPath = path;
        }
    }

    public static String getRootClassPath() {
        return rootClassPath;
    }

}
