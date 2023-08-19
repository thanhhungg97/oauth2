CREATE TABLE `oauth_client` (
                                `id` varchar(255) NOT NULL,
                                `client_secret` varchar(255) DEFAULT NULL,
                                `redirect_uri` varchar(2000) DEFAULT NULL,
                                `scopes` varchar(2000) DEFAULT NULL,
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE user(
                     id varchar(255) primary key,
                     username varchar(255),
                     password  varchar(255),
                     email varchar(255),
                     phone_number int

);
CREATE UNIQUE INDEX user_username_unique_index
    ON  user (username);