package life.majiang.community.mapper;

import life.majiang.community.model.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    void insert(Comment comment);

    Comment selectByPrimaryKey(@Param("parentId") Long parentId);

    List<Comment> selectByQuestionId(@Param("id") Long id);   //id为Question的id，所以需要查询comment的type必须为QUESTION(1)

    List<Comment> selectByTargetId(Long id);
}
