package com.web.warehorses_secure_persistence.domain.activity.entity;

import com.web.warehorses_secure_persistence.domain.client.dto.ClientResponseDto;
import com.web.warehorses_secure_persistence.domain.client.entity.Client;
import com.web.warehorses_secure_persistence.domain.item.dto.ItemResponseDto;
import com.web.warehorses_secure_persistence.domain.item.entity.Item;
import com.web.warehorses_secure_persistence.domain.user.dto.UserDetailsDto;
import com.web.warehorses_secure_persistence.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "activities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id", updatable = false, nullable = false)
    private long activityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TxnType type;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "activities_items",
            joinColumns = @JoinColumn(name = "activity_id", referencedColumnName = "activity_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id", referencedColumnName = "item_id"))
    private List<Item> items;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "owner_user_id")
    private User owner;

    @Nullable
    @Enumerated(EnumType.STRING)
    @Column(name = "evidence_type")
    private EvidenceType evidenceType;

    @Nullable
    @Column(name = "evidence_url")
    private String evidenceUrl;


    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_at_iso", nullable = false, updatable = false)
    private String createdAtIso;

    public Activity(TxnType type, User owner) {
        this.type=type;
        this.owner = owner;
    }

    public Activity(Activity.TxnType type,
                    List<Item> items,
                    Client client,
                    boolean isActive,
                    User owner,
                    @Nullable Activity.EvidenceType evidenceType,
                    @Nullable String evidenceUrl) {
        this.type=type;
        this.items = items;
        this.client = client;
        this.isActive = false;
        this.owner = owner;
        this.evidenceType=evidenceType;
        this.evidenceUrl=evidenceUrl;
        this.createdAt = LocalDateTime.now();
        this.createdAtIso = this.createdAt.toString();
    }

    public enum TxnType {
        STOCK("STOCK"),
        SALE("SALE"),
        WAREHOUSE("WAREHOUSE");

        final String name;

        TxnType(String name) {
            this.name = name;
        }
    }

    public enum EvidenceType {
        IMAGE("img"),
        PDF("pdf");

        final String name;

        EvidenceType(String name) {
            this.name = name;
        }
    }

}