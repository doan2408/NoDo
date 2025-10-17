package com.example.handlehttp;

import com.example.handlehttp.Model.DonHang;
import com.example.handlehttp.Model.KhachHang;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;

@SpringBootApplication
@Configuration
public class HandleHttpApplication {

    public static void main(String[] args) {
        SpringApplication.run(HandleHttpApplication.class, args);

    }
//    @Scope("prototype")
//    @Bean
//    @Order(3)
//    public DonHang getDonHang() {
//        System.out.println("hihi");
//        DonHang donHang = new DonHang();
//        donHang.setMaDon("tesst");
//        return donHang;
//    }
//
//    @Bean("3")
//    @Order(1)
//    public DonHang getDonHang2(DonHang donHang) {
//
//        System.out.println("hihi");
//        donHang.setMaDon("tesst222");
//        return donHang;
//    }
}
