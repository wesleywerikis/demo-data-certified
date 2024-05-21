package br.com.weswerikis.main;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import br.com.swconsultoria.certificado.Certificado;
import br.com.swconsultoria.certificado.CertificadoService;
import br.com.swconsultoria.certificado.exception.CertificadoException;

public class CertificadoHandler {

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Uso: java CertificadoHandler <caminho do certificado> <senha>");
			return;
		}

		String caminhoCertificado = args[0];
		String senha = args[1];

		try {
			criarArquivoTx2(caminhoCertificado, senha);
		} catch (CertificadoException | IOException e) {
			e.printStackTrace();
		}
	}

	public static void criarArquivoTx2(String caminhoCertificado, String senha)
			throws CertificadoException, IOException {
		Certificado certificado = CertificadoService.certificadoPfx(caminhoCertificado, senha);

		Map<String, Object> dadosCertificado = new HashMap<>();
		dadosCertificado.put("Nome", certificado.getNome());
		dadosCertificado.put("CNPJ", certificado.getCnpjCpf());
		dadosCertificado.put("DataHoraVencimento", certificado.getDataHoraVencimento());
		dadosCertificado.put("Vencimento", certificado.getVencimento());
		dadosCertificado.put("NumeroSerie", certificado.getNumeroSerie());
		dadosCertificado.put("Tipo", certificado.getArquivo());
		dadosCertificado.put("TipoICPBrasil", certificado.getTipoCertificado());

		ObjectMapper objectMapper = new ObjectMapper();
		JavaTimeModule javaTimeModule = new JavaTimeModule();

		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

		javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
		javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));

		objectMapper.registerModule(javaTimeModule);

		String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dadosCertificado);

		File arquivoTx2 = new File("certificado.tx2");
		objectMapper.writeValue(arquivoTx2, dadosCertificado);

		System.out.println("Arquivo certificado.tx2 criado com sucesso!");
	}
}
