package com.side.jiboong.presentation.api;

import com.side.jiboong.common.annotation.Authenticated;
import com.side.jiboong.common.component.FileManager;
import com.side.jiboong.common.constant.FilePath;
import com.side.jiboong.common.security.UserAuth;
import com.side.jiboong.common.util.Page;
import com.side.jiboong.domain.notice.NoticeCommentWriteService;
import com.side.jiboong.domain.notice.NoticeReadService;
import com.side.jiboong.domain.notice.NoticeWriteService;
import com.side.jiboong.domain.notice.request.NoticeCondition;
import com.side.jiboong.domain.notice.response.NoticeAlarm;
import com.side.jiboong.domain.user.UserRoleType;
import com.side.jiboong.presentation.dto.FileDto;
import com.side.jiboong.presentation.dto.NoticeDto;
import com.side.jiboong.presentation.dto.SearchDto;
import com.side.jiboong.presentation.usecase.AlarmUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notices")
@Tag(name = "Notice API", description = "공지사항 API")
public class NoticeRestController {
    private final NoticeReadService noticeReadService;
    private final NoticeWriteService noticeWriteService;
    private final NoticeCommentWriteService noticeCommentWriteService;
    private final AlarmUseCase alarmUseCase;
    private final FileManager fileManager;

    @GetMapping
    @Operation(summary = "전체조회")
    public ResponseEntity<Page<NoticeDto.Items>> getAllBy(
            @PageableDefault(page = 0, size = 20) Pageable pageable,
            @ParameterObject SearchDto.NoticeOptions option
    ) {
        NoticeCondition condition = option.toNoticeCondition(pageable);
        List<NoticeDto.Items> notices = noticeReadService.findByAll(condition).stream()
                .map(NoticeDto.Items::from)
                .toList();
        int count = noticeReadService.countBy(condition);

        Page<NoticeDto.Items> response = Page.from(pageable, count, notices);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Authenticated
    @GetMapping("/{notice-id}")
    @Operation(summary = "상세조회")
    public ResponseEntity<NoticeDto.Info> getById(
            @PathVariable("notice-id") Long id
    ) {
        noticeWriteService.increaseViewCount(id);
        NoticeDto.Info notice = NoticeDto.Info.from(noticeReadService.findById(id));
        return ResponseEntity.status(HttpStatus.OK).body(notice);
    }

    @Authenticated(userType = UserRoleType.ADMIN)
    @PostMapping
    @Operation(summary = "생성")
    public ResponseEntity<Void> create(@RequestBody NoticeDto.Create create) {
        NoticeAlarm noticeAlarm = NoticeAlarm.from(noticeWriteService.create(create.toNoticeCreate()));
        alarmUseCase.register(noticeAlarm, Duration.ofDays(3));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Authenticated(userType = UserRoleType.ADMIN)
    @PutMapping("/{notice-id}")
    @Operation(summary = "수정")
    public ResponseEntity<Void> update(
            @PathVariable("notice-id") Long id,
            @RequestBody NoticeDto.Update update
    ) {
        noticeWriteService.update(id, update.toNoticeUpdate());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Authenticated(userType = UserRoleType.ADMIN)
    @PostMapping("/delete")
    @Operation(summary = "삭제")
    public ResponseEntity<Void> delete(@RequestBody NoticeDto.Delete delete) {
        noticeWriteService.delete(delete.idList());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Authenticated(userType = UserRoleType.ADMIN)
    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "파일 첨부", description = """
        파일을 저장합니다.
    """)
    public ResponseEntity<FileDto.FileResponse> fileSave(
            @RequestPart MultipartFile noticeFile
    ) {
        FileDto.FileRequest fileRequest = FileDto.FileRequest.create(noticeFile, FilePath.NOTICE);
        FileDto.FileResponse fileResponse = fileManager.saveFile(fileRequest);

        return ResponseEntity.status(HttpStatus.OK).body(fileResponse);
    }

    @Authenticated
    @PostMapping("/comment")
    @Operation(summary = "댓글 생성")
    public ResponseEntity<Void> commentCreate(
            @AuthenticationPrincipal UserAuth userAuth,
            @RequestBody NoticeDto.CommentCreate create
    ) {
        noticeCommentWriteService.create(create.toNoticeCommentCreate(userAuth.getUserId()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Authenticated
    @PostMapping("/comment-delete")
    @Operation(summary = "댓글 삭제")
    public ResponseEntity<Void> commentDelete(
            @AuthenticationPrincipal UserAuth userAuth,
            @RequestBody NoticeDto.CommentDelete delete
    ) {
        noticeCommentWriteService.delete(userAuth.getUserId(), delete.id());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
