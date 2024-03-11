package tel.ran.photo.hub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "post")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @ManyToOne()
    @JoinColumn(name = "owner_username", referencedColumnName = "username")
    private Person owner;
    @Column(name = "title")
    @NotBlank(message = "title can not be empty")
    @Size(min = 3, max = 25, message = "size must be from 3 to 25 letters")
    private String title;
    @Column(name = "description")
    @NotBlank(message = "description can not be empty")
    @Size(min = 3, max = 150, message = "description must be from 3 to 150 letters")
    private String description;
    @Column(name = "views")
    private int views;
    @Column(name = "photo", columnDefinition = "bytea")
    private byte[] photo;
    @Column(name = "created_at")
    private LocalDate createdAt;
    @OneToMany(mappedBy = "post")
    private List<Comment> comments;
}
