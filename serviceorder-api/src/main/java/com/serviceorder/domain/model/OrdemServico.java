package com.serviceorder.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.serviceorder.domain.ValidationGroups;
import com.serviceorder.domain.exception.NegocioException;

@Entity
@Table(name = "tb_ordem_servico")
public class OrdemServico {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Valid
	@ConvertGroup(from = Default.class, to = ValidationGroups.ClienteId.class) // convertendo validação default.
	@NotNull
	@ManyToOne // relacionamento com cliente
	private Cliente cliente;

	@NotBlank
	private String descricao;

	@NotNull
	private BigDecimal preco;

	@JsonProperty(access = Access.READ_ONLY) // apenas para leitura
	@Enumerated(EnumType.STRING)
	private StatusOrdemServico status;

	@JsonProperty(access = Access.READ_ONLY) // apenas para leitura
	private OffsetDateTime dataAbertura;

	@JsonProperty(access = Access.READ_ONLY) // apenas para leitura
	private OffsetDateTime dataFinalizacao;

	@JsonProperty(access = Access.READ_ONLY) // apenas para leitura
	private OffsetDateTime dataCancelamento;

	@OneToMany(mappedBy = "ordemServico", cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties("ordemServico")
	private List<Comentarios> comentarios = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public StatusOrdemServico getStatus() {
		return status;
	}

	public void setStatus(StatusOrdemServico status) {
		this.status = status;
	}

	public OffsetDateTime getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(OffsetDateTime dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public OffsetDateTime getDataFinalizacao() {
		return dataFinalizacao;
	}

	public void setDataFinalizacao(OffsetDateTime dataFinalizacao) {
		this.dataFinalizacao = dataFinalizacao;
	}

	public OffsetDateTime getDataCancelamento() {
		return dataCancelamento;
	}

	public void setDataCancelamento(OffsetDateTime dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	public List<Comentarios> getComentarios() {
		return comentarios;
	}

	public void setComentarios(List<Comentarios> comentarios) {
		this.comentarios = comentarios;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrdemServico other = (OrdemServico) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	private boolean aberta() {
		return StatusOrdemServico.ABERTA.equals(getStatus());
	}

	private boolean finalizada() {
		return StatusOrdemServico.FINALIZADA.equals(getStatus());
	}

	private boolean cancelada() {
		return StatusOrdemServico.CANCELADA.equals(getStatus());
	}

	public void finalizar() {
		if (aberta() == false || cancelada() == true) {
			throw new NegocioException("Ordem de serviço não pode ser finalizada");
		}

		setStatus(StatusOrdemServico.FINALIZADA);
		setDataFinalizacao(OffsetDateTime.now());
	}

	public void cancelar() {
		if (aberta() == false || finalizada() == true) {
			throw new NegocioException("Ordem de serviço não pode ser cancelada");
		}

		setStatus(StatusOrdemServico.CANCELADA);
		setDataCancelamento(OffsetDateTime.now());
	}

}
