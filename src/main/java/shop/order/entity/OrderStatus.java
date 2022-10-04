package shop.order.entity;

public interface OrderStatus {

    /**
     * 상품 신청 , 취소, 완료
     */
    String STATUS_REQ = "REQ";
    String STATUS_CANCEL = "CANCEL";
    String STATUS_COMPLETE = "COMPLETE";

}
