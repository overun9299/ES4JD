package soap.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by ZhangPY on 2020/12/12
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: 用户对象
 */
@Data
@Accessors(chain = true)
public class MyUser {

    /** id **/
    private String id;

    /** 姓名 **/
    private String name;

    /** 年龄 **/
    private int age;

    /** 地址 **/
    private String address;
}
