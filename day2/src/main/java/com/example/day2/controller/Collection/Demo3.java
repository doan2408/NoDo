package com.example.day2.controller.Collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Demo3 {

    String[] arr = {"A", "B", "C"};

    public void manipulate() {
        // option 1
        // convert array về arrayList
        // cách 1: Arrays.asList()
        List<String> list1 = Arrays.asList(arr); // tạo list (fixed-size, không thêm/xóa được)

        // cách 2: Collections.addAll()
        List<String> list3 = new ArrayList<>();
        Collections.addAll(list3, arr);

        // cách 3: new Demo1<>(Arrays.asList())
        List<String> list2 = new ArrayList<>(Arrays.asList(arr)); // list có thể thêm/xóa
        list2.add("D");

        // cách 4: Stream
        List<String> list4 = Arrays.stream(arr).toList(); // Java 16 trở lên trả về List immutable

        ArrayList<String> list = Arrays.stream(arr)
                .collect(Collectors.toCollection(ArrayList::new));

        // cách 5: foreach
        List<String> arrayList = new ArrayList<>();
        for(String x: arr) {
            arrayList.add(x);
        }
    }
    
    public static void main(String[] args) {

    }

}
