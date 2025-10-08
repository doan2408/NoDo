package com.example.day2.controller.Collection;

import java.util.ArrayList;

public class Demo1 {
    ArrayList<String> list = new ArrayList<>();

    ArrayList<Integer> list1 = new ArrayList<>(10); // 10 is also the default capacity

    ArrayList<Integer> list2 = new ArrayList<>(list1); // copy from another list

    public void manipulate() {
        // the way to manipulate elements in an array
        list.add("A");                   // Thêm phần tử
        list.add("B");
        list.add(1, "C");  // Thêm tại vị trí chỉ định
        list.get(0);                     // Lấy phần tử
        list.set(0, "Z");                // Cập nhật phần tử
        list.remove(1);            // Xóa phần tử tại chỉ số
        list.contains("A");              // Kiểm tra tồn tại
        list.size();                     // Lấy số lượng phần tử
        list.clear();                    // Xóa toàn bộ
    }

    public static void main(String[] args) {
        Demo1 demo1 = new Demo1();
        demo1.manipulate();
        System.out.println(demo1.list);

    }


}
