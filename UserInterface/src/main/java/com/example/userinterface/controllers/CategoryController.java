package com.example.userinterface.controllers;

import com.example.userinterface.exceptions.ApiError;
import com.example.userinterface.external.dtos.category.PostCategoryDto;
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

import static com.example.userinterface.controllers.ItemController.getString;

@Controller
public class CategoryController {

    private final AuthService authService;

    private final HttpServletRequest request;

    private final ApiService apiService;

    public CategoryController(AuthService authService, HttpServletRequest request, ApiService apiService1) {
        this.authService = authService;
        this.request = request;
        this.apiService = apiService1;
    }

    @GetMapping("/restaurants/{restaurantId}/categories/new")
    public String newCategory(Model model, @PathVariable Long restaurantId, RedirectAttributes attributes) {
        String auth = getAuthString(attributes);
        if (auth != null) return auth;
        PostCategoryDto postCategoryDto = new PostCategoryDto();
        postCategoryDto.setRestaurantId(restaurantId);
        model.addAttribute("category", postCategoryDto);
        return "categories/new";
    }

    @RequestMapping(value = "/categories", method = RequestMethod.POST)
    public String create(@ModelAttribute PostCategoryDto postCategoryDto, RedirectAttributes attributes) throws JsonProcessingException {
        try {
            apiService.saveCategory(postCategoryDto);
        }catch (FeignException e){
            ApiError apiError = new ObjectMapper().readValue(e.contentUTF8(), ApiError.class);
            attributes.addFlashAttribute("error", apiError);
            return "redirect:/restaurants/" + postCategoryDto.getRestaurantId() + "/categories/new";
        }
        return "redirect:/restaurants/" + postCategoryDto.getRestaurantId();
    }

    private String getAuthString(RedirectAttributes attributes) {
        return getString(attributes, authService, request);
    }
}
