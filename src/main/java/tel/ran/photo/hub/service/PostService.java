package tel.ran.photo.hub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import tel.ran.photo.hub.model.Post;
import tel.ran.photo.hub.repository.PostRepository;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostService(PostRepository postRepository, JdbcTemplate jdbcTemplate) {
        this.postRepository = postRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Post> getAll() {
        return postRepository.findAll();
    }

    public Post getById(long id) {
        addViewById(id);
        return postRepository.findById(id).stream().findAny().orElse(null);
    }

    public void save(Post post, String username) {
//        postRepository.save(post);

        jdbcTemplate.update("insert into post(owner_username, title, description, photo) values(?,?,?,?)",
                username, post.getTitle(), post.getDescription(), post.getPhoto());
    }

    public void delete(long id) {
        postRepository.deleteById(id);
    }

    public void update(long id, Post post) {
        Post postToBeSaved = getById(id);
        postToBeSaved.setDescription(post.getDescription());
        postToBeSaved.setTitle(post.getTitle());
        postRepository.save(postToBeSaved);
    }

    private void addViewById(long id) {
        Post post = postRepository.findById(id).stream().findAny().orElse(null);
        post.setViews(post.getViews() + 1);
        postRepository.save(post);
    }

    public List<Post> sortedByViews() {
        return jdbcTemplate.query(
                "select * from post order by views desc", new BeanPropertyRowMapper<>(Post.class)
        );
    }

    public List<Post> findAllByKey(String key) {
        String queryStr = "select * from post where title like ?";
        String parameterizedKey = "%" + key + "%";
        return jdbcTemplate.query(
                queryStr, new Object[]{parameterizedKey}, new BeanPropertyRowMapper<>(Post.class)
        );
    }

    public List<Post> findAllPostsByOwner(String username){
        return jdbcTemplate.query(
                "select * from post where owner_username=? order by created_at desc", new Object[]{username}, new BeanPropertyRowMapper<>(Post.class)
        );
    }

    public List<Post> getAllPersonLikes(String username){
        return jdbcTemplate.query(
                "select post.id, post.title from post join photo_like on post.id = photo_like.post_id where photo_like.person_username=?", new Object[]{username}, new BeanPropertyRowMapper<>(Post.class)
        );
    }
}

