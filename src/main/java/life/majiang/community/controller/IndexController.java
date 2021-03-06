package life.majiang.community.controller;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.dto.QuestionDto;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class IndexController {
    @Autowired
    private QuestionService questionService;
    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "size", defaultValue = "5") Integer size,
                        @RequestParam(value = "search", required = false) String search){

        PaginationDTO<QuestionDto> pagination = questionService.list(search, page, size);

        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);
        return "index";
    }
}
