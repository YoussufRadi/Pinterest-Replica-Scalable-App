package msa.pojo;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.util.*;
import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
            name="uuid",
            strategy="org.hibernate.id.UUIDGenerator"

    )
    @Column( columnDefinition = "uuid", updatable = false,unique =true,nullable = false)
    private UUID id;

    @Column(name = "first_name",nullable = false)
    private String firstName;

    @Column(name = "last_name",nullable = false)
    private String lastName;

    @Column(name = "email",nullable = false,unique = true)
    private String email;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "gender" ,nullable = false)
    private boolean gender;







    @ManyToMany
    @JoinTable(name="block",
            joinColumns=@JoinColumn(name="userId"),
            inverseJoinColumns=@JoinColumn(name="blockedId")
    )
    private List<User> block;
    public List<User> getBlock() {
        return block;
    }
    public void setBlock(List<User> block) {
        this.block = block;
    }

    @ManyToMany
    @JoinTable(name="block",
            joinColumns=@JoinColumn(name="blockedId"),
            inverseJoinColumns=@JoinColumn(name="userId")
    )
    private List<User> blockedBy;
    public List<User> getBlockedBy() {
        return blockedBy;
    }
    public void setBlockedBy(List<User> blockedBy) {
        this.blockedBy = blockedBy;
    }



    @ManyToMany
    @JoinTable(name="following",
            joinColumns=@JoinColumn(name="userId"),
            inverseJoinColumns=@JoinColumn(name="followingId")
    )
    private List<User> following;
    public List<User> getFollowing() {
        return following;
    }
    public void setFollowing(List<User> following) {
        this.following = following;
    }


    @ManyToMany
    @JoinTable(name="following",
            joinColumns=@JoinColumn(name="followingId"),
            inverseJoinColumns=@JoinColumn(name="userId")
    )
    private List<User> followedBy;
    public List<User> getFollowedBy() {
        return followedBy;
    }
    public void setFollowedBy(List<User> followedBy) {
        this.followedBy = followedBy;
    }




    @ManyToMany
    @JoinTable(name="followers",
            joinColumns=@JoinColumn(name="userId"),
            inverseJoinColumns=@JoinColumn(name="followerId")
    )
    private List<User> followers;
    public List<User> getFollowers() { return followers;}

    public void setFollowers(List<User> followers) { this.followers = followers; }



    @ManyToMany
    @JoinTable(name="followers",
            joinColumns=@JoinColumn(name="followerId"),
            inverseJoinColumns=@JoinColumn(name="userId")
    )
    private List<User> follow;
    public List<User> getFollow() {return follow; }
    public void setFollow(List<User> follow) { this.follow = follow; }




    @ElementCollection
    @CollectionTable(name="Categories", joinColumns=@JoinColumn(name="user_id"))
    @Column(name="category")
    private List<UUID> userCat;
    public List<UUID> getUserCat() {
        return userCat;
    }
    public void setUserCat(List<UUID> userCat) {
        this.userCat = userCat;
    }




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

    public User(){

    }
    public User( String firstName, String lastName, String email, String password, boolean gender) {


        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.gender = gender;
    }



}
