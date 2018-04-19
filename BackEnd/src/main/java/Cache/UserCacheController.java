package Cache;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import Models.User;
import Models.UserLiveObject;
import Database.DatabaseController;
import Cache.RedisConf;
import org.redisson.api.RBucket;
import org.redisson.api.RLiveObjectService;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class UserCacheController {

    private DatabaseController dbcont;
    private RedisConf redisConf;
    private RLiveObjectService service;


    public  UserCacheController() throws IOException {

        //initializations no big issue
        dbcont = new DatabaseController();
        redisConf = new RedisConf();
        service = redisConf.getService();

    }

    public UUID addUser(String fname, String lname,
                        String username, String email, String password,
                        boolean gender, int age) {

        //call the method from the database
        UUID id = dbcont.addUser(fname, lname, username, email, password, gender, age);
        if (id == null)
            return null;
        try {
            //make a live object which is explained in the UserLiveObject.class
            UserLiveObject userLive = new UserLiveObject(id.toString(),
                    fname, lname, username, email, password, gender, age);
            userLive.setId(id.toString());
            //save it in Cache cache
            userLive = service.persist(userLive);

            //save email - id pair in Cache also
            //by creating a bucket which is a class that wraps the string object
            //to store it in Cache
            // the email in redisConf.getClient().getBucket(email); is the id of the Cache object


            RBucket<String> bucket = redisConf.getClient().getBucket(email);
            // set the value to the user id
            bucket.set(id.toString());

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        }
        return id;

        //we store email -id pair on Cache to ease for us searching in Cache
        //using the email also not only the uuid this will be used in the sign in and getUserEmail
        //methods


    }

    public Set<UUID> getBoards(UUID userID) {

        try {
            if (service.get(UserLiveObject.class, userID.toString()) != null) {
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                return user.getBoards();


            } else {
                return dbcont.getBoards(userID);

            }
        }
        catch(Exception e) {
                e.printStackTrace();
                return null;

            }


    }

    public Set<UUID> getCategories(UUID userID) {

        try {
            if (service.get(UserLiveObject.class, userID.toString()) != null) {
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                return user.getUserCat();


            } else {
                return dbcont.getCategories(userID);

            }
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;

        }


    }

    public Set<UUID> getPins(UUID userID) {

        try {
            if (service.get(UserLiveObject.class, userID.toString()) != null) {
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                return user.getPinnedPosts();


            } else {
                return dbcont.getPins(userID);

            }
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;

        }


    }

    public Set<User> getFollowedUsers(UUID userID) {

        try {

            if (service.get(UserLiveObject.class, userID.toString()) != null) {
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());

                return user.getFollow();


            } else {
                System.out.println(dbcont.getFollowedUsers(userID));
                return dbcont.getFollowedUsers(userID);

            }
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;

        }


    }

    public Set<User> getFollowers(UUID userID) {

        try {

            if (service.get(UserLiveObject.class, userID.toString()) != null) {
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());

                return user.getFollowedBy();


            } else {

                return dbcont.getFollowersUsers(userID);

            }
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;

        }


    }



    public boolean updateUser(UUID userID,
                           String firstName, String lastname, String password,
                           String username, int age, boolean gender) {

        //we update the user in the database easy
       boolean res= dbcont.updateUser(userID, firstName, lastname, password, username, age, gender);

        //we check if the user is in the cache we update him
        try {
            if (service.get(UserLiveObject.class, userID.toString()) != null) {


                // we make a live object and update the fields no need to save anything
                //its a live object so it updates in a live matter ya salma w ya youmna
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                user.setFirstName(firstName);
                user.setLastName(lastname);
                user.setPassword(password);
                user.setUsername(username);
                user.setAge(age);
                user.setGender(gender);
                return res;

            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean addBoard(UUID userID, UUID boardID) {

        // add a board in the db by using the normal method
        boolean flag = dbcont.addBoard(userID, boardID);


        // if the user is in the cache then update his boards also !!
        try {
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                //get the boards
                Set boards = user.getBoards();
                //add a board to it
                boards.add(boardID);
                return flag;

            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return flag;
    }

    public boolean removeBoard(UUID userID, UUID boardID) {

        // add a board in the db by using the normal method
        boolean flag = dbcont.removeBoard(userID, boardID);


        // if the user is in the cache then update his boards also !!
        try {
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                //get the boards
                Set boards = user.getBoards();
                //add a board to it
                boards.remove(boardID);
                return flag;


            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        System.out.println(service.get(UserLiveObject.class,
                userID.toString()).getBoards().toString());

        return flag;
    }

    public boolean followCategories(UUID userID, UUID categoryID) {

        // add a board in the db by using the normal method
        boolean flag = dbcont.followCategories(userID, categoryID);

        try {
            // if the user is in the cache then update his boards also !!
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                //get the boards
                Set categories = user.getUserCat();
                //add a board to it
                categories.add(categoryID);
                return flag;


            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return flag;

    }

    public boolean unfollowCategories(UUID userID, UUID categoryID) {

        // add a board in the db by using the normal method
        boolean flag = dbcont.unfollowCategories(userID, categoryID);

        try {
            // if the user is in the cache then update his boards also !!
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                //get the boards
                Set categories = user.getUserCat();
                //add a board to it
                categories.remove(categoryID);
                return flag;


            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return flag;

    }

    public boolean addPin(UUID userID, UUID pinID) {

        // add a board in the db by using the normal method
        boolean flag = dbcont.addPin(userID, pinID);

        try {
            // if the user is in the cache then update his boards also !!
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                //get the boards
                Set pins = user.getPinnedPosts();
                //add a board to it
                pins.add(pinID);
                return flag;


            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return flag;


    }

    public boolean removePin(UUID userID, UUID pinID) {

        // add a board in the db by using the normal method
        boolean flag = dbcont.removePin(userID, pinID);


        // if the user is in the cache then update his boards also !!
        try {
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                //get the boards
                Set pins = user.getPinnedPosts();
                //add a board to it
                pins.remove(pinID);
                return flag;


            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return flag;


    }

    public boolean followHashtags(UUID userID, UUID hashtagID) {

        // add a board in the db by using the normal method
        boolean flag = dbcont.followHashtag(userID, hashtagID);


        // if the user is in the cache then update his boards also !!
        try {
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                //get the boards
                Set hashtags = user.getHashtags();
                //add a board to it
                hashtags.add(hashtagID);
                return flag;


            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return flag;

    }

    public boolean unfollowHashtags(UUID userID, UUID hashtagID) {

        // add a board in the db by using the normal method
        boolean flag = dbcont.unfollowHashtag(userID, hashtagID);


        // if the user is in the cache then update his boards also !!
        try {
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                //get the boards
                Set hashtags = user.getHashtags();
                //add a board to it
                hashtags.remove(hashtagID);
                return flag;


            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return flag;


    }

    public boolean likePhotos(UUID userID, UUID likedPhotoID) {

        // add a board in the db by using the normal method
        boolean state = dbcont.likePhotos(userID, likedPhotoID);


        // if the user is in the cache then update his boards also !!
        try {
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                //get the boards
                Set likesPhotos = user.getUserLikedPhotos();
                //add a board to it
                likesPhotos.add(likedPhotoID);
                return state;


            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


        return false;
    }

    public boolean unlikePhotos(UUID userID, UUID unlikedPhotoID) {

        // add a board in the db by using the normal method
        boolean flag = dbcont.unlikePhotos(userID, unlikedPhotoID);


        // if the user is in the cache then update his boards also !!
        try {
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                //get the boards
                Set unlikedPhotos = user.getUserLikedPhotos();
                //add a board to it
                unlikedPhotos.remove(unlikedPhotoID);
                return flag;


            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


        return flag;
    }

    public boolean dislikePhotos(UUID userID, UUID dislikedPhotoID) {

        // add a board in the db by using the normal method
        boolean flag = dbcont.dislikePhotos(userID, dislikedPhotoID);


        // if the user is in the cache then update his boards also !!
        try {
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                //get the boards
                Set dislikedPhotos = user.getUserDislikedPhotos();
                //add a board to it
                dislikedPhotos.add(dislikedPhotoID);
                return flag;


            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return flag;


    }

    public boolean undislikePhotos(UUID userID, UUID UndislikedPhotoID) {

        // add a board in the db by using the normal method
        boolean flag = dbcont.undislikePhotos(userID, UndislikedPhotoID);


        // if the user is in the cache then update his boards also !!
        try {
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                //get the boards
                Set undislikedPhotos = user.getUserDislikedPhotos();
                //add a board to it
                undislikedPhotos.remove(UndislikedPhotoID);
                return flag;

            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


        return flag;

    }

    public boolean blockUser(UUID userID, UUID blocked) {

        // add a board in the db by using the normal method
        boolean flag = dbcont.blockUser(userID, blocked);


        // if the user is in the cache then update his boards also !!
        try {
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                //get the boards
                Set blockedUsers = user.getBlock();
                //add a board to it
                blockedUsers.add(blocked);
                return flag;


            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


        return flag;
    }

    public boolean UnblockUser(UUID userID, UUID unBlocked) {

        // add a board in the db by using the normal method
        boolean flag = dbcont.unblockUser(userID, unBlocked);


        // if the user is in the cache then update his boards also !!
        try {
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                //get the boards
                Set blockedUsers = user.getBlock();
                //add a board to it
                blockedUsers.remove(unBlocked);
                return flag;


            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return flag;


    }

    public boolean followUser(UUID userID, UUID followingId) {

        // add a board in the db by using the normal method
            boolean flag = dbcont.followUser(userID, followingId);
         System.out.println(flag +"Follow UserModel");

        // if the user is in the cache then update his boards also !!
        try {
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                UserLiveObject userFollowed = service.get(
                        UserLiveObject.class, followingId.toString());

                //get the boards
                Set followingUsers = user.getFollowing();
                Set followedUsers = userFollowed.getFollowedBy();

                //add a board to it
                followingUsers.add(followingId);
                followedUsers.add(userID);
                return flag;


            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


        return flag;

    }

    public boolean unfollowUser(UUID userID, UUID followingId) {

        // add a board in the db by using the normal method
        boolean flag = dbcont.unfollowUser(userID, followingId);


        // if the user is in the cache then update his boards also !!
        try {
            if (service.get(UserLiveObject.class, userID.toString()) != null) {

                //same as the above
                UserLiveObject user = service.get(UserLiveObject.class, userID.toString());
                UserLiveObject userFollowed = service.get(UserLiveObject.class,
                        followingId.toString());

                //get the boards
                Set unfollowingUsers = user.getFollowing();
                Set followedUsers = userFollowed.getFollowedBy();

                //add a board to it
                unfollowingUsers.remove(followingId);
                followedUsers.remove(userID);
                return flag;

            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return flag;


    }

    public User signIn(String email, String password) {


        try {
            User user = dbcont.signIn(email, password);

            return user;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }
          /*  RBucket<String> bucket = redisConf.getClient().getBucket(email);
            if (bucket.get() != null) {

                if (service.get(UserLiveObject.class, bucket.get()) != null) {
                    UserLiveObject userLiveObject = service.get(UserLiveObject.class, bucket.get());
                    if (email.equals(userLiveObject.getEmail()) &&
                            password.equals(userLiveObject.getPassword())) {

                        System.out.println("1111111111111111111111");


                        return userLiveObject;
                    }
                } else {
                    UserModel user = dbcont.signIn(email, password);
                    System.out.println("22222222222222222222");

                    if (user == null)
                        return null;

                    String message = new Gson().toJson(user);
                    Gson gson = new GsonBuilder().create();
                    UserLiveObject userLive = gson.fromJson(message, UserLiveObject.class);
                    userLive = service.persist(userLive);
                    System.out.println("3333333333333333");


                }

            } else {

                UserModel user = dbcont.signIn(email, password);

                System.out.println("444444444444444");


                if (user == null)
                    return null;

                System.out.println("after null");

                if (service.get(UserLiveObject.class, user.getId().toString()) != null) {

                    System.out.println("5555555555555");


                    RBucket<String> bucket2 = redisConf.getClient().getBucket(email);
                    bucket2.set(user.getId().toString());

                    String message = new Gson().toJson(user);
                    Gson gson = new GsonBuilder().create();
                    UserLiveObject userLive = gson.fromJson(message, UserLiveObject.class);

                    return userLive;
                } else {

                    System.out.println(user.getId());
                    String json = new Gson().toJson(user);
                    System.out.println(json);
                    System.out.println("66666666666666");

                  *//*  Gson gson = new GsonBuilder().create();
                    UserLiveObject userLive = gson.fromJson(message, UserLiveObject.class);
                    userLive = service.persist(userLive);
                    System.out.println(userLive.getId() + "elsaya");

                    RBucket<String> bucket2 = redisConf.getClient().getBucket(email);
                    bucket2.set(user.getId().toString());
                    return userLive;*//*
                  return null;*/
//
//                }
//
//
//        } catch (Exception e) {
//        }
//        return null;
//    }










   /* public UserLiveObject getUserByEmail (String email ){

        RBucket<String> bucket = redisConf.getClient().getBucket(email);

        boolean emailIdInRedis = bucket.get()!=null;
        boolean objectInRedis = service.get(UserLiveObject.class, userId.toString())!=null;



        //check if the email - id pair are in Cache
        if(emailIdInRedis){

            //get the id from the email -id pair
            String userId = bucket.get();

            //check if the user is in Cache
            if(service.get(UserLiveObject.class, userId.toString())!=null) {
                return service.get(UserLiveObject.class, userId.toString());
            }
            //not in Cache
            //get him and puut him in Cache
            UserModel user = dbcont.getUserByEmail(email);
            String message = new Gson().toJson(user);
            Gson gson = new GsonBuilder().create();
            UserLiveObject userLive = gson.fromJson(message, UserLiveObject.class);
            userLive = service.persist(userLive);
            return userLive;


        }

        // you didnt find the email - id pair then
        //add the user to Cache
        //add the email - id pair to Cache
        //easy

        UserModel user = dbcont.getUserByEmail(email);
        String message = new Gson().toJson(user);
        Gson gson = new GsonBuilder().create();
        UserLiveObject userLive = gson.fromJson(message, UserLiveObject.class);
        userLive = service.persist(userLive);
        bucket.set(userLive.getId());
        return userLive;

    }

*/




    // gets the user with all collections (Sets)

    public UserLiveObject getUserWithProfileById(UUID userId) {

        if (service.get(UserLiveObject.class, userId.toString()) != null) {
            return service.get(UserLiveObject.class, userId.toString());
        }

        User user = dbcont.getUserWithProfileById(userId);
        String message = new Gson().toJson(user);
        System.out.println("not in cache");
        Gson gson = new GsonBuilder().create();
        UserLiveObject userLive = gson.fromJson(message, UserLiveObject.class);

        userLive = service.persist(userLive);

        return userLive;


    }
}
