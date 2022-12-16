package telran.java45.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookDto {
	
	    String isbn;
		String title;
		Set<AuthorDto> authors;
		String publisher;

}
