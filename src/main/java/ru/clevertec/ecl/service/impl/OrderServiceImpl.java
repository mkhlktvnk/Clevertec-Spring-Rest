package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.domain.entity.GiftCertificate;
import ru.clevertec.ecl.domain.entity.Order;
import ru.clevertec.ecl.domain.entity.User;
import ru.clevertec.ecl.domain.repository.OrderRepository;
import ru.clevertec.ecl.service.GiftCertificateService;
import ru.clevertec.ecl.service.OrderService;
import ru.clevertec.ecl.service.UserService;
import ru.clevertec.ecl.service.exception.ResourceNotFoundException;
import ru.clevertec.ecl.service.message.MessageKey;
import ru.clevertec.ecl.service.message.MessagesSource;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final GiftCertificateService giftCertificateService;
    private final MessagesSource messages;

    @Override
    public List<Order> findAllByUserId(long userId, Pageable pageable) {
        if (!userService.existsById(userId)) {
            throw new ResourceNotFoundException(
                    messages.get(MessageKey.ORDER_NOT_FOUND)
            );
        }
        return orderRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public Order findById(long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messages.get(MessageKey.ORDER_NOT_FOUND)
                ));
    }

    @Override
    @Transactional
    public Order makeOrder(long userId, long certificateId) {
        User user = userService.findById(userId);
        GiftCertificate giftCertificate = giftCertificateService.findById(certificateId);

        Order order = new Order();
        order.setUser(user);
        order.setGiftCertificate(giftCertificate);

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    messages.get(MessageKey.ORDER_NOT_FOUND)
            );
        }
        orderRepository.deleteById(id);
    }

}
