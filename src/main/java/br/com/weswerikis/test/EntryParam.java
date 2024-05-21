package br.com.weswerikis.test;

import br.com.weswerikis.main.CertificadoHandler;

public class EntryParam {

	public static void main(String[] args) {

		args = new String[2];

		args[0] = "C:\\CERTA1\\CERT\\CERTIFICADO TESTE.pfx";
		args[1] = "12345678";

		CertificadoHandler.main(args);

	}
}
