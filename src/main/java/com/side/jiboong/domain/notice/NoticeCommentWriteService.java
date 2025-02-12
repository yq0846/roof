package com.side.jiboong.domain.notice;

import com.side.jiboong.common.annotation.WriteService;
import com.side.jiboong.common.exception.NotFoundException;
import com.side.jiboong.common.exception.UserNotFoundException;
import com.side.jiboong.domain.notice.entity.Notice;
import com.side.jiboong.domain.notice.entity.NoticeComment;
import com.side.jiboong.domain.notice.request.NoticeCommentCreate;
import com.side.jiboong.domain.user.entity.User;
import com.side.jiboong.infrastructure.notice.NoticeCommentRepository;
import com.side.jiboong.infrastructure.notice.NoticeRepository;
import com.side.jiboong.infrastructure.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;

@WriteService
@RequiredArgsConstructor
public class NoticeCommentWriteService {
    private final NoticeCommentRepository noticeCommentRepository;
    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;

    public void create(NoticeCommentCreate create) {
        User user = userRepository.findById(create.userId())
                .orElseThrow(UserNotFoundException::new);

        Notice notice = noticeRepository.findById(create.noticeId())
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));

        noticeCommentRepository.save(create.toNoticeComment(notice, user));
    }

    public void delete(Long userId, Long id) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        NoticeComment noticeComment = noticeCommentRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));

        if(!noticeComment.getUser().equals(user)) {
            throw new AccessDeniedException("자신이 쓴 댓글만 삭제할 수 있습니다.");
        }
        noticeCommentRepository.deleteById(id);
    }
}
