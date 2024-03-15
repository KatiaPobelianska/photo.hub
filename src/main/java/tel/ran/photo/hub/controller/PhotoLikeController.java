package tel.ran.photo.hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tel.ran.photo.hub.model.Post;
import tel.ran.photo.hub.security.PersonDetails;
import tel.ran.photo.hub.service.PhotoLikeService;

@Controller
@RequestMapping("/likes")
public class PhotoLikeController {
    private final PhotoLikeService photoLikeService;

    @Autowired
    public PhotoLikeController(PhotoLikeService photoLikeService) {
        this.photoLikeService = photoLikeService;
    }
    @PostMapping()
    public String save(@ModelAttribute("post") Post post, @AuthenticationPrincipal PersonDetails personDetails){
       photoLikeService.save(post, personDetails.getPerson());
       return "redirect:/posts/" + post.getId();
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id){
        photoLikeService.delete(id);
        return "redirect:/posts";
    }

}
