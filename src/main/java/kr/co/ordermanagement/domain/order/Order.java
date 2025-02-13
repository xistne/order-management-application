package kr.co.ordermanagement.domain.order;

import java.util.List;

public class Order {
    private Long id;
    private List<OrderedProduct> orderedProducts;
    private Integer totalPrice;
    private State state;

    public void setId(Long id) {
        this.id = id;
    }

    public Order(List<OrderedProduct> orderedProducts) {
        this.orderedProducts = orderedProducts;
        this.totalPrice = calculateTotalPrice(orderedProducts);
        this.state = State.CREATED;
    }

    public Integer calculateTotalPrice(List<OrderedProduct> orderedProducts) {
        Integer calcaulatedTotalPrice = 0;
        for (OrderedProduct orderedProduct : orderedProducts) {
            calcaulatedTotalPrice += orderedProduct.getPrice();
        }
        return calcaulatedTotalPrice;
    }

    public Long getId() {
        return id;
    }

    public List<OrderedProduct> getOrderedProducts() {
        return orderedProducts;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public State getState() {
        return state;
    }

    public Boolean sameId(Long id) {
        return this.id.equals(id);
    }

    public void changeStateForce(State state) {
        this.state = state;
    }

    public Boolean sameState(State state) {
        return this.state.equals(state);
    }
}
