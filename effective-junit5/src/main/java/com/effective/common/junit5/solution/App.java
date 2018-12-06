package com.effective.common.junit5.solution;


public class App {

    public long add(long[] operands) {
        // Compute the sum
        long ret = 0;
        if (operands == null) {
            throw new IllegalArgumentException("Operands argument cannot be null");
        }
        if (operands.length == 0) {
            throw new IllegalArgumentException("Operands argument cannot be empty");
        }
        for (long operand : operands) {
            ret += operand;
        }
        return ret;
    }
}
