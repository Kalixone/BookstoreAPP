package mate.academy.springbootintro.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@SQLDelete(sql = "UPDATE orders SET is_deleted = true WHERE id=?")
@SQLRestriction(value = "is_deleted = FALSE")
@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "id")
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @Column(name = "status", nullable = false)
    private Status status;
    @Column(nullable = false)
    private BigDecimal total;
    @CreationTimestamp
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;
    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;
    @Column(name = "order_id", nullable = false)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems = new HashSet<>();
    @Column(nullable = false)
    private boolean isDeleted = false;

    public enum Status {
        COMPLETED,
        PENDING,
        DELIVERED,
        CANCELLED,
        PROCESSING
    }
}
