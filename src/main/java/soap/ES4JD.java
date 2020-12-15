package soap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Created by ZhangPY on 2020/12/15
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: 使用ES查询模仿jd页面启动类
 */

// 暂时不使用数据库 配上exclude= {DataSourceAutoConfiguration.class}项目不报错
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class ES4JD {


    public static void main(String[] args) {
        SpringApplication.run(ES4JD.class, args);
    }
}
