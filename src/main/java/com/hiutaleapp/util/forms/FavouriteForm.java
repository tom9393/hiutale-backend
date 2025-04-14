package com.hiutaleapp.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavouriteForm {

    private Long id;

    public FavouriteForm(Long id) {
        this.id = id;
    }
}
