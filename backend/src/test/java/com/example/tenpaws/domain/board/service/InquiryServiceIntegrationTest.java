package com.example.tenpaws.domain.board.service;

import com.example.tenpaws.domain.board.dto.request.InquiryRequest;
import com.example.tenpaws.domain.board.dto.response.InquiryDetailResponse;
import com.example.tenpaws.domain.board.dto.response.InquiryListViewResponse;
import com.example.tenpaws.domain.board.dto.response.InquiryResponse;
import com.example.tenpaws.domain.board.repository.InquiryRepository;
import com.example.tenpaws.domain.notification.dto.request.NotificationRequest;
import com.example.tenpaws.domain.notification.factory.NotificationFactory;
import com.example.tenpaws.domain.notification.service.NotificationService;
import com.example.tenpaws.global.entity.UserRole;
import com.example.tenpaws.global.exception.BaseException;
import com.example.tenpaws.global.exception.ErrorCode;
import com.example.tenpaws.global.security.service.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Transactional
class InquiryServiceIntegrationTest {

    @Autowired
    private InquiryService inquiryService;

    @Autowired
    private InquiryRepository inquiryRepository;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private NotificationFactory notificationFactory;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    private InquiryRequest request;
    private final String testEmail = "test@email.com";
    private final String testName = "테스터";

    @BeforeEach
    void setUp() {
        request = new InquiryRequest("테스트 제목", "테스트 내용");

        // CustomUserDetailsService Mock 설정
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("role", UserRole.ROLE_USER);
        userInfo.put("username", testName);
        given(userDetailsService.getInfosByEmail(testEmail)).willReturn(userInfo);

        // NotificationFactory Mock 설정
        given(notificationFactory.createInquirySubmittedNotification(any()))
                .willReturn(mock(NotificationRequest.class));
    }

    @Test
    void 문의_생성() {
        // when
        InquiryResponse response = inquiryService.create(request, testEmail);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("테스트 제목");
        assertThat(response.getContent()).isEqualTo("테스트 내용");
        assertThat(response.getWriterName()).isEqualTo(testName);
        verify(notificationService).create(any());
    }

    @Test
    void 문의_목록_조회() {
        // given
        inquiryService.create(request, testEmail);
        inquiryService.create(request, testEmail);
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<InquiryListViewResponse> result = inquiryService.getList(pageable);

        // then
        assertThat(result.getContent()).hasSize(2);
    }

    @Test
    void 문의_상세_조회() {
        // given
        InquiryResponse created = inquiryService.create(request, testEmail);

        // when
        InquiryDetailResponse response = inquiryService.findById(created.getId());

        // then
        assertThat(response.getTitle()).isEqualTo("테스트 제목");
        assertThat(response.getContent()).isEqualTo("테스트 내용");
        assertThat(response.getWriterEmail()).isEqualTo(testEmail);
        assertThat(response.getWriterName()).isEqualTo(testName);
    }

    @Test
    void 문의_수정() {
        // given
        InquiryResponse created = inquiryService.create(request, testEmail);
        InquiryRequest updateRequest = new InquiryRequest("수정된 제목", "수정된 내용");

        // when
        InquiryResponse updated = inquiryService.update(created.getId(), updateRequest);

        // then
        assertThat(updated.getTitle()).isEqualTo("수정된 제목");
        assertThat(updated.getContent()).isEqualTo("수정된 내용");
    }

    @Test
    void 문의_삭제() {
        // given
        InquiryResponse created = inquiryService.create(request, testEmail);

        // when
        inquiryService.delete(created.getId());

        // then
        assertThatThrownBy(() -> inquiryService.findById(created.getId()))
                .isInstanceOf(BaseException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INQUIRY_NOT_FOUND);
    }

    @Test
    void 존재하지_않는_문의_조회() {
        assertThatThrownBy(() -> inquiryService.findById(999L))
                .isInstanceOf(BaseException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INQUIRY_NOT_FOUND);
    }
}