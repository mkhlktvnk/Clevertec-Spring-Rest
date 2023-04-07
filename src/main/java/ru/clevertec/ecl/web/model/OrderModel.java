package ru.clevertec.ecl.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class OrderModel {

    @NotNull
    @Min(1)
    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull
    @JsonProperty(value = "totalPrice", access = JsonProperty.Access.READ_ONLY)
    private BigDecimal totalPrice;

    @NotNull
    @JsonProperty(value = "giftCertificate", access = JsonProperty.Access.READ_ONLY)
    private GiftCertificateModel giftCertificateModel;

    @NotNull
    @JsonProperty(value = "user", access = JsonProperty.Access.READ_ONLY)
    private UserModel userModel;
}
