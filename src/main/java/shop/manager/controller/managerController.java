package shop.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class managerController {

    @GetMapping("/manager/main.do")
    public String manager() {

        return "manager/main";
    }
}
