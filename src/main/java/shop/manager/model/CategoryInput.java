package shop.manager.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CategoryInput {
    // list.html에 있는 categoryName

    String categoryName;
    long id;
    int sortValue;
    boolean usingYn;
    //LocalDateTime regDt;

}
