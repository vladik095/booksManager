CREATE SEQUENCE author_sequence START 1 INCREMENT 1;
CREATE SEQUENCE book_sequence START 1 INCREMENT 1;
CREATE SEQUENCE tag_sequence START 1 INCREMENT 1;

CREATE TABLE author (
    id BIGINT PRIMARY KEY DEFAULT nextval('author_sequence'),
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE book (
    id BIGINT PRIMARY KEY DEFAULT nextval('book_sequence'),
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    author_id BIGINT,
    FOREIGN KEY (author_id) REFERENCES author(id)
);

CREATE TABLE tag (
    id BIGINT PRIMARY KEY DEFAULT nextval('tag_sequence'),
    name VARCHAR(255) NOT NULL
);
