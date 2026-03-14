package com.ohgiraffers.COZYbe.domain.board.service;

import com.ohgiraffers.COZYbe.common.error.ApplicationException;
import com.ohgiraffers.COZYbe.common.error.ErrorCode;
import com.ohgiraffers.COZYbe.domain.board.dto.*;
import com.ohgiraffers.COZYbe.domain.board.entity.PostType;
import com.ohgiraffers.COZYbe.domain.board.entity.TeamPost;
import com.ohgiraffers.COZYbe.domain.board.entity.TeamPostComment;
import com.ohgiraffers.COZYbe.domain.board.entity.TeamPostLike;
import com.ohgiraffers.COZYbe.domain.board.repository.TeamPostCommentRepository;
import com.ohgiraffers.COZYbe.domain.board.repository.TeamPostLikeRepository;
import com.ohgiraffers.COZYbe.domain.board.repository.TeamPostRepository;
import com.ohgiraffers.COZYbe.domain.member.domain.service.MemberDomainService;
import com.ohgiraffers.COZYbe.domain.teams.domain.entity.Team;
import com.ohgiraffers.COZYbe.domain.teams.domain.service.TeamDomainService;
import com.ohgiraffers.COZYbe.domain.user.domain.entity.User;
import com.ohgiraffers.COZYbe.domain.user.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TeamPostService {

    private final TeamPostRepository postRepository;
    private final TeamPostCommentRepository commentRepository;
    private final TeamPostLikeRepository likeRepository;
    private final TeamDomainService teamDomainService;
    private final MemberDomainService memberDomainService;
    private final UserDomainService userDomainService;

    @Transactional(readOnly = true)
    public List<PostListItemDTO> getPosts(String teamId, PostType type, String userId) {
        assertTeamMember(teamId, userId);
        List<TeamPost> posts = postRepository.findByTeam_TeamIdAndTypeOrderByCreatedAtDesc(
                UUID.fromString(teamId),
                type
        );

        return posts.stream()
                .map(post -> new PostListItemDTO(
                        post.getPostId().toString(),
                        post.getTitle(),
                        post.getAuthor().getNickname(),
                        likeRepository.countByPost_PostId(post.getPostId()),
                        post.getCreatedAt()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public PostDetailDTO getPostDetail(String postId, String userId) {
        TeamPost post = getPost(postId);
        assertTeamMember(post.getTeam().getTeamId().toString(), userId);
        long likeCount = likeRepository.countByPost_PostId(post.getPostId());
        long commentCount = commentRepository.countByPost_PostId(post.getPostId());
        boolean liked = likeRepository.existsByPost_PostIdAndUser_UserId(
                post.getPostId(), UUID.fromString(userId)
        );

        return new PostDetailDTO(
                post.getPostId().toString(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getNickname(),
                likeCount,
                commentCount,
                liked,
                post.getCreatedAt()
        );
    }

    @Transactional
    public PostDetailDTO createPost(CreatePostDTO dto, String userId) {
        Team team = teamDomainService.getTeam(dto.teamId());
        assertTeamMember(dto.teamId(), userId);
        if (dto.type() == PostType.NOTICE && !isLeaderOrSubLeader(team, userId)) {
            throw new ApplicationException(ErrorCode.NOT_ALLOWED);
        }

        TeamPost post = TeamPost.builder()
                .team(team)
                .author(userDomainService.getReference(userId))
                .type(dto.type())
                .title(dto.title())
                .content(dto.content())
                .build();

        TeamPost saved = postRepository.save(post);
        return getPostDetail(saved.getPostId().toString(), userId);
    }

    @Transactional
    public PostDetailDTO updatePost(String postId, UpdatePostDTO dto, String userId) {
        TeamPost post = getPost(postId);
        assertTeamMember(post.getTeam().getTeamId().toString(), userId);
        if (!isAuthorOrLeader(post, userId)) {
            throw new ApplicationException(ErrorCode.NOT_ALLOWED);
        }

        if (dto.title() != null && !dto.title().isBlank()) {
            post.setTitle(dto.title());
        }
        if (dto.content() != null && !dto.content().isBlank()) {
            post.setContent(dto.content());
        }

        return getPostDetail(postId, userId);
    }

    @Transactional
    public void deletePost(String postId, String userId) {
        TeamPost post = getPost(postId);
        assertTeamMember(post.getTeam().getTeamId().toString(), userId);
        if (!isAuthorOrLeader(post, userId)) {
            throw new ApplicationException(ErrorCode.NOT_ALLOWED);
        }
        commentRepository.deleteByPost_PostId(post.getPostId());
        likeRepository.deleteByPost_PostId(post.getPostId());
        postRepository.delete(post);
    }

    @Transactional
    public LikeResponseDTO toggleLike(String postId, String userId) {
        TeamPost post = getPost(postId);
        assertTeamMember(post.getTeam().getTeamId().toString(), userId);
        if (post.getType() == PostType.NOTICE) {
            throw new ApplicationException(ErrorCode.NOT_ALLOWED);
        }
        UUID userUUID = UUID.fromString(userId);

        boolean exists = likeRepository.existsByPost_PostIdAndUser_UserId(post.getPostId(), userUUID);
        if (exists) {
            likeRepository.deleteByPost_PostIdAndUser_UserId(post.getPostId(), userUUID);
        } else {
            TeamPostLike like = TeamPostLike.builder()
                    .post(post)
                    .user(userDomainService.getReference(userId))
                    .build();
            likeRepository.save(like);
        }
        long count = likeRepository.countByPost_PostId(post.getPostId());
        boolean liked = likeRepository.existsByPost_PostIdAndUser_UserId(post.getPostId(), userUUID);
        return new LikeResponseDTO(count, liked);
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> getComments(String postId, String userId) {
        TeamPost post = getPost(postId);
        assertTeamMember(post.getTeam().getTeamId().toString(), userId);
        if (post.getType() == PostType.NOTICE) {
            return List.of();
        }
        return commentRepository.findByPost_PostIdOrderByCreatedAtAsc(post.getPostId())
                .stream()
                .map(comment -> new CommentDTO(
                        comment.getCommentId().toString(),
                        comment.getAuthor().getNickname(),
                        comment.getContent(),
                        comment.getCreatedAt()
                ))
                .toList();
    }

    @Transactional
    public CommentDTO createComment(String postId, CreateCommentDTO dto, String userId) {
        TeamPost post = getPost(postId);
        assertTeamMember(post.getTeam().getTeamId().toString(), userId);
        if (post.getType() == PostType.NOTICE) {
            throw new ApplicationException(ErrorCode.NOT_ALLOWED);
        }

        TeamPostComment comment = TeamPostComment.builder()
                .post(post)
                .author(userDomainService.getReference(userId))
                .content(dto.content())
                .build();
        TeamPostComment saved = commentRepository.save(comment);

        return new CommentDTO(
                saved.getCommentId().toString(),
                saved.getAuthor().getNickname(),
                saved.getContent(),
                saved.getCreatedAt()
        );
    }

    @Transactional
    public void deleteComment(String commentId, String userId) {
        TeamPostComment comment = commentRepository.findById(UUID.fromString(commentId))
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_SUCH_POST_COMMENT));
        TeamPost post = comment.getPost();
        assertTeamMember(post.getTeam().getTeamId().toString(), userId);

        if (!isAuthorOrLeader(post, userId) && !comment.getAuthor().getUserId().toString().equals(userId)) {
            throw new ApplicationException(ErrorCode.NOT_ALLOWED);
        }

        commentRepository.delete(comment);
    }

    private TeamPost getPost(String postId) {
        return postRepository.findById(UUID.fromString(postId))
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_SUCH_POST));
    }

    private void assertTeamMember(String teamId, String userId) {
        if (!memberDomainService.isMemberOfTeam(teamId, userId)) {
            throw new ApplicationException(ErrorCode.NOT_ALLOWED);
        }
    }

    private boolean isLeaderOrSubLeader(Team team, String userId) {
        UUID userUUID = UUID.fromString(userId);
        if (team.getLeader() != null && team.getLeader().getUserId().equals(userUUID)) {
            return true;
        }
        return team.getSubLeader() != null && team.getSubLeader().getUserId().equals(userUUID);
    }

    private boolean isAuthorOrLeader(TeamPost post, String userId) {
        if (post.getAuthor().getUserId().toString().equals(userId)) {
            return true;
        }
        return isLeaderOrSubLeader(post.getTeam(), userId);
    }
}
