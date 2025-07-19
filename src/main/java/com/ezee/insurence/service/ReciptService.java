package com.ezee.insurence.service;

import java.util.List;

import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.dto.ReciptDTO;

public interface ReciptService {

	public void addRecipt(AuthDTO authDTO, ReciptDTO reciptlDTO);

	public void updateRecipt(AuthDTO authDTO, ReciptDTO reciptDTO);

	public ReciptDTO getReciptByCode(String code);

	public List<ReciptDTO> getAllRecipt();

}
