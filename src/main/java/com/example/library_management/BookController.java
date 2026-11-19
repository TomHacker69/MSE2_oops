
package com.example.library_management;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/books")
public class BookController {

    // ArrayList to store books
    private List<Book> books = new ArrayList<>();

    // 1. Add a new book
    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody Book book) {

        for (Book b : books) {
            if (Objects.equals(b.getId(), book.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Book ID already exists!");
            }
        }

        books.add(book);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(book);
    }

    // 2. Get all books
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {

        return ResponseEntity.ok(books);
    }

    // 3. Get book by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Integer id) {

        for (Book book : books) {
            if (Objects.equals(book.getId(), id)) {
                return ResponseEntity.ok(book);
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Book not found!");
    }

    // 4. Update book by ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(
            @PathVariable Integer id,
            @RequestBody Book updatedBook) {

        for (Book book : books) {

            if (Objects.equals(book.getId(), id)) {

                book.setTitle(updatedBook.getTitle());
                book.setAuthor(updatedBook.getAuthor());
                book.setCategory(updatedBook.getCategory());
                book.setAvailableCopies(
                        updatedBook.getAvailableCopies()
                );

                return ResponseEntity.ok(book);
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Book not found!");
    }

    // 5. Delete book by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(
            @PathVariable Integer id) {

        boolean deleted = books.removeIf(
                book -> Objects.equals(book.getId(), id)
        );

        if (deleted) {
            return ResponseEntity.ok("Book deleted successfully!");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Book not found!");
    }
}
