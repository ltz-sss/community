package life.majiang.community.service;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.dto.QuestionDto;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    public PaginationDTO list(Integer page, Integer size) {
        Integer totalCount = questionMapper.count();
        Integer totalpage;
        if(totalCount % size == 0){
            totalpage = totalCount / size;
        }else {
            totalpage = totalCount / size + 1;
        }
        page = page < 1 ? 1 : page;
        page = page > totalpage ? totalpage : page;

        Integer offset = (page - 1) * size;
        List<QuestionDto> questionDtoList = new ArrayList<>();

        List<Question> questions = questionMapper.list(offset, size);

        PaginationDTO paginationDTO = new PaginationDTO();

        for (Question question : questions) {
            User user  = userMapper.findById(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question, questionDto);
            questionDto.setUser(user);    //设置questionDto就是为了能在每个question中存入user, 原来的question类没有user属性
            questionDtoList.add(questionDto);
        }

        paginationDTO.setData(questionDtoList);
        paginationDTO.setPagination(totalCount,page,size);

        return paginationDTO;
    }
    public PaginationDTO<QuestionDto> list(Long userId, Integer page, Integer size) {
        Integer totalCount = questionMapper.countByUserId(userId);
        Integer totalpage;
        if(totalCount % size == 0){
            totalpage = totalCount / size;
        }else {
            totalpage = totalCount / size + 1;
        }
        page = page < 1 ? 1 : page;
        page = page > totalpage ? totalpage : page;

        Integer offset = (page - 1) * size;

        List<QuestionDto> questionDtoList = new ArrayList<>();

        List<Question> questions = questionMapper.listByUserId(userId, offset, size);

        PaginationDTO<QuestionDto> paginationDTO = new PaginationDTO<>();

        for (Question question : questions) {
            User user  = userMapper.findById(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question, questionDto);
            questionDto.setUser(user);    //设置questionDto就是为了能在每个question中存入user, 原来的question类没有user属性
            questionDtoList.add(questionDto);
        }

        paginationDTO.setData(questionDtoList);
        paginationDTO.setPagination(totalCount,page,size);

        return paginationDTO;
    }

    public QuestionDto getById(Long id) {
        Question question = questionMapper.getById(id);
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDto questionDto = new QuestionDto();
        BeanUtils.copyProperties(question, questionDto);
        questionDto.setUser(userMapper.findById(question.getCreator()));

        return questionDto;
    }

    public void createOrUpdate(Question question) {
        if(question.getId() == null) {
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setCommentCount(0);
            question.setLikeCount(0);
            questionMapper.create(question);
        }else {
            question.setGmtModified(System.currentTimeMillis());
            questionMapper.update(question);

        }
    }

    public void incView(Long id) {  //在questionController调用此方法，每次访问一次controller就加一次
        Question question = questionMapper.getById(id);
        questionMapper.updateViewById(id);
    }

    public List<QuestionDto> selectRelated(QuestionDto queryDTO) {
        if(StringUtils.isBlank(queryDTO.getTag())){
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(queryDTO.getTag(), ",");
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);
        List<Question> questions = questionMapper.selectRelated(question);
        List<QuestionDto> collect = questions.stream().map(q -> {
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(q, questionDto);
            return questionDto;
        }).collect(Collectors.toList());

        return collect;
    }
}
