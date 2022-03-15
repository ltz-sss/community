package life.majiang.community.controller;

import life.majiang.community.dto.QuestionDto;
import life.majiang.community.service.CommentService;
import life.majiang.community.dto.CommentDTO;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    QuestionService questionService;
    @Autowired
    CommentService commentService;
    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id,
                           Model model){
        QuestionDto questionDto =  questionService.getById(id);//给页面返回问题
        List<QuestionDto> relatedQuestions = questionService.selectRelated(questionDto);

        List<CommentDTO> comments = commentService.listByQuestionId(id);//返回当前问题的所有评论
        //累加阅读数
        questionService.incView(id);
        model.addAttribute("question", questionDto);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }

}
