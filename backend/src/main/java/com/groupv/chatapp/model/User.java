package com.groupv.chatapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;



@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User implements UserDetails {

    @Id
    @NotEmpty(message = "username cannot be empty")
    private String username;

    @Column
//    @NotEmpty(message = "name cannot be empty")
    private String name;

    @Column(
            nullable = false,
            updatable = false,
            unique = true
    )
    @NotEmpty(message = "email cannot be empty")
    private String email;

    @Column(
            nullable = false
    )
    @NotEmpty(message = "password cannot be empty")
    @JsonIgnore
    private String password;


    @Column
    private String bio;

    @Column(
            nullable = false
    )
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "userProfileImg"
    )
    @JsonIgnore
    private Content profileImg; // foreign key of image table

    @OneToMany(
            mappedBy = "creator")
    @JsonManagedReference("creator")
    private List<Group> creator;

    @OneToMany(
            mappedBy = "user1",cascade = CascadeType.ALL, orphanRemoval = true
    )
    @JsonManagedReference("sender")
    @JsonIgnore
    private List<ChatRoom> user1;

    @OneToMany(
            mappedBy = "user2",cascade = CascadeType.ALL, orphanRemoval = true
    )
    @JsonManagedReference("receiver")
    @JsonIgnore
    private List<ChatRoom> user2;

    @OneToMany(
            mappedBy = "sender"
    )
    @JsonManagedReference("sender-chat")
//    @JsonIgnore
    private List<ChatData> chatSenderData;

    @OneToMany(
            mappedBy = "receiver"
    )
    @JsonManagedReference("receiver-chat")
    private List<ChatData> chatRecieverData;

    @OneToMany(mappedBy = "sender")
    @JsonBackReference("group-sender")
    private List<GroupChatData> senderGroupChatData;

    @OneToMany(
            mappedBy = "username",cascade = CascadeType.ALL, orphanRemoval = true
    )
    @JsonManagedReference("participant")
    private List<GroupParticipant> groupParticipants;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<>();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // add validator
}
