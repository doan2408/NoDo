package com.example.day2.repository;

import java.util.*;

class Config {
    public static void main(String[] args) {

        // arrayList
        // cách tao:
        ArrayList<String> list = new ArrayList<>();      // rỗng
        ArrayList<Integer> list0 = new ArrayList<>(20);  // khởi tạo dung lượng ban đầu = 20


        // thao tác phần tử trong mảng:
        list.add("A");              // thêm
        list.add("B");
        list.add(1, "C");           // thêm tại vị trí chỉ định
        list.set(0, "X");           // sửa phần tử
        list.remove(1);             // xóa phần tử index = 1
        String val = list.get(0);   // lấy phần tử
        int size = list.size();     // số phần tử
        boolean hasA = list.contains("A"); // kiểm tra có phần tử không



        // linkedList
        // cách tạo:
        LinkedList<String> linkedList = new LinkedList<>();

        // cách thao tác với phần tử:
        linkedList.add("A");
        linkedList.add("B");
        linkedList.addFirst("Start");    // thêm vào đầu
        linkedList.addLast("End");       // thêm vào cuối
        linkedList.remove("B");          // xóa phần tử
        linkedList.removeFirst();        // xóa đầu
        linkedList.removeLast();         // xóa cuối
        String first = linkedList.getFirst();  // lấy phần tử đầu
        String last  = linkedList.getLast();   // lấy phần tử cuối


        // convert array về arrayList
        // cách 1: Arrays.asList()
        String[] arr = {"A", "B", "C", "X", "Y", "Z"};
        List<String> list1 = Arrays.asList(arr); // tạo list (fixed-size, không thêm/xóa được)

        // cách 2: Collections.addAll()
        List<String> list3 = new ArrayList<>();
        Collections.addAll(list3, arr);

        // cách 3: new Demo1<>(Arrays.asList())
        List<String> list2 = new ArrayList<>(Arrays.asList(arr)); // list có thể thêm/xóa
        list2.add("D");

        // cách 4: Stream
        List<String> list4 = Arrays.stream(arr).toList(); // Java 16 trở lên trả về List immutable

        // cách 5: foreach
        List<String> arrayList = new ArrayList<>();
        for(String x: arr) {
            arrayList.add(x);
        }
    }
}
