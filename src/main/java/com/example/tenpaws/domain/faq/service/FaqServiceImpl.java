package com.example.tenpaws.domain.faq.service;

import com.example.tenpaws.domain.faq.dto.FaqRequest;
import com.example.tenpaws.domain.faq.dto.FaqResponse;
import com.example.tenpaws.domain.faq.entity.Faq;
import com.example.tenpaws.domain.faq.repository.FaqRepository;
import com.example.tenpaws.global.exception.BaseException;
import com.example.tenpaws.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {

    private final FaqRepository faqRepository;

    @Override
    @Transactional
    public FaqResponse create(FaqRequest faqRequest) {
        try {
            if (faqRequest.getParentId() != null) {
                Faq faq = faqRepository.findById(faqRequest.getParentId()).orElseThrow(() -> new BaseException(ErrorCode.FAQ_NOT_FOUND));
                return new FaqResponse(faqRepository.save(faqRequest.toEntity(faq)));
            }
            return new FaqResponse(faqRepository.save(faqRequest.toEntity(null)));
        } catch (Exception e) {
            throw new BaseException(ErrorCode.FAQ_NOT_REGISTERED);
        }
    }

    @Override
    public List<FaqResponse> read() {
        return faqRepository.findTopLevel().stream().map(FaqResponse::new).toList();
    }

    @Override
    @Transactional
    public FaqResponse update(FaqRequest faqRequest) {
        try {
            Optional<Faq> faq = faqRepository.findById(faqRequest.getFaqId());
            if (faq.isPresent()) {
                faq.get().changeContent(faqRequest.getContent());
                return new FaqResponse(faq.get());
            } else {
                throw new BaseException(ErrorCode.FAQ_NOT_FOUND);
            }
        } catch (Exception e) {
            throw new BaseException(ErrorCode.FAQ_NOT_MODIFIED);
        }
    }

    @Override
    @Transactional
    public void delete(Long faqId) {
        try {
            faqRepository.findById(faqId).orElseThrow(() -> new BaseException(ErrorCode.FAQ_NOT_FOUND));
            faqRepository.deleteById(faqId);
        } catch (Exception e) {
            throw new BaseException(ErrorCode.FAQ_NOT_DELETED);
        }
    }

    @Override
    public List<FaqResponse> findByParentId(Long parentId) {
        List<FaqResponse> faqResponseList = faqRepository.findByParentId(parentId).stream().map(FaqResponse::new).toList();
        if (faqResponseList.isEmpty()) {
            throw new BaseException(ErrorCode.FAQ_NO_CHILD);
        }
        return faqResponseList;
    }
}
