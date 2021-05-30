package org.serratec.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.serratec.models.Pessoa;
import org.serratec.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api (value = "API - Consulta de pessoa física")

public class PessoaResource {
	@Autowired
	PessoaRepository pessoaRepository;
	
	@ApiOperation (value = "Consultando todas pessoas físicas")
	@GetMapping("/pessoa/todos")
	public Iterable<Pessoa> getTodos(){
		return pessoaRepository.findAll();
	}
	
	@ApiOperation (value = "Consultando pessoas por CPF")
	@GetMapping("/pessoa/por-cpf/{cpf}")
	private Optional<Pessoa>getByCpf(@PathVariable String cpf) {
		return pessoaRepository.findByCpf(cpf);
	}
	
	@ApiOperation (value = "Consultando pessoas por nome ou parte do nome")
	@GetMapping("/pessoa")
	public Iterable<Pessoa> getPessoas(@RequestParam(required = false) String nome){
		if(nome == null)
		return pessoaRepository.findAll();
		return pessoaRepository.findAllByNomeContainingIgnoreCase(nome);
	}
	
	@ApiOperation (value = "Postando uma pessoa física")
	@PostMapping("/pessoa")
	public void postPessoa(@RequestBody Pessoa novo) {
			pessoaRepository.save(novo);		
	}
	
	@ApiOperation (value = "Postando várias pessoas físicas ao mesmo tempo")
	@PostMapping("/pessoa/em-lote")
	public String postPessoaEmLote(@RequestBody Iterable<Pessoa> novos) {
		List<Pessoa>existentes = new ArrayList<>();
		for (Pessoa novo : novos) {
			try {
			pessoaRepository.save(novo);
		} catch (DataIntegrityViolationException e) {
			existentes.add(novo);
	}	
}
		if(existentes.isEmpty()) 
			return "Todos as pessoas foram cadastrados!";
		else 
			return "Algumas pessoas não foram cadastrados: " + existentes.toString();
}
	@ApiOperation (value = "Alterando pessoa")
	@PutMapping("/pessoa/{cpf}")
	public void putPessoa(@PathVariable String cpf, @RequestBody Pessoa modificado) {
		Optional<Pessoa> opcional = pessoaRepository.findByCpf(cpf);
		
		if(opcional.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa Inexistente");
		
		Pessoa existente = opcional.get();
		existente.setNome(modificado.getNome());
		
		pessoaRepository.save(existente);
	}
	
	@ApiOperation (value = "Deletando pessoa por CPF")
	@DeleteMapping("/pessoa/{cpf}")
	public void deletePessoa(@PathVariable String cpf) {
		Optional<Pessoa> opcional = pessoaRepository.findByCpf(cpf);
		
		if(opcional.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa Inexistente");
		
		Pessoa existente = opcional.get();
		pessoaRepository.delete(existente);
}
}



	


	
