package tel.ran.photo.hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tel.ran.photo.hub.model.Comment;
import tel.ran.photo.hub.model.PhotoLike;

@Repository
public interface PhotoLikeRepository extends JpaRepository<PhotoLike, Long> {

}
