package com.leyou.order.client;

import com.leyou.order.dto.AddressDTO;

import java.util.ArrayList;
import java.util.List;

public abstract class AddressClient {
    public static final List<AddressDTO> addressList = new ArrayList<AddressDTO>(){
        {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setId(1L);
            addressDTO.setAddress("水斗村");
            addressDTO.setCity("深圳");
            addressDTO.setDistrict("龙华区");
            addressDTO.setName("大山哥");
            addressDTO.setPhone("13833333333");
            addressDTO.setState("广东");
            addressDTO.setZipCode("0755");
            addressDTO.setIsDefault(true);
            add(addressDTO);

            AddressDTO addressDTO2 = new AddressDTO();
            addressDTO2.setId(2L);
            addressDTO2.setAddress("坂田微谷");
            addressDTO2.setCity("深圳");
            addressDTO2.setDistrict("龙岗区");
            addressDTO2.setName("大牛");
            addressDTO2.setPhone("13855555555");
            addressDTO2.setState("广东");
            addressDTO2.setZipCode("0755");
            addressDTO2.setIsDefault(false);
            add(addressDTO2);

        }
    };

    public static AddressDTO findById(Long id){
        for(AddressDTO addressDTO: addressList) {
            if(addressDTO.getId() == id) return addressDTO;
        }
        return null;
    }
}
