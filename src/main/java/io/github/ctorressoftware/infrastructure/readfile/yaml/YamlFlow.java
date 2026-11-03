package io.github.ctorressoftware.infrastructure.readfile.yaml;

import java.util.List;

public class YamlFlow {
    private String name;
    private List<YamlStep> steps;

    public YamlFlow() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<YamlStep> getSteps() {
        return steps;
    }

    public void setSteps(List<YamlStep> steps) {
        this.steps = steps;
    }
}