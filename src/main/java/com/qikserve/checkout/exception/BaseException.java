package com.qikserve.checkout.exception;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import lombok.Getter;
import org.springframework.web.ErrorResponse;
import java.util.function.Predicate;


@Getter(onMethod_ = {@Override})
public abstract class BaseException extends RuntimeException implements IResponseMessage, IResponseHttpStatus {
    protected final String messageCode;
    protected final HttpStatus httpStatus;

    protected BaseException(BaseExceptionBuilder<?, ?> b) {
        this.httpStatus = b.httpStatus;
        this.messageCode = b.messageCode;
    }

    public ErrorResponse toResponse(MessageSource messageSource) {
        return ErrorResponse.create(this, this.httpStatus, this.getMessage(messageSource));
    }

    public String getMessage(MessageSource messageSource) {
        return messageSource.getMessage(messageCode, this.getArgs(), LocaleContextHolder.getLocale());
    }

    public final void throwIf(Predicate<BaseException> condition) {
        if (condition.test(this)) {
            throw this;
        }
    }

    public final void throwIf(boolean condition) {
        if (condition) {
            throw this;
        }
    }


    protected abstract Object[] getArgs();

    public static abstract class BaseExceptionBuilder<C extends BaseException, B extends BaseExceptionBuilder<C, B>> {
        private String messageCode;
        private HttpStatus httpStatus;

        public B messageCode(String messageCode) {
            this.messageCode = messageCode;
            return self();
        }

        public B httpStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
            return self();
        }

        protected abstract B self();

        public abstract C build();

        public String toString() {
            return "BaseException.BaseExceptionBuilder(super=" + super.toString() + ", messageCode=" + this.messageCode + ", httpStatus=" + this.httpStatus + ")";
        }

    }
}