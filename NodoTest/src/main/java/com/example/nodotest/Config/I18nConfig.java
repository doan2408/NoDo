package com.example.nodotest.Config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

@Configuration
public class I18nConfig {
    // Kết hợp cả hai resolver
    @Bean
    public LocaleResolver localeResolver() {
        // Dùng AcceptHeaderLocaleResolver để đọc locale từ header "Accept-Language"
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH); // fallback nếu không có header "Accept-Language"

        // Nếu bạn muốn thay đổi ngôn ngữ trong phiên, có thể sử dụng SessionLocaleResolver
        // (cho phép lưu trạng thái ngôn ngữ trong session)
        return resolver;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
        ms.setBasename("classpath:messages");
        ms.setDefaultEncoding("UTF-8");
        ms.setFallbackToSystemLocale(false);
        return ms;
    }

    // Liên kết messageSource với Bean Validation (Hibernate Validator)
    @Bean
    public LocalValidatorFactoryBean getValidator(MessageSource messageSource) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }

    // Cấu hình interceptor để thay đổi locale từ tham số "lang" trong URL
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang"); // Tham số "lang" sẽ được dùng trong URL để thay đổi locale
        return interceptor;
    }

    // Đăng ký LocaleChangeInterceptor vào registry
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor()); // Đăng ký interceptor để thay đổi locale từ tham số "lang"
    }
}
