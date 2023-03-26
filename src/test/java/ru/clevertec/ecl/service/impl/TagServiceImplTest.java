package ru.clevertec.ecl.service.impl;

import builder.impl.TagTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.domain.entity.Tag;
import ru.clevertec.ecl.domain.repository.GiftCertificateRepository;
import ru.clevertec.ecl.domain.repository.TagRepository;
import ru.clevertec.ecl.service.exception.ResourceNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {
    @Mock
    private TagRepository tagRepository;

    @Mock
    private GiftCertificateRepository giftCertificateRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    private static final Long ID = 1L;

    @Test
    void checkGetByIdShouldReturnExpectedResult() {
        Tag expected = TagTestDataBuilder.aTag().build();
        doReturn(Optional.of(expected)).when(tagRepository).findById(ID);

        Tag actual = tagService.getById(ID);

        verify(tagRepository).findById(ID);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetByIdShouldThrowResourceNotFoundException() {
        doReturn(Optional.empty()).when(tagRepository).findById(ID);

        assertThatThrownBy(() -> tagService.getById(ID))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void checkInsertShouldReturnExpectedResult() {
        Tag expected = TagTestDataBuilder.aTag().build();
        doReturn(expected).when(tagRepository).insert(expected);

        Tag actual = tagService.insert(expected);

        verify(tagRepository).insert(expected);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkInsertAndAddToGiftCertificateShouldReturnExpectedResult() {
        Tag expected = TagTestDataBuilder.aTag().build();
        doReturn(true).when(giftCertificateRepository).existsById(ID);
        doReturn(expected).when(tagRepository).insertAndAddToGiftCertificate(ID, expected);

        Tag actual = tagService.insertAndAddToGiftCertificate(ID, expected);

        verify(giftCertificateRepository).existsById(ID);
        verify(tagRepository).insertAndAddToGiftCertificate(ID, expected);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkInsertAndAddToGiftCertificateShouldThrowResourceNotFoundException() {
        Tag tag = TagTestDataBuilder.aTag().build();
        doReturn(false).when(giftCertificateRepository).existsById(ID);

        assertThatThrownBy(() -> tagService.insertAndAddToGiftCertificate(ID, tag))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void checkUpdateByIdShouldCallRepositoryTwice() {
        Tag tag = TagTestDataBuilder.aTag().build();
        doReturn(true).when(tagRepository).existsById(ID);

        tagService.updateById(ID, tag);

        verify(tagRepository).existsById(ID);
        verify(tagRepository).update(ID, tag);
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
        verify(tagRepository).delete(ID);
    }

    @Test
    void checkDeleteByIdShouldThrowResourceNotFoundException() {
        doReturn(false).when(tagRepository).existsById(ID);

        assertThatThrownBy(() -> tagService.deleteById(ID))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}