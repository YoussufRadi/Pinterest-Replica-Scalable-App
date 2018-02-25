 CREATE FUNCTION signUp(gender boolean, email character varying(100),username character varying(50),pass character varying(128),country character varying(50),firstname character varying(100),lastname character varying(100))   
    RETURNS void AS $$
    BEGIN
      INSERT INTO "user"."Users" (gender, email, user_name, pass, country, first_name, last_name) VALUES (gender,email,username,pass,country,firstname,lastname);
    END;
    $$ LANGUAGE plpgsql;
    

  CREATE FUNCTION blockUser(blocking bigint, blockee bigint)
    RETURNS void AS $$
    BEGIN
      INSERT INTO "user"."Blocks" ( user_blocking_id, user_blocked_id) VALUES (blocking,blockee);
    END;
    $$ LANGUAGE plpgsql;
    
 CREATE FUNCTION editUser(userId bigint,gen boolean,username character varying(50),coun character varying(50),firstname character varying(100),lastname character varying(100))   
    RETURNS void AS $$
    BEGIN
    	UPDATE "user"."Users"
		SET gender=gen,user_name=username, country=coun, first_name=firstname, last_name=lastname
		WHERE "user"."Users".user_id = userId;
    END;
    $$ LANGUAGE plpgsql;
    
  CREATE FUNCTION likePhoto(photo_id bigint, userId bigint)   
    RETURNS void AS $$
    BEGIN
    	UPDATE "user"."Users" SET liked_images_id = array_append(liked_images_id, photo_id)
        WHERE "user"."Users".user_id=userId;
    END;
    $$ LANGUAGE plpgsql;
    
       
  CREATE FUNCTION dislikePhoto(photo_id bigint, userId bigint)   
    RETURNS void AS $$
    BEGIN
    	UPDATE "user"."Users" SET disliked_images_id = array_append(disliked_images_id, photo_id)
        WHERE "user"."Users".user_id=userId;
    END;
    $$ LANGUAGE plpgsql;
    
       
  CREATE FUNCTION unlikePhoto(photo_id bigint, userId bigint)   
    RETURNS void AS $$
    BEGIN
    	UPDATE "user"."Users" SET liked_images_id = array_remove(liked_images_id, photo_id)
        WHERE "user"."Users".user_id=userId;
    END;
    $$ LANGUAGE plpgsql;
    
       
  CREATE FUNCTION undislikePhoto(photo_id bigint, userId bigint)   
    RETURNS void AS $$
    BEGIN
    	UPDATE "user"."Users" SET disliked_images_id = array_remove(disliked_images_id, photo_id)
        WHERE "user"."Users".user_id=userId;
    END;
    $$ LANGUAGE plpgsql;
    
     CREATE FUNCTION followcategories(category_id bigint [],userId bigint)   
    RETURNS void AS $$
    BEGIN
    	UPDATE "user"."Users" SET categories_id = array_cat(categories_id, category_id)
        WHERE "user"."Users".user_id=userId;
    END;
    $$ LANGUAGE plpgsql;
    
    
    
 	CREATE FUNCTION followUser(follower_id bigint,following_id bigint)   
    RETURNS void AS $$
    BEGIN
    	UPDATE "user"."Users" SET users_followers_id = array_append(users_followers_id, follower_id)
        WHERE "user"."Users".user_id=following_id;
    	UPDATE "user"."Users" SET users_following_id = array_append(users_following_id, following_id)
        WHERE "user"."Users".user_id=follower_id;
    END;
    $$ LANGUAGE plpgsql;
    
        
 	CREATE FUNCTION unfollowUser(follower_id bigint,following_id bigint)   
    RETURNS void AS $$
    BEGIN
    	UPDATE "user"."Users" SET users_followers_id = array_remove(users_followers_id, follower_id)
        WHERE "user"."Users".user_id=following_id;
    	UPDATE "user"."Users" SET users_following_id = array_remove(users_following_id, following_id)
        WHERE "user"."Users".user_id=follower_id;
    END;
    $$ LANGUAGE plpgsql;
    
    CREATE FUNCTION unblockUser(blocking_id bigint ,blocked_id bigint)
    RETURNS void AS $$
    BEGIN 
    	DELETE FROM "user"."Blocks" 
        WHERE "user"."Blocks".user_blocking_id = blocking_id AND "user"."Blocks".user_blocked_id = blocked_id;
     END;
     $$ LANGUAGE plpgsql;
    
    
    

       
    
   
    
    