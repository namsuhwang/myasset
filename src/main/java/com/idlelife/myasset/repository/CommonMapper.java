package com.idlelife.myasset.repository;

import com.idlelife.myasset.models.dto.AssetDto;
import com.idlelife.myasset.models.dto.AssetSearch;
import com.idlelife.myasset.models.dto.CommonCodeDto;
import com.idlelife.myasset.models.dto.TotalAssetSummaryDto;
import com.idlelife.myasset.models.dto.form.CommonCodeForm;
import com.idlelife.myasset.models.entity.AssetEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommonMapper {

    List<CommonCodeDto> selectCommonCodeDtoList(CommonCodeForm dom);

}
