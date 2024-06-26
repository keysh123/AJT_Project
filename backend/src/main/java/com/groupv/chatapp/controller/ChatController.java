package com.groupv.chatapp.controller;

import com.groupv.chatapp.dto.ChatDto;
import com.groupv.chatapp.dto.GroupChatDto;
import com.groupv.chatapp.dto.SuccessDto;
import com.groupv.chatapp.exception.DoesNotExistException;
import com.groupv.chatapp.model.ChatData;
import com.groupv.chatapp.model.GroupChatData;
import com.groupv.chatapp.model.GroupParticipant;
import com.groupv.chatapp.repository.ChatDataRepository;
import com.groupv.chatapp.repository.GroupParticipantRepository;
import com.groupv.chatapp.service.ChatDataService;
import com.groupv.chatapp.service.ContentService;
import com.groupv.chatapp.service.GroupChatDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;


import java.util.List;

@Controller
@CrossOrigin(originPatterns = "**",allowCredentials = "true")
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatDataService chatDataService;
    private final GroupChatDataService groupChatDataService;
    private final GroupParticipantRepository groupParticipantRepository;
    private final ContentService contentService;
    private final ChatDataRepository chatDataRepository;

    @MessageMapping("/chat")
    public void oneToOne(
            @Payload ChatDto chat
    ) throws DoesNotExistException {
        System.out.println(chat);
//        System.out.println(user);
        ChatData savedChat = chatDataService.saveChatData(chat);
        System.out.println(savedChat.getSender().getUsername()+" - "+savedChat.getReceiver().getUsername()+" - "+savedChat.getTime());
        ChatDto send;
        if(chat.getContent()==null){
            send = new ChatDto(savedChat);
        }else {
            send = new ChatDto(savedChat,savedChat.getContent());
        }

        messagingTemplate.convertAndSendToUser(
                    savedChat.getReceiver().getUsername(),
                    "/queue/message",
                    send
        );

        messagingTemplate.convertAndSendToUser(
                savedChat.getSender().getUsername(),
                "/queue/message",
                send
        );

    }

    @MessageMapping("/group")
    public void groupChat(
            @Payload GroupChatDto chat
    ) throws DoesNotExistException {
        System.out.println(chat);
//        System.out.println(user);
        GroupChatData savedChat = groupChatDataService.saveChat(chat);
        System.out.println(savedChat.getSender().getUsername()+savedChat.getTime());
        List<GroupParticipant> participants = groupParticipantRepository.findByGroupId(savedChat.getGroupId());
        for (GroupParticipant p: participants) {
            messagingTemplate.convertAndSendToUser(
                    p.getUsername().getUsername(),
                    "/queue/message",
                    new GroupChatDto(savedChat)
            );
        }
    }


    @GetMapping("/chat-room/chat/{id}")
    public ResponseEntity<?> sendChats(
            @PathVariable Integer id,
            @RequestAttribute String username
    ){
        List<ChatDto> chatData = chatDataRepository.getAllChatsByChatRoomId(id);
        return new ResponseEntity<>(new SuccessDto(HttpStatus.OK.value(),chatData),HttpStatus.OK);
    }

//    @PostMapping("/upload")
//    public ResponseEntity<?> uploadImg(@RequestParam("file") MultipartFile file) throws IOException {
//        System.out.println(file.getOriginalFilename());
//        System.out.println("sdsssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss"+file);
//
//        int id = contentService.saveContent(new Content(file.getOriginalFilename(),file.getContentType(), file.getBytes()));
//
//        return new Resp   onseEntity<>(new SuccessDto(HttpStatus.OK.value(),id),HttpStatus.OK);
//    }
}

