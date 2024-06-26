package com.groupv.chatapp.service;

import com.groupv.chatapp.dto.GroupParticipantDto;
import com.groupv.chatapp.model.*;
import com.groupv.chatapp.repository.GroupParticipantRepository;
import com.groupv.chatapp.repository.GroupRepository;
import com.groupv.chatapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupParticipantService {
    private final GroupParticipantRepository groupParticipantRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    public void saveParticipants(GroupParticipantDto groupParticipant){
        GroupParticipant participant;
        Integer groupId= groupParticipant.getGroupId();
        List<String> username=groupParticipant.getUsername();
        for (String s : username) {
            participant = GroupParticipant.builder()
                    .groupId(groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Group Not Found")))
                    .username(userRepository.findById(s).orElseThrow(() -> new IllegalArgumentException("User:"+username+" Not Found")))
                    .role(GroupRole.PARTICIPANT)
                    .joinedAt(LocalDateTime.now())
                    .build();

            groupParticipantRepository.save(participant);
        }

    }


    public List<GroupParticipant> findParticipant(Integer groupId){
        return groupParticipantRepository.findByGroupId(groupRepository.findById(groupId).orElseThrow(()-> new IllegalArgumentException("Group Not Found")));
    }

    public GroupParticipant promoteParticipant(Integer groupId,String username){
        GroupUserComposite groupUserComposite=GroupUserComposite.builder()
                .groupId(groupRepository.findById(groupId).orElseThrow(()-> new IllegalArgumentException("Group Not Found")))
                .username(userRepository.findById(username).orElseThrow(()-> new IllegalArgumentException("User Not Found")))
                .build();
        GroupParticipant groupParticipant=groupParticipantRepository.findById(groupUserComposite).orElseThrow(()-> new IllegalArgumentException("Participant not found"));
        groupParticipant.setRole(GroupRole.ADMIN);
        return groupParticipantRepository.save(groupParticipant);
    }
    public GroupParticipant DemoteParticipant(Integer groupId,String username){
        GroupUserComposite groupUserComposite=GroupUserComposite.builder()
                .groupId(groupRepository.findById(groupId).orElseThrow(()-> new IllegalArgumentException("Group Not Found")))
                .username(userRepository.findById(username).orElseThrow(()-> new IllegalArgumentException("User Not Found")))
                .build();
        GroupParticipant groupParticipant=groupParticipantRepository.findById(groupUserComposite).orElseThrow(()-> new IllegalArgumentException("Participant not found"));
        groupParticipant.setRole(GroupRole.PARTICIPANT);
        return groupParticipantRepository.save(groupParticipant);
    }
}
