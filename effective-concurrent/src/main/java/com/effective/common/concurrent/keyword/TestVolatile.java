package com.effective.common.concurrent.keyword;

public class TestVolatile {


    public static void main(String[] args) {

        MutableInteger m = new MutableInteger();

        new Thread(
                () -> {
                    while (true) {
                        m.set(m.get()+1);

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).start();

        new Thread(
                () -> {
                    while (true) {
                        m.set(200);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).start();


        while (true){
            System.out.println(m.get());

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    static class MutableInteger {
        private int value;

        public int get() {
            return value;
        }

        public void set(int value) {
            this.value = value;
        }
    }
}
