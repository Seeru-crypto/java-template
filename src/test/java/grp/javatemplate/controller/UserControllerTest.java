package grp.javatemplate.controller;

import grp.javatemplate.TestObjects;
import grp.javatemplate.controller.dto.UserDto;
import grp.javatemplate.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static grp.javatemplate.TestObjects.createUser;
import static grp.javatemplate.TestObjects.createUserDto;
import static grp.javatemplate.exception.UserException.USER_EXISTS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
class UserControllerTest extends BaseIntegrationTest {
    private static final String API_PATH = "/users";

    @Test
    void findAll_shouldReturnAllUsersOrderedById() throws Exception {
        User firstUser = createUser().setName("Alice");
        entityManager.persist(firstUser);

        User secondUser = createUser().setName("Bob");
        entityManager.persist(secondUser);

        mockMvc.perform(get(API_PATH))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Alice"));
    }

    @Test
    void findAll_shouldReturnAllUsersOrderedByName() throws Exception {
        User firstUser = createUser().setName("Alice");
        entityManager.persist(firstUser);

        User secondUser = createUser().setName("Bob");
        entityManager.persist(secondUser);

        mockMvc.perform(get(API_PATH + "?sortBy=name"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Alice"));
    }

    @Test
    void save_shouldSaveUser() throws Exception {
        UserDto userDto = createUserDto().setName("Alice");
        byte[] bytes = getBytes(userDto);

        mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bytes))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.dob").value(userDto.getDob().toString()));
    }

    @Test
    void update_shouldUpdateUser() throws Exception {
        User user = createUser().setName("Alice");
        entityManager.persist(user);

        UserDto userDto = userMapper.toDto(user).setName("Bob");
        byte[] bytes = getBytes(userDto);

        mockMvc.perform(put(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bytes))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bob"));

        List<User> createdUsers = findAll(User.class);
        assertEquals("Bob", createdUsers.get(0).getName());
        assertNotNull(createdUsers.get(0).getDob());
        assertNotNull(createdUsers.get(0).getCreatedAt());
        assertNotNull(createdUsers.get(0).getCreatedBy());
    }

    @Test
    void update_shouldReturn404IfUserNotFound() throws Exception {
        UserDto userDto = createUserDto().setName("Bob");
        byte[] bytes = getBytes(userDto);

        mockMvc.perform(put(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bytes))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value(USER_EXISTS));
    }

    @Test
    void delete_shouldDeleteUser() throws Exception {
        User user = createUser().setName("Alice");
        entityManager.persist(user);

        mockMvc.perform(delete(API_PATH + "/" + user.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        List<User> createdUsers = findAll(User.class);
        assertEquals(0, createdUsers.size());
    }

    @Test
    void delete_shouldReturn404IfUserNotFound() throws Exception {
        mockMvc.perform(delete(API_PATH + "/1"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value(USER_EXISTS));
    }






//    @Test
//    void findAll_shouldReturnOrderedList() throws Exception {
//        Tag tag = createTag();
//        entityManager.persist(tag);
//
//        Order firstOrder = createOrder().setOrderNr("C number");
//        entityManager.persist(firstOrder);
//
//        firstOrder.addOrderTag(tag);
//
//        Order secondOrder = createOrder().setOrderNr("A number");
//        entityManager.persist(secondOrder);
//
//        Order thirdOrder = createOrder().setOrderNr("B number");
//        entityManager.persist(thirdOrder);
//
//        Product product = createProduct();
//        entityManager.persist(product);
//
//        OrderItem orderItem = createOrderItem().setOrder(firstOrder).setProduct(product);
//        entityManager.persist(orderItem);
//        firstOrder.setOrderItems(List.of(orderItem));
//
//        String url = API_PATH + "?sortBy=orderNr";
//        mockMvc.perform(get(url))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(3))
//                .andExpect(jsonPath("$[0].orderNr").value(secondOrder.getOrderNr()))
//                .andExpect(jsonPath("$[1].orderNr").value(thirdOrder.getOrderNr()))
//                .andExpect(jsonPath("$[2].orderNr").value(firstOrder.getOrderNr()));
//    }
//
//    @Test
//    void saveOrder_shouldSaveOrderAndItems() throws Exception {
//        Product product = createProduct();
//        Tag tag = createTag();
//        persistCreatedEntities(entityManager);
//
//        OrderItemDto orderItemDto = new OrderItemDto()
//                .setAmount(PRODUCT_AMOUNT)
//                .setProductId(product.getId());
//
//        OrderDto orderDto = createOrderDto()
//                .setOrderItems(List.of(orderItemDto))
//                .setTags(Set.of(new TagDto()
//                        .setName(tag.getName())
//                        .setId(tag.getId())))
//                .setPriority(NEW_PRIORITY);
//
//        mockMvc.perform(post(API_PATH)
//                        .contentType(APPLICATION_JSON)
//                        .content(getBytes(orderDto)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value(CREATED.toString()))
//                .andExpect(jsonPath("$.orderNr").value(ORDER_NUMBER))
//                .andExpect(jsonPath("$.id").isNotEmpty())
//                .andExpect(jsonPath("$.orderItems[0].productId").value(product.getId()))
//                .andExpect(jsonPath("$.orderItems[0].amount").value(PRODUCT_AMOUNT))
//                .andExpect(jsonPath("$.tags[0].name").value(tag.getName()))
//                .andExpect(jsonPath("$.priority").value(NEW_PRIORITY));
//        assertEquals(1, findAll(Order.class).size());
//        List<OrderItem> createdItems = findAll(OrderItem.class);
//        assertEquals(1, createdItems.size());
//        assertNotNull(createdItems.get(0).getCreatedAt());
//        assertNotNull(createdItems.get(0).getCreatedBy());
//        assertNotNull(createdItems.get(0).getModifiedAt());
//        assertNotNull(createdItems.get(0).getModifiedBy());
//        assertEquals(createdItems.get(0).getModifiedAt(), createdItems.get(0).getCreatedAt());
//    }
//
//    @Test
//    void saveOrder_shouldThrowOrderException_whenProductDoesNotExist() throws Exception {
//        OrderItemDto orderItemDto = new OrderItemDto()
//                .setAmount(2)
//                .setProductId(97L);
//
//        OrderDto orderDto = createOrderDto()
//                .setOrderItems(List.of(orderItemDto))
//                .setPriority(true);
//
//        mockMvc.perform(post(API_PATH)
//                        .contentType(APPLICATION_JSON)
//                        .content(getBytes(orderDto)))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$[0]").value(PRODUCT_DOES_NOT_EXIST));
//    }
//
//    @Test
//    void saveOrder_shouldThrowOrderException_whenDuplicateProductIds() throws Exception {
//        Product product = createProduct();
//        entityManager.persist(product);
//
//        OrderItemDto orderItemDto = new OrderItemDto()
//                .setAmount(2)
//                .setProductId(product.getId());
//
//        OrderItemDto orderItemDto2 = new OrderItemDto()
//                .setAmount(2)
//                .setProductId(product.getId());
//
//        OrderDto orderDto = new OrderDto()
//                .setOrderNr("123")
//                .setTags(new HashSet<>())
//                .setOrderItems(List.of(orderItemDto, orderItemDto2))
//                .setPriority(true);
//
//        mockMvc.perform(post(API_PATH)
//                        .contentType(APPLICATION_JSON)
//                        .content(getBytes(orderDto)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$[0]").value(ORDER_DUPLICATE_PRODUCT));
//    }
//
//    @Test
//    void saveOrder_shouldThrowOrderException_whenNoAmountSpecified() throws Exception {
//        Product product = createProduct();
//        entityManager.persist(product);
//
//        OrderItemDto orderItemDto = new OrderItemDto()
//                .setAmount(0)
//                .setProductId(product.getId());
//
//        OrderDto orderDto = createOrderDto()
//                .setOrderItems(List.of(orderItemDto));
//
//        mockMvc.perform(post(API_PATH)
//                        .contentType(APPLICATION_JSON)
//                        .content(getBytes(orderDto)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$[0]").value(ORDER_WRONG_AMOUNT));
//    }
//
//    @Test
//    void updateOrder_shouldIgnoreGivenStatus_whenUpdating() throws Exception {
//        Order order = createOrder();
//        entityManager.persist(order);
//
//        Tag oldTag = createTag().setName("old tag 1");
//        entityManager.persist(oldTag);
//        order.addOrderTag(oldTag);
//
//        Tag oldTag2 = createTag().setName("old tag 2");
//        entityManager.persist(oldTag2);
//        order.addOrderTag(oldTag2);
//
//        Product oldProduct = createProduct().setName("old product 1");
//        entityManager.persist(oldProduct);
//        OrderItem oldOrderItem = createOrderItem().setOrder(order).setProduct(oldProduct);
//        entityManager.persist(oldOrderItem);
//        order.addOrderItem(oldOrderItem);
//
//        Product oldProduct2 = createProduct().setName("old product 2");
//        entityManager.persist(oldProduct2);
//        OrderItem oldOrderItem2 = createOrderItem().setOrder(order).setProduct(oldProduct2);
//        entityManager.persist(oldOrderItem2);
//        order.addOrderItem(oldOrderItem2);
//
//        Product newProduct = createProduct().setName("new product 1");
//        entityManager.persist(newProduct);
//        Tag newTag = createTag().setName("new tag 1");
//        entityManager.persist(newTag);
//
//        int newAmount = 420;
//
//        OrderItemDto orderItemDto1 = new OrderItemDto()
//                .setAmount(newAmount)
//                .setProductId(newProduct.getId())
//                .setProductName(newProduct.getName());
//
//        OrderItemDto orderItemDto2 = new OrderItemDto()
//                .setAmount(newAmount)
//                .setProductId(oldProduct.getId())
//                .setProductName(oldProduct.getName());
//
//        TagDto newTagDto = new TagDto()
//                .setName(newTag.getName())
//                .setId(newTag.getId());
//
//        TagDto newTagDto2 = new TagDto()
//                .setName(oldTag.getName())
//                .setId(oldTag.getId());
//
//        boolean newPriority = !order.isPriority();
//
//        OrderDto orderDto = createOrderDto()
//                .setId(order.getId())
//                .setTags(Set.of(newTagDto, newTagDto2))
//                .setStatus(OrderStatus.IN_PROGRESS)
//                .setPriority(newPriority)
//                .setOrderNr(order.getOrderNr())
//                .setOrderItems(List.of(orderItemDto1, orderItemDto2));
//
//        mockMvc.perform(put(API_PATH)
//                        .contentType(APPLICATION_JSON)
//                        .content(getBytes(orderDto)))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(jsonPath("$.priority").value(newPriority))
//                .andExpect(jsonPath("$.status").value(CREATED.toString()))
//                .andExpect(jsonPath("$.orderItems.length()").value(2))
//                .andExpect(jsonPath("$.orderItems[*].productName").value(containsInAnyOrder(orderItemDto1.getProductName(), orderItemDto2.getProductName())))
//                .andExpect(jsonPath("$.orderItems[*].productId").value(containsInAnyOrder(orderItemDto1.getProductId().intValue(), orderItemDto2.getProductId().intValue())))
//                .andExpect(jsonPath("$.tags.length()").value(2))
//                .andExpect(jsonPath("$.tags[*].id").value(containsInAnyOrder(newTagDto.getId().intValue(), newTagDto2.getId().intValue())))
//                .andExpect(jsonPath("$.tags[*].name").value(containsInAnyOrder(newTagDto.getName(), newTagDto2.getName())));
//    }
//
//    @Test
//    void updateOrderStatus_shouldUpdate_whenOrderStatusIsCompleted() throws Exception {
//        Order order = createOrder().setStatus(COMPLETED);
//        entityManager.persist(order);
//
//        String path = API_PATH + "/" + order.getId() + "?newStatus=" + ARCHIVED;
//        mockMvc.perform(put(path))
//                .andExpect(status().isOk());
//
//        String newStatus = (findAll(Order.class)).get(0).getStatus().toString();
//        assertEquals(ARCHIVED.toString(), newStatus);
//    }
//
//    @Test
//    void updateOrderStatus_shouldUpdate_whenOrderStatusIsCreated() throws Exception {
//        Order order = createOrder().setStatus(CREATED);
//        entityManager.persist(order);
//
//        String path = API_PATH + "/" + order.getId() + "?newStatus=" + COMPLETED;
//        mockMvc.perform(put(path))
//                .andDo(print())
//                .andExpect(jsonPath("$.status").value(COMPLETED.toString()))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void updateOrderStatus_shouldThrowException_whenOrderStatusIsCreated() throws Exception {
//        Tag tag = createTag();
//        entityManager.persist(tag);
//
//        Order order = createOrder().setStatus(CREATED);
//        entityManager.persist(order);
//
//        String path = API_PATH + "/" + order.getId() + "?newStatus=" + ARCHIVED;
//        mockMvc.perform(put(path))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$[0]").value(ORDER_CANNOT_BE_ARCHIVED));
//
//    }
}