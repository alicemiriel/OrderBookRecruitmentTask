package org.recruitment.service;

import it.unimi.dsi.fastutil.doubles.Double2ObjectMap;
import it.unimi.dsi.fastutil.doubles.Double2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.doubles.DoubleComparators;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import org.recruitment.domain.Order;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OrderBook {

    private final Map<Long, Order> orderIdsMap = new HashMap<>();
    private final Double2ObjectRBTreeMap<List<Order>> bids = new Double2ObjectRBTreeMap<>(DoubleComparators.OPPOSITE_COMPARATOR);
    private final Double2ObjectRBTreeMap<List<Order>> offers = new Double2ObjectRBTreeMap<>(DoubleComparators.NATURAL_COMPARATOR);

    public synchronized void addOrder(Order order) {
        Double2ObjectRBTreeMap<List<Order>> levels = getInnerOrders(order.getSide());

        List<Order> orders = levels.get(order.getPrice());
        if (orders != null) {
            orders.add(order);
        } else {
            orders = new LinkedList<>();
            orders.add(order);
            levels.put(order.getPrice(), orders);
        }

        orderIdsMap.put(order.getId(), order);
    }

    public synchronized void removeOrder(long orderId) {
        Order removedOrder = orderIdsMap.remove(orderId);
        if (removedOrder != null) {
            Double2ObjectRBTreeMap<List<Order>> levels = getInnerOrders(removedOrder.getSide());
            List<Order> orders = levels.get(removedOrder.getPrice());
            orders.remove(removedOrder);
            if (orders.isEmpty()) {
                levels.remove(removedOrder.getPrice());
            }
        }
    }

    public synchronized void modifyOrderSize(long orderId, long size) {
        if (size <= 0) {
            removeOrder(orderId);
            return;
        }

        Order toUpdate = orderIdsMap.get(orderId);
        Order updatedOrder = new Order(
                toUpdate.getId(),
                toUpdate.getPrice(),
                toUpdate.getSide(),
                size
        );

        Double2ObjectRBTreeMap<List<Order>> levels = getInnerOrders(updatedOrder.getSide());
        List<Order> orders = levels.get(updatedOrder.getPrice());
        orders.set(orders.indexOf(toUpdate), updatedOrder);
        orderIdsMap.replace(orderId, updatedOrder);
    }

    public double getPriceForLevel(char side, int level) {
        if (level <= 0) {
            throw new IllegalArgumentException("Level should be number bigger then 0");
        }
        if (level == 1) {
            Double2ObjectRBTreeMap<List<Order>> innerOrders = getInnerOrders(side);
            return innerOrders.firstDoubleKey();
        }

        List<Order> orders = getOrders(side);
        if (orders.size() < level)
            return 0.0;

        return orders.get(level - 1).getPrice();
    }

    public long getTotalSizeForLevel(char side, int level) {
        if (level <= 0) {
            throw new IllegalArgumentException("Level should be number bigger then 0");
        }

        List<Order> orders = getOrders(side);
        if (orders.size() < level)
            return 0;

        return orders.get(level - 1).getSize();
    }

    public List<Order> getOrders(char side) {
        ObjectSortedSet<Double2ObjectMap.Entry<List<Order>>> entries = getInnerOrders(side).double2ObjectEntrySet();
        return entries.stream()
                .flatMap(o -> o.getValue().stream())
                .toList();
    }


    private Double2ObjectRBTreeMap<List<Order>> getInnerOrders(char side) {
        if (side == 'B') {
            return bids;
        } else if (side == 'O') {
            return offers;
        } else {
            throw new IllegalArgumentException("Unknown side " + side);
        }
    }
}
