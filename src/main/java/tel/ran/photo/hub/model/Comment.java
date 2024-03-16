package tel.ran.photo.hub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Entity
@Table(name = "comment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @ManyToOne()
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;
    @ManyToOne()
    @JoinColumn(name = "person_username", referencedColumnName = "username")
    private Person person;
    @Column(name = "description")
    @NotBlank(message = "description can not be empty")
    @Size(min = 5, max = 150, message = "description can be from 5 to 150 letters")
    private String description;
    @Column(name = "created_at")
    private LocalDate createdAt;
}
