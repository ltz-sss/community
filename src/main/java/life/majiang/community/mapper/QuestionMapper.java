package life.majiang.community.mapper;

import life.majiang.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface QuestionMapper {
    void create(Question question);
    List<Question> list(@Param(value = "offset") Integer offset, @Param(value = "size") Integer size);
    Integer count();
}
