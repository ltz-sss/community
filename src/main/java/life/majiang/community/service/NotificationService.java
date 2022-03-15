package life.majiang.community.service;

import life.majiang.community.dto.NotificationDTO;
import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.enums.NotificationTypeEnum;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.mapper.NotificationMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Notification;
import life.majiang.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {
    @Autowired
    NotificationMapper notificationMapper;
    @Autowired
    UserMapper userMapper;
    public PaginationDTO<NotificationDTO> list(Long id, Integer page, Integer size) {
        Integer totalCount = notificationMapper.countByUserId(id); //计算当前用户的消息数
        Integer totalpage;
        if(totalCount % size == 0){
            totalpage = totalCount / size;
        }else {
            totalpage = totalCount / size + 1;
        }
        page = page < 1 ? 1 : page;
        page = page > totalpage ? totalpage : page;
        Integer offset = (page - 1) * size;


        List<Notification> notifications = notificationMapper.listByUserId(id, offset, size);
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        if(notifications.size() == 0){
            return paginationDTO;
        }
        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setTypeName((NotificationTypeEnum.nameOfType(notification.getType())));
            notificationDTOS.add(notificationDTO);
        }
        paginationDTO.setData(notificationDTOS);
        paginationDTO.setPagination(totalCount, page, size);
        return paginationDTO;
    }

    public Long unreadCount(Long userId) {
        Long c = notificationMapper.countUnreadByUserId(userId); //查找当前用户未读的通知个数
        return c;
    }

    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.getById(id);
        if(notification == null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if(!Objects.equals(notification.getReceiver(), user.getId())){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        notificationMapper.updateStatusById(notification);  //更新状态为已读
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
