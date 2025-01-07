package kr.co.ordermanagement.presentation.dto;

import kr.co.ordermanagement.domain.order.Order;
import kr.co.ordermanagement.domain.order.OrderedProduct;
import kr.co.ordermanagement.domain.order.State;

import java.util.ArrayList;
import java.util.List;

public class OrderResponseDto {
    private Long id;
    private List<OrderedProductDto> orderedProducts;
    private Integer totalPrice;
    private State state;

    public OrderResponseDto(Long id, List<OrderedProductDto> orderedProducts, Integer totalPrice, State state) {
        this.id = id;
        this.orderedProducts = orderedProducts;
        this.totalPrice = totalPrice;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public List<OrderedProductDto> getOrderedProducts() {
        return orderedProducts;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public State getState() {
        return state;
    }

    public static OrderResponseDto toDto(Order order) {
        List<OrderedProductDto> orderedProductDtos = new ArrayList<>();
        for (OrderedProduct orderedProduct : order.getOrderedProducts()) {
            orderedProductDtos.add(OrderedProductDto.toDto(orderedProduct));
        }
        return new OrderResponseDto(
                order.getId(),
                orderedProductDtos,
                order.getTotalPrice(),
                order.getState()
        );
    }

}
