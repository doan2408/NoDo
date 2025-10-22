package com.example.mappingdto.Converter;

import com.example.mappingdto.Dto.DonHangDTO;
import com.example.mappingdto.Entity.DonHang;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DonHangToDonHangDTO implements Converter<DonHang, DonHangDTO> {

    @Override
    public DonHangDTO convert(DonHang entity) {
        DonHangDTO dto = new DonHangDTO();
        BeanUtils.copyProperties(entity, dto);

        if (entity.getMaKH() != null) {
            dto.setMaKHId(entity.getMaKH().getId());
        }
        return dto;
    }

}
