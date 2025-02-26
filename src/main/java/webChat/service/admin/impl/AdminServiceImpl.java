package webChat.service.admin.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import webChat.dto.room.ChatRoomDto;
import webChat.dto.room.ChatRoomMap;
import webChat.dto.room.KurentoRoomDto;
import webChat.service.admin.AdminService;
import webChat.service.chat.KurentoManager;
import webChat.service.file.FileService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final KurentoManager kurentoManager;
    private final FileService fileService;

    @Override
    public Map<String, Object> getAllRooms() {
        Map<String, Object> result = new HashMap<>();

        ConcurrentMap<String, ChatRoomDto> chatRooms = ChatRoomMap.getInstance().getChatRooms();

        JsonArray joRooms = new JsonArray();
        chatRooms.values()
                .forEach(room -> {
                    JsonObject joRoom = new JsonObject();
                    joRoom.addProperty("id", room.getRoomId());
                    joRoom.addProperty("name", room.getRoomName());
                    joRoom.addProperty("pwd", room.getRoomPwd());
                    joRoom.addProperty("isSecret", room.isSecretChk());
                    joRoom.addProperty("type", room.getChatType().toString());
                    joRoom.addProperty("count", room.getUserCount());

                    joRooms.add(joRoom);
                });

        result.put("roomList", joRooms);
        return result;
    }

    @Override
    public String delRoom(String roomId) {
        Optional<KurentoRoomDto> kurentoRoom = Optional
                .ofNullable((KurentoRoomDto) ChatRoomMap.getInstance().getChatRooms().get(roomId));

        if (kurentoRoom.isPresent()) {
            kurentoManager.removeRoom(kurentoRoom.get());
            return "success del chatroom";
        }

        // room 안에 있는 파일 삭제
        fileService.deleteFileDir(roomId);

        return "no such room exist";
    }
}