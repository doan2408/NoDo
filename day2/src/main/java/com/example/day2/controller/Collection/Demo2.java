package com.example.day2.controller.Collection;

import java.util.LinkedList;

public class Demo2 {
    LinkedList<String> linked = new LinkedList<>();

    LinkedList<Integer> linked1 = new LinkedList<>();
    LinkedList<Integer> linked2 = new LinkedList<>(linked1);  // copy from another LinkedList

    public void manipulate() {
        linked.add("A");                // Thêm cuối danh sách
        linked.addFirst("Start");       // Thêm đầu
        linked.addLast("End");          // Thêm cuối
        linked.get(1);                  // Lấy phần tử
        linked.removeFirst();           // Xóa đầu
        linked.removeLast();            // Xóa cuối
        linked.remove("A");             // Xóa theo giá trị
        linked.set(0, "Z");             // Cập nhật phần tử
        linked.contains("B");           // Kiểm tra tồn tại
        linked.size();                  // Đếm số phần tử
    }
}
