package tel.ran.photo.hub.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import tel.ran.photo.hub.exception.LikeAlreadyPresentException;
import tel.ran.photo.hub.model.Person;
import tel.ran.photo.hub.model.PhotoLike;
import tel.ran.photo.hub.model.Post;
import tel.ran.photo.hub.repository.PhotoLikeRepository;

import java.util.Objects;
import java.util.Optional;

@Service
public class PhotoLikeService {
    private final PhotoLikeRepository photoLikeRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PhotoLikeService(PhotoLikeRepository photoLikeRepository, JdbcTemplate jdbcTemplate) {
        this.photoLikeRepository = photoLikeRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public int getLikesCountByPostId(long postId) {
        return jdbcTemplate.queryForObject(
                "select count(*) from photo_like where post_id=?", Integer.class, postId
        );
    }

    public void save(long postId, String username) {
        Optional<PhotoLike> photoLikeOptional = jdbcTemplate.query(
                "select * from photo_like where post_id=? and person_username=?", new Object[]{postId, username},
                new BeanPropertyRowMapper<>(PhotoLike.class)
        ).stream().findAny();
        if (photoLikeOptional.isPresent()) {
            throw new LikeAlreadyPresentException("this person has already liked this post");
        }
       jdbcTemplate.update("insert into photo_like(post_id, person_username) values(?,?)", postId, username);
    }

    public void deleteByPostId(long postId) {
        jdbcTemplate.update("delete from photo_like where post_id=?", postId);
    }
}
