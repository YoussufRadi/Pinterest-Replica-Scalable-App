package Models;

import com.arangodb.entity.DocumentEntity;
import com.arangodb.entity.VertexEntity;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.GenericGenerator;
import com.arangodb.entity.DocumentField;
import com.arangodb.entity.DocumentField.Type;

import java.util.*;
import javax.persistence.*;

@Entity
@Table(name = "users")
public class User{
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
            name="uuid",
            strategy="org.hibernate.id.UUIDGenerator"
    )
    @Column( columnDefinition = "uuid", updatable = false,unique =true,nullable = false)
    @DocumentField(Type.ID)
    private UUID id;

    @Column(name = "first_name",nullable = false)
    private String firstName;

    @Column(name = "last_name",nullable = false)
    private String lastName;

    @Column(name = "email",nullable = false,unique = false)
    private String email;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "gender" ,nullable = false)
    private boolean gender;

    @Column(name = "age" ,nullable = false)
    private int age;

    @Column(name = "username",nullable = false)
    private String username;

    private UserVertex userVertex;
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @PersistenceContext(type = PersistenceContextType.EXTENDED)

    @JoinTable(name="block",
            joinColumns=@JoinColumn(name="userId"),
            inverseJoinColumns=@JoinColumn(name="blockedId")
    )

    private Set<User> block;
    public Set<User> getBlock() {
        return block;
    }
    public void setBlock(Set<User> block) {
        this.block = block;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")


    @JoinTable(name="block",
            joinColumns=@JoinColumn(name="blockedId"),
            inverseJoinColumns=@JoinColumn(name="userId")
    )
    private Set<User> blockedBy;
    public Set<User> getBlockedBy() {
        return blockedBy;
    }
    public void setBlockedBy(Set<User> blockedBy) {
        this.blockedBy = blockedBy;
    }


    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")

    @JoinTable(name="following",
            joinColumns=@JoinColumn(name="userId"),
            inverseJoinColumns=@JoinColumn(name="followingId")
    )
    private Set<User> following;
    public Set<User> getFollowing() {
        return following;
    }
    public void setFollowing(Set<User> following) {
        this.following = following;
    }


    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")

    @JoinTable(name="following",
            joinColumns=@JoinColumn(name="followingId"),
            inverseJoinColumns=@JoinColumn(name="userId")
    )
    private Set<User> followedBy;
    public Set<User> getFollowedBy() {
        return followedBy;
    }
    public void setFollowedBy(Set<User> followedBy) {
        this.followedBy = followedBy;
    }




    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")

    @JoinTable(name="followers",
            joinColumns=@JoinColumn(name="userId"),
            inverseJoinColumns=@JoinColumn(name="followerId")
    )
    private Set<User> followers;
    public Set<User> getFollowers() { return followers;}

    public void setFollowers(Set<User> followers) { this.followers = followers; }



    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")

    @JoinTable(name="followers",
            joinColumns=@JoinColumn(name="followerId"),
            inverseJoinColumns=@JoinColumn(name="userId")
    )
    private Set<User> follow;
    public Set<User> getFollow() {return follow; }
    public void setFollow(Set<User> follow) { this.follow = follow; }




    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="Categories", joinColumns=@JoinColumn(name="user_id"))
    @Column(name="category")
    private Set<UUID> userCat;
    public Set<UUID> getUserCat() {
        return userCat;
    }
    public void setUserCat(Set<UUID> userCat) {
        this.userCat = userCat;
    }


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="liked_photos", joinColumns=@JoinColumn(name="user_id"))
    @Column(name="likedphoto_id")
    private Set<UUID> userLikedPhotos;
    public Set<UUID> getUserLikedPhotos(){ return userLikedPhotos; }
    public void setUserLikedPhotos(Set<UUID> userLikedPhotos) { this.userLikedPhotos = userLikedPhotos; }



    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="disliked_photos", joinColumns=@JoinColumn(name="user_id"))
    @Column(name="dislikedphoto_id")
    private Set<UUID> userDislikedPhotos;
    public Set<UUID> getUserDislikedPhotos() { return userDislikedPhotos; }
    public void setUserDislikedPhotos(Set<UUID> userDislikedPhotos) { this.userDislikedPhotos = userDislikedPhotos; }



    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="Pins", joinColumns=@JoinColumn(name="user_id"))
    @Column(name="pin_id")
    private Set<UUID> pinnedPosts;
    public Set<UUID> getPinnedPosts() { return pinnedPosts; }
    public void setPinnedPosts(Set<UUID> pinnedPosts) { this.pinnedPosts = pinnedPosts; }


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="Hashtags", joinColumns=@JoinColumn(name="user_id"))
    @Column(name="hashtag_id")
    private Set<UUID> hashtags;
    public Set<UUID> getHashtags() { return hashtags; }
    public void setHashtags(Set<UUID> hashtags) { this.hashtags = hashtags; }



    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="Boards", joinColumns=@JoinColumn(name="user_id"))
    @Column(name="board_id")
    private Set<UUID> boards;
    public Set<UUID> getBoards() { return boards; }
    public void setBoards(Set<UUID> boards) { this.boards = boards;}


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public UserVertex getUserVertex() {
        return userVertex;
    }

    public void setUserVertex(UserVertex userVertex) {
        this.userVertex = userVertex;
    }

    public User(){

    }
    public User( String firstName, String lastName, String username, String email, String password, boolean gender, int age) {


        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username=username;
        this.age=age;
        this.password = password;
        this.gender = gender;
    }



}

