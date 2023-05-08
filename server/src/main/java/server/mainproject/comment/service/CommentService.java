package server.mainproject.comment.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.mainproject.comment.entity.Comment;
import server.mainproject.comment.repository.CommentRepository;
import server.mainproject.exception.BusinessLogicException;
import server.mainproject.exception.ExceptionCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
@Component
@Service
public class CommentService {
    private CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment createComment(Comment comment) {  // 생성
        return commentRepository.save(comment);
    }

    public Comment updateComment(Comment comment) {  // 댓글과 별점 수정
        Comment findComment = findVerifiedComment(comment.getCommentId());

        Optional.ofNullable(comment.getContent())
                .ifPresent(content -> findComment.setContent(content));
        Optional.ofNullable(comment.getStar())
                .ifPresent(star -> findComment.setStar(star));

        findComment.setModifiedAt(LocalDateTime.now());

        return commentRepository.save(findComment);
    }

    public List<Comment> findComments() {  // 모든 댓글 조회

        return commentRepository.findAll();
    }

    public void deleteComment(long commentId) {  // 특정 댓글 삭제
        commentRepository.deleteById(commentId);
    }

    public Comment findVerifiedComment(long commentId) {  // 해당 댓글의 존재 유무 체크
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));

        return comment;
    }
}
