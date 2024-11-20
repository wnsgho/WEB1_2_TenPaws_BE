package com.example.tenpaws.faq.service;

import com.example.tenpaws.faq.dto.FaqRequest;
import com.example.tenpaws.faq.dto.FaqResponse;
import com.example.tenpaws.faq.entity.Faq;
import com.example.tenpaws.faq.exception.FaqException;
import com.example.tenpaws.faq.repository.FaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {
    private final FaqRepository faqRepository;

    @Override
    public FaqResponse create(FaqRequest faqRequest) {
        try {
            return new FaqResponse(faqRepository.save(faqRequest.toEntity()));
        } catch (Exception e) {
            throw FaqException.FAQ_NOT_REGISTERED.getFaqTaskException();
        }
    }

    @Override
    public FaqResponse read(Long faqId) {
        return new FaqResponse(faqRepository.findById(faqId)
                .orElseThrow(FaqException.FAQ_NOT_FOUND::getFaqTaskException)
        );
    }

    @Override
    public FaqResponse update(FaqRequest faqRequest) {
        try {
            Optional<Faq> faq = faqRepository.findById(faqRequest.getFaqId());
            if (faq.isPresent()) {
                faq.get().changeContent(faqRequest.getContent());
                return new FaqResponse(faq.get());
            } else {
                throw FaqException.FAQ_NOT_FOUND.getFaqTaskException();
            }
        } catch (Exception e) {
            throw FaqException.FAQ_NOT_MODIFIED.getFaqTaskException();
        }
    }

    @Override
    public void delete(Long faqId) {
        try {
            faqRepository.findById(faqId).orElseThrow(FaqException.FAQ_NOT_FOUND::getFaqTaskException);
            faqRepository.deleteById(faqId);
        } catch (Exception e) {
            throw FaqException.FAQ_NOT_DELETE.getFaqTaskException();
        }
    }

    @Override
    public List<FaqResponse> findByParentId(Long parentId) {
        List<FaqResponse> faqResponseList = faqRepository.findByParentId(parentId).stream().map(FaqResponse::new).toList();
        if (faqResponseList.isEmpty()) {
            throw FaqException.FAQ_NO_CHILD.getFaqTaskException();
        }
        return faqResponseList;
    }
}
