package com.ohgiraffers.COZYbe.domain.board.controller;

import com.ohgiraffers.COZYbe.domain.board.dto.*;
import com.ohgiraffers.COZYbe.domain.board.entity.PostType;
import com.ohgiraffers.COZYbe.domain.board.service.TeamPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/team/post")
@RequiredArgsConstructor
public class TeamPostController {

    private final TeamPostService service;

    @GetMapping("/list")
    public ResponseEntity<List<PostListItemDTO>> getList(
            @RequestParam String teamId,
            @RequestParam PostType type,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(service.getPosts(teamId, type, jwt.getSubject()));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailDTO> getDetail(
            @PathVariable String postId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(service.getPostDetail(postId, jwt.getSubject()));
    }

    @PostMapping
    public ResponseEntity<PostDetailDTO> create(
            @RequestBody CreatePostDTO dto,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(service.createPost(dto, jwt.getSubject()));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostDetailDTO> update(
            @PathVariable String postId,
            @RequestBody UpdatePostDTO dto,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(service.updatePost(postId, dto, jwt.getSubject()));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(
            @PathVariable String postId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        service.deletePost(postId, jwt.getSubject());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<LikeResponseDTO> toggleLike(
            @PathVariable String postId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(service.toggleLike(postId, jwt.getSubject()));
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentDTO>> getComments(
            @PathVariable String postId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(service.getComments(postId, jwt.getSubject()));
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentDTO> createComment(
            @PathVariable String postId,
            @RequestBody CreateCommentDTO dto,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(service.createComment(postId, dto, jwt.getSubject()));
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable String commentId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        service.deleteComment(commentId, jwt.getSubject());
        return ResponseEntity.noContent().build();
    }
}
