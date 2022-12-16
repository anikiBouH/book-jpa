package telran.java45.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import telran.java45.dto.exeptions.EntityNotFoundException;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import telran.java45.dao.AuthorRepository;
import telran.java45.dao.BookRepository;
import telran.java45.dao.PublicherRepository;
import telran.java45.dto.AuthorDto;
import telran.java45.dto.BookDto;
import telran.java45.model.Author;
import telran.java45.model.Book;
import telran.java45.model.Publisher;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

	final BookRepository bookRepository;
	final AuthorRepository authorRepository;
	final PublicherRepository publicherRepository;
	final ModelMapper modelMapper;

	@Override
	@Transactional
	public Boolean addBook(BookDto bookDto) {
		if (bookRepository.existsById(bookDto.getIsbn()))
			return false;

		Publisher publisher = publicherRepository.findById(bookDto.getPublisher())
				.orElse(publicherRepository.save(new Publisher(bookDto.getPublisher())));

		Set<Author> authors = bookDto.getAuthors().stream()
				.map(a -> authorRepository.findById(a.getName())
						.orElse(authorRepository.save(new Author(a.getName(), a.getBirthDate()))))
				.collect(Collectors.toSet());
		Book book = new Book(bookDto.getIsbn(), bookDto.getTitle(), authors, publisher);

		bookRepository.save(book);
		return true;
	}

	@Override
	public BookDto findBookById(String isbn) {
		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
		return modelMapper.map(book, BookDto.class);
	}

	@Override
	@Transactional
	public BookDto remove(String isbn) {
		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
		bookRepository.delete(book);
		return modelMapper.map(book, BookDto.class);
	}

	@Override
	@Transactional
	public BookDto updateBook(String isbn, String title) {
		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
		book.setTitle(title);
		return modelMapper.map(book, BookDto.class);
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<BookDto> findBooksByAuthor(String authorName) {
		return bookRepository.findBooksByAuthorsName(authorName).map(b -> modelMapper.map(b, BookDto.class))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<BookDto> findBooksByPublisher(String publisherName) {
		return bookRepository.findBooksByPublisherPublisherName(publisherName)
				.map(b -> modelMapper.map(b, BookDto.class)).collect(Collectors.toList());
	}

	@Override
	public Iterable<AuthorDto> findBookAuthors(String isbn) {
		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
		return book.getAuthors().stream().map(a -> modelMapper.map(a, AuthorDto.class)).collect(Collectors.toList());
	}

	@Override
	public Iterable<String> findPublishersByAuthor(String authorName) {
		return publicherRepository.findPublishersByAuthor(authorName);
	}

	@Override
	@Transactional
	public AuthorDto removeAuthor(String authorName) {
		Author author = authorRepository.findById(authorName).orElseThrow(EntityNotFoundException::new);
		List<Book> books = bookRepository.findBooksByAuthorsName(authorName).collect(Collectors.toList());
		books = books.stream().map(b -> {
						b.getAuthors().remove(author);
						return b;
					  })
				.collect(Collectors.toList());
		bookRepository.deleteAll(books.stream().filter(b->b.getAuthors().size()==0).collect(Collectors.toList()));
		authorRepository.delete(author);
		return modelMapper.map(author, AuthorDto.class);
	}

}
