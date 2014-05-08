package me.loki2302;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.loki2302.changelog.ChangeLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableTransactionManagement
@EntityScan(basePackages = "me.loki2302.entities")
@EnableJpaRepositories(basePackages = "me.loki2302.entities")
public class Config extends WebMvcConfigurerAdapter {


    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Bean
    public DataSource dataSource() throws SQLException {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .build();
    }

    /*@Bean
    public MyHandlerInterceptorAdapter myHandlerInterceptorAdapter() {
        return new MyHandlerInterceptorAdapter();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myHandlerInterceptorAdapter());
    }


    private static class MyHandlerInterceptorAdapter extends HandlerInterceptorAdapter {
        @Autowired
        private ChangeLog changeLog;

        @Override
        public void postHandle(
                HttpServletRequest request,
                HttpServletResponse response,
                Object handler,
                ModelAndView modelAndView) throws Exception {

            changeLog.saveLog();
        }
    }*/
}
