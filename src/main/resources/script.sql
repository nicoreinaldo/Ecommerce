drop table cart;
drop table product;

CREATE TABLE cart (
                      id UUID PRIMARY KEY,
                      created_at TIMESTAMP,
                      lastActivity TIMESTAMP
);

CREATE TABLE product (
                         id INT PRIMARY KEY,
                         cart_id UUID,
                         description VARCHAR(255) NOT NULL,
                         amount DOUBLE,
                         FOREIGN KEY (cart_id) REFERENCES cart(id)
);