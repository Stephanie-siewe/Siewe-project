package org.isj.ing.annuarium.webapp.presentation.controller;


import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.isj.ing.annuarium.webapp.model.dto.ActeDto;
import org.isj.ing.annuarium.webapp.model.entities.User;
import org.isj.ing.annuarium.webapp.service.IActe;
import org.isj.ing.annuarium.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j// librairie pour les log des utilisateurs
public class ActeController {

	@Autowired
	IActe iActe;
	@Autowired
	UserService userService;

	@GetMapping("/")
	public String pageAcceuil(Model model) {
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		final User user=userService.findUserByEmail(auth.getName());
		if (user !=null)
			//String name= user.getName();
		    //String lastname = user.getLastName;
			model.addAttribute("userName",user.getName()+" "+user.getLastName());
		else
			model.addAttribute("");


		return "index";
	}

	@GetMapping("/listactes")
	public String pageListActes(Model model){
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		final User user=userService.findUserByEmail(auth.getName());
		if (user !=null)
			//String name= user.getName();
			//String lastname = user.getLastName;
			model.addAttribute("userName",user.getName()+" "+user.getLastName());
		else
			model.addAttribute("");
		//appel de la couche service pour avoir la liste des actes Dto
		List<ActeDto> acteDtos = iActe.listActes();
		model.addAttribute("acteDtos",acteDtos);

		return "liste";
	}

	@GetMapping("/detail")
	public String pageDetail(@RequestParam(name ="numero") String numero ,Model model){

		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		final User user=userService.findUserByEmail(auth.getName());
		if (user !=null)
			//String name= user.getName();
			//String lastname = user.getLastName;
			model.addAttribute("userName",user.getName()+" "+user.getLastName());
		else
			model.addAttribute("");
		//appel de la couche service pour avoir les details de l'acte Dto
		ActeDto acteDto = iActe.searchActeByNumero(numero);
		model.addAttribute("acteDto",acteDto);
		return "detail";
	}
	@GetMapping("/supprimer")
	public String pageSupprimer(@RequestParam(name ="numero") String numero ,Model model){
		//appel de la couche service pour supprimer l'acte Dto
		iActe.deleteActe(numero);

		return "redirect:/listactes";
	}

	@GetMapping("/enregistreracteform")
	public String enregistrerActeForm(Model model){
		
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		final User user=userService.findUserByEmail(auth.getName());
		if (user !=null)
			//String name= user.getName();
			//String lastname = user.getLastName;
			model.addAttribute("userName",user.getName()+" "+user.getLastName());
		else
			model.addAttribute("");
		//appel de la couche service pour enregistrer un acte : methode bending
		ActeDto acteDto = new ActeDto();
		acteDto.setNumero("CM");
		model.addAttribute("acteDto",acteDto);
		return "enregistrer";
	}

	@PostMapping("/enregistreracte")
	public String enregistrerActe(@ModelAttribute ActeDto acteDto, Model model){

		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		final User user=userService.findUserByEmail(auth.getName());
		if (user !=null)
			//String name= user.getName();
			//String lastname = user.getLastName;
			model.addAttribute("userName",user.getName()+" "+user.getLastName());
		else
			model.addAttribute("");
		ActeController.log.info("enregistrer-acte");

		iActe.saveActe(acteDto);
		return "redirect:/listactes";
	}


	@GetMapping("/rechercherform")
	public String pagerechercher(Model model){

		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		final User user=userService.findUserByEmail(auth.getName());
		if (user !=null)
			//String name= user.getName();
			//String lastname = user.getLastName;
			model.addAttribute("userName",user.getName()+" "+user.getLastName());
		else
			model.addAttribute("");
		//appel de la couche service pour rechercher un acte :
		List<ActeDto> acteDtos = iActe.listActes();
		model.addAttribute("acteDtos",acteDtos);
		return "rechercher";
	}

	@PostMapping("/recherche")
	public String rechercheractes(@RequestParam(value = "keyword") String keyword, Model model){
		List<ActeDto> acteDtos = iActe.searchActesByKeyword(keyword);
		model.addAttribute("acteDtos",acteDtos);
		return "rechercher";
	}

	@GetMapping("/editerform")
	public String pageEditerForm(@RequestParam(name= "numero") String numero, Model model){

		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		final User user=userService.findUserByEmail(auth.getName());
		if (user !=null)
			//String name= user.getName();
			//String lastname = user.getLastName;
			model.addAttribute("userName",user.getName()+" "+user.getLastName());
		else
			model.addAttribute("");

		ActeDto acteDto = iActe.searchActeByNumero(numero);
		model.addAttribute("acteDto",acteDto);
		return "editer";
	}

	@PostMapping("/editer")
	public String editerActe(@ModelAttribute ActeDto acteDto, Model model){
		ActeController.log.info("editer-acte");

		iActe.saveActe(acteDto);
		return "redirect:/listactes";
	}


	public void TelechargerPdf (HttpServletResponse response) throws JRException, IOException {

		InputStream japerStream  = (InputStream) this.getClass().getResourceAsStream("/Acte.jasper");
       //adding attribute names
		Map params = new HashMap<>();
		/*params.put('stid', 'stid');
		params.put('name','name');
		params.put('programme','programme');*/

		//Fetchng the student from the data database
		final JRBeanCollectionDataSource source= new JRBeanCollectionDataSource(iActe.listActes());
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(japerStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, source);

		response.setContentType("application/x-pdf");
		response.setHeader("Content-disposition","inline:filename=Acte.pdf");

		final ServletOutputStream outputStream = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);


	}

	@GetMapping("/report")
	public ResponseEntity<byte[]> generateReport(@RequestParam(value ="numero") String numero) throws FileNotFoundException,JRException{
		ActeDto acteDto = iActe.searchActeByNumero(numero);
		final byte [] data = iActe.exportReport(acteDto);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "inline:filename*Actepdf.pdf");
		return ResponseEntity.ok().headers(httpHeaders).contentType(MediaType.APPLICATION_PDF).body(data);
	}

}