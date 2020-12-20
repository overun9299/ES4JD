package soap.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "elasticsearch")
@Configuration
public class ESProperties {
    /**
     * http连接超时时间
     */
    private String connectTimeout;

    /**
     * socket连接超时时间
     */
    private String socketTimeout;

    /**
     * 获取连接的超时时间
     */
    private String connectionRequestTimeout;

    /**
     * 最大连接数
     */
    private String maxConnTotal;

    /**
     * 最大路由连接数
     */
    private String maxConnPerRoute;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * Elasticsearch http访问路径
     */
    private String httpHost;
}
