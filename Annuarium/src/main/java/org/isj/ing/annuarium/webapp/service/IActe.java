package org.isj.ing.annuarium.webapp.service;

import net.sf.jasperreports.engine.JRException;
import org.isj.ing.annuarium.webapp.model.dto.ActeDto;

import java.io.FileNotFoundException;
import java.util.List;

public interface IActe {
    int saveActe(ActeDto acteDto);
    ActeDto searchActeByNumero(String numero);
    List<ActeDto> listActes();
    int deleteActe(String numero);
    List<ActeDto> searchActesByKeyword(String keyword);


    byte[] exportReport(ActeDto acteDto) throws JRException, FileNotFoundException;
}
