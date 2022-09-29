package com.hubert.downloader.domain;

import lombok.*;
import lombok.extern.java.Log;
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
        Long actualTransferInBytes = transfer.parseTo(InformationUnit.BYTE).size();
        Long actualFileSizeInBytes = informationSizeToSubtract.parseTo(InformationUnit.BYTE).size();

        Long diff = actualTransferInBytes - actualFileSizeInBytes;
        InformationSize diffInformationSize = new InformationSize(InformationUnit.BYTE, diff);

        this.transfer = diffInformationSize.parseTo(transfer.unit());
    }
}
