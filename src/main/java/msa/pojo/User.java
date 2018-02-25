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
    @Column( columnDefinition = "uuid", updatable = false,unique =true )
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "gender")
    private boolean gender;

    public List<User> getBlock() {
        return block;
    }



    public void setBlock(List<User> block) {
        this.block = block;
    }

    public List<User> getBlockedBy() {
        return blockedBy;
    }

    public void setBlockedBy(List<User> blockedBy) {
        this.blockedBy = blockedBy;
    }

    @ManyToMany
    @JoinTable(name="block",
            joinColumns=@JoinColumn(name="userId"),
            inverseJoinColumns=@JoinColumn(name="blockedId")
    )
    private List<User> block;

    @ManyToMany
    @JoinTable(name="block",
            joinColumns=@JoinColumn(name="blockedId"),
            inverseJoinColumns=@JoinColumn(name="userId")
    )
    private List<User> blockedBy;


    public List<User> getFollowing() {
        return following;
    }

    public void setFollowing(List<User> following) {
        this.following = following;
    }



    @ManyToMany
    @JoinTable(name="following",
            joinColumns=@JoinColumn(name="userId"),
            inverseJoinColumns=@JoinColumn(name="followingId")
    )
    private List<User> following;


    public List<User> getFollowedBy() {
        return followedBy;
    }

    public void setFollowedBy(List<User> followedBy) {
        this.followedBy = followedBy;
    }

    @ManyToMany
    @JoinTable(name="following",
            joinColumns=@JoinColumn(name="followingId"),
            inverseJoinColumns=@JoinColumn(name="userId")
    )
    private List<User> followedBy;


    @ManyToMany
    @JoinTable(name="followers",
            joinColumns=@JoinColumn(name="userId"),
            inverseJoinColumns=@JoinColumn(name="followerId")
    )
    private List<User> followers;



    @ManyToMany
    @JoinTable(name="followers",
            joinColumns=@JoinColumn(name="followerId"),
            inverseJoinColumns=@JoinColumn(name="userId")
    )
    private List<User> follow;

    public List<Category> getUserCat() {
        return userCat;
    }

    public void setUserCat(List<Category> userCat) {
        this.userCat = userCat;
    }


    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "User_Category",
            joinColumns = { @JoinColumn(name = "userId") },
            inverseJoinColumns = { @JoinColumn(name = "categoryId") }
    )
    private List<Category> userCat;




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

