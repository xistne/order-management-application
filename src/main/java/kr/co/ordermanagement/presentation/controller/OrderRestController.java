package kr.co.ordermanagement.presentation.controller;

import kr.co.ordermanagement.application.SimpleOrderService;
import kr.co.ordermanagement.domain.order.State;
import kr.co.ordermanagement.presentation.dto.ChangeStateRequestDto;
import kr.co.ordermanagement.presentation.dto.OrderProductRequestDto;
import kr.co.ordermanagement.presentation.dto.OrderResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderRestController {
    private SimpleOrderService simpleOrderService;

    @Autowired
    public OrderRestController(SimpleOrderService simpleOrderService) {
        this.simpleOrderService = simpleOrderService;
    }

    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody List<OrderProductRequestDto> orderProductRequestDtos) {
        OrderResponseDto orderResponseDto = simpleOrderService.createOrder(orderProductRequestDtos);
        return ResponseEntity.ok(orderResponseDto);
    }

    @RequestMapping(value = "/orders/{orderId}", method = RequestMethod.PATCH)
    public ResponseEntity<OrderResponseDto> changeOrderState(@PathVariable Long orderId, @RequestBody ChangeStateRequestDto changeStateRequestDto) {
        OrderResponseDto orderResponseDto = simpleOrderService.changeOrderState(orderId, changeStateRequestDto);
        return ResponseEntity.ok(orderResponseDto);
    }

    @RequestMapping(value = "/orders/{orderId}", method = RequestMethod.GET)
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long orderId) {
        OrderResponseDto orderResponseDto = simpleOrderService.findById(orderId);
        return ResponseEntity.ok(orderResponseDto);
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public ResponseEntity<List<OrderResponseDto>> getOrdersByState(@RequestParam State state) {
        List<OrderResponseDto> orderResponseDtos = simpleOrderService.findByState(state);
        return ResponseEntity.ok(orderResponseDtos);
    }

    @RequestMapping(value = "/orders/{orderId}/cancel", method = RequestMethod.PATCH)
    public ResponseEntity<OrderResponseDto> cancelOrderById(@PathVariable Long orderId) {
        OrderResponseDto orderResponseDtos = simpleOrderService.cancelOrderById(orderId);
        return ResponseEntity.ok(orderResponseDtos);
    }
}
