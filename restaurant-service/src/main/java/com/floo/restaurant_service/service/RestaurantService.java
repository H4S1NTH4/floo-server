package com.floo.restaurant_service.service;

import com.floo.restaurant_service.dto.RestaurantDto;
import com.floo.restaurant_service.dto.RestaurantRequest;
import com.floo.restaurant_service.dto.RestaurantResponse;
import com.floo.restaurant_service.model.MenuItem;
import com.floo.restaurant_service.model.OwnerInfo;
import com.floo.restaurant_service.model.Restaurant;
import com.floo.restaurant_service.repository.MenuItemRepository;
import com.floo.restaurant_service.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    private final String ROLE = "RESTAURANT_OWNER";
    private final String ADMIN_ROLE = "ADMIN";

    public String addRestaurant(RestaurantRequest request) {
        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .description(request.getDescription())
                .contactInfo(request.getContactInfo())
                .address(request.getAddress())
                .rating(request.getRating())
                .isAvailable(true)
                .isVerified(false)
                .build();

        Restaurant saved = restaurantRepository.save(restaurant);
        return "Restaurant with id: " + saved.getId() + " added successfully";
    }

    public RestaurantResponse getRestaurant(String id) {
        return restaurantRepository.findById(id)
                .map(restaurant -> RestaurantResponse.builder()
                        .restaurantDto(getRestaurantDto(restaurant))
                        .responseCode(200)
                        .msg("Success")
                        .build())
                .orElse(RestaurantResponse.builder()
                        .responseCode(404)
                        .msg("Restaurant not found")
                        .build());
    }

    @Transactional(readOnly = true)
    public List<RestaurantDto> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(this::getRestaurantDto)
                .collect(Collectors.toList());
    }

    public String updateRestaurant(String id, RestaurantRequest request) {
        return restaurantRepository.findById(id)
                .map(restaurant -> {
                    restaurant.setName(request.getName());
                    restaurant.setDescription(request.getDescription());
                    restaurant.setAddress(request.getAddress());
                    restaurant.setContactInfo(request.getContactInfo());
                    restaurant.setRating(request.getRating());
                    restaurantRepository.save(restaurant);
                    return "Restaurant updated successfully";
                })
                .orElse("Restaurant not found");
    }

    public String updateAvailability(String id, boolean isAvailable) {
        return restaurantRepository.findById(id)
                .map(restaurant -> {
                    restaurant.setAvailable(isAvailable);
                    restaurantRepository.save(restaurant);
                    return "Availability updated to: " + isAvailable;
                })
                .orElse("Restaurant not found");
    }

    @Transactional(readOnly = true)
    public List<RestaurantDto> getPendingRestaurants() {
        return restaurantRepository.findByIsVerifiedFalse().stream()
                .map(this::getRestaurantDto)
                .collect(Collectors.toList());
    }

    public String verifyRestaurant(String id, boolean isApproved) {
        return restaurantRepository.findById(id)
                .map(restaurant -> {
                    if (isApproved) {
                        restaurant.setVerified(true);
                        restaurantRepository.save(restaurant);
                        return "Restaurant approved successfully";
                    } else {
                        restaurantRepository.delete(restaurant);
                        return "Restaurant rejected and removed";
                    }
                })
                .orElse("Restaurant not found");
    }

    private RestaurantDto getRestaurantDto(Restaurant restaurant) {
        RestaurantDto dto = RestaurantDto.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .description(restaurant.getDescription())
                .address(restaurant.getAddress())
                .contactInfo(restaurant.getContactInfo())
                .rating(restaurant.getRating())
                .isAvailable(restaurant.isAvailable())
                .isVerified(restaurant.isVerified())
                .build();

        List<MenuItem> menuItems = menuItemRepository.findByRestaurantId(restaurant.getId());
        dto.setMenuItems(menuItems);
        return dto;
    }
}