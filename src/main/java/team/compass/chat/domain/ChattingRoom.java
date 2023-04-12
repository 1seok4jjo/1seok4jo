package team.compass.chat.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class ChattingRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatting_room_id")
    private Long id;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "chattingRoom")
    private List<ChattingRoomUser> chattingRoomUsers;


    @OneToMany(mappedBy = "chattingRoom")
    private List<ChatMessage> chatMessages;
}
