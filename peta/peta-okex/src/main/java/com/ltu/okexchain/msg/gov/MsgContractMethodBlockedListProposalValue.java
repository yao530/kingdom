package com.ltu.okexchain.msg.gov;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.gson.annotations.SerializedName;
import com.ltu.okexchain.msg.common.Contract_addresses;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder(alphabetic = true)
public class MsgContractMethodBlockedListProposalValue {


    @JsonProperty("title")
    @SerializedName("title")
    private String title;


    @JsonProperty("description")
    @SerializedName("description")
    private String description;

    @JsonProperty("contract_addresses")
    @SerializedName("contract_addresses")
    private List<Contract_addresses> contract_addresses;


    @JsonProperty("is_added")
    @SerializedName("is_added")
    private boolean isAdded;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("title", title)
                .append("description", description)
                .append("contract_addresses", contract_addresses)
                .append("isAdded", isAdded)
                .toString();
    }
}
