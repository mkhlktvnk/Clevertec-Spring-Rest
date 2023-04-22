package ru.clevertec.ecl.unit.service.impl;

import builder.impl.TagTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.domain.entity.Tag;
import ru.clevertec.ecl.domain.repository.TagRepository;
import ru.clevertec.ecl.service.exception.ResourceNotFoundException;
import ru.clevertec.ecl.service.impl.TagServiceImpl;
import ru.clevertec.ecl.service.message.MessagesSource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {
    private static final Long ID = 1L;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private MessagesSource messages;

    @InjectMocks
    private TagServiceImpl tagService;

    @Test
    void checkGetTagsShouldCallRepositoryAndReturnExpectedResult() {
        Pageable pageable = PageRequest.of(0, 1);
        List<Tag> tags = List.of(TagTestDataBuilder.aTag().build());
        Page<Tag> expected = new PageImpl<>(tags);

        doReturn(expected).when(tagRepository).findAll(pageable);

        List<Tag> actual = tagService.findAllByPageable(pageable);

        verify(tagRepository).findAll(pageable);
        assertThat(actual).isEqualTo(expected.getContent());
    }

    @Test
    void checkGetByIdShouldReturnExpectedResult() {
        Tag expected = TagTestDataBuilder.aTag().build();
        doReturn(Optional.of(expected)).when(tagRepository).findById(ID);

        Tag actual = tagService.findById(ID);

        verify(tagRepository).findById(ID);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetByIdShouldThrowResourceNotFoundException() {
        doReturn(Optional.empty()).when(tagRepository).findById(ID);

        assertThatThrownBy(() -> tagService.findById(ID))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void checkInsertShouldReturnExpectedResult() {
        Tag expected = TagTestDataBuilder.aTag().build();
        doReturn(expected).when(tagRepository).save(expected);

        Tag actual = tagService.insert(expected);

        verify(tagRepository).save(expected);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkUpdateByIdShouldCallRepositoryTwice() {
        Tag tag = TagTestDataBuilder.aTag().build();
        doReturn(true).when(tagRepository).existsById(ID);
        doReturn(Optional.of(tag)).when(tagRepository).findById(ID);

        tagService.updateById(ID, tag);

        verify(tagRepository).existsById(ID);
        verify(tagRepository).save(tag);
    }

    @Test
    void checkUpdateByIdShouldThrowResourceNotFoundException() {
        Tag tag = TagTestDataBuilder.aTag().build();
        doReturn(false).when(tagRepository).existsById(ID);

        assertThatThrownBy(() -> tagService.updateById(ID, tag))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void checkDeleteByIdShouldCallRepositoryTwice() {
        doReturn(true).when(tagRepository).existsById(ID);

        tagService.deleteById(ID);

        verify(tagRepository).existsById(ID);
        verify(tagRepository).deleteById(ID);
    }

    @Test
    void checkDeleteByIdShouldThrowResourceNotFoundException() {
        doReturn(false).when(tagRepository).existsById(ID);

        assertThatThrownBy(() -> tagService.deleteById(ID))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}