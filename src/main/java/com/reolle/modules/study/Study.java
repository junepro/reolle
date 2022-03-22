package com.reolle.modules.study;

import com.reolle.modules.account.UserAccount;
import com.reolle.modules.account.Account;
import com.reolle.modules.tag.Tag;
import com.reolle.modules.zone.Zone;
import lombok.*;

import javax.persistence.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

//엔티티 조회시점에 연관된 엔티티들을 함께 조회하는 기능 담당
//@NamedEntityGraph(name = "Study.withAll", attributeNodes = {
//        @NamedAttributeNode("tags"),
//        @NamedAttributeNode("zones"),
//        @NamedAttributeNode("managers"),
//        @NamedAttributeNode("members")})
//@NamedEntityGraph(name = "Study.withTagsAndManagers", attributeNodes = {
//        @NamedAttributeNode("tags"),
//        @NamedAttributeNode("managers")})
//@NamedEntityGraph(name = "Study.withZonesAndManagers", attributeNodes = {
//        @NamedAttributeNode("zones"),
//        @NamedAttributeNode("managers")})
//@NamedEntityGraph(name = "Study.withManagers", attributeNodes = {
//        @NamedAttributeNode("managers")})
//@NamedEntityGraph(name = "Study.withMembers", attributeNodes = {
//        @NamedAttributeNode("members")})
//@NamedEntityGraph(name = "Study.withTagsAndZones", attributeNodes = {
//        @NamedAttributeNode("tags"),
//        @NamedAttributeNode("zones")})
//@NamedEntityGraph(name = "Study.withManagersAndMembers", attributeNodes = {
//        @NamedAttributeNode("managers"),
//        @NamedAttributeNode("members")})

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Study {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany
    private Set<Account> managers = new HashSet<>();

    @ManyToMany
    private Set<Account> members = new HashSet<>();

    @Column(unique = true)
    private String path;

    private String title;

    private String shortDescription;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String fullDescription;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String image;

    @ManyToMany
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany
    private Set<Zone> zones = new HashSet<>();

    private LocalDateTime publishedDateTime;

    private LocalDateTime closedDateTime;

    private LocalDateTime recruitingUpdatedDateTime;

    private boolean recruiting;

    private boolean published;

    private boolean closed;

    private boolean useBanner;

    public void addManager(Account account) {
        this.managers.add(account);
    }

    public boolean isJoinable(UserAccount userAccount) {
        Account account = userAccount.getAccount();
        return this.isPublished() && this.isRecruiting()
                && !this.members.contains(account) && !this.managers.contains(account);

    }

    public boolean isMember(UserAccount userAccount) {
        return this.members.contains(userAccount.getAccount());
    }

    public boolean isManager(UserAccount userAccount) {
        return this.managers.contains(userAccount.getAccount());
    }

    public String getImage() {
        return image != null ? image : "/images/default_banner.png";
    }

    public void publish() {
        if (!this.closed && !this.published) {
            this.published = true;
            this.publishedDateTime = LocalDateTime.now();
        } else {
            throw new RuntimeException("스터디를 공개 할 수 없습니다. 스터디를 이미 공개했거나 종료했습니다");
        }
    }

    public void close() {
        if (!this.closed && this.published) {
            this.closed = true;
            this.closedDateTime = LocalDateTime.now();
        } else {
            throw new RuntimeException("스터디를 종료 할 수 없습니다. 스터디를 공개하지 않았거나 이미 종료한 스터디입니다");
        }
    }

    public void startRecruit() {
        if (canUpdateRecruiting()) {
            this.recruiting = true;
            this.recruitingUpdatedDateTime = LocalDateTime.now();
        } else {
            throw new RuntimeException("인원 모집을 시작할 수 없습니다. 스터디를 공개하거나 한 시간 뒤 다시 시도하세요.");
        }
    }

    public void stopRecruit() {
        if (canUpdateRecruiting()) {
            this.recruiting = false;
            this.recruitingUpdatedDateTime = LocalDateTime.now();
        } else {
            throw new RuntimeException("인원 모집을 멈출 수 없습니다. 스터디를 공개하거나 한 시간 뒤 다시 시도하세요.");

        }
    }

    public boolean canUpdateRecruiting() {
        return this.published && this.recruitingUpdatedDateTime == null || this.recruitingUpdatedDateTime.isBefore(LocalDateTime.now().minusHours(1));
    }

    public boolean isRemovable() {
        return !this.published;
    }

    public void addMember(Account account) {
        this.getMembers().add(account);
    }

    public void removeMember(Account account) {
        this.getMembers().remove(account);
    }

    public String getEncodedPath() {
        return URLEncoder.encode(this.path, StandardCharsets.UTF_8);
    }

    public boolean isManagedBy(Account account) {
        return this.getManagers().contains(account);
    }
}
