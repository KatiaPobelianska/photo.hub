package tel.ran.photo.hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tel.ran.photo.hub.model.Comment;
import tel.ran.photo.hub.model.Post;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
