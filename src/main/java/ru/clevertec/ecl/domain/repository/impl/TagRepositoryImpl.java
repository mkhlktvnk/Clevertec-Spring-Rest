package ru.clevertec.ecl.domain.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.domain.constant.column.TagColumns;
import ru.clevertec.ecl.domain.entity.Tag;
import ru.clevertec.ecl.domain.query.TagQueries;
import ru.clevertec.ecl.domain.repository.TagRepository;
import ru.clevertec.ecl.domain.repository.exception.DomainException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Tag> mapper;

    @Override
    public List<Tag> findAll(int page, int size) {
        List<Tag> tags;
        try {
            tags = jdbcTemplate.query(TagQueries.FIND_WITH_LIMIT_AND_OFFSET,
                    new Object[]{ size, page }, mapper);
        } catch (DataAccessException e) {
            throw new DomainException(e.getMessage(), e);
        }
        return tags;
    }

    @Override
    public Optional<Tag> findById(long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(TagQueries.FIND_BY_ID,
                    new Object[]{id}, mapper));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Tag insert(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(TagQueries.INSERT,
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, tag.getName());
                return ps;
            }, keyHolder);
        } catch (DataAccessException e) {
            throw new DomainException(e.getMessage(), e);
        }
        return mapInsertResult(keyHolder.getKeys());
    }

    @Override
    public void delete(Long id) {
        try {
            jdbcTemplate.update(TagQueries.DELETE_BY_ID, id);
        } catch (DataAccessException e) {
            throw new DomainException(e.getMessage(), e);
        }
    }

    private Tag mapInsertResult(Map<String, Object> map) {
        return Tag.builder()
                .id((Long) map.get(TagColumns.ID))
                .name((String) map.get(TagColumns.NAME))
                .build();
    }
}
