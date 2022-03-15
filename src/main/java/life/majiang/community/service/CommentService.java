package life.majiang.community.service;

import life.majiang.community.dto.CommentDTO;
import life.majiang.community.enums.CommentTypeEnum;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.mapper.CommentMapper;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Comment;
import life.majiang.community.model.Question;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Transactional
    public void insert(Comment comment) {
        if(comment.getParentId() == null || comment.getParentId() == 0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if(comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if(comment.getType() == CommentTypeEnum.COMMENT.getType()){ //COMMENT = 2
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
        }else{
            //回复问题
            Question question = questionMapper.getById(comment.getParentId());
            if(question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionMapper.incCommentCount(question);
        }
    }

    public List<CommentDTO> listByQuestionId(Long id) {

        List<Comment> comments = commentMapper.selectByQuestionId(id);  //需要查到comment表中type为CommentTypeEnum.QUESTION的comment
        if(comments.size() == 0){
            return new ArrayList<>();
        }
//        Set<Long> commentors = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());

        List<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMapper.findById(comment.getCommentator()));
            commentDTOS.add(commentDTO);
        }
        return commentDTOS;
    }

    public List<CommentDTO> listByTargetId(Long id) {
        List<Comment> comments = commentMapper.selectByTargetId(id);//
        if(comments.size() == 0){
            return new ArrayList<>();
        }
        List<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMapper.findById(comment.getCommentator()));
            commentDTOS.add(commentDTO);
        }
        return commentDTOS;
    }
}
