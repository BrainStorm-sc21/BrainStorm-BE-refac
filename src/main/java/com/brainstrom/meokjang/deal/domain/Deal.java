package com.brainstrom.meokjang.deal.domain;

import com.brainstrom.meokjang.chat.domain.ChatRoom;
import com.brainstrom.meokjang.review.domain.Review;
import com.brainstrom.meokjang.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DEAL")
@Getter
@NoArgsConstructor
public class Deal {

    @Id @Column(name = "deal_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dealId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @Column(name = "deal_type", nullable = false)
    private Byte dealType;

    @Column(name = "deal_name", length = 30, nullable = false)
    private String dealName;

    @Column(name = "deal_content", length = 1000, nullable = false)
    private String dealContent;

    @Column(name = "location", length = 40, nullable = false)
    private String location;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "is_closed", nullable = false)
    private Boolean isClosed;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "deal", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoom> chatRoomList = new ArrayList<>();

    @OneToMany(mappedBy = "deal")
    private List<DealImage> dealImageList = new ArrayList<>();

    @OneToMany(mappedBy = "deal")
    private List<Review> reviewList = new ArrayList<>();

    @Builder
    public Deal(User user, Byte dealType, String dealName, String dealContent, String location,
                Double latitude, Double longitude, Boolean isClosed, Boolean isDeleted) {
        this.user = user;
        this.dealType = dealType;
        this.dealName = dealName;
        this.dealContent = dealContent;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isClosed = isClosed;
        this.isDeleted = isDeleted;
    }

//    public void update(Byte dealType, String dealName, String dealContent, String[] imageList) {
//        this.dealType = dealType;
//        this.dealName = dealName;
//        this.dealContent = dealContent;
//        this.image1 = imageList[0];
//        this.image2 = imageList[1];
//        this.image3 = imageList[2];
//        this.image4 = imageList[3];
//    }

    public void complete() {
        this.isClosed = true;
    }

    public void delete() {
        this.isDeleted = true;
    }

//    public String[] getImageList() {
//        return new String[]{image1, image2, image3, image4};
//    }
}
