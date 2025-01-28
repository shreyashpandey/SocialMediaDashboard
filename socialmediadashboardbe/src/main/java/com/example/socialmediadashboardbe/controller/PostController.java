
package com.example.socialmediadashboardbe.controller;

import com.example.socialmediadashboardbe.model.PostEntity;
import com.example.socialmediadashboardbe.model.CommentEntity;
import com.example.socialmediadashboardbe.model.UserEntity;
import com.example.socialmediadashboardbe.service.PostService;
import com.example.socialmediadashboardbe.service.CommentService;
import com.example.socialmediadashboardbe.service.UserService;
import com.example.socialmediadashboardbe.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService; // Fetch user details

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Get all posts for the authenticated user.
     */
    @GetMapping
    public List<PostEntity> getPostsForAuthenticatedUser(Authentication authentication) {
        String username = authentication.getName(); // Extract username from SecurityContext
        UserEntity user = userService.getUserByUsername(username);
        return postService.getPostsByUser(user.getId());
    }

    /**
     * Create a new post for the authenticated user.
     */
    @PostMapping
    public PostEntity createPost(@RequestBody PostEntity post, Authentication authentication) {
        String username = authentication.getName(); // Extract username from SecurityContext
        UserEntity user = userService.getUserByUsername(username);

        post.setUser(user); // Associate the post with the authenticated user
        return postService.createPost(post);
    }

    /**
     * Add a comment to a post for the authenticated user.
     */
    @PostMapping("/{postId}/comments")
    public CommentEntity addCommentToPost(
            @PathVariable Long postId,
            @RequestBody CommentEntity comment,
            Authentication authentication
    ) {
        String username = authentication.getName(); // Extract username from SecurityContext
        UserEntity user = userService.getUserByUsername(username);

        // Associate the comment with the post and user
        PostEntity post = postService.getPostById(postId);
        comment.setPost(post);
        comment.setUser(user);

        return commentService.createComment(comment);
    }
}
