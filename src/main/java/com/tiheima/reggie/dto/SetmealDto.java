package com.tiheima.reggie.dto;

import com.tiheima.reggie.pojo.Setmeal;
import com.tiheima.reggie.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
