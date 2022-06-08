package org.isj.ing.annuarium.webapp.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.isj.ing.annuarium.webapp.mapper.ActeMapper;
import org.isj.ing.annuarium.webapp.model.dto.ActeDto;
import org.isj.ing.annuarium.webapp.model.entities.Acte;
import org.isj.ing.annuarium.webapp.repository.ActeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
public class ActeServiceImpl implements IActe{

    @Autowired
    ActeRepository acteRepository;
    @Autowired
    ActeMapper acteMapper;

    @Override
    public int saveActe(ActeDto acteDto) {
        if (acteRepository.findActeByNumero(acteDto.getNumero()).isPresent()){
          return 0;
        }
        return acteRepository.save(acteMapper.toEntity(acteDto)).getId().intValue();
    }

    @Override
    public ActeDto searchActeByNumero(String numero) {
        return acteMapper.toDto(acteRepository.findActeByNumero(numero).get());
    }

    @Override
    public List<ActeDto> listActes() {
        /*List<Acte> actes = acteRepository.findAll();
        List<ActeDto> acteDtos =new ArrayList<ActeDto>();
        for(Acte acte: actes){
            ActeDto acteDto = acteMapper.toDto(acte);
            acteDtos.add(acteDto);
        }
        return acteDtos;

         */
        return acteRepository.findAll().stream().map(acte -> acteMapper.toDto(acte)).collect(Collectors.toList());
    }

    @Override
    public int deleteActe(String numero) {
        acteRepository.deleteById(acteRepository.findActeByNumero(numero).get().getId());
        return 1;
    }

    @Override
    public List<ActeDto> searchActesByKeyword(String keyword) {
        return acteRepository.findActeByNumeroOrNom(keyword,keyword).get().stream()
                //.map(acte -> acteMapper.toDto(acte))
                .map(acteMapper ::toDto)
                .collect(Collectors.toList());
    }
    @Override
    public byte [] exportReport (ActeDto acteDto) throws FileNotFoundException, JRException {
        List<ActeDto> actes = new ArrayList<>();
        actes.add(acteDto);

        JasperReport jasperReport = JasperCompileManager.compileReport(new FileInputStream("C:\\Users\\Stephanie\\Desktop\\dev-java class\\Annuarium\\src\\main\\resources\\Acte.jrxml"));
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(actes);
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("createBy","java Technie");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);

        return data;
    }
}
