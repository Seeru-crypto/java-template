package grp.javatemplate.controller;

import grp.javatemplate.controller.dto.UserDto;
import grp.javatemplate.mapper.UserMapper;
import grp.javatemplate.model.User;
import grp.javatemplate.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public List<UserDto> findAll( @RequestParam(required = false) String sortBy ) {
        log.info("REST request to findAll users");
        List<User> res = userService.findAll(sortBy);
        return userMapper.toDto(res);
    }

    @PostMapping
    public UserDto save( @Valid @RequestBody UserDto userDto, HttpServletResponse response ) {
        log.info("REST request to save user " + userDto);
        response.setStatus(HttpServletResponse.SC_CREATED);
        return userMapper.toDto(userService.save(userDto));
    }

    @PutMapping
    public UserDto update( @Valid @RequestBody UserDto userDto ) {
        log.info("REST request to update user " + userDto);
        User user = userMapper.toEntity(userDto);
        return userMapper.toDto(userService.update(user));
    }

    @DeleteMapping(path = "/{userId}")
    public void delete( @PathVariable Long userId ) {
        log.info("REST request to delete user " + userId);
        userService.delete(userId);
    }

//    @PostMapping
//    public OrderDto save( @Valid @RequestBody OrderDto orderDto ) {
//        log.info("REST request to save order " + orderDto);
//        return orderMapper.toDto(orderService.save(orderDto));
//    }
//
//    @PutMapping
//    public OrderDto update( @Valid @RequestBody OrderDto orderDto ) {
//        log.info("REST request to update order " + orderDto);
//        Order order = orderMapper.toEntity(orderDto);
//        return orderMapper.toDto(orderService.update(order));
//    }
//
//    @PutMapping(path = "/{orderId}")
//    public OrderDto updateOrderStatus( @PathVariable Long orderId, @RequestParam String newStatus ) {
//        log.info("REST request to update order status " + orderId);
//        Order updatedOrder = orderService.updateOrderStatus(orderId, newStatus);
//        return orderMapper.toDto(updatedOrder);
//    }
}
