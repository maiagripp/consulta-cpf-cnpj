package org.serratec.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.serratec.models.Empresa;
import org.serratec.repository.EmpresaRepository;
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
@Api (value = "API - Consulta de pessoa jurídica")

public class EmpresaResource {
	@Autowired
	EmpresaRepository empresaRepository;
		
		@ApiOperation (value = "Consultando todas pessoas jurídicas")
		@GetMapping("/empresa/todas")
		public Iterable<Empresa> getTodos(){
			return empresaRepository.findAll();
		}
		
		@ApiOperation (value = "Consultando pessoas por CNPJ")
		@GetMapping("/empresa/por-cnpj/{cnpj}")
		private Optional<Empresa>getByCnpj(@PathVariable String cnpj) {
			return empresaRepository.findByCnpj(cnpj);
		}
		
		@ApiOperation (value = "Consultando empresas por nome ou parte do nome")
		@GetMapping("/empresa")
		public Iterable<Empresa> getEmpresas(@RequestParam(required = false) String nome){
			if(nome == null)
			return empresaRepository.findAll();
			return empresaRepository.findAllByNomeContainingIgnoreCase(nome);
		}
		
		@ApiOperation (value = "Postando uma pessoa jurídica")
		@PostMapping("/empresa")
		public void postEmpresa(@RequestBody Empresa novo) {
				empresaRepository.save(novo);		
		}
		
		@ApiOperation (value = "Postando várias pessoas jurídicas ao mesmo tempo")
		@PostMapping("/empresas/em-lote")
		public String postEmpresaEmLote(@RequestBody Iterable<Empresa> novos) {
			List<Empresa>existentes = new ArrayList<>();
			for (Empresa novo : novos) {
				try {
				empresaRepository.save(novo);
			} catch (DataIntegrityViolationException e) {
				existentes.add(novo);
		}	
	}
			if(existentes.isEmpty()) 
				return "Todos as empresas foram cadastrados!";
			else 
				return "Algumas empresas não foram cadastrados: " + existentes.toString();
	}
		@ApiOperation (value = "Alterando pessoa jurídica")
		@PutMapping("/empresa/{cnpj}")
		public void putEmpresa(@PathVariable String cnpj, @RequestBody Empresa modificado) {
			Optional<Empresa> opcional = empresaRepository.findByCnpj(cnpj);
			
			if(opcional.isEmpty())
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa Inexistente");
			
			Empresa existente = opcional.get();
			existente.setNome(modificado.getNome());
			
			empresaRepository.save(existente);
		}
		
		@ApiOperation (value = "Deletando pessoa por CNPJ")
		@DeleteMapping("/empresa/{cnpj}")
		public void deleteEmpresa(@PathVariable String cnpj) {
			Optional<Empresa> opcional = empresaRepository.findByCnpj(cnpj);
			
			if(opcional.isEmpty())
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa Inexistente");
			
			Empresa existente = opcional.get();
			empresaRepository.delete(existente);
	}
	}

