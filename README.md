# Electronic-Store-Bullish (Backend)

To run the application: `mvn spring-boot:run` 

To run tests: `mvn test`

Library used: Spring Boot, Junit, Hibernate, JPA

Database: H2

Build automation: Maven


## Major Logic Class:


### Product (entity, repository, service, controller) for admin operation with product (add, delete, update, etc)
Admin User Operations\
&emsp;•  Create a new product\
&emsp;•  Remove a product\
&emsp;•  Add discount deals for products (Example: Buy 1 get 50% off the second)

Product Model:\
	&emsp;Attribute:\
		&emsp;&emsp;1. Id\
		&emsp;&emsp;2. Name\
		&emsp;&emsp;3. Price\
		&emsp;&emsp;4. Discount\
		&emsp;&emsp;5. Availability (0 is false, 1 is true)

Product Repo:\
	&emsp;Method:\
		&emsp;&emsp;1. Save (create) Product \
		&emsp;&emsp;2. findbyProductId

Product Service:\
	&emsp;Method:\
		&emsp;&emsp;1. Create Product and perform checking\
			&emsp;&emsp;&emsp;a. Duplicate Product name\
			&emsp;&emsp;&emsp;b. Valid discount\
			&emsp;&emsp;&emsp;c. valid price range\
		&emsp;&emsp;2. Remove Product by id (make it to unavailable)\
		&emsp;&emsp;3. Change Discount of the Product and valiadation of discount range\
		&emsp;&emsp;4. Change availability and valiation of number

Product Controller:\
	&emsp;Method:\
		&emsp;&emsp;1. Save (create) Product \
		&emsp;&emsp;2. Remove (make it unavailable) a product
  		&emsp;&emsp;3. Update product discount


### Basket (entity, repository, service, controller) for customer operation with basket (add, delete etc)

Customer\
&emsp;•  Add and remove products to and from a basket\
&emsp;•  Calculate a receipt of items, including all purchases, deals applied and total price\

Customer\
	&emsp;Attribute:\
		&emsp;&emsp;1. id\
		&emsp;&emsp;2. Name
		
Basket Model:\
	&emsp;Attribute:\
		&emsp;&emsp;1. basket id\
		&emsp;&emsp;2. customer id\
		&emsp;&emsp;3. product id\
		&emsp;&emsp;4. quantity

Basket Controller:\
	&emsp;Method:\
		&emsp;&emsp;1. Add product to basket (input, customer id, product id, quantity\
		&emsp;&emsp;2. Delete product item\
		&emsp;&emsp;3. Return all basket items and final total price for the customer
Basket Service:\
	&emsp;Method\
		&emsp;&emsp;1. Add item to basket \
			&emsp;&emsp;&emsp;a. check if the same item is already added to the basket, if yes, add the quantity\
		&emsp;&emsp;2. delete product item\
			&emsp;&emsp;&emsp;a. check if the product is presence in the basket, if not return error\
		&emsp;&emsp;3. Calculate total price, get all basket product, quantity from the same customer id
  
Basket Repo:\
	&emsp;Method\
		&emsp;&emsp;1. Get all basket items by customer id\
		&emsp;&emsp;2. get basket item by customer id and product id\
		&emsp;&emsp;3. delete basket row by customer id and product id\
    		&emsp;&emsp;4. Save basket to persistence


