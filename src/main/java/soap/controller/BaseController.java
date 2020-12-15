package soap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by ZhangPY on 2020/12/15
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: 基础Controller
 */
@Controller
public class BaseController {

    @GetMapping({"/","/index"})
    public String indexController() {
        return "index";
    }
}
