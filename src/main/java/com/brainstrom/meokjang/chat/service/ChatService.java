package com.brainstrom.meokjang.chat.service;

import com.brainstrom.meokjang.chat.dto.ChatRoom;
import com.brainstrom.meokjang.chat.repository.ChatRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Slf4j
@Service
public class ChatService {
    private ChatRepository chatRepo;
    @Autowired
    public ChatService(ChatRepository chatRepo){
        this.chatRepo = chatRepo;
    }

    // 전체 채팅방 조회
    public List<ChatRoom> findAllRoom(){
        return chatRepo.findAll();
    }

    // roomID 기준으로 채팅방 찾기
    public ChatRoom findRoomById(String roomId){
        try{
            ChatRoom room = chatRepo.findById(roomId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 채팅방이 없습니다. id=" + roomId));
            return room;
        }catch (IllegalArgumentException e){
            log.error(e.getMessage());
            return null;
        }
    }

    public void plusUserCnt(String roomId){
        try{
            ChatRoom room = chatRepo.findById(roomId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 채팅방이 없습니다. id=" + roomId));
            room.setUserCount(room.getUserCount()+1);
        }catch (IllegalArgumentException e){
            log.error(e.getMessage());
        }
    }

    // 채팅방 인원-1
    public void minusUserCnt(String roomId){
        try{
            ChatRoom room = chatRepo.findById(roomId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 채팅방이 없습니다. id=" + roomId));
            room.setUserCount(room.getUserCount()-1);
        }catch (IllegalArgumentException e){
            log.error(e.getMessage());
        }
    }

    // 채팅방 유저 리스트에 유저 추가
    public String addUser(String roomId, String userName){
        try{
            ChatRoom room = chatRepo.findById(roomId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 채팅방이 없습니다. id=" + roomId));
            String userUUID = UUID.randomUUID().toString();

            // 아이디 중복 확인 후 userList 에 추가
            room.getUserlist().put(userUUID, userName);

            return userUUID;
        }catch (IllegalArgumentException e){
            log.error(e.getMessage());
            return null;
        }
    }

    // 채팅방 유저 이름 중복 확인
    public String isDuplicateName(String roomId, String username){
        try{
            ChatRoom room = chatRepo.findById(roomId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 채팅방이 없습니다. id=" + roomId));

            // 만약 userName 이 중복이라면 랜덤한 숫자를 붙임
            // 이때 랜덤한 숫자를 붙였을 때 getUserlist 안에 있는 닉네임이라면 다시 랜덤한 숫자 붙이기!
            while(room.getUserlist().containsValue(username)){
                int ranNum = (int) (Math.random()*100)+1;

                username = username+ranNum;
            }

            return username;
        }
        catch (IllegalArgumentException e){
            log.error(e.getMessage());
            return null;
        }
    }

    // 채팅방 유저 리스트 삭제
    public void delUser(String roomId, String userUUID){
        try{
            ChatRoom room = chatRepo.findById(roomId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 채팅방이 없습니다. id=" + roomId));
            room.getUserlist().remove(userUUID);
        }catch (IllegalArgumentException e){
            log.error(e.getMessage());
        }
    }

    // 채팅방 userName 조회
    public String getUserName(String roomId, String userUUID){
        try{
            ChatRoom room = chatRepo.findById(roomId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 채팅방이 없습니다. id=" + roomId));
            return room.getUserlist().get(userUUID);
        }catch (IllegalArgumentException e){
            log.error(e.getMessage());
            return null;
        }
    }

    // 채팅방 전체 userlist 조회
    public ArrayList<String> getUserList(String roomId){
        ArrayList<String> list = new ArrayList<>();
        try{
            ChatRoom room = chatRepo.findById(roomId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 채팅방이 없습니다. id=" + roomId));

            // hashmap 을 for 문을 돌린 후
            // value 값만 뽑아내서 list 에 저장 후 reutrn
            room.getUserlist().forEach((key, value) -> list.add(value));
            return list;
        }catch(IllegalArgumentException e){
            log.error(e.getMessage());
            return null;
        }
    }

    public ChatRoom createChatRoom(String name) {
        ChatRoom room = new ChatRoom();
        room.setRoomId(UUID.randomUUID().toString());
        room.setRoomName(name);
        room.setUserCount(0);
        room.setUserlist(new HashMap<>());
        chatRepo.save(room);
        return room;
    }
}