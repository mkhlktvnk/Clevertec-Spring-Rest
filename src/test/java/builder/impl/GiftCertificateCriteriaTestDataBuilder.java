package builder.impl;

import builder.TestDataBuilder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.web.criteria.GiftCertificateCriteria;

import java.util.List;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aGiftCertificateCriteria")
public class GiftCertificateCriteriaTestDataBuilder implements TestDataBuilder<GiftCertificateCriteria> {
    private String name;
    private String description;
    private List<String> tagName;

    @Override
    public GiftCertificateCriteria build() {
        GiftCertificateCriteria giftCertificateCriteria = new GiftCertificateCriteria();
        giftCertificateCriteria.setName(name);
        giftCertificateCriteria.setDescription(description);
        giftCertificateCriteria.setTagName(tagName);
        return giftCertificateCriteria;
    }
}
