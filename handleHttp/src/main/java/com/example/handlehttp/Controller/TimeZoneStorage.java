package com.example.handlehttp.Controller;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class TimeZoneStorage {
    // Lưu trữ múi giờ của người dùng
    private Map<Integer, ZoneId> userTimezones;

    public TimeZoneStorage() {
        userTimezones = new HashMap<>();
    }

    // Lưu múi giờ cho người dùng
    public void setTimezone(int userId, String timezone) {
        try {
            ZoneId zone = ZoneId.of(timezone);
            userTimezones.put(userId, zone);
            System.out.println("Múi giờ của user " + userId + " đã được thiết lập: " + timezone);
        } catch (Exception e) {
            System.out.println("Múi giờ không hợp lệ: " + timezone);
        }
    }

    // Lấy múi giờ của người dùng
    public ZoneId getTimezone(int userId) {
        return userTimezones.getOrDefault(userId, ZoneId.of("UTC"));  // Mặc định là UTC nếu không có thông tin múi giờ
    }

    // Chuyển đổi thời gian từ UTC sang múi giờ của người dùng
    public ZonedDateTime convertTime(int userId, ZonedDateTime utcTime) {
        ZoneId userZone = getTimezone(userId);
        return utcTime.withZoneSameInstant(userZone);
    }

    public static void main(String[] args) {
        TimeZoneStorage tzStorage = new TimeZoneStorage();

        // Thiết lập múi giờ cho người dùng
        tzStorage.setTimezone(123, "Asia/Ho_Chi_Minh");
        tzStorage.setTimezone(456, "Europe/London");

        // Giả sử chúng ta có một thời gian UTC
        ZonedDateTime utcTime = ZonedDateTime.now(ZoneId.of("UTC"));

        // Chuyển đổi thời gian UTC sang múi giờ của người dùng
        ZonedDateTime user123Time = tzStorage.convertTime(123, utcTime);
        ZonedDateTime user456Time = tzStorage.convertTime(456, utcTime);

        // Hiển thị thời gian sau khi chuyển đổi
        System.out.println("Thời gian của user 123: " + user123Time);
        System.out.println("Thời gian của user 456: " + user456Time);
    }
}


//Giải thích:
//
//Lớp TimeZoneStorage: Lớp này lưu trữ múi giờ của người dùng trong một Map<Integer, ZoneId>,
// trong đó Integer là ID người dùng và ZoneId là múi giờ của họ.
//
//Phương thức setTimezone: Cập nhật múi giờ cho người dùng.
//
//Phương thức convertTime: Chuyển đổi một thời gian UTC sang múi giờ của người dùng.
//
//Ví dụ sử dụng: Trong ví dụ trên, thời gian UTC được chuyển đổi sang múi giờ của người dùng 123 (Asia/Ho_Chi_Minh)
// và người dùng 456 (Europe/London).
