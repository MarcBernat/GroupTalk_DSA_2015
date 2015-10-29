package edu.upc.eetac.dsa.grouptalk.auth;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by marc on 16/10/15.
 */
public class AuthorizedResource {
    private String path;
    private List<String> methods;
    private Pattern pattern;

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        pattern = Pattern.compile(path);
    }
}
