package com.example.userinterface.external.services;

import com.example.userinterface.external.dtos.cart.GetCartDto;
import com.example.userinterface.external.dtos.cart.PostCartDto;
import com.example.userinterface.external.dtos.category.GetCategoryDto;
import com.example.userinterface.external.dtos.category.PostCategoryDto;
import com.example.userinterface.external.dtos.item.GetItemDto;
import com.example.userinterface.external.dtos.item.PostItemDto;
import com.example.userinterface.external.dtos.orders.GetOrderDto;
import com.example.userinterface.external.dtos.orders.PostOrderDto;
import com.example.userinterface.external.dtos.restaurant.GetRestaurantDto;
import com.example.userinterface.external.dtos.restaurant.PostRestaurantDto;
import com.example.userinterface.external.dtos.users.GetUserDto;
import com.example.userinterface.external.dtos.users.LoginDto;
import com.example.userinterface.external.dtos.users.RegisterDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "API-GATEWAY")
public interface ApiService {

    @GetMapping("/restaurants")
    List<GetRestaurantDto> getAllRestaurants();

    @GetMapping("/restaurants/{restaurantId}")
    GetRestaurantDto findRestaurantById(@PathVariable Long restaurantId);

    @PostMapping("/restaurants")
    GetRestaurantDto createRestaurant(PostRestaurantDto restaurantDto);

    @DeleteMapping("/restaurants/{restaurantId}")
    void deleteRestaurant(@PathVariable Long restaurantId);

    @PutMapping("/restaurants/{restaurantId}")
    GetRestaurantDto updateRestaurant(@PathVariable Long restaurantId, PostRestaurantDto restaurantDto);

    @PostMapping("/categories")
    GetCategoryDto saveCategory(@RequestBody PostCategoryDto categoryDto);

    @GetMapping("/items/restaurants/{restaurantId}")
    List<GetItemDto> getItemsByRestaurant(@PathVariable Long restaurantId);

    @GetMapping("/items/{itemId}")
    GetItemDto getItemById(@PathVariable Long itemId);

    @GetMapping("/items/categories/{categoryId}")
    List<GetItemDto> getItemsByCategoryId(@PathVariable Long categoryId);

    @PostMapping(value = "/items/restaurants/{restaurantId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    GetItemDto createItem(@PathVariable Long restaurantId, PostItemDto postItem);

    @GetMapping("/categories/restaurants/{restaurantId}")
    List<GetCategoryDto> getCategoriesByRestaurantId(@PathVariable Long restaurantId);

    @GetMapping("/categories/{categoryId}")
    GetCategoryDto getCategoryById(@PathVariable Long categoryId);

    @PutMapping(value = "/items/{itemId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    GetItemDto updateItem(@PathVariable Long itemId, PostItemDto itemDto);

    @PostMapping("/registration")
    GetUserDto registerUser(RegisterDto registerDto);

    @PostMapping(value = "/sign_in", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    GetUserDto loginUser(LoginDto loginDto);

    @PostMapping("/sign_in/verify_token")
    GetUserDto verifyUser(@RequestHeader String token);

    @PostMapping("/users/logout")
    Boolean logout(@RequestHeader String token);

    @PostMapping("/cart/users/{userId}")
    GetCartDto saveCartFromSession(@PathVariable Long userId, @RequestBody GetCartDto cartDto);

    @GetMapping("/cart/users/{userId}")
    GetCartDto getCartByUserId(@PathVariable Long userId);

    @PostMapping("/cart/users/{userId}/items/{itemId}/increment")
    GetCartDto incrementQuantity(@PathVariable Long userId, @PathVariable Long itemId);

    @PostMapping("/cart/users/{userId}/items/{itemId}/decrement")
    GetCartDto decrementQuantity(@PathVariable Long userId, @PathVariable Long itemId);

    @DeleteMapping("/cart/users/{userId}")
    void deleteCartByUserId(@PathVariable Long userId);

    @PostMapping("/cart/users/{userId}/save")
    GetCartDto saveCartItems(@PathVariable Long userId, @RequestBody PostCartDto cartDto);

    @DeleteMapping("/cart/users/{userId}/items/{itemId}/remove")
    GetCartDto removeCartItem(@PathVariable Long userId, @PathVariable Long itemId);

    @GetMapping("/orders")
    List<GetOrderDto> getAllOrders();

    @GetMapping("/orders/users/{userId}")
    List<GetOrderDto> getAllOrdersByUserId(@PathVariable Long userId);

    @PostMapping("/orders/users/{userId}")
    GetOrderDto createOrder(@PathVariable Long userId, @RequestBody PostOrderDto orderDto);

    @PutMapping("/orders/{orderId}/paid")
    GetOrderDto updateOrderStatusToPaid(@PathVariable Long orderId);

    @PutMapping("/orders/{orderId}/completed")
    GetOrderDto updateOrderStatusToCompleted(@PathVariable Long orderId);

    @PutMapping("/orders/{orderId}/cancelled")
    GetOrderDto updateOrderStatusToCancelled(@PathVariable Long orderId);

    @GetMapping("/orders/{orderId}")
    GetOrderDto getOrderByOrderId(@PathVariable Long orderId);

    @PutMapping("/items/{itemId}/retire")
    GetItemDto retireItem(@PathVariable Long itemId);

    @PutMapping("/items/{itemId}/allow")
    GetItemDto allowItem(@PathVariable Long itemId);

    @PutMapping("/items/{itemId}/unassign")
    GetItemDto unassignCategory(@PathVariable Long itemId, @RequestBody Long categoryId);

    @GetMapping("/users/{userId}")
    GetUserDto getUserById(@PathVariable Long userId);

    @GetMapping("/items/restaurants/{restaurantId}/famous")
    GetItemDto getFamousItem(@PathVariable Long restaurantId);
}
