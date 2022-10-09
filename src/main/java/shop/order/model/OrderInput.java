package shop.order.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderInput {

    long productId;
    String userId;
    long productOrderId;

}
