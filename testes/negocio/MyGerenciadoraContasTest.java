package negocio;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MyGerenciadoraContasTest {
	
	private GerenciadoraContas gerenciadoraContas;
	private List<ContaCorrente> contasDoBanco;
	private int quantidadeContas;
	private List<Integer> idsContas;
	private List<Integer> idsContasInexistentes;
	private List<Double> saldosContas;
	private List<Boolean> ativosContas;
	
	@Before
	public void setUp() {
		this.quantidadeContas = 4;
		
		this.idsContas = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
		this.idsContasInexistentes = new ArrayList<>(Arrays.asList(5, 6));
		this.saldosContas = new ArrayList<>(Arrays.asList(100.0, 200.0, 300.0, 400.0));
		this.ativosContas = new ArrayList<>(Arrays.asList(false, false, true, true));
		
		this.contasDoBanco = new ArrayList<>();
		for (int i = 0; i < this.quantidadeContas; i++) {
			this.contasDoBanco.add(
					new ContaCorrente (
							this.idsContas.get(i),
							this.saldosContas.get(i),
							this.ativosContas.get(i)
					)
			);
		}
		
		this.gerenciadoraContas = new GerenciadoraContas(this.contasDoBanco);
	}
	
	@After
	public void tearDown() {
		this.gerenciadoraContas.limpa();
		this.contasDoBanco.clear();
		this.idsContas.clear();
		this.idsContasInexistentes.clear();
		this.saldosContas.clear();
		this.ativosContas.clear();
		this.quantidadeContas = 0;
	}
	
	@Test
	public void testGetContasDoBanco() {
		ContaCorrente contaObtida;
		List<ContaCorrente> listaContas = this.gerenciadoraContas.getContasDoBanco();
		assertThat(contasDoBanco.size(), is(this.quantidadeContas));
		for (int i = 0; i < this.quantidadeContas; i++) {
			contaObtida = listaContas.get(i);
			assertThat(contaObtida, is(this.contasDoBanco.get(i)));
		}
	}
	
	@Test
	public void testPesquisaConta_Existente() {
		ContaCorrente contaObtida;
		for (int i = 0; i < this.quantidadeContas; i++) {
			contaObtida = this.gerenciadoraContas.pesquisaConta(this.idsContas.get(i));
			assertThat(contaObtida, is(this.contasDoBanco.get(i)));
		}
	}
	
	@Test
	public void testPesquisaConta_Inexistente() {
		ContaCorrente contaInexistente;
		for (int i = 0; i < this.idsContasInexistentes.size(); i++) {
			contaInexistente = this.gerenciadoraContas.pesquisaConta(this.idsContasInexistentes.get(i));
			assertNull(contaInexistente);
		}
	}
	
	@Test
	public void testAdicionaConta() {
		int idContaNova = 500;
		
		ContaCorrente contaNova = new ContaCorrente(idContaNova, 1000.0, true);
		this.gerenciadoraContas.adicionaConta(contaNova);
		ContaCorrente contaNovaObtida = this.gerenciadoraContas.pesquisaConta(idContaNova);
		
		assertThat(this.gerenciadoraContas.getContasDoBanco().size(), is(this.quantidadeContas + 1));
		assertThat(contaNovaObtida, is(contaNova));
	}
	
	@Test
	public void testRemoveConta_Existente() {
		boolean resultadoRemocao;
		ContaCorrente contaRemovida;
		
		for (int i = 0; i < this.quantidadeContas; i++) {
			resultadoRemocao = this.gerenciadoraContas.removeConta(this.idsContas.get(i));
			contaRemovida = this.gerenciadoraContas.pesquisaConta(this.idsContas.get(i));
			assertTrue(resultadoRemocao);
			assertNull(contaRemovida);
		}
		assertThat(this.gerenciadoraContas.getContasDoBanco().size(), is(0));
	}
	
	@Test
	public void testRemoveConta_Inexistente() {
		boolean resultadoRemocao;
		ContaCorrente contaRemovida;
		
		for (int i = 0; i < this.idsContasInexistentes.size(); i++) {
			resultadoRemocao = this.gerenciadoraContas.removeConta(this.idsContasInexistentes.get(i));
			contaRemovida = this.gerenciadoraContas.pesquisaConta(this.idsContasInexistentes.get(i));
			assertFalse(resultadoRemocao);
			assertNull(contaRemovida);
		}
		assertThat(this.gerenciadoraContas.getContasDoBanco().size(), is(this.quantidadeContas));
	}
	
	@Test
	public void testContaAtiva() {
		boolean contaAtiva;
		for (int i = 0; i < this.quantidadeContas; i++) {
			contaAtiva = this.gerenciadoraContas.contaAtiva(this.idsContas.get(i));
			assertThat(contaAtiva, is(this.ativosContas.get(i)));
		}
	}
	
	@Test
	public void testLimpa() {
		ContaCorrente contaInexistente;
		this.gerenciadoraContas.limpa();
		assertThat(this.gerenciadoraContas.getContasDoBanco().size(), is(0));
		for (int i = 0; i < this.quantidadeContas; i++) {
			contaInexistente = this.gerenciadoraContas.pesquisaConta(this.idsContas.get(i));
			assertNull(contaInexistente);
		}
	}
	
	@Test
	public void testTransfereValor_SaldoSuficiente() {
		ContaCorrente contaOrigem = this.gerenciadoraContas.pesquisaConta(this.idsContas.get(0));
		ContaCorrente contaDestino = this.gerenciadoraContas.pesquisaConta(this.idsContas.get(1));
		double saldoInicialContaOrigem = contaOrigem.getSaldo();
		double saldoInicialContaDestino = contaDestino.getSaldo();
		
		boolean resultadoTransferencia = this.gerenciadoraContas.transfereValor(contaOrigem.getId(), contaOrigem.getSaldo(), contaDestino.getId());
		assertTrue(resultadoTransferencia);
		
		double saldoFinalContaOrigem = contaOrigem.getSaldo();
		assertEquals(0.0, saldoFinalContaOrigem, 0.0001);
		
		double saldoFinalContaDestino = contaDestino.getSaldo();
		assertEquals(saldoInicialContaOrigem + saldoInicialContaDestino, saldoFinalContaDestino, 0.0001);
	}
	
	@Test
	public void testTransfereValor_SaldoInsuficiente() {
		ContaCorrente contaOrigem = this.gerenciadoraContas.pesquisaConta(this.idsContas.get(0));
		ContaCorrente contaDestino = this.gerenciadoraContas.pesquisaConta(this.idsContas.get(1));
		double saldoInicialContaOrigem = contaOrigem.getSaldo();
		double saldoInicialContaDestino = contaDestino.getSaldo();
		
		boolean resultadoTransferencia = this.gerenciadoraContas.transfereValor(contaOrigem.getId(), contaOrigem.getSaldo()*2, contaDestino.getId());
		assertFalse(resultadoTransferencia);
		
		double saldoFinalContaOrigem = contaOrigem.getSaldo();
		assertEquals(saldoInicialContaOrigem, saldoFinalContaOrigem, 0.0001);
		
		double saldoFinalContaDestino = contaDestino.getSaldo();
		assertEquals(saldoInicialContaDestino, saldoFinalContaDestino, 0.0001);
	}

}
