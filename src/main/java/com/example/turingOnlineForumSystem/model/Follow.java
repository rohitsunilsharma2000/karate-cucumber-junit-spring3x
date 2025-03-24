package com.example.turingOnlineForumSystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "follow")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
//    private User follower;

    @ManyToOne
    private User following;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    @OnDelete(action = OnDeleteAction.CASCADE) // Hibernate-specific
    private User follower;


}

