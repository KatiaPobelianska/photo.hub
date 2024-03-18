package tel.ran.photo.hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tel.ran.photo.hub.exception.LikeAlreadyPresentException;
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
    @PostMapping("/{postId}")
    public String save(@PathVariable("postId") long postId, @AuthenticationPrincipal PersonDetails personDetails){

      try {
          photoLikeService.save(postId, personDetails.getUsername());
      }catch (LikeAlreadyPresentException e){
          photoLikeService.deleteByPostId(postId);
      }
       return "redirect:/posts/" + postId;
    }
//    @DeleteMapping("/{id}")
//    public String delete(@PathVariable("id") long id){
//        photoLikeService.delete(id);
//        return "redirect:/posts";
//    }

}
