package com.serviceorder.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.serviceorder.domain.model.OrdemServico;
import com.serviceorder.domain.repository.OrdemServicoRepository;
import com.serviceorder.domain.service.OrdemServicoService;
import com.serviceorder.dto.OrdemServicoInputModel;
import com.serviceorder.dto.OrdemServicoModel;

@RestController
@RequestMapping("/ordens-servico")
public class OrdemServicoController {

	@Autowired
	private OrdemServicoService ordemServicoService;
	
	@Autowired
	private OrdemServicoRepository ordemServicoRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public OrdemServicoModel post(@Valid @RequestBody OrdemServicoInputModel ordemServicoInput) {
		OrdemServico ordemServico = toEntity(ordemServicoInput); 
		return toModel(ordemServicoService.post(ordemServico));
	}
	
	@PutMapping("/{ordemServicoId}/finalizacao")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void finalizar(@PathVariable Long ordemServicoId) {
		ordemServicoService.finalizar(ordemServicoId);
	}
	
	@PutMapping("/{ordemServicoId}/cancelacao")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void cancelar(@PathVariable Long ordemServicoId) {
		ordemServicoService.cancelar(ordemServicoId);
	}
	
	@GetMapping
	public List<OrdemServicoModel> getAll(){
		return toCollectionModel(ordemServicoRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<OrdemServicoModel> getById(@PathVariable Long id){
		Optional<OrdemServico> ordemServico = ordemServicoRepository.findById(id);
		
		if(ordemServico.isPresent()) {
			OrdemServicoModel ordemServicoModel = toModel(ordemServico.get());
			return ResponseEntity.ok(ordemServicoModel);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	private OrdemServicoModel toModel(OrdemServico ordemServico) {
		return modelMapper.map(ordemServico, OrdemServicoModel.class);
	}
	
	private List<OrdemServicoModel> toCollectionModel(List<OrdemServico> ordensServico){
		return ordensServico.stream()
				.map(ordemServico -> toModel(ordemServico))
				.collect(Collectors.toList());
	}
	
	private OrdemServico toEntity(OrdemServicoInputModel ordemServicoInput) {
		return modelMapper.map(ordemServicoInput, OrdemServico.class);
	}
}
