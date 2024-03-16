package tel.ran.photo.hub.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tel.ran.photo.hub.model.Comment;
import tel.ran.photo.hub.security.PersonDetails;
import tel.ran.photo.hub.service.CommentService;

@Controller
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    @PostMapping("/{postId}")
    public String save(@PathVariable("postId") long postId, @ModelAttribute("comment") @Valid Comment comment,
                       BindingResult bindingResult, @AuthenticationPrincipal PersonDetails personDetails){
        if (bindingResult.hasErrors()){
            return "post/post";
        }

        commentService.save(comment, postId, personDetails.getUsername());
        return "redirect:/posts/" + postId;
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id){
        commentService.delete(id);
        return "redirect:/posts";
    }
}
