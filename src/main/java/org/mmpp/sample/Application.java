package org.mmpp.sample;

import org.mmpp.sample.jdbc_extender.DBAccessService;
import org.mmpp.sample.jdbc_extender.DBAccessService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private DBAccessService service1;
    @Autowired
    private DBAccessService2 service2;
    /**
     * 実行
     * @param args 引数
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        // 実行時のバナーをなくします
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(String... strings) {

        service1.start();
        service2.start();
//        // これはエラーになる。原因はAutowaredじゃないのでJdbcTemplateがnull
//        new AbstractJDBCExtender("sql/service2.properties"){
//
//        }.start();
    }
}