package telran.java45.dao;

import org.springframework.data.repository.CrudRepository;

import telran.java45.model.Author;

public interface AuthorRepository extends CrudRepository<Author, String> {

}
