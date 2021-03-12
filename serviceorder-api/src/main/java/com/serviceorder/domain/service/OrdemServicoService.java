package com.serviceorder.domain.service;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.serviceorder.domain.exception.EntidadeNaoEncontradaException;
import com.serviceorder.domain.exception.NegocioException;
import com.serviceorder.domain.model.Cliente;
import com.serviceorder.domain.model.Comentarios;
import com.serviceorder.domain.model.OrdemServico;
import com.serviceorder.domain.model.StatusOrdemServico;
import com.serviceorder.domain.repository.ClienteRepository;
import com.serviceorder.domain.repository.ComentariosRepository;
import com.serviceorder.domain.repository.OrdemServicoRepository;

@Service
public class OrdemServicoService {

	@Autowired
	private OrdemServicoRepository ordemServicoRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private ComentariosRepository comentariosRepository;

	public OrdemServico post(OrdemServico ordemServico) {
		Cliente cliente = clienteRepository.findById(ordemServico.getCliente().getId())
				.orElseThrow(() -> new NegocioException("Cliente não encontrado"));

		ordemServico.setCliente(cliente);
		ordemServico.setStatus(StatusOrdemServico.ABERTA);
		ordemServico.setDataAbertura(OffsetDateTime.now());

		return ordemServicoRepository.save(ordemServico);
	}

	public void finalizar(Long ordemServicoId) {
		OrdemServico ordemServico = buscarOrdemServico(ordemServicoId);
		
		ordemServico.finalizar();
		
		ordemServicoRepository.save(ordemServico);
	}
	
	public void cancelar(Long ordemServicoId) {
		OrdemServico ordemServico = buscarOrdemServico(ordemServicoId);
		
		ordemServico.cancelar();
		
		ordemServicoRepository.save(ordemServico);
	}

	public Comentarios postComentario(Long ordemServicoId, String descricao) {

		OrdemServico ordemServico = buscarOrdemServico(ordemServicoId);

		Comentarios comentarios = new Comentarios();

		comentarios.setDataEnvio(OffsetDateTime.now());
		comentarios.setDescricao(descricao);
		comentarios.setOrdemServico(ordemServico);

		return comentariosRepository.save(comentarios);

	}

	private OrdemServico buscarOrdemServico(Long ordemServicoId) {
		return ordemServicoRepository.findById(ordemServicoId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Ordem de serviço não encontrada"));
	}
}
