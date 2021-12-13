package br.com.spring.ms.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.com.spring.ms.utils.EnvioEmail;

@RestController
@RequestMapping(path = "/send", consumes = { MediaType.MULTIPART_FORM_DATA })
public class EnvioDeEmail {

	@PostMapping
	public Response envioEmailsenviar(@RequestParam("htmlFile") MultipartFile htmlFile,
			@RequestParam("emailFile") MultipartFile emailFile) throws Exception {

		String html = "";

		try {

			BufferedReader brHtml = new BufferedReader(new InputStreamReader(htmlFile.getInputStream(), "UTF-8"));

			String linhaHtml;

			while ((linhaHtml = brHtml.readLine()) != null) {
				html += linhaHtml;
			}

			brHtml.close();

		} catch (JSONException e) {

			return Response.status(400).entity(e.toString()).build();
		}

		try {

			BufferedReader brEmail = new BufferedReader(new InputStreamReader(emailFile.getInputStream(), "UTF-8"));

			String linhaEmail;
			String[] emailsArray;
			String emailExiste = "";
			ArrayList<JsonObject> retornoArray = new ArrayList<JsonObject>();

			while ((linhaEmail = brEmail.readLine()) != null) {

				if (linhaEmail.toLowerCase().contains("@")) {

					emailsArray = linhaEmail.split(";");

					for (String email : emailsArray) {

						if (email.contains("@")) {

							if (!emailExiste.toLowerCase().contains(email.toLowerCase())) {

								retornoArray.add(EnvioEmail.enviar(email.trim(), html));

							}

							emailExiste += linhaEmail + " ";
						}
					}
				}
			}
			brEmail.close();

			Gson gson = new Gson();

			return Response.ok(gson.toJson(retornoArray)).build();
		} catch (JSONException e) {
			return Response.status(400).entity(e.toString()).build();
		}
	}
}