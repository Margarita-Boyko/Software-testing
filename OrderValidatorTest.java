package uk.ac.ed.inf;

import junit.framework.TestCase;
import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.*;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class OrderValidatorTest extends TestCase {

    public void testInvalidOrderDate(){
        var orderValidator = new OrderValidator();
        Orders order = new Orders("1234",
                LocalDate.now(),
                OrdersStatus.UNDEFINED,
                OrderValidationCodes.UNDEFINED,
                12,
                new Pizza[]{new Pizza("Margarita", 11)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("1234567891123456",
                "10/26",
                "123"));
        DayOfWeek[] openingDaysTonys = {DayOfWeek.SATURDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                                        DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SUNDAY};
        Pizza[] menuTonys = {new Pizza("Pepperoni", 12), new Pizza("Cheesy", 8)};
        DayOfWeek[] openingDaysGalaxy = {DayOfWeek.SATURDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                                         DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SUNDAY};
        Pizza[] menuGalaxy = {new Pizza("Margarita", 11), new Pizza("Hawaiian", 9)};
        Restaurant[] restaurants = {new Restaurant("Tony's", new LngLat(2,5), openingDaysTonys, menuTonys),
                                   new Restaurant("Galaxy", new LngLat(4,5), openingDaysGalaxy, menuGalaxy)};
        order.setOrderDate(LocalDate.of(2023, 10, 12));
        orderValidator.validateOrder(order, restaurants);
        assertEquals(OrdersStatus.INVALID, order.getOrderStatus());
    }


    public void testInvalidCVV4(){
        var orderValidator = new OrderValidator();
        Orders order = new Orders("1234",
                LocalDate.now(),
                OrdersStatus.INVALID,
                OrderValidationCodes.UNDEFINED,
                10,
                new Pizza[]{new Pizza("Margarita", 10)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("1234567891123456",
                "07/26",
                "1234"));
        Restaurant[] restaurant = new Restaurant[0];
        orderValidator.validateOrder(order, restaurant);

        assertEquals(OrderValidationCodes.CVV_INVALID, order.getOrderValidationCode() );
    }

    public void testValidOrder(){
        var orderValidator = new OrderValidator();
        Orders order = new Orders("1234",
                LocalDate.now(),
                OrdersStatus.VALID,
                OrderValidationCodes.UNDEFINED,
                110,
                new Pizza[]{new Pizza("Margarita", 10)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("1234567891123456",
                "07/26",
                "123"));
        DayOfWeek[] openingDaysTonys = {DayOfWeek.SATURDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SUNDAY};
        Pizza[] menuTonys = {new Pizza("Pepperoni", 12), new Pizza("Cheesy", 8)};
        DayOfWeek[] openingDaysGalaxy = {DayOfWeek.SATURDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SUNDAY};
        Pizza[] menuGalaxy = {new Pizza("Margarita", 10), new Pizza("Hawaiian", 9)};
        Restaurant[] restaurants = {new Restaurant("Tony's", new LngLat(2,5), openingDaysTonys, menuTonys),
                new Restaurant("Galaxy", new LngLat(4,5), openingDaysGalaxy, menuGalaxy)};
        order.setOrderDate(LocalDate.now());
        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCodes.NO_ERROR, order.getOrderValidationCode() );
    }

    public void testInvalidCVV2(){
        var orderValidator = new OrderValidator();
        Orders order = new Orders("1234",
                LocalDate.now(),
                OrdersStatus.VALID,
                OrderValidationCodes.UNDEFINED,
                110,
                new Pizza[]{new Pizza("Margarita", 10)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("1234567891123456",
                "07/26",
                "12"));
        DayOfWeek[] openingDaysTonys = {DayOfWeek.SATURDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SUNDAY};
        Pizza[] menuTonys = {new Pizza("Pepperoni", 12), new Pizza("Cheesy", 8)};
        DayOfWeek[] openingDaysGalaxy = {DayOfWeek.SATURDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SUNDAY};
        Pizza[] menuGalaxy = {new Pizza("Margarita", 10), new Pizza("Hawaiian", 9)};
        Restaurant[] restaurants = {new Restaurant("Tony's", new LngLat(2,5), openingDaysTonys, menuTonys),
                new Restaurant("Galaxy", new LngLat(4,5), openingDaysGalaxy, menuGalaxy)};
        order.setOrderDate(LocalDate.now());
        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCodes.CVV_INVALID, order.getOrderValidationCode() );
    }

    public void testInvalidCreditCardNumberLetter(){
        var orderValidator = new OrderValidator();
        Orders order = new Orders("1234",
                LocalDate.now(),
                OrdersStatus.INVALID,
                OrderValidationCodes.UNDEFINED,
                10,
                new Pizza[]{new Pizza("Margarita", 10)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("123478789112345G",
                "07/26",
                "123"));
        Restaurant[] restaurant = new Restaurant[0];
        orderValidator.validateOrder(order, restaurant);

        assertEquals(OrderValidationCodes.CARD_NUMBER_INVALID, order.getOrderValidationCode() );
    }

    public void testInvalidCreditCardNumber15(){
        var orderValidator = new OrderValidator();
        Orders order = new Orders("1234",
                LocalDate.now(),
                OrdersStatus.INVALID,
                OrderValidationCodes.UNDEFINED,
                10,
                new Pizza[]{new Pizza("Margarita", 10)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("123478789112345",
                "07/26",
                "123"));
        Restaurant[] restaurant = new Restaurant[0];
        orderValidator.validateOrder(order, restaurant);

        assertEquals(OrderValidationCodes.CARD_NUMBER_INVALID, order.getOrderValidationCode() );
    }

    public void testInvalidCreditCardNumber17(){
        var orderValidator = new OrderValidator();
        Orders order = new Orders("1234",
                LocalDate.now(),
                OrdersStatus.INVALID,
                OrderValidationCodes.UNDEFINED,
                10,
                new Pizza[]{new Pizza("Margarita", 10)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("12347878911234562",
                "07/26",
                "123"));
        Restaurant[] restaurant = new Restaurant[0];
        orderValidator.validateOrder(order, restaurant);

        assertEquals(OrderValidationCodes.CARD_NUMBER_INVALID, order.getOrderValidationCode() );
    }

    public void testInvalidExpiryDate(){
        var orderValidator = new OrderValidator();
        Orders order = new Orders("1234",
                LocalDate.now(),
                OrdersStatus.INVALID,
                OrderValidationCodes.UNDEFINED,
                10,
                new Pizza[]{new Pizza("Margarita", 10)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("1234567891123456",
                "09/23",
                "123"));
        Restaurant[] restaurant = new Restaurant[0];
        orderValidator.validateOrder(order, restaurant);

        assertEquals(OrderValidationCodes.EXPIRY_DATE_INVALID, order.getOrderValidationCode() );
    }

    public void testInvalidPizzaTotalPrice(){
        var orderValidator = new OrderValidator();
        Orders order = new Orders("1234",
                LocalDate.now(),
                OrdersStatus.INVALID,
                OrderValidationCodes.UNDEFINED,
                109,
                new Pizza[]{new Pizza("Margarita", 10)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("1234567891123456",
                "09/26",
                "123"));
        Restaurant[] restaurant = new Restaurant[0];
        orderValidator.validateOrder(order, restaurant);

        assertEquals(OrderValidationCodes.TOTAL_INCORRECT, order.getOrderValidationCode() );
    }

    public void testUndefinedPizza(){
        var orderValidator = new OrderValidator();
        Orders order = new Orders("1234",
                LocalDate.now(),
                OrdersStatus.INVALID,
                OrderValidationCodes.UNDEFINED,
                -2,
                null,
                null);
        order.setCreditCardInformation(new CreditCardInformation("1234567891123456",
                "09/26",
                "123"));
        Restaurant[] restaurant = new Restaurant[0];
        orderValidator.validateOrder(order, restaurant);

        assertEquals(OrderValidationCodes.PIZZA_NOT_DEFINED, order.getOrderValidationCode() );
    }

    public void testInvalidPizzaCount5(){
        var orderValidator = new OrderValidator();
        Orders order = new Orders("1234",
                LocalDate.now(),
                OrdersStatus.INVALID,
                OrderValidationCodes.UNDEFINED,
                150,
                new Pizza[]{new Pizza("Margarita", 10), new Pizza("Margarita", 10),new Pizza("Margarita", 10),new Pizza("Margarita", 10),new Pizza("Margarita", 10)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("1234567891123456",
                "09/26",
                "123"));
        Restaurant[] restaurant = new Restaurant[0];
        orderValidator.validateOrder(order, restaurant);

        assertEquals(OrderValidationCodes.MAX_PIZZA_COUNT_EXCEEDED, order.getOrderValidationCode() );
    }
    public void testZeroPizzaCount(){
        var orderValidator = new OrderValidator();
        Orders order = new Orders("1234",
                LocalDate.now(),
                OrdersStatus.INVALID,
                OrderValidationCodes.UNDEFINED,
                100,
                new Pizza[]{},
                null);
        order.setCreditCardInformation(new CreditCardInformation("1234567891123456",
                "09/26",
                "123"));
        Restaurant[] restaurant = new Restaurant[0];
        orderValidator.validateOrder(order, restaurant);

        assertEquals(OrderValidationCodes.PIZZA_NOT_DEFINED, order.getOrderValidationCode() );
    }

    public void testNoPricePizza(){
        var orderValidator = new OrderValidator();
        Orders order = new Orders("1234",
                LocalDate.now(),
                OrdersStatus.INVALID,
                OrderValidationCodes.UNDEFINED,
                140,
                new Pizza[]{new Pizza("Margarita", 0), new Pizza("Margarita", 10),new Pizza("Margarita", 10),new Pizza("Margarita", 10)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("1234567891123456",
                "09/26",
                "123"));
        Restaurant[] restaurant = new Restaurant[0];
        orderValidator.validateOrder(order, restaurant);

        assertEquals(OrderValidationCodes.PIZZA_NOT_DEFINED, order.getOrderValidationCode() );
    }

    public void testNoNamePizza(){
        var orderValidator = new OrderValidator();
        Orders order = new Orders("1234",
                LocalDate.now(),
                OrdersStatus.INVALID,
                OrderValidationCodes.UNDEFINED,
                140,
                new Pizza[]{new Pizza(null, 10), new Pizza("Margarita", 10),new Pizza("Margarita", 10),new Pizza("Margarita", 10)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("1234567891123456",
                "09/26",
                "123"));
        Restaurant[] restaurant = new Restaurant[0];
        orderValidator.validateOrder(order, restaurant);

        assertEquals(OrderValidationCodes.PIZZA_NOT_DEFINED, order.getOrderValidationCode() );
    }

    public void testMultipleRestaurants(){
        var orderValidator = new OrderValidator();
        Orders order = new Orders("1234",
                LocalDate.now(),
                OrdersStatus.UNDEFINED,
                OrderValidationCodes.UNDEFINED,
                123,
                new Pizza[]{new Pizza("Margarita", 11), new Pizza("Pepperoni", 12)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("1234567891123456",
                "10/26",
                "123"));
        DayOfWeek[] openingDaysTonys = {DayOfWeek.SATURDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                                        DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SUNDAY};
        Pizza[] menuTonys = {new Pizza("Pepperoni", 12), new Pizza("Cheesy", 8)};
        DayOfWeek[] openingDaysGalaxy = {DayOfWeek.SATURDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                                         DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SUNDAY};
        Pizza[] menuGalaxy = {new Pizza("Margarita", 11), new Pizza("Hawaiian", 9)};
        Restaurant[] restaurants = {new Restaurant("Tony's", new LngLat(2,5), openingDaysTonys, menuTonys),
                new Restaurant("Galaxy", new LngLat(4,5), openingDaysGalaxy, menuGalaxy)};
        order.setOrderDate(LocalDate.now());
        orderValidator.validateOrder(order, restaurants);
        assertEquals(OrderValidationCodes.PIZZA_FROM_MULTIPLE_RESTAURANTS, order.getOrderValidationCode());
    }

    public void testUndefinedPizzaName(){
        var orderValidator = new OrderValidator();
        Orders order = new Orders("1234",
                LocalDate.now(),
                OrdersStatus.UNDEFINED,
                OrderValidationCodes.UNDEFINED,
                120,
                new Pizza[]{new Pizza("Mushroom", 11), new Pizza("Hawaiian", 9)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("1234567891123456",
                "10/26",
                "123"));
        DayOfWeek[] openingDaysTonys = {DayOfWeek.SATURDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                                        DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SUNDAY};
        Pizza[] menuTonys = {new Pizza("Pepperoni", 12), new Pizza("Cheesy", 8)};
        DayOfWeek[] openingDaysGalaxy = {DayOfWeek.SATURDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                                         DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SUNDAY};
        Pizza[] menuGalaxy = {new Pizza("Margarita", 11), new Pizza("Hawaiian", 9)};
        Restaurant[] restaurants = {new Restaurant("Tony's", new LngLat(2,5), openingDaysTonys, menuTonys),
                new Restaurant("Galaxy", new LngLat(4,5), openingDaysGalaxy, menuGalaxy)};
        order.setOrderDate(LocalDate.now());
        orderValidator.validateOrder(order, restaurants);
        assertEquals(OrderValidationCodes.PIZZA_NOT_DEFINED, order.getOrderValidationCode());
    }

    public void testNoDefinedRestaurants(){
        var orderValidator = new OrderValidator();
        Orders order = new Orders("1234",
                LocalDate.now(),
                OrdersStatus.UNDEFINED,
                OrderValidationCodes.UNDEFINED,
                120,
                new Pizza[]{new Pizza("Mushroom", 11), new Pizza("Hawaiian", 9)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("1234567891123456",
                "10/26",
                "123"));
        Restaurant[] restaurants = {};
        order.setOrderDate(LocalDate.now());
        orderValidator.validateOrder(order, restaurants);
        assertEquals(OrderValidationCodes.PIZZA_NOT_DEFINED, order.getOrderValidationCode());
    }

    public void testPizzaPriceDifferent(){
        var orderValidator = new OrderValidator();
        Orders order = new Orders("1234",
                LocalDate.now(),
                OrdersStatus.UNDEFINED,
                OrderValidationCodes.UNDEFINED,
                125,
                new Pizza[]{new Pizza("Margarita", 13), new Pizza("Hawaiian", 12)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("1234567891123456",
                "10/26",
                "123"));
        DayOfWeek[] openingDaysTonys = {DayOfWeek.SATURDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                                        DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SUNDAY};
        Pizza[] menuTonys = {new Pizza("Pepperoni", 12), new Pizza("Cheesy", 8)};
        DayOfWeek[] openingDaysGalaxy = {DayOfWeek.SATURDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                                         DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SUNDAY};
        Pizza[] menuGalaxy = {new Pizza("Margarita", 11), new Pizza("Hawaiian", 9)};
        Restaurant[] restaurants = {new Restaurant("Tony's", new LngLat(2,5), openingDaysTonys, menuTonys),
                new Restaurant("Galaxy", new LngLat(4,5), openingDaysGalaxy, menuGalaxy)};
        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCodes.TOTAL_INCORRECT, order.getOrderValidationCode() );
    }

    public void testRestaurantClosed(){
        var orderValidator = new OrderValidator();
        Orders order = new Orders("1234",
                LocalDate.now(),
                OrdersStatus.UNDEFINED,
                OrderValidationCodes.UNDEFINED,
                125,
                new Pizza[]{new Pizza("Margarita", 13), new Pizza("Hawaiian", 12)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("1234567891123456",
                "10/26",
                "123"));
        DayOfWeek[] openingDaysTonys = {DayOfWeek.SATURDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SUNDAY};
        Pizza[] menuTonys = {new Pizza("Pepperoni", 12), new Pizza("Cheesy", 8)};
        DayOfWeek[] openingDaysGalaxy = {};
        Pizza[] menuGalaxy = {new Pizza("Margarita", 13), new Pizza("Hawaiian", 12)};
        Restaurant[] restaurants = {new Restaurant("Tony's", new LngLat(2,5), openingDaysTonys, menuTonys),
                new Restaurant("Galaxy", new LngLat(4,5), openingDaysGalaxy, menuGalaxy)};
        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCodes.RESTAURANT_CLOSED, order.getOrderValidationCode() );
    }

    public void testMultipleValidationErrors(){
        var orderValidator = new OrderValidator();
        Orders order = new Orders("1234",
                LocalDate.now(),
                OrdersStatus.UNDEFINED,
                OrderValidationCodes.UNDEFINED,
                125,
                new Pizza[]{new Pizza("Margarita", 13), new Pizza("Hawaiian", 12)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("1234567891123456J",
                "10/26",
                "1234"));
        DayOfWeek[] openingDaysTonys = {DayOfWeek.SATURDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SUNDAY};
        Pizza[] menuTonys = {new Pizza("Pepperoni", 12), new Pizza("Cheesy", 8)};
        DayOfWeek[] openingDaysGalaxy = {};
        Pizza[] menuGalaxy = {new Pizza("Margarita", 13), new Pizza("Hawaiian", 12)};
        Restaurant[] restaurants = {new Restaurant("Tony's", new LngLat(2,5), openingDaysTonys, menuTonys),
                new Restaurant("Galaxy", new LngLat(4,5), openingDaysGalaxy, menuGalaxy)};
        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCodes.CVV_INVALID, order.getOrderValidationCode() );
    }

}