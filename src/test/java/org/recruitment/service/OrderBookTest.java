package org.recruitment.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recruitment.domain.Order;

import java.util.List;

public class OrderBookTest {

    private OrderBook orderBook;


    @BeforeEach
    void setUp() {
        orderBook = new OrderBook();
    }

    @Test
    void given_bids_when_price_for_levels_return_correct_price_value() {
        //GIVEN
        Order order1 = createBid(1, 100.40, 200);
        Order order2 = createBid(2, 101.40, 100);
        Order order3 = createBid(3, 200.40, 50);

        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        orderBook.addOrder(order3);
        //WHEN
        double highestBid = orderBook.getPriceForLevel('B', 1);
        //THEN
        Assertions.assertEquals(highestBid, 200.4);

    }

    @Test
    void given_offers_when_price_for_levels_return_correct_price_value() {
        //GIVEN
        Order order1 = createOffer(1, 100.40, 200);
        Order order2 = createOffer(2, 101.40, 100);
        Order order3 = createOffer(3, 200.40, 50);

        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        orderBook.addOrder(order3);
        //WHEN
        double lowestOffer = orderBook.getPriceForLevel('O', 1);
        //THEN
        Assertions.assertEquals(lowestOffer, 100.4);

    }

    @Test
    void given_offers_when_total_size_for_levels_return_correct_size() {
        //GIVEN
        Order order1 = createOffer(1, 100.40, 200);
        Order order2 = createOffer(2, 101.40, 100);
        Order order3 = createOffer(3, 200.40, 50);

        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        orderBook.addOrder(order3);
        //WHEN
        double lowestOffer = orderBook.getTotalSizeForLevel('O', 1);
        //THEN
        Assertions.assertEquals(lowestOffer, 200);

    }

    @Test
    void given_bids_when_total_size_for_levels_return_correct_size() {
        //GIVEN
        Order order1 = createBid(1, 100.40, 200);
        Order order2 = createBid(2, 101.40, 100);
        Order order3 = createBid(3, 200.40, 50);

        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        orderBook.addOrder(order3);
        //WHEN
        double highestBid = orderBook.getTotalSizeForLevel('B', 1);
        //THEN
        Assertions.assertEquals(highestBid, 50);

    }

    @Test
    void given_bids_when_total_size_for_higher_levels_return_correct_size() {
        //GIVEN
        Order order1 = createBid(1, 100.40, 200);
        Order order2 = createBid(2, 101.40, 100);
        Order order3 = createBid(3, 200.40, 50);
        Order order4 = createBid(4, 100.40, 600);

        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        orderBook.addOrder(order3);
        orderBook.addOrder(order4);
        //WHEN
        long totalSizeForLevel = orderBook.getTotalSizeForLevel('B', 3);
        //THEN
        Assertions.assertEquals(totalSizeForLevel, 200);

    }

    @Test
    void given_bids_when_remove_order_return_correct_size() {
        //GIVEN
        Order order1 = createBid(1, 100.40, 200);
        Order order2 = createBid(2, 101.40, 100);
        Order order3 = createBid(3, 200.40, 50);
        Order order4 = createBid(4, 100.40, 600);

        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        orderBook.addOrder(order3);
        orderBook.addOrder(order4);
        //WHEN
        orderBook.removeOrder(3);

        //THEN
        Assertions.assertEquals(orderBook.getOrders('B').size(), 3);

    }

    @Test
    void given_bids_when_modify_order_return_correct_size() {
                //GIVEN
        Order order1 = createBid(1, 100.40, 200);
        Order order2 = createBid(2, 101.40, 100);
        Order order3 = createBid(3, 200.40, 50);
        Order order4 = createBid(4, 100.40, 600);

        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        orderBook.addOrder(order3);
        orderBook.addOrder(order4);
        //WHEN
        orderBook.modifyOrderSize(1,10000);
        long totalSizeForLevel = orderBook.getTotalSizeForLevel('B', 3);
        //THEN
        Assertions.assertEquals(totalSizeForLevel, 10000);

    }

    @Test
    void given_bids_when_add_order_correct_size() {
        //GIVEN
        Order order1 = createBid(1, 100.40, 200);
        Order order2 = createBid(2, 101.40, 100);
        Order order3 = createBid(3, 200.40, 50);

        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        orderBook.addOrder(order3);
        //WHEN
        List<Order> b = orderBook.getOrders('B');

        //THEN
        Assertions.assertEquals(b.size(), 3);

    }

    private Order createBid(long id, double price, long size) {
        return new Order(id, price, 'B', size);
    }

    private Order createOffer(long id, double price, long size) {
        return new Order(id, price, 'O', size);
    }
}
