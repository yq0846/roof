package com.side.jiboong.presentation.api;

import com.side.jiboong.common.util.Page;
import com.side.jiboong.domain.notice.NoticeReadService;
import com.side.jiboong.domain.notice.NoticeWriteService;
import com.side.jiboong.domain.notice.request.NoticeCondition;
import com.side.jiboong.presentation.dto.NoticeDto;
import com.side.jiboong.presentation.dto.SearchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notices")
@Tag(name = "Notice API", description = "공지사항 API")
public class NoticeRestController {
    private final NoticeReadService noticeReadService;
    private final NoticeWriteService noticeWriteService;

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

    @GetMapping("/{notice-id}")
    @Operation(summary = "상세조회")
    public ResponseEntity<NoticeDto.Info> getById(
            @PathVariable("notice-id") Long id
    ) {
        noticeWriteService.increaseViewCount(id);
        NoticeDto.Info notice = NoticeDto.Info.from(noticeReadService.findById(id));
        return ResponseEntity.status(HttpStatus.OK).body(notice);
    }

    @PostMapping
    @Operation(summary = "생성")
    public ResponseEntity<Void> create(@RequestBody NoticeDto.Create create) {
        noticeWriteService.create(create.toNoticeCreate());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{notice-id}")
    @Operation(summary = "수정")
    public ResponseEntity<Void> update(
            @PathVariable("notice-id") Long id,
            @RequestBody NoticeDto.Update update
    ) {
        noticeWriteService.update(id, update.toNoticeUpdate());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/delete")
    @Operation(summary = "삭제")
    public ResponseEntity<Void> delete(@RequestBody NoticeDto.Delete delete) {
        noticeWriteService.delete(delete.idList());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
