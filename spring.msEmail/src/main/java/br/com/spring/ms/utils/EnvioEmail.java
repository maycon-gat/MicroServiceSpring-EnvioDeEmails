package br.com.spring.ms.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.gson.JsonObject;

public class EnvioEmail {

	public static JsonObject enviar(String email, String html) throws Exception {

		String titulo = "Envio de email com Spring";

		String emailPara = email;
		String emailDe = "seu@email.com.br";
		String senhaEmail = "suasenha";
		String usuario = "seu@email.com.br";
		String host = "smtp.gmail.com";
		int porta = 587;
		Properties props = System.getProperties();

		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.port", porta);
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.ssl.trust", host);
		props.put("mail.smtp.starttls.required", "true");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");

		Session sessao = Session.getDefaultInstance(props);
		Transport transport = sessao.getTransport();
		JsonObject retorno = new JsonObject();

		try {

			MimeMessage msg = new MimeMessage(sessao);
			msg.setFrom(new InternetAddress(emailDe));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(emailPara));
			msg.setSubject(titulo);
			msg.setContent(html, "text/html; charset=UTF-8");

			transport.connect(host, usuario, senhaEmail);
			transport.sendMessage(msg, msg.getAllRecipients());
			retorno.addProperty("mensagem", "E-mail enviado para: " + email);
			retorno.addProperty("statusMensagem", 200);
			return retorno;

		} catch (Exception ex) {
			retorno.addProperty("erro", "E-mail não enviado para: " + email);
			retorno.addProperty("statusMensagem", 400);
			retorno.addProperty("mensagemErro", ex.getMessage());
			return retorno;
		} finally {
			transport.close();
		}
	}

}