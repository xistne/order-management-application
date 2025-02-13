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
        // 1. 주문 가능한지 확인
            // 1-1. 잘못된 상품번호로 요청하는경우
            // 1-2. 주문된 상품의 재고수량이 부족한지 확인
        // 2-1. 주문이 가능하다면 상품의 재고수량 줄이기
        // 2-2. 주문이 불가능하다면 주문 실패
        /* DESC: 원래 상품이 하나라도 없는경우, 수량이 부족한 상품이 있는 경우 2번의 조건을 가지려고 했음..
            그러나 상품 findByAllId를 쓰려니 받은 상품정보를 idList로 만든 후 조회를 해야하고, 응답이 정상적으로 올 경우 그 데이터를 쓸 일이 없을 것 같아서 한번에 처리하기로 함...
        * */
        // DESC : 주문상품을 돌면서 해당상품이 아예없을 경우 주문실패(해당상품없음), 있을경우 배열에 해당 상품의 id를 저장해 놓았다가 주문실패(수량부족), 그 외 주문성공
        List<OrderedProduct> orderedProducts = new ArrayList<>();
        for(OrderProductRequestDto orderProductRequestDto : orderProductRequestDtos) {
            Product product = this.productRepository.findById(orderProductRequestDto.getId());
            // DESC : if문에서 orderProductRequestDto와 조회한 product를 비교하였는데 Product 개체의 함수를 이용하는걸로 변경
            product.checkEnoughAmount(orderProductRequestDto.getAmount());
            product.decreaseAmount(orderProductRequestDto.getAmount());
            orderedProducts.add(new OrderedProduct(product.getId(),product.getName(), product.getPrice(), product.getAmount()));
        }
        // DESC : 주문 생성
        Order order = new Order(orderedProducts);
        Order savedOrder = this.orderRepository.add(order);
        OrderResponseDto orderResponseDto = OrderResponseDto.toDto(savedOrder);
        return orderResponseDto;
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
}
