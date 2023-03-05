package grp.javatemplate.service;

import grp.javatemplate.model.User;
import grp.javatemplate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private static final String USER_NAME = "name";

    public List<User> findAll( String sortBy ) {
        if(Objects.equals(sortBy, USER_NAME)) {
            return userRepository.findAll(Sort.by(Sort.Direction.ASC, USER_NAME));
        }
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

//    public Order save( OrderDto orderDto ) {
//        if(orderRepository.existsByOrderNr(orderDto.getOrderNr())) {
//            throw new OrderException(ORDER_NR_EXISTS);
//        }
//        Order order = new Order()
//                .setOrderNr(orderDto.getOrderNr())
//                .setPriority(orderDto.isPriority())
//                .setStatus(OrderStatus.CREATED);
//
//        Set<Tag> orderTags = getTagDtos(orderDto.getTags());
//
//        List<OrderItem> orderItems = getOrderItems(orderDto.getOrderItems());
//        orderRepository.save(order);
//
//        orderTags.forEach(order::addOrderTag);
//        orderItems.forEach(order::addOrderItem);
//        orderItemRepository.saveAll(orderItems);
//        return order;
//    }

//    private Set<Tag> getTagDtos( Set<TagDto> toDos ) {
//        Set<Tag> result = new HashSet<>();
//        if(toDos.isEmpty()) {
//            return result;
//        }
//
//        for (TagDto tagDto : toDos) {
//            result.add(tagService.findById(tagDto.getId()));
//        }
//        return result;
//    }
//
//    private List<OrderItem> getOrderItems( List<OrderItemDto> orderItemDtos ) {
//        List<OrderItem> orderItems = new ArrayList<>();
//        for (OrderItemDto itemDto : orderItemDtos) {
//            if(!productService.existsById(itemDto.getProductId())) {
//                throw new OrderException(PRODUCT_DOES_NOT_EXIST);
//            }
//
//            List<OrderItem> existingItem = orderItems.stream().filter(item -> Objects.equals(item.getProduct().getId(), itemDto.getProductId())).toList();
//            if(!existingItem.isEmpty()) {
//                throw new OrderException(ORDER_DUPLICATE_PRODUCT);
//            }
//
//            Product product = productService.getById(itemDto.getProductId());
//
//            OrderItem orderItem = new OrderItem()
//                    .setProduct(product)
//                    .setAmount(itemDto.getAmount());
//            orderItems.add(orderItem);
//        }
//        return orderItems;
//    }
//
//    @Transactional
//    public Order update( Order updatedOrder ) throws OrderException {
//        Order originalOrder = orderRepository.findById(updatedOrder.getId())
//                .orElseThrow(() -> new OrderException("given order does not exist"));
//
//        entityManager.detach(originalOrder);
//        fuckOrderItems(updatedOrder.getOrderItems(), originalOrder);
//
//        originalOrder
//                .setPriority(updatedOrder.isPriority())
//                .setOrderNr(updatedOrder.getOrderNr())
//                .setOrderTags(updatedOrder.getTags());
//        return entityManager.merge(originalOrder);
//    }
//
//    private void fuckOrderItems( List<OrderItem> updatedOrderItems, Order originalOrder ) {
//        List<OrderItem> orderItems = new ArrayList<>(
//                originalOrder.getOrderItems()
//        );
//        orderItems.removeAll(updatedOrderItems);
//        for (OrderItem orderItem : orderItems) {
//            originalOrder.removeOrderItem(orderItem);
//        }
//
//        List<OrderItem> newComments = new ArrayList<>(updatedOrderItems);
//        newComments.removeAll(originalOrder.getOrderItems());
//        updatedOrderItems.removeAll(newComments);
//
//        for (OrderItem existingComment : updatedOrderItems) {
//            existingComment.setOrder(originalOrder);
//            OrderItem mergedComment = entityManager.merge(existingComment);
//            originalOrder.getOrderItems()
//                    .set(originalOrder.getOrderItems().indexOf(mergedComment), mergedComment);
//        }
//        for (OrderItem newComment : newComments) {
//            originalOrder.addOrderItem(newComment);
//        }
//    }
//
//    public Order updateOrderStatus( Long orderId, String newStatus ) {
//        Order existingOrder = orderRepository.findById(orderId).orElseThrow(() -> new OrderException(ORDER_DOES_NOT_EXIST));
//
//        OrderStatus currentOrderStatus = existingOrder.getStatus();
//
//        OrderStatus newOrderStatus;
//        try {
//            newOrderStatus = OrderStatus.valueOf(newStatus);
//        } catch (IllegalArgumentException e) {
//            throw new OrderException(ORDER_STATUS_DOES_NOT_EXIST);
//        }
//
//        switch (newOrderStatus) {
//            case ARCHIVED -> {
//                if(!Objects.equals(currentOrderStatus, COMPLETED)) {
//                    throw new OrderException(ORDER_CANNOT_BE_ARCHIVED);
//                }
//                // ToDo: When order_steps are implemented they should also be archived here!
//                orderRepository.save(existingOrder.setStatus(ARCHIVED));
//            }
//            case COMPLETED -> {
//                if(Objects.equals(currentOrderStatus, ARCHIVED)) {
//                    throw new OrderException(ORDER_CANNOT_BE_COMPLETED);
//                }
//                orderRepository.save(existingOrder.setStatus(COMPLETED));
//            }
//            default -> throw new OrderException(ORDER_ERROR);
//        }
//        return existingOrder;
//    }
}
