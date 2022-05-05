package me.itoncek.pluginlauncher;

public class Config {
    private String name;
    private String param;

    public Config() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String age) {
        this.param = param;
    }

    public String toString() {
        return "Student [ name: " + name + ", age: " + param + " ]";
    }
}
