package msa.pojo;

import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@REntity
public class UserLiveObject {

    @RId
    private String id;

    private String firstName;



    private String username;

    private int age;


    private String lastName;

    private String email;

    private String password;

    private boolean gender;

    private Set<User> block;
    public Set<User> getBlock() {
        return block;
    }
    public void setBlock(Set<User> block) {
        this.block = block;
    }


    private Set<User> blockedBy;
    public Set<User> getBlockedBy() {
        return blockedBy;
    }
    public void setBlockedBy(Set<User> blockedBy) {
        this.blockedBy = blockedBy;
    }



    private Set<User> following;
    public Set<User> getFollowing() {
        return following;
    }
    public void setFollowing(Set<User> following) {
        this.following = following;
    }



    private Set<User> followedBy;
    public Set<User> getFollowedBy() {
        return followedBy;
    }
    public void setFollowedBy(Set<User> followedBy) {
        this.followedBy = followedBy;
    }





    private Set<User> followers;
    public Set<User> getFollowers() { return followers;}

    public void setFollowers(Set<User> followers) { this.followers = followers; }




    private Set<User> follow;
    public Set<User> getFollow() {return follow; }
    public void setFollow(Set<User> follow) { this.follow = follow; }





    private Set<UUID> userCat;
    public Set<UUID> getUserCat() {
        return userCat;
    }
    public void setUserCat(Set<UUID> userCat) {
        this.userCat = userCat;
    }



    private Set<UUID> userLikedPhotos;
    public Set<UUID> getUserLikedPhotos(){ return userLikedPhotos; }
    public void setUserLikedPhotos(Set<UUID> userLikedPhotos) { this.userLikedPhotos = userLikedPhotos; }




    private Set<UUID> userDislikedPhotos;
    public Set<UUID> getUserDislikedPhotos() { return userDislikedPhotos; }
    public void setUserDislikedPhotos(Set<UUID> userDislikedPhotos) { this.userDislikedPhotos = userDislikedPhotos; }




    private Set<UUID> pinnedPosts;
    public Set<UUID> getPinnedPosts() { return pinnedPosts; }
    public void setPinnedPosts(Set<UUID> pinnedPosts) { this.pinnedPosts = pinnedPosts; }



    private Set<UUID> hashtags;
    public Set<UUID> getHashtags() { return hashtags; }
    public void setHashtags(Set<UUID> hashtags) { this.hashtags = hashtags; }


    private Set<UUID> boards;
    public Set<UUID> getBoards() { return boards; }
    public void setBoards(Set<UUID> boards) { this.boards = boards;}





    public String getId() {

        return id;
    }

    public void setId(String id) {
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

    public String getUsername() { return username;}

    public void setUsername(String username) { this.username = username; }

    public int getAge() { return age; }

    public void setAge(int age) { this.age = age; }

    public UserLiveObject(){

    }
    public UserLiveObject( String id,String firstName, String lastName, String username,String email, String password, boolean gender,int age) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username=username;
        this.age=age;
        this.email = email;
        this.password = password;
        this.gender = gender;
    }


}
