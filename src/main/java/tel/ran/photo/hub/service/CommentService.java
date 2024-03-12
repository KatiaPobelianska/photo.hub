package tel.ran.photo.hub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import tel.ran.photo.hub.exception.CommentAlreadyPresentException;
import tel.ran.photo.hub.model.Comment;
import tel.ran.photo.hub.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CommentService(CommentRepository commentRepository, JdbcTemplate jdbcTemplate) {
        this.commentRepository = commentRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Comment> getAllByPostId(long postId) {
        return jdbcTemplate.query(
                "select * from comment where post_id=? order by created_at desc", new Object[]{postId}, new BeanPropertyRowMapper<>(Comment.class)
        );
    }

    public void save(Comment comment) {
        Optional<Comment> commentOptional = jdbcTemplate.query(
                "select * from comment where post_id=? and person_username=?", new Object[]{comment.getPost().getId(), comment.getPerson().getUsername()}, new BeanPropertyRowMapper<>(Comment.class)
        ).stream().findAny();
        if (commentOptional.isPresent()) {
            throw new CommentAlreadyPresentException("this person has already created comment to this post");
        }
        commentRepository.save(comment);
    }

    public void delete(long id) {
        commentRepository.deleteById(id);
    }

}
