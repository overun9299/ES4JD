package soap.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Created by ZhangPY on 2020/12/19
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: 京东商品信息
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class JDGoodsInfo {

    /** 商品标题 **/
    private String title;

    /** 商品价格 **/
    private String price;

    /** 图片路径 **/
    private String imgUrl;

}
