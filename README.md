Point B answer

1. I would add final keyword to all the properties of offer class, so it would be immutable.
2. Instead of char side I would use enum.
3. I would change double to BigDecimal because we can lose precision with double.
4. Such thing should probably be persisted in the database.
5. Since my implementation is not for distributed environments I did not include solution for that.(The orderbook should be distributed inside a cluster)
6. I assumed that orderId is unique, but we should probably check to make sure we don't add the same order twice.
