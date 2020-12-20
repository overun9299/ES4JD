package soap.service;

import java.util.List;

/**
 * Created by ZhangPY on 2020/12/20
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: 爬取京东数据等相关
 */
public interface JDInfoEsService {

    /**
     * 获取京东数据，并打入es
     * @param keyWord
     * @return
     */
    Boolean getJDInfoToES(String keyWord);

    /**
     * 搜索京东数据
     * @param keyWord
     * @param pageNo
     * @param pageSize
     * @return
     */
    String searchJDGoods(String keyWord, Integer pageNo, Integer pageSize);

}
