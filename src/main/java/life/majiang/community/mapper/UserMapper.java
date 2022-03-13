package life.majiang.community.mapper;

import life.majiang.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
     void insert(User user);
     User findByToken(@Param("token") String token);

    User findById(@Param("id") Integer id);


    User findByAccountId(@Param("accountId") String accountId);

    void update(User dbUser);
}
