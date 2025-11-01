package com.example.chatserver.message;

import com.example.chatserver.chatroom.Chatroom;
import com.example.chatserver.chatroom.ChatroomRepository;
import com.example.chatserver.user.User;
import com.example.chatserver.user.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
// @Component không cần thiết khi đã có @Mapper(componentModel = "spring")

import java.util.List;

@Mapper(componentModel = "spring")
// SỬA LẠI: Đổi "interface" thành "abstract class"
public abstract class MessageMapper {

    // SỬA LẠI: Dùng 'protected' và xóa '= null'
    // Spring sẽ tiêm (inject) các repository này vào
    @Autowired
    protected ChatroomRepository chatroomRepository;

    @Autowired
    protected UserRepository userRepository;

    // Các hàm abstract này sẽ được MapStruct tự động tạo
    @Mapping(source = "chatroomId", target = "chatroom", qualifiedByName = "mapChatroomById")
    @Mapping(source = "senderId", target = "sender", qualifiedByName = "mapUserById")
    public abstract Messages toEntity(MessageDTO dto);

    @Mapping(source = "chatroom.chatroomId", target = "chatroomId")
    @Mapping(source = "sender.userId", target = "senderId")
    public abstract MessageDTO toDTO(Messages entity);

    public abstract List<MessageDTO> toDTOs(List<Messages> entities);

    // Các hàm 'default' bây giờ không cần 'default' nữa
    // Chúng sẽ sử dụng các repository đã được tiêm ở trên
    @Named("mapChatroomById")
    protected Chatroom mapChatroomById(Long chatroomId) {
        if (chatroomId == null) {
            return null;
        }
        // Bây giờ 'chatroomRepository' sẽ không bị null
        return chatroomRepository.findById(chatroomId).orElse(null);
    }

    @Named("mapUserById")
    protected User mapUserById(Long userId) {
        if (userId == null) {
            return null;
        }
        // Bây giờ 'userRepository' sẽ không bị null
        return userRepository.findById(userId).orElse(null);
    }
}