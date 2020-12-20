package soap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import soap.service.JDInfoEsService;

import java.util.List;

/**
 * Created by ZhangPY on 2020/12/20
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: 爬取京东数据等相关
 */
@RestController
@RequestMapping("/jd")
public class JDInfoEsController {

    @Autowired
    private JDInfoEsService jdInfoEsService;


    /**
     * 获取京东数据，并打入es
     * @param keyWord
     * @return
     */
    @GetMapping("/getJDInfoToES")
    public Boolean getJDInfoToES(String keyWord) {
        return jdInfoEsService.getJDInfoToES(keyWord);
    }

    /**
     * 搜索京东数据
     * @param keyWord
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/searchJDGoods")
    public String searchJDGoods(String keyWord , Integer pageNo , Integer pageSize) {
        return jdInfoEsService.searchJDGoods(keyWord , pageNo , pageSize);
    }

}
