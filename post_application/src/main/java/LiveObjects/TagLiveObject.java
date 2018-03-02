package LiveObjects;


import org.redisson.api.annotation.*;

@REntity
public class TagLiveObject {
    @RId
    private String id;
    private String tag_name;
    private String post_id;

}
