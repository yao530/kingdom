package com.ltu.model.response.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
/**
 * <p>
 * 用户参加活动的排名信息
 * </p>
 *
 * @author 若尘
 * @since 2022-05-19
 */
@Data
public class UserRankingDto {


    @ApiModelProperty(value="主键记录ID")
    private Integer value;
    @ApiModelProperty(value="排名值 按邀请人数排名则为邀请人数；按完成邀请任务先后排名则为完成时间")
    private Long score;

}
