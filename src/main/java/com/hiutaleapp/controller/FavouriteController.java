package com.hiutaleapp.controller;

import com.hiutaleapp.dto.FavouriteDTO;
import com.hiutaleapp.entity.Favourite;
import com.hiutaleapp.service.FavouriteService;
import com.hiutaleapp.util.UserContext;
import com.hiutaleapp.util.forms.FavouriteForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favourites")
public class FavouriteController {
    @Autowired
    private FavouriteService favouriteService;

    @GetMapping("/all")
    public List<FavouriteDTO> getAllFavourites() {
        return favouriteService.getAllFavourites();
    }

    @GetMapping("/one/{id}")
    public ResponseEntity<FavouriteDTO> getFavouriteById(@PathVariable Long id) {
        return favouriteService.getFavouriteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/me")
    public List<FavouriteDTO> getFavouritesByUser() {
        return favouriteService.getFavouriteByUser(UserContext.getUserId());

    }

    @PostMapping("/create")
    public FavouriteDTO createFavourite(@RequestBody FavouriteForm favouriteForm) {
        return favouriteService.createFavourite(UserContext.getUserId(), favouriteForm);
    }

    @PutMapping("/update/{id}")
    public FavouriteDTO updateFavourite(@PathVariable Long id, @RequestBody Favourite favourite) {
        return favouriteService.updateFavourite(id, favourite);
    }

    @DeleteMapping("/delete/{id}")
    public Boolean deleteFavourite(@PathVariable Long id) {
        return favouriteService.deleteFavourite(UserContext.getUserId(), id);
    }
}