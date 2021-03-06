package life.majiang.community.controller;

import life.majiang.community.cache.TagCache;
import life.majiang.community.dto.QuestionDto;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import life.majiang.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionService questionService;
    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Long id,
                       Model model){
        QuestionDto question = questionService.getById(id);
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("id", question.getId());
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }



    @GetMapping("/publish")
    public String publish(Model model){
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            @RequestParam(value = "id", required = false) Long id,
            HttpServletRequest request,
            Model model){

        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        model.addAttribute("tags", TagCache.get());
        if(title == null || title.equals("")){
            model.addAttribute("error", "??????????????????");
            return "publish";
        }
        if(description == null || description.equals("")){
            model.addAttribute("error", "????????????????????????");
            return "publish";
        }
        if(tag == null || tag.equals("")){
            model.addAttribute("error", "??????????????????");
            return "publish";
        }
        String invalid = TagCache.filterInvaild(tag);
        if(StringUtils.isNotBlank(invalid)){
            model.addAttribute("error", "??????????????????: " + invalid);
            return "publish";
        }


        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            model.addAttribute("error", "???????????????");
            System.out.println("???????????????");
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());

        question.setId(id);
        questionService.createOrUpdate(question);
        System.out.println("???????????????");
        return "redirect:/";
    }
}
