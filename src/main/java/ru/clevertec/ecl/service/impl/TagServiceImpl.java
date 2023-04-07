package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.domain.entity.Tag;
import ru.clevertec.ecl.domain.repository.TagRepository;
import ru.clevertec.ecl.service.TagService;
import ru.clevertec.ecl.service.exception.ResourceNotFoundException;
import ru.clevertec.ecl.service.message.TagMessages;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final TagMessages tagMessages;

    @Override
    public List<Tag> findAllByPageable(Pageable pageable) {
        return tagRepository.findAll(pageable).getContent();
    }

    @Override
    public Tag findById(long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(tagMessages.getNotFound()));
    }

    @Override
    public Tag findUserMostPopularTagWithTheHighestCostOfAllOrders() {
        return tagRepository.findUserMostPopularTagWithTheHighestCostOfAllOrders()
                .orElseThrow(() -> new ResourceNotFoundException(tagMessages.getNotFound()));
    }

    @Override
    @Transactional
    public Tag insert(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    @Transactional
    public void updateById(long id, Tag updateTag) {
        if (!tagRepository.existsById(id)) {
            throw new ResourceNotFoundException(tagMessages.getNotFound());
        }
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(tagMessages.getNotFound()));
        tag.setName(updateTag.getName());
        tagRepository.save(tag);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        if (!tagRepository.existsById(id)) {
            throw new ResourceNotFoundException(tagMessages.getNotFound());
        }
        tagRepository.deleteById(id);
    }
}
