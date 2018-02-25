package msa.pojo;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.util.*;
import javax.persistence.*;

@Entity
@Table(name = "Category")
public class Category {
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
            name = "uuid",
            strategy = "org.hibernate.id.UUIDGenerator"

    )
    @Column(columnDefinition = "uuid", updatable = false, unique = true)
    private UUID id;



    public List<User> getUsersFollowCategory() {
        return usersFollowCategory;
    }

    public void setUsersFollowCategory(List<User> usersFollowCategory) {
        this.usersFollowCategory = usersFollowCategory;
    }

    @ManyToMany(mappedBy = "userCat")
    private List<User> usersFollowCategory;
}