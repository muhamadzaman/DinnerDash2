package com.example.userinterface.controllers;

import com.example.userinterface.exceptions.ApiError;
import com.example.userinterface.external.dtos.category.GetCategoryDto;
import com.example.userinterface.external.dtos.item.GetItemDto;
import com.example.userinterface.external.dtos.item.PostItemDto;
import com.example.userinterface.external.dtos.users.GetUserDto;
import com.example.userinterface.external.services.ApiService;
import com.example.userinterface.external.services.impl.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
public class ItemController {

    private final ApiService apiService;

    private final AuthService authService;

    private final HttpServletRequest request;

    public ItemController(ApiService apiService, AuthService authService, HttpServletRequest request) {
        this.apiService = apiService;
        this.authService = authService;
        this.request = request;
    }

    @GetMapping("/items/{id}")
    public String getOneItem(@PathVariable Long id, Model model, RedirectAttributes attributes) throws JsonProcessingException {
        GetItemDto getItemDto;
        try {
            getItemDto = apiService.getItemById(id);
        } catch (FeignException e){
            ApiError apiError = new ObjectMapper().readValue(e.contentUTF8(), ApiError.class);
            attributes.addFlashAttribute("error", apiError);
            return "redirect:/";
        }
        List<GetCategoryDto> categoryDtos = new ArrayList<>();
        getItemDto.getCategoryIds().forEach(
                categoryId -> categoryDtos.add(apiService.getCategoryById(categoryId))
        );
        model.addAttribute("category", categoryDtos);
        model.addAttribute("item", getItemDto);
        return "items/show";
    }

    @GetMapping("/categories/{categoryId}/items")
    public String getItemsByCategory(@PathVariable Long categoryId, RedirectAttributes attributes) throws JsonProcessingException {
        List<GetItemDto> itemDtos = apiService.getItemsByCategoryId(categoryId);
        GetCategoryDto getCategoryDto;
        try {
            getCategoryDto = apiService.getCategoryById(categoryId);
        }catch (FeignException e){
            ApiError apiError = new ObjectMapper().readValue(e.contentUTF8(), ApiError.class);
            attributes.addFlashAttribute("error", apiError);
            return "redirect:/";
        }
        attributes.addFlashAttribute("items", itemDtos);
        attributes.addFlashAttribute("category", getCategoryDto.getName());
        return "redirect:/restaurants/" + getCategoryDto.getRestaurantId();
    }

    @GetMapping("/restaurants/{restaurantId}/items/new")
    public String newItem(Model model, @PathVariable Long restaurantId, RedirectAttributes attributes){
        String auth = getAuthString(attributes);
        if (auth != null) return auth;
        PostItemDto postItemDto = new PostItemDto();
        postItemDto.setRestaurantId(restaurantId);
        List<GetCategoryDto> categoryDtos = apiService.getCategoriesByRestaurantId(restaurantId);
        model.addAttribute("categories", categoryDtos);
        model.addAttribute("item", postItemDto);
        return "items/new";
    }

    @RequestMapping(value = "/restaurants/{restaurantId}/items", method = RequestMethod.POST)
    public String create(@PathVariable Long restaurantId, @ModelAttribute PostItemDto postItemDto, RedirectAttributes attributes) throws JsonProcessingException {
        try {
            apiService.createItem(restaurantId, postItemDto);
        }
        catch (FeignException e){
            ApiError apiError = new ObjectMapper().readValue(e.contentUTF8(), ApiError.class);
            attributes.addFlashAttribute("error", apiError);
            return "redirect:/restaurants/" + restaurantId + "/items/new";
        }
        return "redirect:/restaurants/" + restaurantId;
    }

    @GetMapping("/items/{itemId}/edit")
    public String edit(@PathVariable Long itemId, Model model, RedirectAttributes attributes){
        String auth = getAuthString(attributes);
        if (auth != null) return auth;
        GetItemDto getItemDto = apiService.getItemById(itemId);
        model.addAttribute("item", getItemDto);
        List<GetCategoryDto> categoryDtos = apiService.getCategoriesByRestaurantId(getItemDto.getRestaurantId());
        model.addAttribute("categories", categoryDtos);
        return "items/edit";
    }

    @PutMapping("/items/{itemId}")
    public String update(@PathVariable Long itemId, @ModelAttribute PostItemDto postItemDto, RedirectAttributes attributes) throws JsonProcessingException {
        GetItemDto getItemDto;
        try {
            getItemDto = apiService.updateItem(itemId, postItemDto);
        }catch (FeignException e){
            ApiError apiError = new ObjectMapper().readValue(e.contentUTF8(), ApiError.class);
            attributes.addFlashAttribute("error", apiError);
            return "redirect:/items/" + itemId + "/edit";
        }
        return "redirect:/restaurants/" + getItemDto.getRestaurantId();
    }

    private String getAuthString(RedirectAttributes attributes) {
        return getString(attributes, authService, request);
    }

    static String getString(RedirectAttributes attributes, AuthService authService, HttpServletRequest request) {
        GetUserDto user = authService.getLoggedInUser(request);
        if (user == null || !user.getRole().equals("ADMIN")) {
            attributes.addFlashAttribute("errors", user == null ? "User not logged in" : "You are not authorized");
            return user == null ? "redirect:/users/sign_in/new" : "redirect:/";
        }
        return null;
    }

    @PutMapping("/items/{itemId}/retire")
    public String retireItem(@PathVariable Long itemId){
        GetItemDto itemDto = apiService.retireItem(itemId);
        return "redirect:/items/" + itemDto.getId();
    }

    @PutMapping("/items/{itemId}/allow")
    public String allowItem(@PathVariable Long itemId){
        GetItemDto itemDto = apiService.allowItem(itemId);
        return "redirect:/items/" + itemDto.getId();
    }

    @PutMapping("/items/{itemId}/unassign")
    public String unassignCategory(@PathVariable Long itemId, @RequestParam("category") Long categoryId){
        GetItemDto itemDto = apiService.unassignCategory(itemId, categoryId);
        return "redirect:/items/" + itemDto.getId();
    }

    @GetMapping("/restaurants/{restaurantId}/famous-item")
    public String getFavouriteItem(@PathVariable Long restaurantId, RedirectAttributes attributes){
        GetItemDto itemDto = apiService.getFamousItem(restaurantId);
        attributes.addFlashAttribute("items", Collections.singletonList(itemDto));
        attributes.addFlashAttribute("category", "Customer Favourite");
        return "redirect:/restaurants/" + restaurantId;
    }
}
