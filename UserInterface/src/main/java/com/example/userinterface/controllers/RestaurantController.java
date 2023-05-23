package com.example.userinterface.controllers;

import com.example.userinterface.exceptions.ApiError;
import com.example.userinterface.external.dtos.item.GetItemDto;
import com.example.userinterface.external.dtos.restaurant.GetRestaurantDto;
import com.example.userinterface.external.dtos.restaurant.PostRestaurantDto;
import com.example.userinterface.external.dtos.users.GetUserDto;
import com.example.userinterface.external.services.ApiService;
import com.example.userinterface.external.services.CartSessionService;
import com.example.userinterface.external.services.impl.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.example.userinterface.controllers.ItemController.getString;

@Controller
public class RestaurantController {

    private final ApiService apiService;

    private final AuthService authService;

    private final CartSessionService cartSessionService;

    private final HttpServletRequest request;

    public RestaurantController(ApiService apiService, AuthService authService, CartSessionService cartSessionService, HttpServletRequest request) {
        this.apiService = apiService;
        this.authService = authService;
        this.cartSessionService = cartSessionService;
        this.request = request;
    }

    @RequestMapping("/")
    public String index(Model model) {
        List<GetRestaurantDto> restaurantDtos = apiService.getAllRestaurants();
        model.addAttribute("restaurants", restaurantDtos);
        return "restaurants/index";
    }

    @RequestMapping(value = "/restaurants/{restaurantId}", method = RequestMethod.GET)
    public String show(Model model, @PathVariable Long restaurantId, RedirectAttributes attributes) {
        GetRestaurantDto restaurantDto;
        try {
            restaurantDto = apiService.findRestaurantById(restaurantId);
        }catch (FeignException e) {
            attributes.addFlashAttribute("error", parseFeignError(e));
            return "redirect:/";
        }
        model.addAttribute("restaurant", restaurantDto);
        return "restaurants/show";
    }

    @GetMapping("/restaurants/new")
    public String newRestaurant(Model model, RedirectAttributes attributes) {
        String auth = getAuthString(attributes);
        if (auth != null) return auth;
        model.addAttribute("restaurant", new PostRestaurantDto());
        return "restaurants/new";
    }

    @RequestMapping(value = "/restaurants", method = RequestMethod.POST)
    public String create(@ModelAttribute PostRestaurantDto restaurant, RedirectAttributes attributes) throws JsonProcessingException {
        GetRestaurantDto restaurantDto;
        try {
            restaurantDto = apiService.createRestaurant(restaurant);
        }catch (FeignException e){
            attributes.addFlashAttribute("error", parseFeignError(e));
            return "redirect:/restaurants/new";
        }

        return "redirect:/restaurants/" + restaurantDto.getId();
    }

    @RequestMapping(value = "/restaurants/{restaurantId}", method = RequestMethod.DELETE)
    public String delete(@PathVariable Long restaurantId, RedirectAttributes attributes) {
        String auth = getAuthString(attributes);
        if (auth != null) return auth;
        List<GetItemDto> items = apiService.getItemsByRestaurant(restaurantId);
        items.forEach(
                item -> cartSessionService.removeItem(request, item.getId())
        );
        try {
            apiService.deleteRestaurant(restaurantId);
        } catch (FeignException e) {
            attributes.addFlashAttribute("error", parseFeignError(e));
        }
        return "redirect:/";
    }

    private String getAuthString(RedirectAttributes attributes) {
        return getString(attributes, authService, request);
    }

    private ApiError parseFeignError(FeignException e) {
        try {
            return new ObjectMapper().readValue(e.contentUTF8(), ApiError.class);
        } catch (JsonProcessingException jpe) {
            return ApiError.builder().message("An error occurred while parsing the error message").build();
        }
    }


    @RequestMapping(value = "/restaurants/{restaurantId}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable Long restaurantId, Model model, RedirectAttributes attributes){
        String auth = getAuthString(attributes);
        if (auth != null) return auth;
        GetRestaurantDto getRestaurantDto = apiService.findRestaurantById(restaurantId);
        model.addAttribute("restaurant", getRestaurantDto);
        return "restaurants/edit";
    }

    @RequestMapping(value = "/restaurants/{restaurantId}", method = RequestMethod.PUT)
    public ModelAndView update(@ModelAttribute PostRestaurantDto restaurantDto, @PathVariable Long restaurantId){
        GetRestaurantDto getRestaurantDto = apiService.updateRestaurant(restaurantId, restaurantDto);
        ModelAndView model = new ModelAndView();
        model.setViewName("restaurants/show");
        model.addObject("restaurant", getRestaurantDto);
        return model;
    }
}
