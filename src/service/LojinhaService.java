package src.service;

import src.usuarios.Cliente;
import src.usuarios.Gerente;
import src.usuarios.UsuarioManager;

import java.io.FileNotFoundException;
import java.sql.SQLOutput;
import java.util.Scanner;

import static src.constants.Constantes.BD_GERENTES;
import static src.constants.Constantes.BD_USUARIOS;
import static src.constants.Constantes.CLIENTE;
import static src.constants.Constantes.GERENTE;
import static src.constants.Constantes.LOGIN_GERENTES;
import static src.constants.Constantes.LOGIN_USUARIOS;
import static src.service.Menus.menuCliente;
import static src.service.Menus.menuGerente;
import static src.service.Menus.menuInicial;

public class LojinhaService {
  private final UsuarioManager usuarioManager;

  public LojinhaService(UsuarioManager usuarioManager) {
    this.usuarioManager = usuarioManager;
  }

  public void iniciar() throws FileNotFoundException {
    usuarioManager.createIfNotExists();

    Scanner scanner = new Scanner(System.in);

    int opcao = 0;

    menuInicial();

    while (opcao != 4) {
      opcao = scanner.nextInt();
      scanner.nextLine();

      switch (opcao) {
        case 1:
          System.out.println("Opção 1 selecionada: FAZER LOGIN DE CLIENTE");
          System.out.print("Digite o CPF: ");
          String cpf = scanner.nextLine();
          if (usuarioManager.usuarioExiste(cpf, BD_USUARIOS)) {
            System.out.print("Digite a SENHA: ");
            String senha = scanner.nextLine();
            if (usuarioManager.login(cpf, senha, LOGIN_USUARIOS)) {
              usuarioManager.bemVindoUsuario(cpf, BD_USUARIOS);
              exibirMenuCliente();
            } else {
              System.out.println("Não foi possível fazer login. Por favor tente novamente.");
              return;
            }
          } else {
            System.out.printf("O usuário com o CPF %s não existe. Retornando ao menu inicial.", cpf);
            System.out.println();
            menuInicial();
          }
          break;
        case 2:
          System.out.println("Opção 2 selecionada: FAZER CADASTRO");
          System.out.println("Você deseja fazer o cadastro de cliente ou administrador? ");
          System.out.print("Digite 1 para Cliente ou 2 para Administrador: ");

          int tipoCadastro = scanner.nextInt();
          scanner.nextLine();

          if (tipoCadastro == 1) {
            System.out.print("Digite o CPF: ");
            cpf = scanner.next();
            if (usuarioManager.usuarioExiste(cpf, BD_USUARIOS)) {
              System.out.println("Já existe um usuário com este CPF.");
              System.out.println("Redirecionando ao menu principal");
              menuInicial();
              return;
            }
            System.out.print("Digite o nome: ");
            String nome = scanner.nextLine();
            System.out.print("Digite o telefone: ");
            String telefone = scanner.next();
            System.out.print("Digite sua senha de acesso: ");
            String senha = scanner.next();
            Cliente cliente = new Cliente(cpf, nome, telefone);
            usuarioManager.salvarUsuario(cpf, nome, telefone, CLIENTE, BD_USUARIOS);
            usuarioManager.salvarDadosAcessoUsuario(cliente.getCpf(), senha, CLIENTE, LOGIN_USUARIOS);
            System.out.println("Cadastro realizado com sucesso!");

          } else if (tipoCadastro == 2) {
            System.out.print("Digite o CPF: ");
            cpf = scanner.next();
            scanner.nextLine();
            if (usuarioManager.usuarioExiste(cpf, BD_GERENTES)) {
              System.out.println("Já existe um administrador com este CPF.");
              System.out.println("Redirecionando ao menu principal");
              menuInicial();
              return;
            }
            System.out.print("Digite o nome: ");
            String nome = scanner.nextLine();
            System.out.print("Digite o telefone: ");
            String telefone = scanner.next();
            System.out.print("Digite sua senha de acesso: ");
            String senha = scanner.next();
            Gerente gerente = new Gerente(cpf, nome, telefone);
            usuarioManager.salvarUsuario(cpf, nome, telefone, GERENTE, BD_GERENTES);
            usuarioManager.salvarDadosAcessoUsuario(gerente.getCpf(), senha, GERENTE, LOGIN_GERENTES);
            System.out.println("Cadastro realizado com sucesso!");
          }

          System.out.println("Redirecionando para o Menu Inicial");
          System.out.println("Faça login, por favor.");
          menuInicial();
          break;
        case 3:
          System.out.println("Opção 3 selecionada: FAZER LOGIN DE GERENTE");
          System.out.print("Digite o CPF: ");
          cpf = scanner.nextLine();
          if (usuarioManager.usuarioExiste(cpf, BD_GERENTES)) {
            System.out.print("Digite a SENHA: ");
            String senha = scanner.nextLine();
            if (usuarioManager.login(cpf, senha, LOGIN_GERENTES)) {
              usuarioManager.bemVindoUsuario(cpf, BD_GERENTES);
              exibirMenuGerente();
            } else {
              System.out.println("Não foi possível fazer login. Por favor tente novamente.");
              return;
            }
          } else {
            System.out.printf("O usuário com o CPF %s não existe. Retornando ao menu inicial.", cpf);
            System.out.println();
            menuInicial();
          }
          break;

        case 4:
          System.out.println("Saindo do sistema. Até breve!");
          scanner.close();
          return;
        default:
          return;
      }
    }
    scanner.close();
  }

  public void exibirMenuCliente() throws FileNotFoundException {
    UsuarioManager usuarioManager = new UsuarioManager();
    menuCliente();
    int selecionar = 0;
    Scanner scanner = new Scanner(System.in);
    while (selecionar != 5) {
      selecionar = scanner.nextInt();
//      scanner.nextLine();
      switch (selecionar) {
        case 1:
          System.out.println("Opção 1 selecionada: Nova Compra");
          break;
        case 2:
          System.out.println("Opção 2 selecionada: Visualizar Cadastro");
          System.out.print("Digite o CPF novamente: ");
          String cpf = scanner.next();
          usuarioManager.visualizarCadastro(cpf, BD_USUARIOS);
          System.out.println("Retornar ao menu de cliente? S/N");
          String resposta = scanner.next();
          resposta.toLowerCase();
          switch (resposta) {
            case "s":
              exibirMenuCliente();
              break;
            case "n":
              menuInicial();
              selecionar = 5;
              return;
            default:
              System.out.println("Saindo do sistema. Até breve!");
              return;
          }
          break;
        case 3:
          System.out.println("Opção 3 selecionada: Alterar Cadastro");
          System.out.print("Digite o CPF novamente: ");
          cpf = scanner.next();
          scanner.nextLine();
          usuarioManager.visualizarCadastro(cpf, BD_USUARIOS);
          System.out.println("NOVO NOME: ");
          String novoNome = scanner.nextLine();
          System.out.println("NOVO TELEFONE: ");
          String novoTelefone = scanner.nextLine();
          usuarioManager.alterarCadastro(cpf, novoNome, novoTelefone);
          System.out.println("Retornar ao menu de cliente? S/N");
          resposta = scanner.next();
          resposta.toLowerCase();
          switch (resposta) {
            case "s":
              exibirMenuCliente();
              break;
            case "n":
              menuInicial();
              selecionar = 5;
              return;
            default:
              System.out.println("Saindo do sistema. Até breve!");
              return;
          }
          break;
        case 4:
          System.out.println("Opção 4 selecionada: Remover Cadastro");
          System.out.print("Tem certeza que deseja deletar o cadastro? S/N: ");
          resposta = scanner.next();
          if (resposta.equalsIgnoreCase("s")) {
            System.out.println("Digite o CPF para confirmar: ");
            cpf = scanner.next();
            usuarioManager.removerCadastro(cpf);
            System.out.println("Voltando ao menu principal.");
            menuInicial();
            break;
          } else if (resposta.equalsIgnoreCase("n")) {
            System.out.println("OK. Voltando ao menu de cliente");
            menuInicial();
            break;
          }
          break;
        case 5:
          System.out.println("Saindo do sistema. Até breve!");
          iniciar();
          break;
        default:
          System.out.println("Opção inválida. Tente novamente.");
      }
    }
    scanner.close();
  }

  public void exibirMenuGerente() throws FileNotFoundException {
    UsuarioManager usuarioManager = new UsuarioManager();
    menuGerente();
    int selecionar = 0;
    Scanner scanner = new Scanner(System.in);
    while (selecionar != 8) {
      selecionar = scanner.nextInt();
      switch (selecionar) {
        case 1:
          System.out.println("Opção 1 selecionada: Cadastrar Cliente");
          break;
        case 2:
          System.out.println("Opção 2 selecionada: Ver cadastros de clientes");
          System.out.println("=".repeat(70));
          usuarioManager.visualizarCadastros();
          System.out.println("Retornar ao menu de gerente? S/N");
          String resposta = scanner.next();
          resposta.toLowerCase();
          switch (resposta) {
            case "s":
              exibirMenuGerente();
              break;
            case "n":
              menuInicial();
              selecionar = 8;
              return;
            default:
              System.out.println("Saindo do sistema. Até breve!");
              return;
          }
          break;
        case 3:
          System.out.println("Opção 3 selecionada: Alterar cadastro de cliente");
          System.out.println("Listando primeiro os cadastros dos clientes para escolha.");
          System.out.println("=".repeat(70));
          usuarioManager.visualizarCadastros();
          System.out.print("Digite o CPF do CLIENTE que deseja alterar: ");
          String cpf = scanner.next();
          scanner.nextLine();
          System.out.println("DIGITE O NOVO NOME: ");
          String novoNome = scanner.nextLine();
          System.out.println("DIGITE O NOVO TELEFONE: ");
          String novoTelefone = scanner.nextLine();
          usuarioManager.alterarCadastro(cpf, novoNome, novoTelefone);
          System.out.println("Retornar ao menu de gerente? S/N");
          resposta = scanner.next();
          resposta.toLowerCase();
          switch (resposta) {
            case "s":
              exibirMenuGerente();
              break;
            case "n":
              menuInicial();
              selecionar = 8;
              return;
            default:
              System.out.println("Saindo do sistema. Até breve!");
              return;
          }
          break;
        case 4:
          System.out.println("Opção 4 selecionada: Excluir Cadastro de Cliente");
          break;
        case 5:
          System.out.println("Opção 5 selecionada: Cadastrar Produto");
          break;
        case 6:
          System.out.println("Opção 6 selecionada: Ver estoque");
          break;
        case 7:
          System.out.println("Opção 7 selecionada: Excluir Cadastro de Produto");
          break;
        case 8:
          System.out.println("Saindo do sistema. Até breve!");
          iniciar();
          break;
        default:
          System.out.println("Opção inválida. Tente novamente.");
      }
    }
    scanner.close();
  }
}