package telran.java45.dao;

import java.util.stream.Stream;

import org.springframework.data.repository.CrudRepository;

import telran.java45.model.Book;

public interface BookRepository extends CrudRepository<Book, String> {
	
	Stream<Book> findBooksByAuthorsName(String authorName);
	
	Stream<Book> findBooksByPublisherPublisherName(String publisherName);
	
	
}
