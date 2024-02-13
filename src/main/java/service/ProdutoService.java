package src.main.java.service;

import src.main.java.infrastructure.exception.BusinessException;
import src.main.java.infrastructure.utils.FileUtils;
import src.main.java.repositories.ProdutoRepositoryVIew;
import src.main.java.repositories.repository.ProdutoRepository;
import src.main.java.rest.dtos.ProdutoDto;

import java.io.FileNotFoundException;

import static src.main.java.constants.Constantes.BD_PRODUTOS;
import static src.main.java.constants.Constantes.BD_PRODUTOS_CABECALHO;

public class ProdutoService {

  private final ProdutoRepositoryVIew repository;

  public ProdutoService(FileUtils fileUtils, ProdutoRepository repository) {
    this.repository = repository;
    fileUtils.createIfNotExists(BD_PRODUTOS, BD_PRODUTOS_CABECALHO);
  }

  public void cadastrarProduto(String codigo, String descricaoProduto, Double valor, int quantidade) throws BusinessException {
    repository.cadastrarProduto(codigo, descricaoProduto, valor, quantidade);
  }

  public boolean produtoExiste(String codigo, String fileName) throws BusinessException {
    return repository.produtoExiste(codigo, fileName);
  }

  public boolean isAvailableQuantity(String codigo, int quantidade) throws FileNotFoundException {
    return repository.haQuantidadeDisponivel(codigo, quantidade);
  }

  public void alterarQuantidadeProduto(String codigo, Integer quantidade, String metodo) throws BusinessException {
    repository.changeQuantity(codigo, quantidade, metodo);
  }

  public void visualizarProdutos() throws BusinessException {
    repository.visualizarCadastroProdutos();
  }

  public void alterarPrecoProduto(String codigo, Double novoValor) throws BusinessException {
    repository.alterarCadastroDeProduto(codigo, null, novoValor, null);
  }

  public void alterarCadastroProduto(String codigo, String novaDescricao, Double novoValor, Integer novaQuantidade) throws BusinessException {
    repository.alterarCadastroDeProduto(codigo, novaDescricao, novoValor, novaQuantidade);
  }

  public void excluirProduto(String codigo) throws BusinessException {
    repository.excluirProduto(codigo);
  }

  public ProdutoDto getProduto(String codigo) {
    return repository.getProduto(codigo);
  }

}
