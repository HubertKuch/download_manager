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
        Float actualTransferInBytes = transfer.parseTo(InformationUnit.BYTE).size();
        Float actualFileSizeInBytes = informationSizeToSubtract.parseTo(InformationUnit.BYTE).size();

        Float diff = actualTransferInBytes - actualFileSizeInBytes;
        InformationSize diffInformationSize = new InformationSize(InformationUnit.BYTE, diff);

        this.transfer = diffInformationSize.parseTo(transfer.unit());
    }
}
