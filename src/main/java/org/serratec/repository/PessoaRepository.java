package org.serratec.repository;

import java.util.Optional;

import org.serratec.models.Pessoa;
import org.springframework.data.repository.CrudRepository;

public interface PessoaRepository extends CrudRepository<Pessoa, String>{
	Optional<Pessoa> findByCpf(String cpf);
	Iterable<Pessoa> findAllByNomeContainingIgnoreCase(String nome);
}
