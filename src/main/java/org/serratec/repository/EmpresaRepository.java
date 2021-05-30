package org.serratec.repository;

import java.util.Optional;

import org.serratec.models.Empresa;
import org.springframework.data.repository.CrudRepository;

public interface EmpresaRepository extends CrudRepository<Empresa, String>{
	Optional<Empresa> findByCnpj(String cnpj);
	Iterable<Empresa> findAllByNomeContainingIgnoreCase(String nome);
}