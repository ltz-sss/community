package life.majiang.community.mapper;

import life.majiang.community.model.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Mapper
public interface NotificationMapper {
    void insert(Notification notification);

    Integer countByUserId(@Param("id") Long id); //当前用户的所有通知

    List<Notification> listByUserId(@Param("id") Long id, @Param("offset") Integer offset,@Param("size") Integer size);

    Long countUnreadByUserId(@Param("userId") Long userId);//当前用户未读的消息的个数

    Notification getById(@Param("id") Long id);

    void updateStatusById(Notification notification);
}
