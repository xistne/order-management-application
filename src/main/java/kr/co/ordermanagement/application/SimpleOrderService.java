package kr.co.ordermanagement.application;

import kr.co.ordermanagement.domain.order.Order;
import kr.co.ordermanagement.domain.order.OrderRepository;
import kr.co.ordermanagement.domain.order.OrderedProduct;
import kr.co.ordermanagement.domain.order.State;
import kr.co.ordermanagement.domain.product.Product;
import kr.co.ordermanagement.domain.product.ProductRepository;
import kr.co.ordermanagement.presentation.dto.ChangeStateRequestDto;
import kr.co.ordermanagement.presentation.dto.OrderProductRequestDto;
import kr.co.ordermanagement.presentation.dto.OrderResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SimpleOrderService {

    private ProductRepository productRepository;
    private OrderRepository orderRepository;

    @Autowired
    public SimpleOrderService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }
    public OrderResponseDto createOrder(List<OrderProductRequestDto> orderProductRequestDtos) {
        List<OrderedProduct> orderedProducts = this.makeOrderedProducts(orderProductRequestDtos);
        this.decreasesProductsAmount(orderedProducts);
        Order order = new Order(orderedProducts);
        Order savedOrder = this.orderRepository.add(order);
        OrderResponseDto orderResponseDto = OrderResponseDto.toDto(savedOrder);
        return orderResponseDto;
    }

    private void decreasesProductsAmount(List<OrderedProduct> orderedProducts) {
        orderedProducts.forEach(orderedProduct -> {
            Product product = this.productRepository.findById(orderedProduct.getId());
            product.decreaseAmount(orderedProduct.getAmount());
        });
    }

    private List<OrderedProduct> makeOrderedProducts(List<OrderProductRequestDto> orderProductRequestDtos) {
        return orderProductRequestDtos.stream().map(orderProductRequestDto -> {
            Long productId = orderProductRequestDto.getId();
            Product product = this.productRepository.findById(productId);

            product.checkEnoughAmount(orderProductRequestDto.getAmount());

            return new OrderedProduct(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    orderProductRequestDto.getAmount()
            );
        }).toList();
    }

    public OrderResponseDto changeOrderState(Long orderId, ChangeStateRequestDto changeStateRequestDto) {
        Order order = this.orderRepository.findById(orderId);
        order.changeStateForce(changeStateRequestDto.getState());
        OrderResponseDto orderResponseDto = OrderResponseDto.toDto(order);
        return orderResponseDto;
    }

    public OrderResponseDto findById(Long orderId) {
        Order order = this.orderRepository.findById(orderId);
        OrderResponseDto orderResponseDto = OrderResponseDto.toDto(order);
        return orderResponseDto;
    }

    public List<OrderResponseDto> findByState(State state) {
        List<Order> orders = this.orderRepository.findByState(state);
        List<OrderResponseDto> orderResponseDtos = orders.stream().map(order -> OrderResponseDto.toDto(order)).toList();
        return orderResponseDtos;
    }

    public OrderResponseDto cancelOrderById(Long orderId) {
        Order order = this.orderRepository.findById(orderId);
        order.cancel();
        OrderResponseDto orderResponseDto = OrderResponseDto.toDto(order);
        return orderResponseDto;
    }
}
