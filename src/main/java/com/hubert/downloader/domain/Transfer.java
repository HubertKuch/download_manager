package com.hubert.downloader.domain;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transfer {
    private InformationSize transfer;
    private InformationSize startTransfer;

    public void subtract(InformationSize informationSizeToSubtract) {
        Float actualSize = transfer.size();
        Float afterSubtraction = actualSize - informationSizeToSubtract.size();
    }
}
