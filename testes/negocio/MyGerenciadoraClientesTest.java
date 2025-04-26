package negocio;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MyGerenciadoraClientesTest {
	
	private GerenciadoraClientes gerenciadoraClientes;
	private List<Cliente> clientesDoBanco;
	private int quantidadeClientes;
	private List<Integer> idsClientes;
	private List<Integer> idsClientesInexistentes;
	private List<String> nomesClientes;
	private List<Integer> idadesClientes;
	private List<String> emailsClientes;
	private List<Integer> idsContasCorrentesClientes;
	private List<Boolean> ativosClientes;
	
	@Before
	public void setUp() {
		this.quantidadeClientes = 4;
		
		this.idsClientes = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
		this.idsClientesInexistentes = new ArrayList<>(Arrays.asList(5, 6));
		this.nomesClientes = new ArrayList<>(Arrays.asList("Ana Machado", "Breno Pires", "Carla Almeida", "Diogo Furtado"));
		this.idadesClientes = new ArrayList<>(Arrays.asList(21, 22, 23, 24));
		this.emailsClientes = new ArrayList<>(Arrays.asList("anamachado@gmail.com", "brenopires@gmail.com", "carlaalmeida@gmail.com", "diogofurtado@gmail.com"));
		this.idsContasCorrentesClientes = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
		this.ativosClientes = new ArrayList<>(Arrays.asList(false, false, true, true));
		
		this.clientesDoBanco = new ArrayList<>();
		for (int i = 0; i < this.quantidadeClientes; i++) {
			this.clientesDoBanco.add(
					new Cliente(
							this.idsClientes.get(i),
							this.nomesClientes.get(i),
							this.idadesClientes.get(i),
							this.emailsClientes.get(i),
							this.idsContasCorrentesClientes.get(i),
							this.ativosClientes.get(i)
					)
			);
		}
		
		this.gerenciadoraClientes = new GerenciadoraClientes(clientesDoBanco);
	}
	
	@After
	public void tearDown() {
		this.gerenciadoraClientes.limpa();
		this.clientesDoBanco.clear();
		this.idsClientes.clear();
		this.nomesClientes.clear();
		this.idadesClientes.clear();
		this.emailsClientes.clear();
		this.idsContasCorrentesClientes.clear();
		this.ativosClientes.clear();
		this.quantidadeClientes = 0;
	}
	
	@Test
	public void testGetClientesDoBanco() {
		Cliente clienteObtido;
		List<Cliente> listaClientes = this.gerenciadoraClientes.getClientesDoBanco();
		assertThat(clientesDoBanco.size(), is(this.quantidadeClientes));
		for (int i = 0; i < this.quantidadeClientes; i++) {
			clienteObtido = listaClientes.get(i);
			assertThat(clienteObtido, is(this.clientesDoBanco.get(i)));
		}
	}
	
	@Test
	public void testPesquisaCliente_Existente() {
		Cliente clienteObtido;
		for (int i = 0; i < this.quantidadeClientes; i++) {
			clienteObtido = this.gerenciadoraClientes.pesquisaCliente(idsClientes.get(i));
			assertThat(clienteObtido, is(this.clientesDoBanco.get(i)));
		}
	}
	
	@Test
	public void testPesquisaCliente_Inexistente() {
		Cliente clienteInexistente;
		for (int i = 0; i < this.idsClientesInexistentes.size(); i++) {
			clienteInexistente = this.gerenciadoraClientes.pesquisaCliente(this.idsClientesInexistentes.get(i));
			assertNull(clienteInexistente);
		}
	}
	
	@Test
	public void testAdicionaCliente() {
		int idClienteNovo = 500;
		
		Cliente clienteNovo = new Cliente(idClienteNovo, "Eliana Gomes", 25, "elianagomes@gmail.com", 500, true);
		this.gerenciadoraClientes.adicionaCliente(clienteNovo);
		Cliente clienteNovoObtido = this.gerenciadoraClientes.pesquisaCliente(idClienteNovo);
		
		assertThat(this.gerenciadoraClientes.getClientesDoBanco().size(), is(this.quantidadeClientes + 1));
		assertThat(clienteNovoObtido, is(clienteNovo));
	}
	
	@Test
	public void testRemoveCliente_Existente() {
		boolean resultadoRemocao;
		Cliente clienteRemovido;
		
		for (int i = 0; i < this.quantidadeClientes; i++) {
			resultadoRemocao = this.gerenciadoraClientes.removeCliente(this.idsClientes.get(i));
			clienteRemovido = this.gerenciadoraClientes.pesquisaCliente(this.idsClientes.get(i));
			assertTrue(resultadoRemocao);
			assertNull(clienteRemovido);
		}
		assertThat(this.gerenciadoraClientes.getClientesDoBanco().size(), is(0));
	}
	
	@Test
	public void testRemoveCliente_Inexistente() {
		boolean resultadoRemocao;
		Cliente clienteInexistente;
		for (int i = 0; i < this.idsClientesInexistentes.size(); i++) {
			resultadoRemocao = this.gerenciadoraClientes.removeCliente(this.idsClientesInexistentes.get(i));
			clienteInexistente = this.gerenciadoraClientes.pesquisaCliente(this.idsClientesInexistentes.get(i));
			assertFalse(resultadoRemocao);
			assertNull(clienteInexistente);
		}
		assertThat(this.gerenciadoraClientes.getClientesDoBanco().size(), is (this.quantidadeClientes));
	}
	
	@Test
	public void testClienteAtivo() {
		boolean clienteAtivo;
		for (int i = 0; i < this.quantidadeClientes; i++) {
			clienteAtivo = this.gerenciadoraClientes.clienteAtivo(this.idsClientes.get(i));
			assertThat(clienteAtivo, is(this.ativosClientes.get(i)));
		}
	}
	
	@Test
	public void testLimpa() {
		Cliente clienteInexistente;
		this.gerenciadoraClientes.limpa();
		for (int i = 0; i < this.quantidadeClientes; i++) {
			clienteInexistente = this.gerenciadoraClientes.pesquisaCliente(this.idsClientes.get(i));
			assertNull(clienteInexistente);
		}
		assertThat(this.gerenciadoraClientes.getClientesDoBanco().size(), is(0));
	}
	
	@Test
	public void testValidaIdade_IdadeValida() {
		boolean resultadoValidaIdade;
		try {
			resultadoValidaIdade = this.gerenciadoraClientes.validaIdade(18);
			assertTrue(resultadoValidaIdade);
			resultadoValidaIdade = this.gerenciadoraClientes.validaIdade(30);
			assertTrue(resultadoValidaIdade);
			resultadoValidaIdade = this.gerenciadoraClientes.validaIdade(65);
			assertTrue(resultadoValidaIdade);
		} catch (IdadeNaoPermitidaException e) {
			fail();
		}
	}
	
	@Test
	public void testValidaIdade_IdadeInValida() {
		try {
			this.gerenciadoraClientes.validaIdade(17);
			fail();
		} catch (IdadeNaoPermitidaException e) {
			assertThat(e.getMessage(), is(IdadeNaoPermitidaException.MSG_IDADE_INVALIDA));
		}
		try {
			this.gerenciadoraClientes.validaIdade(66);
			fail();
		} catch (IdadeNaoPermitidaException e) {
			assertThat(e.getMessage(), is(IdadeNaoPermitidaException.MSG_IDADE_INVALIDA));
		}
	}
	
}
