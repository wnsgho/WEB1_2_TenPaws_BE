package com.example.tenpaws.global.utils;

import com.example.tenpaws.domain.admin.entity.Admin;
import com.example.tenpaws.domain.board.entity.Comment;
import com.example.tenpaws.domain.board.entity.Inquiry;
import com.example.tenpaws.global.entity.UserRole;
import com.example.tenpaws.global.exception.BaseException;
import com.example.tenpaws.global.exception.ErrorCode;

public class AuthenticationUtils {

    public static void validateInquiryWriter(Inquiry inquiry, Long userId, Long shelterId) {
        boolean isWriter = false;

        if (userId != null && inquiry.getUser() != null) {
            isWriter = inquiry.getUser().getId().equals(userId);
        }

        if (shelterId != null && inquiry.getShelter() != null) {
            isWriter = inquiry.getShelter().getId().equals(shelterId);
        }

        if (!isWriter) {
            throw new BaseException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
    }

    public static void validateCommentWriter(Comment comment, Long adminId) {
        if (!comment.getAdmin().getId().equals(adminId)) {
            throw new BaseException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
    }

    public static void validateAdminRole(Admin admin) {
        if (admin.getUserRole() != UserRole.ROLE_ADMIN) {
            throw new BaseException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
    }
}
