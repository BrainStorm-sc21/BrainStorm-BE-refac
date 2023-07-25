package com.brainstrom.meokjang.food.domain;

import com.brainstrom.meokjang.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(schema = "FOOD")
@Getter
@NoArgsConstructor
public class Food {

    @Id @Column(name = "food_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @Column(name = "food_name", length = 30, nullable = false)
    private String foodName;

    @Column(name = "stock", nullable = false)
    private Float stock;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "expire_date", nullable = false)
    private LocalDate expireDate;

    @Column(name = "storage_way", nullable = false)
    private Byte storageWay;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Food(User user, String foodName, Float stock, String expireDate, Byte storageWay) {
        this.user = user;
        this.foodName = foodName;
        this.stock = stock;
        this.expireDate = LocalDate.parse(expireDate);
        this.storageWay = storageWay;
        this.createdAt = LocalDateTime.now();
    }
}
