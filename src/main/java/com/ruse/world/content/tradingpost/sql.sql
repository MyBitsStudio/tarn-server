use tradingpost;

CREATE TABLE IF NOT EXISTS live_offers(
	id INT AUTO_INCREMENT PRIMARY KEY,
    item_id INT NOT NULL,
    item_name VARCHAR(255) NOT NULL,
    item_initial_amount INT NOT NULL,
    item_amount_sold INT NOT NULL,
    price INT NOT NULL,
    seller VARCHAR(255) NOT NULL,
    slot smallint NOT NULL,
    time_stamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(seller, slot)
);

CREATE TABLE IF NOT EXISTS coffers(
	id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    amount BIGINT DEFAULT 0,
    UNIQUE(username)
);

CREATE TABLE IF NOT EXISTS offer_history(
	id INT AUTO_INCREMENT PRIMARY KEY,
    item_id INT NOT NULL,
    item_name VARCHAR(255) NOT NULL,
    item_amount INT NOT NULL,
    price_each INT NOT NULL,
    total_price INT NOT NULL,
    seller VARCHAR(255) NOT NULL,
    buyer VARCHAR(255) NOT NULL,
    time_stamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
