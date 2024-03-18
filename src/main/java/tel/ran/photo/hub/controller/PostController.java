package tel.ran.photo.hub.controller;

import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tel.ran.photo.hub.model.AccountType;
import tel.ran.photo.hub.model.Comment;
import tel.ran.photo.hub.model.Post;
import tel.ran.photo.hub.security.PersonDetails;
import tel.ran.photo.hub.service.CommentService;
import tel.ran.photo.hub.service.PersonDetailsService;
import tel.ran.photo.hub.service.PhotoLikeService;
import tel.ran.photo.hub.service.PostService;

@Controller
@RequestMapping("/posts")
public class PostController {
    private final CommentService commentService;
    private final PhotoLikeService photoLikeService;
    private final PostService postService;
    private final PersonDetailsService personDetailsService;

    @Autowired
    public PostController(CommentService commentService, PhotoLikeService photoLikeService, PostService postService, PersonDetailsService personDetailsService) {
        this.commentService = commentService;
        this.photoLikeService = photoLikeService;
        this.postService = postService;
        this.personDetailsService = personDetailsService;
    }

    @GetMapping()
    public String getAll(Model model) {
        model.addAttribute("posts", postService.getAll());
        return "post/posts";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") long id, Model model, @ModelAttribute("comment") Comment comment) {
        model.addAttribute("post", postService.getById(id));
//        model.addAttribute("comments", commentService.getAllByPostId(id));
        model.addAttribute("count", photoLikeService.getLikesCountByPostId(id));
        return "post/post";
    }

    @GetMapping("/photo/{postId}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable("postId") long postId) {
        Post post = postService.getById(postId);
        byte[] photo = post.getPhoto();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(photo, headers, HttpStatus.OK);
    }


    @GetMapping("/new")
    public String addNewPostPage(@ModelAttribute("post") Post post) {
        return "post/new";
    }

    @PostMapping()
    @SneakyThrows
    public String save(@ModelAttribute("post") @Valid Post post, BindingResult bindingResult,
                       @RequestParam("photoFile") MultipartFile photoFile, @AuthenticationPrincipal PersonDetails personDetails) {
        if (photoFile.isEmpty()) {
            bindingResult.rejectValue("photo", "error.photo", "please select photo file");
        } else {
            post.setPhoto(photoFile.getBytes());
        }
        if (bindingResult.hasErrors()) {
            return "post/new";
        }
        postService.save(post, personDetails.getUsername());
        return "redirect:/posts/profile";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable("id") long id, Model model) {
        model.addAttribute("post", postService.getById(id));
        return "post/edit";
    }

    @PatchMapping("/edit/{id}")
    public String update(@ModelAttribute("post") @Valid Post post, BindingResult bindingResult,
                         @PathVariable("id") long id) {
        if (bindingResult.hasErrors()) {
            return "post/edit";
        }
        postService.update(id, post);
        return "redirect:/posts/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        postService.delete(id);
        return "redirect:/posts/profile";
    }

    @GetMapping("/views")
    public String getAllSortedByViews(Model model) {
        model.addAttribute("posts", postService.sortedByViews());
        return "post/posts";
    }

    @GetMapping("/search/{key}")
    public String getAllPostsByKey(@PathVariable("key") String key, Model model) {
        model.addAttribute("key", postService.findAllByKey(key));
        return "post/posts";
    }

    @GetMapping("/profile")
    public String getProfile(@AuthenticationPrincipal PersonDetails personDetails, Model model) {
        model.addAttribute("person", personDetails.getPerson());
        model.addAttribute("posts", postService.findAllPostsByOwner(personDetails.getUsername()));
        if (personDetails.getPerson().getAccountType() == AccountType.PUBLISHER){
            model.addAttribute("isPublisher", true);
        }
        return "post/profile";
    }

    @GetMapping("/private/{id}")
    public String getPrivateById(@PathVariable("id") long id, Model model,
                                 @AuthenticationPrincipal PersonDetails personDetails) {
        Post post = postService.getById(id);
        if (!post.getOwner().getUsername().equals(personDetails.getUsername())) {
            return "redirect:/posts/" + id;
        }
        model.addAttribute("post", post);
        model.addAttribute("person", personDetails.getPerson());

        return "post/private";
    }

    @GetMapping("/profile/likes")
    public String getAllUserLikes(@AuthenticationPrincipal PersonDetails personDetails, Model model) {
        model.addAttribute("posts", postService.getAllPersonLikes(personDetails.getUsername()));
        return "post/likes";
    }

    @GetMapping("/public/profile/{username}")
    public String getPublicProfile(@PathVariable("username") String username, Model model) {
        model.addAttribute("person", personDetailsService.getByUsername(username).get());
        model.addAttribute("posts", postService.findAllPostsByOwner(username));
        return "post/publicProfile";
    }


}
