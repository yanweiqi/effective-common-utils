package com.effective.common.concurrent.thread;

import java.util.ArrayList;
import java.util.List;

public class Test {


    static List<String> books = new ArrayList<>();

    static int pageSize = 6;

    static int totalRows = 130;


    static {
        for (int i = 0; i < totalRows; i++) {
            books.add("books_" + i);
        }
    }

    public static void main(String[] args) {
        int pageTotal = totalRows % 6 == 0 ? totalRows / pageSize : totalRows / pageSize + 1;

        for (int i = 1; i < pageTotal+1; i++) {
            List<String> list = page(books, i, pageSize);
            System.out.println("打印第(" + i + ")页数据");
            list.forEach((s) -> {
                System.out.println("----"+s);
            });
        }
    }

    public static List<String> page(List<String> datalist, Integer page, Integer size) {
        List<String> list = new ArrayList<>();
        if (datalist != null && datalist.size() > 0) {
            int curIndex = (page > 1 ? (page - 1) * size : 0);
            for (int i = 0; i < size && i < datalist.size() - curIndex; i++) {
                String s = datalist.get(curIndex + i);
                list.add(s);
            }
        }
        return list;
    }
}



