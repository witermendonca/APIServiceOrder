package com.serviceorder.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.serviceorder.domain.exception.EntidadeNaoEncontradaException;
import com.serviceorder.domain.model.Comentarios;
import com.serviceorder.domain.model.OrdemServico;
import com.serviceorder.domain.repository.OrdemServicoRepository;
import com.serviceorder.domain.service.OrdemServicoService;
import com.serviceorder.dto.ComentariosInputModel;
import com.serviceorder.dto.ComentariosModel;

@RestController
@RequestMapping("/ordens-servico/{ordemServicoId}/comentarios")
public class ComentariosController {

	@Autowired
	private OrdemServicoService ordemServicoService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private OrdemServicoRepository ordemServicoRepository;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ComentariosModel post(@PathVariable Long ordemServicoId, @Valid @RequestBody ComentariosInputModel descricao) {
		
		Comentarios comentarios = ordemServicoService.postComentario(ordemServicoId, descricao.getDescricao());
		
		return toModel(comentarios);
	}
	
	@GetMapping
	public List<ComentariosModel> getComentarios(@PathVariable Long  ordemServicoId){
		
		OrdemServico ordemServico = ordemServicoRepository.findById(ordemServicoId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Ordem de serviço não encontrada"));
		
		return toCollectionModel(ordemServico.getComentarios());
	}
	
	private ComentariosModel toModel(Comentarios comentarios) {
		return modelMapper.map(comentarios, ComentariosModel.class);
	}
	
	private List<ComentariosModel> toCollectionModel(List<Comentarios> comentarios){
		return comentarios.stream()
				.map(comentario -> toModel(comentario))
				.collect(Collectors.toList());
	}
}
