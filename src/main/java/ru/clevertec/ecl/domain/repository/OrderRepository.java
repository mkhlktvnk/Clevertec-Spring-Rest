package ru.clevertec.ecl.domain.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.domain.entity.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(long userId, Pageable pageable);
}