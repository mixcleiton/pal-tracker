package io.pivotal.pal.tracker;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class PalTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(PalTrackerApplication.class, args);
    }

    @Bean
    public MysqlDataSource mysqlDataSource() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl(System.getenv("SPRING_DATASOURCE_URL"));
        return dataSource;
    }

    @Bean
    public TimeEntryRepository timeEntryRepository() {

        return new JdbcTimeEntryRepository(this.mysqlDataSource());
    }

}
