package com.store.book;

import com.store.book.model.Book;
import com.store.book.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class BookstoreApplication {
    @Autowired
    private final BookService bookService;

    public BookstoreApplication(BookService bookService) {
        this.bookService = bookService;
    }

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setTitle("Effective Java (3rd Edition)");
            book.setAuthor("Joshua Bloch");
            book.setIsbn("978-0134685991");
            book.setPrice(BigDecimal.valueOf(35));
            book.setDescription("Effective Java  by Joshua Bloch is considered one of the\n "
                    + "most influential books in the Java programming world,\n ");
            bookService.save(book);

            System.out.println(bookService.findAll());
        };

    }
}
