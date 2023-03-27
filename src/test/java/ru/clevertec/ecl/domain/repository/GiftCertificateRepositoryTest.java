package ru.clevertec.ecl.domain.repository;

import builder.impl.GiftCertificateCriteriaTestDataBuilder;
import builder.impl.GiftCertificateTestDataBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import ru.clevertec.ecl.domain.entity.GiftCertificate;
import ru.clevertec.ecl.domain.extractor.FieldExtractor;
import ru.clevertec.ecl.domain.extractor.impl.GiftCertificateFieldExtractor;
import ru.clevertec.ecl.domain.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.domain.query.creator.GiftCertificateQueryCreator;
import ru.clevertec.ecl.domain.repository.impl.GiftCertificateRepositoryImpl;
import ru.clevertec.ecl.web.criteria.GiftCertificateCriteria;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class GiftCertificateRepositoryTest {
    private GiftCertificateRepository repository;
    private EmbeddedDatabase dataSource;
    private final GiftCertificateMapper tagMapper = new GiftCertificateMapper();
    private final FieldExtractor<GiftCertificate> fieldExtractor = new GiftCertificateFieldExtractor();
    private final GiftCertificateQueryCreator queryCreator = new GiftCertificateQueryCreator(fieldExtractor);

    private static final Long CORRECT_ID = 1L;

    private static final Long INCORRECT_ID = 1000L;

    @BeforeEach
    void setUp() {
        dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:sql/migration/test/schema.sql")
                .addScript("classpath:sql/migration/test/data.sql")
                .build();
        repository = new GiftCertificateRepositoryImpl(new JdbcTemplate(dataSource), tagMapper, queryCreator);
    }

    @AfterEach
    void clear() {
        dataSource.shutdown();
    }

    @Test
    void checkFindAllShouldReturnNotEmptyResult() {
        Pageable pageable = PageRequest.of(0, 10,
                Sort.by(Sort.Direction.ASC, "name", "description"));
        GiftCertificateCriteria criteria = GiftCertificateCriteriaTestDataBuilder
                .aGiftCertificateCriteria()
                .withName("c")
                .withDescription("d")
                .withTagName("name-1")
                .build();

        List<GiftCertificate> certificates = repository.findAll(pageable, criteria);

        assertThat(certificates).isNotEmpty();
    }

    @Test
    void checkFindAllShouldReturnEmptyResult() {
        Pageable pageable = PageRequest.of(0, 10);
        GiftCertificateCriteria criteria = GiftCertificateCriteriaTestDataBuilder
                .aGiftCertificateCriteria()
                .withName("name-that-not-exists")
                .withDescription("description-that-not-exists")
                .withTagName("tag-name-that-not-exists")
                .build();

        List<GiftCertificate> certificates = repository.findAll(pageable, criteria);

        assertThat(certificates).isEmpty();
    }

    @Test
    void checkFindByIdShouldReturnNotEmptyResult() {
        Optional<GiftCertificate> actual = repository.findById(CORRECT_ID);

        assertThat(actual).isPresent();
    }

    @Test
    void checkFindByIdShouldReturnEmptyResult() {
        Optional<GiftCertificate> actual = repository.findById(INCORRECT_ID);

        assertThat(actual).isNotPresent();
    }

    @Test
    void checkUpdateShouldUpdateCertificate() {
        GiftCertificate giftCertificate = GiftCertificateTestDataBuilder.aGiftCertificate()
                .withName("new-name")
                .build();

        repository.update(CORRECT_ID, giftCertificate);
        Optional<GiftCertificate> actual = repository.findById(CORRECT_ID);

        assertThat(actual).isPresent();
        assertThat(actual.get().getName()).isEqualTo(giftCertificate.getName());
    }

    @Test
    void checkDeleteShouldDelete() {
        repository.delete(CORRECT_ID);

        Optional<GiftCertificate> actual = repository.findById(CORRECT_ID);

        assertThat(actual).isEmpty();
    }

    @Test
    void checkExistsByIdShouldReturnTrue() {
        assertThat(repository.existsById(CORRECT_ID)).isTrue();
    }

    @Test
    void checkExistsByIdShouldReturnFalse() {
        assertThat(repository.existsById(INCORRECT_ID)).isFalse();
    }
}