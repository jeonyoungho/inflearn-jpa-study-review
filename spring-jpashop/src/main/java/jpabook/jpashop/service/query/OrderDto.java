package jpabook.jpashop.service.query;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by jyh1004 on 2022-10-24
 */

@Data
public class OrderDto {

	private Long orderId;
	private String name;
	private LocalDateTime orderDate;
	private OrderStatus orderStatus;
	private Address address;
	private List<OrderItemDto> orderItems;

	public OrderDto(Order order) {
		this.orderId = order.getId();
		this.name = order.getMember().getName();
		this.orderDate = order.getOrderDate();
		this.orderStatus = order.getStatus();
		this.address = order.getDelivery().getAddress();
		orderItems = order.getOrderItems().stream()
		                  .map(orderItem -> new OrderItemDto(orderItem))
		                  .collect(toList());
	}
}

