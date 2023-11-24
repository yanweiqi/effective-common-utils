package com.effective.common.function;


import java.util.Objects;

public class SwitchStream {

    private String source;

    private Boolean b;

    public static SwitchStream.Builder builder() {
        return new SwitchStream.Builder().builder();
    }

    private SwitchStream() {
    }

    private SwitchStream(SwitchStream instance) {
        this.source = instance.source;
        this.b = instance.b;
    }

    public static class Builder {

        private static SwitchStream instance;

        public Builder builder() {
            instance = Objects.isNull(instance) ? new SwitchStream() : instance;
            return this;
        }

        public Builder source(String source) {
            instance.source = source;
            return this;
        }

        public Builder and(String target) {
            if (Objects.isNull(instance.b) || instance.b) {
                instance.b = Objects.nonNull(target) && instance.source.equals(target);
            }
            return this;
        }

        public Boolean get() {
            return instance.b;
        }
    }

    public static void main(String[] args) {
        Boolean s = SwitchStream.builder()
                .source("u")
                .and("u")
                .and("y")
                .get();
        int y  = 0;
        System.out.println(s);
    }
}
