package tel.ran.photo.hub.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tel.ran.photo.hub.model.Comment;
import tel.ran.photo.hub.model.Post;
import tel.ran.photo.hub.security.PersonDetails;
import tel.ran.photo.hub.service.CommentService;
import tel.ran.photo.hub.service.PhotoLikeService;
import tel.ran.photo.hub.service.PostService;

@Controller
@RequestMapping("/posts")
public class PostController {
    private final CommentService commentService;
    private final PhotoLikeService photoLikeService;
    private final PostService postService;

    @Autowired
    public PostController(CommentService commentService, PhotoLikeService photoLikeService, PostService postService) {
        this.commentService = commentService;
        this.photoLikeService = photoLikeService;
        this.postService = postService;
    }

    @GetMapping()
    public String getAll(Model model) {
        model.addAttribute("posts", postService.getAll());
        return "post/posts";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") long id, Model model, @ModelAttribute("comment")Comment comment) {
        model.addAttribute("id", postService.getById(id));
        model.addAttribute("comments", commentService.getAllByPostId(id));
        model.addAttribute("count", photoLikeService.getLikesCountByPostId(id));
        return "post/post";
    }

    @GetMapping("/new")
    public String addNewPostPage(@ModelAttribute("post") Post post) {
        return "post/new";
    }

    @PostMapping()
    public String save(@ModelAttribute("post") @Valid Post post, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "post/new";
        }
        postService.save(post);
        return "redirect:/";
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
    public String delete(@PathVariable("id") long id){
        postService.delete(id);
        return "redirect:/";
    }
    @GetMapping("/views")
    public String getAllSortedByViews(Model model){
        model.addAttribute("posts", postService.sortedByViews());
        return "post/posts";
    }
    @GetMapping("/{key}")
    public String getAllPostsByKey(@PathVariable("key") String key, Model model){
        model.addAttribute("key", postService.findAllByKey(key));
        return "post/posts";
    }
    @GetMapping("/profile")
    public String getProfile(@AuthenticationPrincipal PersonDetails personDetails, Model model){
        model.addAttribute("person", personDetails.getPerson());
        model.addAttribute("posts", postService.findAllPostsByOwner(personDetails.getUsername()));
        return "post/profile";
    }
    @GetMapping("/profile/likes")
    public String getAllUserLikes(@AuthenticationPrincipal PersonDetails personDetails, Model model){
        model.addAttribute("posts", postService.getAllPersonLikes(personDetails.getUsername()));
        return "post/likes";
    }



}
