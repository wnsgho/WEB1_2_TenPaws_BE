package com.example.tenpaws.domain.faq.exception;

import org.springframework.http.HttpStatus;

public enum FaqException {
    FAQ_NOT_FOUND("존재하지 않는 FAQ 입니다", HttpStatus.NOT_FOUND),
    FAQ_NOT_REGISTERED("FAQ 등록에 실패하였습니다.", HttpStatus.BAD_REQUEST),
    FAQ_NOT_MODIFIED("FAQ 수정에 실패하였습니다.", HttpStatus.BAD_REQUEST),
    FAQ_NOT_DELETE("FAQ 삭제에 실패하였습니다.", HttpStatus.BAD_REQUEST),
    FAQ_NO_CHILD("하위 FAQ가 없습니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus status;

    FaqException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public FaqTaskException getFaqTaskException() {
        return new FaqTaskException(this.message,this.status.value());
    }
}
