package telran.java45.service;

import telran.java45.dto.AuthorDto;
import telran.java45.dto.BookDto;

public interface BookService {
	Boolean addBook(BookDto bookDto);
	
	BookDto findBookById(String isbn);
	
	BookDto remove(String isbn);
	
	BookDto updateBook(String isbn, String title);
	
	Iterable<BookDto> findBooksByAuthor(String authorName);
	
	Iterable<BookDto> findBooksByPublisher(String publisherName);
	
	Iterable<AuthorDto> findBookAuthors(String isbn);
	
	Iterable<String> findPublishersByAuthor(String authorName);
	
	AuthorDto removeAuthor(String authorName);
	
}
